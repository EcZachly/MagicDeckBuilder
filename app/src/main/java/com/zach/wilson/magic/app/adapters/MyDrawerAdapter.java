package com.zach.wilson.magic.app.adapters;

import java.util.ArrayList;

import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.R.drawable;
import com.zach.wilson.magic.app.R.id;
import com.zach.wilson.magic.app.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyDrawerAdapter extends ArrayAdapter<String> {

	ArrayList<String> data;
	Context context;
	int resource;

	public MyDrawerAdapter(Context context, int resource,
			ArrayList<String> objects) {
		super(context, resource, objects);
		data = new ArrayList<String>();
		for (int i = 0; i < objects.size(); i++) {

			data.add(objects.get(i));
		}
		this.context = context;
		this.resource = resource;
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
		View v;

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(layout.drawer_item, null);

		TextView t = (TextView) v.findViewById(id.content);
		ImageView s = (ImageView) v.findViewById(id.imageViewForDrawer);
		t.setText(data.get(position));

		
		switch (position) {
	
		
		case 0:
			s.setImageResource(drawable.homeicon);
			break;
		case 1:
			s.setImageResource(drawable.hearticon);
			break;
		case 2:
			s.setImageResource(drawable.planeschaseicon);
			break;
		case 3:
			s.setImageResource(drawable.archenemyicon);
			break;
		case 4:
			s.setImageResource(drawable.advancedsearchicon);
			break;
		case 5:
			s.setImageResource(drawable.shopping_cart);
			break;
		case 6:
			s.setImageResource(drawable.search_icon);
			break;
		}
		
		View vw = (View) v.findViewById(id.LINE1);
		if(data.get(position).length() < 1){
			
			vw.setVisibility(View.INVISIBLE);
		}

		return v;
	}

}
