package com.zach.wilson.magic.app.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.R.drawable;
import com.zach.wilson.magic.app.R.id;
import com.zach.wilson.magic.app.R.layout;
import com.zach.wilson.magic.app.fragments.LifeCounterFragment;
import com.zach.wilson.magic.app.models.CardList;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class LifeCounterAdapter extends ArrayAdapter<String> {

	TextView name;
	ArrayList<String> data;
	Context context;
	int resource;
   static ArrayList<String> playerNames;
    static HashMap<String, Integer> lifeTotals;


	public LifeCounterAdapter(Context context, int resource, ArrayList<String> players, HashMap<String, Integer> lifeTotals) {
		super(context, resource);
		this.context = context;
	    this.playerNames = players;
        this.lifeTotals = lifeTotals;

	}

	@Override
	public int getCount() {
		return playerNames.size();
	}


	@Override
	public String getItem(int position) {
		return data.get(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(layout.lifecounterlayoutitem, null);
		}
		final TextView name = (TextView) v.findViewById(id.txtViewName);
		final ImageView image = (ImageView) v.findViewById(id.plus1Life);
		final TextView totalLife = (TextView) v
				.findViewById(id.txtViewLifeCounter);
		final TextView verticalView = (TextView) v
				.findViewById(id.verticalText);
		name.setText(playerNames.get(position));
		verticalView.setText("LIFE");
		name.setText(playerNames.get(position));
		image.setImageResource(drawable.heart);
        totalLife.setText(String.valueOf(lifeTotals.get(playerNames.get(position))));
		final Button plusLife = (Button) v.findViewById(id.txtViewLifePlus);
		final Button minusLife = (Button) v.findViewById(id.txtViewLifeMinus);

		plusLife.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int oldLife = lifeTotals.get(playerNames.get(position));

				oldLife++;

				lifeTotals.put(playerNames.get(position), oldLife);
                LifeCounterFragment.setLifeTotals(lifeTotals);


				totalLife.setText(String.valueOf(oldLife));
			}
		});

		minusLife.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                int oldLife = lifeTotals.get(playerNames.get(position));

                oldLife--;

                lifeTotals.put(playerNames.get(position), oldLife);
                LifeCounterFragment.setLifeTotals(lifeTotals);


                totalLife.setText(String.valueOf(oldLife));
			}
		});

		return v;
	}


    public static void setLifeTotals(HashMap<String,Integer> life){
        lifeTotals = life;
    }
    public static void setPlayerNames(ArrayList<String> names){
        playerNames = names;
    }


}
