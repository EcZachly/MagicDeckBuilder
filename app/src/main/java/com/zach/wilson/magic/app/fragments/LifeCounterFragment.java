package com.zach.wilson.magic.app.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.R.id;
import com.zach.wilson.magic.app.R.layout;
import com.zach.wilson.magic.app.adapters.LifeCounterAdapter;
import com.zach.wilson.magic.app.models.CardList;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class LifeCounterFragment extends Fragment {

	LayoutInflater inflater;
	ListView playerList;
	LifeCounterAdapter adapter;
	Button addPlayer;
	Button resetGame;
	Dialog addPlayerDialog;

    static HashMap<String, Integer> lifeTotals;
    static ArrayList<String> playerNames;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;
	
		View v = inflater.inflate(layout.lifecounterlayoutlist, null);
		addPlayerDialog = new Dialog(v.getContext());
		addPlayer = (Button) v.findViewById(id.addPlayer);
		resetGame = (Button) v.findViewById(id.resetGame);
		playerList = (ListView) v.findViewById(id.lifeCounterList);

		if(playerNames == null){
            playerNames = new ArrayList<String>();
            lifeTotals = new HashMap<String, Integer>();
        }


		if(adapter == null) {
            adapter = new LifeCounterAdapter(v.getContext(),
                    id.lifeCounterLayout, playerNames, lifeTotals);
            playerList.setAdapter(adapter);
        }
        else{
            adapter.notifyDataSetChanged();
            playerList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
		addPlayer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				addPlayerDialog.setContentView(layout.addplayerdialog);
				final EditText edit = (EditText) addPlayerDialog
						.findViewById(id.editTextViewAddPlayer);
				addPlayerDialog.setTitle("ADD PLAYER");
				addPlayerDialog.setCanceledOnTouchOutside(true);
				Button add = (Button) addPlayerDialog
						.findViewById(id.addPlayerButtonDialog);

				add.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String temp = edit.getText().toString();
						edit.setText("");
						playerNames.add(temp);
                        lifeTotals.put(temp, 20);
						LifeCounterAdapter.setLifeTotals(lifeTotals);
                        LifeCounterAdapter.setPlayerNames(playerNames);
                        adapter.notifyDataSetChanged();


					}

				});

				addPlayerDialog.show();
			}

		});
		resetGame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

                for(String s : playerNames){
                    lifeTotals.put(s, 20);
                }

                LifeCounterAdapter.setLifeTotals(lifeTotals);
                LifeCounterAdapter.setPlayerNames(playerNames);

			    adapter.notifyDataSetChanged();
               playerList.setAdapter(adapter);

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
