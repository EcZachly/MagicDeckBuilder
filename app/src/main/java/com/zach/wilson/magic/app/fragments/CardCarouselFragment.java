package com.zach.wilson.magic.app.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.adapters.CustomPagerAdapter;
import com.zach.wilson.magic.app.adapters.ImageListAdapter;
import com.zach.wilson.magic.app.helpers.DeckBrewClient;
import com.zach.wilson.magic.app.helpers.JazzyViewPager;
import com.zach.wilson.magic.app.helpers.PriceDialog;
import com.zach.wilson.magic.app.helpers.TCGClient;
import com.zach.wilson.magic.app.models.Card;
import com.zach.wilson.magic.app.models.Products;
import com.zach.wilson.magic.app.models.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CardCarouselFragment extends Fragment {
	JazzyViewPager pager;
	static Card[] cards;
	Context context;
    int currentItem;
	LayoutInflater inflater;
	DisplayImageOptions options;
	static boolean fromAdvSearch;
	static boolean fromSearch;
	ListView[] lists;
	ImageListAdapter[] adapters;
	int counter;
    PriceDialog pricing;
    ProgressDialog loading;

    static LinearLayout filtering;
    static Spinner set;
    static Spinner type;
    static Spinner tournament;
    static Spinner[] spinners = new Spinner[3];
    static String[] formats = {"", "Standard", "Modern", "Vintage", "Legacy",
            "Commander"};
    static List<Set> allSets;
	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
        TCGClient.instantiate();
		counter = 0;
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(android.R.drawable.presence_invisible)
				.showImageOnFail(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		View v = inflater.inflate(R.layout.pagerlayout, null);
        loading = new ProgressDialog(v.getContext());
        filtering = (LinearLayout) v.findViewById(R.id.filterLayout);
        context = v.getContext();
        set = (Spinner) filtering.findViewById(R.id.filterBySet);
        type = (Spinner) filtering.findViewById(R.id.filterByType);
        tournament = (Spinner) filtering.findViewById(R.id.filterByTournamentLegality);
        spinners[0] =set;
        spinners[1] = type;
        spinners[2] = tournament;
        DeckBrewClient.getAPI();


        for(Spinner s : spinners){


            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        }
        Button b = (Button) filtering.findViewById(R.id.applyFilters);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeckBrewClient.getAPI();
                ArrayList ty = new ArrayList<String>();
                ArrayList to = new ArrayList<String>();
                ArrayList set = new ArrayList<String>();
                String selectedS =  spinners[0].getSelectedItem().toString();
                for(Set s : CardCarouselFragment.getSets()){
                    if(s.getName().equals(selectedS)){
                        set.add(s.getId());
                        break;
                    }
                }

                if(!spinners[1].getSelectedItem().toString().equals(""))
                    ty.add(spinners[1].getSelectedItem().toString().toLowerCase());
                if(!spinners[2].getSelectedItem().toString().equals(""))
                    to.add(spinners[2].getSelectedItem().toString().toLowerCase());

                loading.setMessage("Getting cards");
                loading.show();
                DeckBrewClient.deckbrew.getCardsFromFilters(set, ty, to, new Callback<List<Card>>(){

                    @Override
                    public void success(List<Card> cards, Response response) {

                        loading.dismiss();
                        loading.setMessage("Getting prices");
                        Log.i("CARDS", String.valueOf(cards.size()));

                        Bundle args = new Bundle();
                        Card[] c = new Card[cards.size()];
                        for(int i = 0; i < c.length; i++){
                            c[i] = cards.get(i);
                        }
                        args.putSerializable("CARDS FROM MAIN", c);

                        CardCarouselFragment f = CardCarouselFragment.newInstance(c, true, false);
                        getFragmentManager().beginTransaction().replace(R.id.content_frame, f).commit();
                        TCGClient.instantiate();
                        CardCarouselFragment.getFilterLayout().setVisibility(View.GONE);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        loading.dismiss();
                        Log.i("FAILURE FROM GCARDS", error.getMessage());
                        Log.i("FAILURE FROM GCARDS", error.getUrl());
                    }
                });




            }
        });




        DeckBrewClient.deckbrew.getSets(new Callback<List<Set>>(){


            @Override
            public void failure(RetrofitError error) {

            }

            @Override
            public void success(List<Set> sets, Response response) {
                    ArrayList<String> temp = new ArrayList<String>();
                    allSets = new ArrayList<Set>();
                for(Set s : sets){
                    temp.add(s.getName());
                    allSets.add(s);
                }
                String[] temp1 = new String[temp.size() + 1];
                temp1[0] = " ";
                for(int i = 1; i < temp1.length; i++){
                    temp1[i] = temp.get(i - 1);
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, temp1); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                set.setAdapter(spinnerArrayAdapter);
            }
        });

        DeckBrewClient.deckbrew.getTypes(new Callback<List<String>>(){

            @Override
            public void success(List<String> strings, Response response) {
                String[] t = new String[strings.size() + 1];
                t[0] = "";
                for(int i = 1; i < t.length ; i++){
                    String temp = strings.get(i - 1);
                    char a = temp.charAt(0);
                    temp = temp.substring(1);

                    a = Character.toUpperCase(a);

                    temp = a + temp;

                    t[i] = temp;
                }


                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, t); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                type.setAdapter(spinnerArrayAdapter);

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, formats); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tournament.setAdapter(spinnerArrayAdapter);


        loading.setMessage("Getting prices");
		pager = (JazzyViewPager) v.findViewById(R.id.pager);

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int arg0) {


                Log.i("PAGE", String.valueOf(arg0));
                if(!fromAdvSearch) {
                    if (arg0 >= 24) {
                        Random r = new Random();

                        DeckBrewClient.getAPI().getRandomCards(r.nextInt(140), new Callback<Card[]>() {
                            @Override
                            public void success(Card[] cards, Response response) {
                                Log.i("RESPONSE", response.getUrl());
                                CardCarouselFragment f = CardCarouselFragment.newInstance(cards, false, false);


                                getActivity().getFragmentManager().beginTransaction().replace(R.id.content_frame, f).commit();
                                pager.setCurrentItem(0);
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });

                    }
                }
			}

		});

		context = v.getContext();
        Log.i("FROM ADV", String.valueOf(fromAdvSearch));
        Log.i("CURRENT ITEM", String.valueOf(currentItem));
		if(fromAdvSearch){
			lists = new ListView[cards.length];
			adapters = new ImageListAdapter[lists.length];
			for (int i = 0; i < lists.length; i++) {
				lists[i] = new ListView(context);
			}

		}
		else if(fromSearch){
			lists = new ListView[1];
			adapters = new ImageListAdapter[1];
			for (int i = 0; i < 1; i++) {
				lists[i] = new ListView(context);
			}
		}
		else{
			lists = new ListView[25];
			adapters = new ImageListAdapter[25];
			for (int i = 0; i < 25; i++) {
				lists[i] = new ListView(context);
			}
			
		}	
		for (int i = 0; i < lists.length; i++) {
			adapters[i] = new ImageListAdapter(this.inflater,cards[i], options);
			lists[i].setAdapter(adapters[i]);
			lists[i].setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int arg2, long arg3) {
					counter = pager.getCurrentItem();

                    loading.show();
                    TCGClient.pricing.getProductPrice("MAGICVIEW", TCGClient.formatSet(adapters[counter].getCardEditions().get(arg2).getSet(),adapters[counter].getCard().getName()) ,adapters[counter].getCard().getName(), new Callback<Products>() {
                        @Override
                        public void success(Products products, Response response) {
                            loading.hide();
                            pricing = new PriceDialog(context,  adapters[pager.getCurrentItem()].getCard(), arg2, products);
                            pricing.showDialog();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            loading.hide();
                            Log.i("Error", error.getUrl());


                        }
                    } );
				}

			});

		}

		Vector<View> pages = new Vector<View>();
		for (ListView l : lists) {
			pages.add(l);
		}
		pager.setAdapter(new CustomPagerAdapter(pager, context, pages));
        if(fromAdvSearch)
        pager.setCurrentItem(currentItem);
		return v;
	}

	public interface OnItemSelectedListener {
		public void onRssItemSelected(String link);
	}

	@Override
	public void setArguments(Bundle args) {

		cards = new Card[((Card[])args.getSerializable("CARDARRAY")).length];
        for(int i = 0; i < cards.length; i++){
            cards[i] =((Card[]) args.getSerializable("CARDARRAY"))[i];
        }
		fromAdvSearch = args.getBoolean("advSearch");
        fromSearch = args.getBoolean("search");
		super.setArguments(args);
	}


    public static CardCarouselFragment newInstance(Card[] cardList, boolean fAdvSearch, boolean fSearch){
        CardCarouselFragment f = new CardCarouselFragment();

        Bundle args = f.getArguments();
        if(args == null){
            args = new Bundle();
        }


        cards = new Card[cardList.length];
        for(int i = 0; i < cards.length; i++){
            cards[i] = cardList[i];
        }
        fromAdvSearch = fAdvSearch;
        fromSearch = fSearch;
        args.putSerializable("CARDARRAY", cards);
        args.putBoolean("advSearch", fAdvSearch);
        args.putBoolean("search", fSearch);
        f.setArguments(args);
        return f;
    }

    public static LinearLayout getFilterLayout(){
        return filtering;
    }

    public static Spinner[] getSpinners(){
        Spinner[] spinners = new Spinner[3];
        spinners[0] = set;
        spinners[1] = type;
        spinners[2] = tournament;
        return spinners;
    }
    public static List<Set> getSets(){
        return allSets;
    }

}
