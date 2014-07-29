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
import android.widget.Adapter;
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

		View v = inflater.inflate(R.layout.pagerlayout, null);

        loading = new ProgressDialog(v.getContext());
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

                if(!fromAdvSearch) {
                    if (arg0 >49) {
                        Random r = new Random();

                        DeckBrewClient.getAPI().getRandomCards(r.nextInt(140), new Callback<Card[]>() {
                            @Override
                            public void success(Card[] cards, Response response) {
                                Log.i("RESPONSE", response.getUrl());
                                CardCarouselFragment f = new CardCarouselFragment();
                                Bundle args = new Bundle();
                                args.putSerializable("CARDS FROM MAIN", cards);
                                int i = 0;
                                for(ImageListAdapter t : adapters){
                                    t.notifyDataSetChanged();
                                }
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
			lists = new ListView[50];
			adapters = new ImageListAdapter[50];
			for (int i = 0; i < 50; i++) {
				lists[i] = new ListView(context);
			}
			
		}	
		for (int i = 0; i < lists.length; i++) {
			
			//Log.i("APPSTATE", appState.getCardsInCarousel()[i].getName());
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

}
