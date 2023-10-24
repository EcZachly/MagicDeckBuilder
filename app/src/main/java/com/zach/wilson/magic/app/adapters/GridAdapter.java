package com.zach.wilson.magic.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.models.Deck;

/**
 * Created by Zach on 8/20/2014.
 */
public class GridAdapter extends ArrayAdapter<Deck> {

    Deck[] decks;
    Context context;
    LayoutInflater inflater;
    int resource;
    public GridAdapter(Context context, int resource, Deck[] decks, LayoutInflater inflater) {
     super(context, resource);
      this.context = context;
      this.decks = decks;
        this.inflater = inflater;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return decks.length;
    }

    @Override
    public Deck getItem(int position) {
        return decks[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View v = convertView;

        if(v == null){
        v = inflater.inflate(resource, null);

        TextView author =(TextView)v.findViewById(R.id.authorName);
            TextView deckname =(TextView)v.findViewById(R.id.deckName);
            TextView tournament =(TextView)v.findViewById(R.id.formatName);
                author.setText(decks[position].getCreator());
                deckname.setText(decks[position].getName());
                tournament.setText(decks[position].getFormat());


        }

        return v;
    }
}
