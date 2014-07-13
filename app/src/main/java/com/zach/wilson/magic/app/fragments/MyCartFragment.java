package com.zach.wilson.magic.app.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.R.id;
import com.zach.wilson.magic.app.R.layout;
import com.zach.wilson.magic.app.models.CardList;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ListView;

public class MyCartFragment extends Fragment {

	LayoutInflater inflater;
	ListView list;
	Button checkOut;
	Context context;
	ArrayAdapter<String> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(layout.mycart, null);
		context = v.getContext();
		list = (ListView) v.findViewById(id.myCartList);
		if (CardList.cardsToOrder != null) {
			adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_list_item_1, CardList.cardsToOrder);
			list.setAdapter(adapter);
		} else {
			
			CardList.cardsToOrder = new ArrayList<String>();
			adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_list_item_1, CardList.cardsToOrder);
			list.setAdapter(adapter);
		}
		
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
			
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);

				// set title
				alertDialogBuilder.setTitle("Remove From Cart?");

				// set dialog message
				alertDialogBuilder
						.setMessage(
								"Do you want to remove " + CardList.cardsToOrder.get(arg2).substring(1) + " \nfrom your order?")
						.setCancelable(true)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int id) {
										CardList.cardsToOrder.remove(arg2);
										CardList.currentOrder = CardList.massCardOrderingBaseURL;
										for(String s : CardList.cardsToOrder){
											String temp = s.substring(0, 1);
											String name = s.substring(1);
											
											CardList.currentOrder += temp + " " + name + "||";
										}
										adapter.notifyDataSetChanged();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
				
				
			}
			
			
			
			
		});
		
		
		adapter.notifyDataSetChanged();
		checkOut = (Button) v.findViewById(id.checkOut);

		checkOut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (CardList.currentOrder == null) {

				} else {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(CardList.currentOrder));
					startActivity(intent);
				}

			}

		});

		return v;

	}

}
