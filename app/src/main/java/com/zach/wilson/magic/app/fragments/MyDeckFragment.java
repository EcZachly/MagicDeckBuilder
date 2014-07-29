package com.zach.wilson.magic.app.fragments;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.R.drawable;
import com.zach.wilson.magic.app.R.id;
import com.zach.wilson.magic.app.R.layout;
import com.zach.wilson.magic.app.helpers.JazzyViewPager;
import com.zach.wilson.magic.app.helpers.JazzyViewPager.TransitionEffect;
import com.zach.wilson.magic.app.models.CardList;
import com.zach.wilson.magic.app.models.Price;

public class MyDeckFragment extends Fragment {

	String pricingURL;
	ProgressDialog dialog;
	Dialog priceDialog;
	ListView list;
	SharedPreferences prefs;
	ArrayList<String> deckNames;
	ArrayAdapter<String> adapter;
	Map<String, ?> map;
	ArrayList<String> cardsInDeck;
	ArrayList<String> cardsInDeckUrls;
	ImageLoader imageLoader;
	String pricingName;
	Button edit;
	Context context;
	Button delete;
	Button view;
	Button priceDeck;
	Button shareDeck;
	Button addDeckToCart;
	String selectedDeckName;
	String selectedCardID;
	Price[] deckPrices;
	String[] namesFromURLS;
	DisplayImageOptions options;
	String[] keyArr;
	AsyncTask<String, Integer, String> task;
	SearchView searchView;
	SearchFragment fragmentList;
	ArrayList<String> cardNames;
	ArrayList<Price> prices;
	ArrayList<String> cardIDs;
	JazzyViewPager pager;
	ArrayList<String> cardsInDeckToShare;
	ArrayList<String> cardsInDeckToAddToCart;
	ArrayList<String> cardNamesToAddToCart;
	ArrayList<String> cardURLs;
	ArrayList<String> cardNamesToShow;
	TextView addNewDeck;
	@Override
	public void onPause() {
		if (task != null) {
			task.cancel(true);
		}
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(layout.newmydecklist, null);
		context = v.getContext();
		
		prices = new ArrayList<Price>();

		prefs = this.getActivity().getSharedPreferences("DECKSNEW",
				Context.MODE_PRIVATE);
		deckNames = new ArrayList<String>();
		list = (ListView) v.findViewById(id.myDeckLists);
		map = prefs.getAll();
		RelativeLayout t = (RelativeLayout) v.findViewById(id.addDeckLayout);
		
		addNewDeck = (TextView)t.findViewById(id.buttonToAddDeck);
		
		addNewDeck.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
               Fragment f = AddDeckFragment.newInstance(new ArrayList<String>(), "", false);
				getFragmentManager().beginTransaction().replace(id.content_frame, f).commit();
                FlurryAgent.logEvent("Add Deck Fragment");
			}
			
			
			
			
		});
		adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, deckNames);
		list.setAdapter(adapter);
		refresh();
		cardsInDeck = new ArrayList<String>();
		cardsInDeckUrls = new ArrayList<String>();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(drawable.ic_launcher)
				.showImageOnFail(drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		dialog = new ProgressDialog(context) {
			public void hide() {
				dialog.dismiss();
			}
		};
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int x, long l) {
				String z;
				selectedDeckName = deckNames.get(x);
				z = deckNames.get(x);
				cardNames = new ArrayList<String>();
				cardIDs = new ArrayList<String>();
				
						cardsInDeck.clear();
						cardURLs = new ArrayList<String>();
						HashSet hs = new HashSet();
						String worker = " ";
						for (int i = 0; i < keyArr.length; i++) {
							if (keyArr[i].startsWith(z)
									&& !keyArr[i].contains("#")
									&& !keyArr[i].contains("SIZE")) {
								cardsInDeck.add(map.get(keyArr[i]).toString());
							}
						}
						hs.addAll(cardsInDeck);
						cardNamesToShow = new ArrayList<String>();
						cardsInDeckToShare = new ArrayList<String>();
						for (Object s : hs) {
							String temp = s.toString();
							cardsInDeckToShare.add(temp.substring(0,
									temp.indexOf("(+)"))
									+ " x "
									+ Collections.frequency(cardsInDeck, temp));
							cardURLs.add(temp.substring(temp.indexOf("(-)") + 3));
							cardNamesToShow.add(temp.substring(0,
									temp.indexOf("(+)")));
						}		
						
						Fragment f =  new DeckListFragment();
						Bundle args = new Bundle();
						args.putString("DECK NAME", z);
						args.putStringArrayList("CARD NAMES", cardsInDeckToShare);
						args.putStringArrayList("CARD URLS", cardURLs);
						args.putStringArrayList("CARD NAMES ALSO", cardNamesToShow);
						f.setArguments(args);
						getFragmentManager().beginTransaction().replace(id.content_frame, f).commit();
			}

		});
//		InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//		inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//	
		return v;
	}

	public void refresh() {
		deckNames.clear();
		Set<String> keys = map.keySet();
		Object[] tempArr = keys.toArray();
		keyArr = new String[tempArr.length];
		for (int i = 0; i < tempArr.length; i++) {
			keyArr[i] = tempArr[i].toString();
		}

		for (int i = 0; i < keyArr.length; i++) {
			if (keyArr[i].contains("#")) {
				deckNames.add(map.get(keyArr[i]).toString());
			}
		}
		adapter.notifyDataSetChanged();

	}

}
