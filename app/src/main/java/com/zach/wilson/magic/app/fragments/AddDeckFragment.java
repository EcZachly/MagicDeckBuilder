package com.zach.wilson.magic.app.fragments;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.R.id;
import com.zach.wilson.magic.app.R.layout;
import com.zach.wilson.magic.app.helpers.DeckBrewClient;
import com.zach.wilson.magic.app.models.Card;
import com.zach.wilson.magic.app.models.CardList;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;



//DONE INJECTING
public class AddDeckFragment extends Fragment {
    @InjectView(id.addCardsEditText)EditText text;
	@InjectView(id.numberOfCards)TextView numOfCards;
	@InjectView(id.potentialCards)ListView potentialCards;
	@InjectView(R.id.cardsInDeck)ListView cardsInDeck;
    @InjectView(id.saveDeck)Button save;
    @InjectView(id.deckNameEditText)EditText deckName;
    SharedPreferences prefs;
    String deck;
    boolean editMode;
	Context context;
	ArrayList<String> oldCardsInDeck;
	ArrayList<String> holdsAllData;
	ArrayList<String> tempPotNames;
	ArrayList<String> cardsListForList;
	ArrayList<String> potentialNames;
	ArrayAdapter<String> adapter;
	ArrayAdapter<String> cardAdapter;
	int holding;
	ArrayList<Integer> removedSpots;



    public static AddDeckFragment newInstance(ArrayList<String> cards, String deckName, boolean editMode) {
        AddDeckFragment f = new AddDeckFragment();
        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        args.putBoolean("EDITMODE", editMode);
        args.putStringArrayList("CARD NAMES", cards);
        args.putString("DECK NAME", deckName);
        f.setArguments(args);
        return f;
    }



	@Override
	public void setArguments(Bundle args) {
		editMode = args.getBoolean("EDITMODE");	
	
		cardsListForList = new ArrayList<String>();
		holdsAllData = new ArrayList<String>();
		
		if(args.getStringArrayList("CARD NAMES") != null)
		for(int i = 0; i < args.getStringArrayList("CARD NAMES").size(); i++){
			
			holdsAllData.add(args.getStringArrayList("CARD NAMES").get(i));	
			Log.i("LOG", args.getStringArrayList("CARD NAMES").get(i));
			Log.i("DATA",holdsAllData.get(i));
			cardsListForList.add(args.getStringArrayList("CARD NAMES").get(i).substring(0, args.getStringArrayList("CARD NAMES").get(i).indexOf("(+)")));
		}
		oldCardsInDeck = args.getStringArrayList("CARD NAMES");
		deck = args.getString("DECK NAME");
		super.setArguments(args);
	}





	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(layout.adddecklayout, null);
		context = v.getContext();
	    ButterKnife.inject(this, v);
		removedSpots = new ArrayList<Integer>();

		
		InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		if(editMode){
			deckName.setText(deck);			
		}
		else{
			holdsAllData = new ArrayList<String>();
			cardsListForList = new ArrayList<String>();
		}
		potentialNames = new ArrayList<String>();
		prefs = this.getActivity().getSharedPreferences("DECKSNEW",
				Context.MODE_PRIVATE);
		tempPotNames = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this.getActivity().getBaseContext(),
				android.R.layout.simple_list_item_1, potentialNames);
		cardAdapter = new ArrayAdapter<String>(this.getActivity()
				.getBaseContext(), android.R.layout.simple_list_item_1,
				cardsListForList);
		
		potentialCards.setAdapter(adapter);
		cardsInDeck.setAdapter(cardAdapter);
	
		text.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String w = s.toString();
				Log.i("KEY", s.toString());
				if (s.toString().length() > 0) {
                    DeckBrewClient.getAPI();
                    DeckBrewClient.deckbrew.getCardsFromStart(w, new Callback<List<Card>>(){

                        @Override
                        public void success(List<Card> cards, Response response) {
                            if(potentialNames != null){
                                potentialNames.clear();
                            }
                            else{
                                potentialNames = new ArrayList<String>();
                            }
                            ArrayList<String> urls = new ArrayList<String>();
                            CardList.currentCardList = new ArrayList<Card>();

                            for(int i = 0; i < cards.size(); i++){
                                potentialNames.add(cards.get(i).getName());
                                urls.add(cards.get(i).getEditions()[0].getImage_url());
                                CardList.currentCardList.add(cards.get(i));
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });


				}

			}
			

			@Override
			public void afterTextChanged(Editable e) {
			}

		});

		potentialCards.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int x,
					long arg3) {
				cardsListForList.add(CardList.currentCardList.get(x).getName());
				holdsAllData.add(CardList.currentCardList.get(x).getName() + "(+)" + CardList.currentCardList.get(x).getId() + "(-)" + CardList.currentCardList.get(x).getEditions()[0].getImage_url());
				numOfCards.setText(String.valueOf(holding));
				cardAdapter.notifyDataSetChanged();
			}

		});
		cardsInDeck.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int x,
					long arg3) {
				String z = cardAdapter.getItem(x);
				cardsListForList.remove(x);
				holdsAllData.remove(x);
				numOfCards.setText(String.valueOf(holding));
				cardAdapter.notifyDataSetChanged();
			}

		});

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (editMode == false) {
					Log.i("DECK NAME", deckName.getText().toString());
					if (!deckName.getText().toString().equals("")) {
						String name = deckName.getText().toString();
						
						//SAVES DECK NAME WITH A # after it for the key
						prefs.edit().putString(name + "#", name).commit();
						//Saves the cards in the deck using the deck name + the position in the deck 
						//the card is and the URLS are saved with a $ after the deck name
						//And the card ID is saved with a @ sign
						for (int i = 0; i < holdsAllData.size(); i++) {
						
							prefs.edit()
									.putString(name + "" + i,
											holdsAllData.get(i)).commit();
							
							
						}
						prefs.edit().putInt(name + "SIZE", holdsAllData.size()).commit();
						Fragment f = new MyDeckFragment();
						getFragmentManager().beginTransaction()
								.replace(id.content_frame, f).commit();
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);

						builder.setMessage("Deck requires a name");
						AlertDialog alertDialog = builder.create();
						 
					alertDialog.show();
					}
				} else {
					Map<String, ?> temp = prefs.getAll();
					Set<String> keySet = temp.keySet();
					Object[] tempArr = keySet.toArray();
					String[] keyArr;
					keyArr = new String[tempArr.length];
					for (int i = 0; i < tempArr.length; i++) {
						keyArr[i] = tempArr[i].toString();
					}
					for (int i = 0; i < prefs.getAll().size(); i++) {
						if (keyArr[i].startsWith(deck)) {
							prefs.edit().remove(keyArr[i]).commit();
						}
					}
					String name = deckName.getText().toString();
					prefs.edit().putString(name + "#", name).commit();
					for (int i = 0; i < holdsAllData.size(); i++) {
						
						prefs.edit()
								.putString(name + "" + i,
										holdsAllData.get(i)).commit();
						
						
					}
					prefs.edit().putInt(name + "SIZE", holdsAllData.size()).commit();
					Fragment f = new MyDeckFragment();
					getFragmentManager().beginTransaction()
							.replace(id.content_frame, f).commit();
				}

			}

		});
		return v;
	}
}
