package com.zach.wilson.magic.app.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.zach.wilson.magic.app.ItemDetailActivity;
import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.adapters.GridAdapter;
import com.zach.wilson.magic.app.helpers.TappedOutClient;
import com.zach.wilson.magic.app.models.Deck;
import com.zach.wilson.magic.app.models.DeckList;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiscoveryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiscoveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DiscoveryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Deck[] standardDecks;
    private Context context;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

  //  private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoveryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoveryFragment newInstance(Deck[] decks, String param2) {
        DiscoveryFragment fragment = new DiscoveryFragment();
        Bundle args = new Bundle();
        args.putSerializable("STANDARD DECKS", decks);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public DiscoveryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            standardDecks = (Deck[]) getArguments().getSerializable("STANDARD DECKS");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.gridview, null);

        context = v.getContext();
        String[] s = new String[standardDecks.length];

           for(int i = 0; i < s.length; i++){
               s[i] = standardDecks[i].getName();
           }
        GridView g = (GridView) v.findViewById(R.id.gridlayout);

       final GridAdapter adapter = new GridAdapter(v.getContext(), R.layout.boxgriddetail, standardDecks, inflater);


        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               Deck d = adapter.getItem(i);
                Intent t = new Intent(context, ItemDetailActivity.class);
                t.putExtra("DECK", d);

                startActivity(t);




            }



        });

        g.setAdapter(adapter);



        return v;
    }
}
