package com.zach.wilson.magic.app.adapters;

import java.util.ArrayList;

import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.R.id;
import com.zach.wilson.magic.app.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<String> {
		ArrayList<String> data;
		Context context;
		int resource;

		public MyArrayAdapter(Context context, int resource,
				ArrayList<String> data) {		

			super(context, resource);
			this.context = context;
			this.data = new ArrayList<String>();
			this.resource = resource;
			if(data != null){
			for(int i = 0; i < data.size(); i++){
				this.data.add(data.get(i));
			}
			}
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public void clear() {
			data.clear();
			this.notifyDataSetChanged();
		}

		@Override
		public String getItem(int position) {
			return data.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// assign the view we are converting to a local variable
			View v = convertView;

			// first check to see if the view is null. if so, we have to inflate
			// it.
			// to inflate it basically means to render, or show, the view.
			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(layout.listitem, null);
			}

			TextView t = (TextView) v.findViewById(id.cardName);
			t.setText(data.get(position));
			
		
			return v;
		}

	}


