package com.zach.wilson.magic.app.fragments;

import java.util.ArrayList;

import com.zach.wilson.magic.app.MainActivity;
import com.zach.wilson.magic.app.R.id;
import com.zach.wilson.magic.app.R.layout;
import com.zach.wilson.magic.app.SearchActivity;
import com.zach.wilson.magic.app.adapters.MyArrayAdapter;
import com.zach.wilson.magic.app.models.Card;
import com.zach.wilson.magic.app.models.CardList;

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
	public ListView list;
	Context context;
	Fragment fragment;
	ArrayList<String> imageURLs;
	String searchString;
	View view;
	public SearchFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(layout.listview, null);
		context = v.getContext();
		list = (ListView) v.findViewById(id.listing);
		adapter = new MyArrayAdapter(context, layout.listitem, potentialNames);
		list.setAdapter(adapter);
		Log.i("ID", String.valueOf(this.getId()));
		Log.i("CONTEXT", String.valueOf(context));
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int x,
					long arg3) {
			

				((SearchActivity) getActivity()).getSearch().setIconified(true);
				((SearchActivity) getActivity()).getSearch().clearFocus();
				InputMethodManager inputManager = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                Card[] temp = new Card[1];
                temp[0] = CardList.currentCardList.get(x);
				CardCarouselFragment fragment = CardCarouselFragment.newInstance(temp, false, true);
				adapter.clear();
				adapter.notifyDataSetChanged();
				getFragmentManager().beginTransaction()
						.replace(id.content_frame, fragment).commit();

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
