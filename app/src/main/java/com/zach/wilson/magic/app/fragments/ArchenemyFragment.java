package com.zach.wilson.magic.app.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.adapters.CustomPagerAdapter;
import com.zach.wilson.magic.app.adapters.ImageAdapterPlaneschase;
import com.zach.wilson.magic.app.helpers.DeckBrewClient;
import com.zach.wilson.magic.app.helpers.JazzyViewPager;
import com.zach.wilson.magic.app.helpers.MagicAppSettings;
import com.zach.wilson.magic.app.helpers.PriceDialog;
import com.zach.wilson.magic.app.helpers.TCGClient;
import com.zach.wilson.magic.app.models.Card;
import com.zach.wilson.magic.app.models.Products;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


//DONE INJECTING
public class ArchenemyFragment extends Fragment {
	@InjectView(R.id.planespager)JazzyViewPager pager;
	ListView[] lists;
	ImageAdapterPlaneschase[] adapters;
	Card[] schemes;
	int counter;
	ProgressDialog dialog;
    PriceDialog pricing;
	LayoutInflater inflater;
	DisplayImageOptions options;
	Context context;
	Dialog priceDialog;

	MagicAppSettings appState;
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
        TCGClient.instantiate();
		View v = inflater.inflate(R.layout.planesarchenemypagerlayout, null);
		context = v.getContext();
		appState = (MagicAppSettings)getActivity().getApplication();

        ButterKnife.inject(this, v);

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		if(appState.getSchemesInCarousel() == null){
            DeckBrewClient.getAPI();
            Map<String, String> hm = new HashMap<String, String>();
            hm.put("type", "scheme");
            DeckBrewClient.deckbrew.getCardsFromAttributes(hm, new Callback<List<Card>>() {
                @Override
                public void success(List<Card> cards, Response response) {
                    schemes = new Card[cards.size()];

                    ArrayList<Card> temp = new ArrayList<Card>();
                        for (int i = 0; i < cards.size(); i++) {
                        temp.add(cards.get(i));
                    }
                    //Randomizes the planes
                    Collections.shuffle(temp);
                    for (int i = 0; i < cards.size(); i++) {
                       schemes[i] = cards.get(i);
                    }

                    lists = new ListView[schemes.length];
                    adapters = new ImageAdapterPlaneschase[schemes.length];
                    for (int i = 0; i < schemes.length; i++) {
                        lists[i] = new ListView(appState.getActivity().getBaseContext());
                    }
                    for (int i = 0; i < lists.length; i++) {
                        adapters[i] = new ImageAdapterPlaneschase(inflater, schemes[i], options);
                        lists[i].setAdapter(adapters[i]);
                        lists[i].setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1,
                                                   final int arg2, long arg3) {
                                int counter = pager.getCurrentItem();

                                TCGClient.pricing.getProductPrice("MAGICVIEW", TCGClient.formatSet(adapters[counter].getCardEditions().get(arg2).getSet(), adapters[counter].getCard().getName()) ,adapters[counter].getCard().getName(), new Callback<Products>() {
                                    @Override
                                    public void success(Products products, Response response) {
                                        pricing = new PriceDialog(context,  adapters[pager.getCurrentItem()].getCard(), arg2, products);
                                        pricing.showDialog();
                                        Log.i("PRODUCT", products.getProducts().getHiprice());
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
                    for (int i = 0; i < lists.length; i++) {
                        pages.add(lists[i]);
                    }
                    pager.setAdapter(new CustomPagerAdapter(pager, appState.getActivity().getApplicationContext(), pages));
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                }
            });
		}
		else{
			String[] temp = new String[appState.getSchemesInCarousel().length];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = appState.getSchemesInCarousel()[i].getEditions()[0]
						.getImage_url();
			}
			context = v.getContext();

			

			lists = new ListView[schemes.length];
			adapters = new ImageAdapterPlaneschase[schemes.length];
			for (int i = 0; i <schemes.length; i++) {
				lists[i] = new ListView(context);
			}
			for (int i = 0; i < lists.length; i++) {
				adapters[i] = new ImageAdapterPlaneschase(inflater, schemes[i], options);
				lists[i].setAdapter(adapters[i]);
				lists[i].setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							final int arg2, long arg3) {
						counter = pager.getCurrentItem();
                        TCGClient.pricing.getProductPrice("MAGICVIEW", TCGClient.formatSet(adapters[counter].getCardEditions().get(arg2).getSet(), adapters[counter].getCard().getName()), adapters[counter].getCard().getName(), new Callback<Products>() {
                            @Override
                            public void success(Products products, Response response) {
                                pricing = new PriceDialog(context,  adapters[pager.getCurrentItem()].getCard(), arg2, products);
                                pricing.showDialog();
                                Log.i("PRODUCT", products.getProducts().getHiprice());
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.i("Error", error.getUrl());


                            }
                        });
					}

				});

			}

			Vector<View> pages = new Vector<View>();
			for (int i = 0; i < lists.length; i++) {
				pages.add(lists[i]);
			}
			pager.setAdapter(new CustomPagerAdapter(pager, context, pages));
			dialog = new ProgressDialog(v.getContext());
			priceDialog = new Dialog(v.getContext());
			return v;
			
		}
		
		
		dialog = new ProgressDialog(v.getContext());
		priceDialog = new Dialog(v.getContext());
	return v;
	}

	public interface OnItemSelectedListener {
		public void onRssItemSelected(String link);
	}
	
}
