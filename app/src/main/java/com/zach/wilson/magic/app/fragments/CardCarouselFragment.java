package com.zach.wilson.magic.app.fragments;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.adapters.CustomPagerAdapter;
import com.zach.wilson.magic.app.adapters.ImageListAdapter;
import com.zach.wilson.magic.app.helpers.DeckBrewClient;
import com.zach.wilson.magic.app.helpers.JazzyViewPager;
import com.zach.wilson.magic.app.helpers.MagicAppSettings;
import com.zach.wilson.magic.app.helpers.PriceDialog;
import com.zach.wilson.magic.app.helpers.TCGClient;
import com.zach.wilson.magic.app.models.Card;
import com.zach.wilson.magic.app.models.CardList;
import com.zach.wilson.magic.app.models.Products;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CardCarouselFragment extends Fragment {
	JazzyViewPager pager;
	ArrayList<Card> cards;
	Context context;
	LayoutInflater inflater;
	DisplayImageOptions options;
	boolean fromAdvSearch = false;
	boolean fromSearch = false;
	MagicAppSettings appState;
	ListView[] lists;
	ImageListAdapter[] adapters;
	int counter;
    PriceDialog pricing;

	public JazzyViewPager getPager() {
		return pager;
	}

	public void setPager(JazzyViewPager pager) {
		this.pager = pager;
	}

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

		appState = (MagicAppSettings) getActivity().getApplication();
		View v = inflater.inflate(R.layout.pagerlayout, null);

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

                if(!fromAdvSearch) {
                    if (arg0 > 48) {
                        Random r = new Random();
                        String z = CardList.allCards + r.nextInt(140);
                        DeckBrewClient.getAPI().getRandomCards(r.nextInt(), new Callback<Card[]>() {
                            @Override
                            public void success(Card[] cards, Response response) {
                                appState.setCardsInCarousel(new Card[cards.length]);
                                for (int i = 0; i < cards.length; i++) {
                                    appState.getCardsInCarousel()[i] = cards[i];
                                }
                                CardCarouselFragment f = new CardCarouselFragment();
                                getActivity().getFragmentManager().beginTransaction().replace(R.id.content_frame, f).commit();
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                        pager.setCurrentItem(0);
                    }
                }
			}

		});

		context = v.getContext();


		if(fromAdvSearch){
			appState.setCardsInCarousel(new Card[appState.getCardsFromAdvSearch().length]);
			for(int i = 0; i < appState.getCardsInCarousel().length; i++){
				appState.getCardsInCarousel()[i] = appState.getCardsFromAdvSearch()[i];
				
			}
			lists = new ListView[appState.getCardsFromAdvSearch().length];
			adapters = new ImageListAdapter[lists.length];
			for (int i = 0; i < lists.length; i++) {
				lists[i] = new ListView(context);
			}
			
			
		}
		else if(fromSearch){		
			appState.setCardsInCarousel(new Card[1]);
			appState.getCardsInCarousel()[0] = appState.getCardFromSearch();
			lists = new ListView[1];
			adapters = new ImageListAdapter[1];
			for (int i = 0; i < 1; i++) {
				lists[i] = new ListView(context);
			}
		}
		else{
			lists = new ListView[50];
			adapters = new ImageListAdapter[50];
			for (int i = 0; i < 50; i++) {
				lists[i] = new ListView(context);
			}
			
		}	
		for (int i = 0; i < lists.length; i++) {
			
			//Log.i("APPSTATE", appState.getCardsInCarousel()[i].getName());
			adapters[i] = new ImageListAdapter(this.inflater, appState.getCardsInCarousel()[i], options);
			lists[i].setAdapter(adapters[i]);
			lists[i].setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int arg2, long arg3) {
					counter = pager.getCurrentItem();
                    TCGClient.pricing.getProductPrice("MAGICVIEW", TCGClient.formatSet(adapters[counter].getCardEditions().get(arg2).getSet()) ,adapters[counter].getCard().getName(), new Callback<Products>() {
                        @Override
                        public void success(Products products, Response response) {
                            pricing = new PriceDialog(context, appState, adapters[pager.getCurrentItem()].getCard(), arg2, products);
                            pricing.showDialog();
                        }

                        @Override
                        public void failure(RetrofitError error) {
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
		
		return v;
	}

	public interface OnItemSelectedListener {
		public void onRssItemSelected(String link);
	}

	@Override
	public void setArguments(Bundle args) {
		cards = new ArrayList<Card>();
		
		if(args.containsKey("FROM ADV SEARCH"))
		fromAdvSearch = args.getBoolean("FROM ADV SEARCH");
		if(args.containsKey("FROM SEARCH"))
		fromSearch = args.getBoolean("FROM SEARCH");
		//Log.i("CARD SIZE", String.valueOf(cards.size()));
		super.setArguments(args);
	}

}
