package com.zach.wilson.magic.app.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.R.id;
import com.zach.wilson.magic.app.R.layout;
import com.zach.wilson.magic.app.adapters.LifeCounterAdapter;
import com.zach.wilson.magic.app.helpers.MagicAppSettings;
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
	ArrayList<String> playerNames;
	Dialog addPlayerDialog;
	MagicAppSettings appState;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;
	
		View v = inflater.inflate(layout.lifecounterlayoutlist, null);
		addPlayerDialog = new Dialog(v.getContext());
		addPlayer = (Button) v.findViewById(id.addPlayer);
		resetGame = (Button) v.findViewById(id.resetGame);
		appState = (MagicAppSettings) getActivity().getApplication();
		playerList = (ListView) v.findViewById(id.lifeCounterList);

		
		if(appState.getPlayerNames() == null){
			appState.setPlayerNames(new ArrayList<String>());
			appState.setLifeTotals(new HashMap<String, Integer>());
			appState.setPoisonTotals(new HashMap<String, Integer>());
		}
		
		
		adapter = new LifeCounterAdapter(v.getContext(),
				id.lifeCounterLayout, getActivity().getApplication());
		playerList.setAdapter(adapter);
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
						appState.getLifeTotals().put(temp, 20);
						appState.getPoisonTotals().put(temp, 0);
						if (appState.getPlayerNames() == null) {
							appState.getPlayerNames().add(temp);
						} else {
							appState.getPlayerNames().add(temp);
						}
						adapter = new LifeCounterAdapter(v.getContext(),
								id.lifeCounterLayout, getActivity()
										.getApplication());
						playerList.setAdapter(adapter);

					}

				});

				addPlayerDialog.show();
			}

		});
		resetGame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				adapter = new LifeCounterAdapter(v.getContext(),
						id.lifeCounterLayout, getActivity().getApplication());
				playerList.setAdapter(adapter);

			}

		});
		return v;
	}
	
	
	
	
	
}
