package com.zach.wilson.magic.app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.SearchView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.fragments.SearchFragment;
import com.zach.wilson.magic.app.helpers.DeckBrewClient;
import com.zach.wilson.magic.app.models.Card;
import com.zach.wilson.magic.app.models.CardList;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;



public class SearchActivity extends Activity {

    View searchPlate;

    public SearchView getSearch() {
        return search;
    }

    public void setSearch(SearchView search) {
        this.search = search;
    }

    Fragment searchingFragment;
    SearchView search;
    @Override
    public void onPause(){
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        setUpTintBar();
        setContentView(R.layout.activity_search);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);

        search = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();


        // Getting id for 'search_plate' - the id is part of generate R file,
        // so we have to get id on runtime.
        int searchPlateId = search.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        // Getting the 'search_plate' LinearLayout.
        searchPlate = search.findViewById(searchPlateId);
        // Setting background of 'search_plate' to earlier defined drawable.
        searchPlate.setBackgroundResource(R.drawable.textfield_searchview_holo_light);


        search.setEnabled(true);
        search.setIconified(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    DeckBrewClient.getAPI();
                    DeckBrewClient.deckbrew.getCardsFromStart(newText, new Callback<List<Card>>() {

                        @Override
                        public void success(List<Card> cards, Response response) {
                            searchingFragment = new SearchFragment();
                            Bundle args = new Bundle();
                            CardList.currentCardList = new ArrayList<Card>();
                            ArrayList<String> names = new ArrayList<String>();
                            ArrayList<String> URLS = new ArrayList<String>();
                            for (int i = 0; i < cards.size(); i++) {
                                names.add(cards.get(i).getName());
                                URLS.add(cards.get(i).getEditions()[0].getImage_url());
                                CardList.currentCardList.add(cards.get(i));
                            }
                            args.putStringArrayList("POTENTIAL", names);
                            args.putStringArrayList("URLS", URLS);
                            searchingFragment.setArguments(args);
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, searchingFragment).commit();
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });


                } else {


                    search.setIconified(true);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                return true;
            }

        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setUpTintBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            findViewById(android.R.id.content).setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(), config.getPixelInsetBottom());
        }
        tintManager.setTintColor(getResources().getColor(R.color.gray_action));
    }

}
