package com.zach.wilson.magic.app.fragments;

import java.text.NumberFormat;
import java.util.ArrayList;

import com.zach.wilson.magic.app.MainActivity;
import com.zach.wilson.magic.app.R.id;
import com.zach.wilson.magic.app.R.layout;
import com.zach.wilson.magic.app.adapters.MyArrayAdapter;
import com.zach.wilson.magic.app.helpers.MagicAppSettings;
import com.zach.wilson.magic.app.models.Card;
import com.zach.wilson.magic.app.models.CardList;
import com.zach.wilson.magic.app.models.Price;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchFragment extends Fragment {
	public ArrayList<String> potentialNames;
	ArrayAdapter<String> adapter;
	LayoutInflater inflater;
	LinearLayout searchlayout;
	public ListView list;
	Context context;
	Fragment fragment;
	Card[] potentialCards;
	ArrayList<String> imageURLs;
	String searchString;
	View view;
	MagicAppSettings appState;
	public SearchFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(layout.listview, null, false);
		appState = (MagicAppSettings) getActivity().getApplication();
		context = v.getContext();
		list = (ListView) v.findViewById(id.listing);
		adapter = new MyArrayAdapter(context, layout.listitem, potentialNames);
		list.setAdapter(adapter);
		Log.i("ID", String.valueOf(this.getId()));
		Log.i("CONTEXT", String.valueOf(context));
		searchlayout = (LinearLayout) v.findViewById(id.searchLayout);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int x,
					long arg3) {
			
				SearchView searchView = ((MainActivity) getActivity())
						.getSearchView();
				

				String c = adapter.getItem(x);
				String viewURL = imageURLs.get(x);

				
				appState.setCardFromSearch(CardList.currentCardList.get(x));
				MainActivity activity = (MainActivity) getActivity();

				
				((MainActivity) getActivity()).getSearchView().setIconified(true);
				((MainActivity) getActivity()).getSearchView().clearFocus();
				InputMethodManager inputManager = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

				CardCarouselFragment fragment = new CardCarouselFragment();
				Bundle args = new Bundle();
				args.putBoolean("FROM SEARCH", true);
				fragment.setArguments(args);
				adapter.clear();
				adapter.notifyDataSetChanged();
				Log.i("ISHIDDEN", String.valueOf(isHidden()));
				getFragmentManager().beginTransaction()
						.replace(id.content_frame, fragment).commit();
				Log.i("ISHIDDEN", String.valueOf(isHidden()));

			}

		});
		return v;
	}
	

	public interface OnItemSelectedListener {
		public void onRssItemSelected(String link);
	}

	@Override
	public void setArguments(Bundle args) {
		if (args.containsKey("POTENTIAL")) {
			if(potentialNames != null)
			potentialNames.clear();
			else
				potentialNames = new ArrayList<String>();
			if(imageURLs != null)
				imageURLs.clear();
				else
					imageURLs = new ArrayList<String>();
			
			for (String s : args.getStringArrayList("POTENTIAL")) {
				this.potentialNames.add(s);
			}
			imageURLs.clear();
			for (String p : args.getStringArrayList("URLS")) {
				this.imageURLs.add(p);
			}
			if(adapter != null){
				adapter.notifyDataSetChanged();
			}
			
		} else {
			searchString = " ";
			this.potentialNames = new ArrayList<String>();
			this.imageURLs = new ArrayList<String>();
		}
	}

}
