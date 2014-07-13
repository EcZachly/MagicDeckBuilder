package com.zach.wilson.magic.app.fragments;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

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

public class AddDeckFragment extends Fragment {
	EditText text;
	static String URL = "https://api.deckbrew.com/mtg/cards/typeahead?q=";
	TextView numOfCards;
	ListView potentialCards;
	ListView cardsInDeck;
	SearchView searchView;
	SearchFragment fragmentList;
	SharedPreferences prefs;
	Button save;
	AsyncTask<String, Integer, String> task;
	String deck;
	Bundle extras;
	boolean editMode;
	EditText deckName;
	Context context;
	
	ArrayList<String> oldCardsInDeck;
	ArrayList<String> holdsAllData;
	ArrayList<String> tempPotNames;
	ArrayList<String> cardsListForList;
	ArrayList<String> potentialNames;
	ArrayAdapter<String> adapter;
	ArrayAdapter<String> cardAdapter;
	Card[] cardsFromSearching;
	int holding;
	ArrayList<Integer> removedSpots;

	public interface OnItemSelectedListener {
		public void onRssItemSelected(String link);
	}

	public AddDeckFragment() {
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
		
		removedSpots = new ArrayList<Integer>();
		text = (EditText) v.findViewById(id.addCardsEditText);
		numOfCards = (TextView) v.findViewById(id.numberOfCards);
		potentialCards = (ListView) v.findViewById(id.potentialCards);
		cardsInDeck = (ListView) v.findViewById(id.cardsInDeck);
		save = (Button) v.findViewById(id.saveDeck);
		deckName = (EditText) v.findViewById(id.deckNameEditText);
		
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
					if(task != null){
						task.cancel(true);
					}
					task = new SearchingForNames().execute(URL + Uri.encode(s.toString().toLowerCase()));
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
				
				cardsListForList.add(cardsFromSearching[x].getName());
				holdsAllData.add(cardsFromSearching[x].getName() + "(+)" + cardsFromSearching[x].getId() + "(-)" + cardsFromSearching[x].getEditions()[0].getImage_url());
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
	
	
	class SearchingForNames extends AsyncTask<String, Integer, String> {
		
	
		@Override
		protected String doInBackground(String... params) {
			 HttpClient httpclient = new DefaultHttpClient();
		        HttpResponse response;
		        String responseString = null;
		        try {
		            response = httpclient.execute(new HttpGet(params[0]));
		            StatusLine statusLine = response.getStatusLine();
		            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		                ByteArrayOutputStream out = new ByteArrayOutputStream();
		                response.getEntity().writeTo(out);
		                out.close();
		                responseString = out.toString();
		            } else{
		                //Closes the connection.
		                response.getEntity().getContent().close();
		                throw new IOException(statusLine.getReasonPhrase());
		            }
		        } catch (ClientProtocolException e) {
		            //TODO Handle problems..
		        } catch (IOException e) {
		            //TODO Handle problems..
		        }
		        
		       
		        return responseString;		
		}

		@Override
		protected void onPostExecute(String result) {
			if(potentialNames != null){
				potentialNames.clear();
				//CardList.currentCardList.clear();
			}
			StringReader reader = new StringReader(result);
			jsonParse(reader);
			ArrayList<String> names = new ArrayList<String>();
			ArrayList<String> urls = new ArrayList<String>();
			Bundle args = new Bundle();
			CardList.currentCardList = new ArrayList<Card>();
			
			for(int i = 0; i < cardsFromSearching.length; i++){
				potentialNames.add(cardsFromSearching[i].getName());
				urls.add(cardsFromSearching[i].getEditions()[0].getImage_url());
			}
			
			
			
			adapter.notifyDataSetChanged();

		}

		private void jsonParse(Reader r) {
			Gson gson = new Gson();
			cardsFromSearching = gson.fromJson(r, Card[].class);
		}
	}
	
	public void addToData(String a, String b, String c){
		holdsAllData.add(a + "(+)" + b + "(-)" + c);		
	}
	
}
