package com.zach.wilson.magic.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TableLayout;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zach.wilson.magic.app.adapters.MyDrawerAdapter;
import com.zach.wilson.magic.app.fragments.AddDeckFragment;
import com.zach.wilson.magic.app.fragments.AdvancedSearchFragment;
import com.zach.wilson.magic.app.fragments.ArchenemyFragment;
import com.zach.wilson.magic.app.fragments.CardCarouselFragment;
import com.zach.wilson.magic.app.fragments.LifeCounterFragment;
import com.zach.wilson.magic.app.fragments.MyCartFragment;
import com.zach.wilson.magic.app.fragments.MyDeckFragment;
import com.zach.wilson.magic.app.fragments.PlaneschaseFragment;
import com.zach.wilson.magic.app.fragments.SearchFragment;
import com.zach.wilson.magic.app.helpers.DeckBrewClient;
import com.zach.wilson.magic.app.helpers.MagicAppSettings;
import com.zach.wilson.magic.app.helpers.TCGClient;
import com.zach.wilson.magic.app.models.Card;
import com.zach.wilson.magic.app.models.CardList;
import com.zach.wilson.magic.app.models.Product;
import com.zach.wilson.magic.app.models.Products;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends FragmentActivity implements
        SearchFragment.OnItemSelectedListener {

    static String URL = "https://api.deckbrew.com/mtg/cards/typeahead?q=";
    static ArrayList<String> cardCart;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private SearchView searchView;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence title;
    AsyncTask<String, Integer, String> task;
    private ProgressDialog dialog;
    ArrayList<String> potentialNames = new ArrayList<String>();
    String searchString;
    Resources r;
    LinearLayout layout;
    Dialog priceDialog;
    Card[] cards;
    String jsonCards;
    Button search;
    ListView list;
    Button random;
    OnClickListener buttons;
    String URLP;
    TableLayout main;
    Drawable pic;
    SharedPreferences prefs;
    Bundle extras;

    String[] urls;
    String[] namesFromURLS;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    String pricingName;
    String pricingURL;
    Fragment currentFragment;
    Fragment archenemyFragment;
    SearchFragment searchFragment;
    SearchFragment searchingFragment;
    Fragment verticalpagerfragment;
    Fragment mydeckFragment;
    Fragment mycartFragment;
    Fragment addDeckFragment;
    Fragment planesChaseFragment;
    Fragment randomCarouselFragment;
    Fragment advancedSearchFragment;
    Fragment lifeCounterFragment;
    LinearLayout rightDrawer;
    Context context;


    AsyncTask<String, String, String> taskLoad;
    Activity activity;
    String[] jsonStrings;
    ArrayList<Card[]> cardsList;

    static String[] fragmentNames = {"Archenemy", "Add Deck",
            "Advanced Search", "Random Carousel", "View Card"};
    MagicAppSettings appState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appState = (MagicAppSettings) this.getApplicationContext();
        appState.setContextForPreferences(this.getBaseContext(), this);
        if (!this.getSharedPreferences("DECKS", Context.MODE_PRIVATE).getAll()
                .isEmpty()) {
            this.getSharedPreferences("DECKS", Context.MODE_PRIVATE).edit()
                    .clear().commit();
        }


        appState.setManager(getFragmentManager());
        cardCart = new ArrayList<String>();
        rightDrawer = (LinearLayout) findViewById(R.id.rightDrawerLayout);
        context = this.getBaseContext();
        searchFragment = new SearchFragment();
        archenemyFragment = new ArchenemyFragment();
        addDeckFragment = new AddDeckFragment();

        advancedSearchFragment = new AdvancedSearchFragment();
        dialog = new ProgressDialog(this);
        initImageLoader(this);
        ArrayList<String> temp = new ArrayList<String>();
        title = getActionBar().getTitle();
        mPlanetTitles = getResources()
                .getStringArray(R.array.operating_systems);
        for (int i = 0; i < mPlanetTitles.length; i++) {
            temp.add(mPlanetTitles[i]);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        MyDrawerAdapter ad = new MyDrawerAdapter(this, R.layout.drawer_item,
                temp);
        mDrawerList.setAdapter(ad);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                R.drawable.navigationdrawericon, /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */) {

            /** Called when a drawer has settled in a completely closed state. */

            public void onDrawerClosed(View view) {
                getActionBar().setTitle(title);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }
        };
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        Random r = new Random();
        String z = CardList.allCards + r.nextInt(140);
        if (appState.getCardsInCarousel() == null) {
            Random ra = new Random();
            int za = ra.nextInt(140);
            DeckBrewClient.getAPI();
            DeckBrewClient.deckbrew.getRandomCards(za, new Callback<Card[]>() {
                @Override
                public void success(Card[] cards, Response response) {
                    appState.setCardsInCarousel(new Card[cards.length]);
                    for (int i = 0; i < cards.length; i++) {
                        appState.getCardsInCarousel()[i] = cards[i];
                    }
                    CardCarouselFragment f = new CardCarouselFragment();
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, f).commit();
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                }

            });

        }
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        MenuItem lifeCounter = (MenuItem) menu
                .findItem(R.id.lifeCounterActionBar);
        lifeCounter.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (lifeCounterFragment == null) {
                    lifeCounterFragment = new LifeCounterFragment();
                }
                FragmentManager fragmentManager = getFragmentManager();
                Fragment f;
                f = fragmentManager.findFragmentByTag("CardCarousel");
                if (f != null) {
                    fragmentManager.beginTransaction().remove(f).commit();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, lifeCounterFragment)
                        .commit();
                return false;
            }

        });

        MenuItem myCart = (MenuItem) menu.findItem(R.id.myCart);
        myCart.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mycartFragment == null) {
                    mycartFragment = new MyCartFragment();
                }
                FragmentManager fragmentManager = getFragmentManager();
                Fragment f;
                f = fragmentManager.findFragmentByTag("CardCarousel");
                if (f != null) {
                    fragmentManager.beginTransaction().remove(f).commit();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, mycartFragment).commit();
                return false;
            }

        });

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setOnSearchClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                searchFragment = new SearchFragment();
                Bundle args = new Bundle();
                searchFragment.setArguments(args);
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, searchFragment).commit();

            }

        });
        searchView.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    if (task != null) {
                        task.cancel(true);
                    }

                    task = new SearchingForNames().execute(URL
                            + Uri.encode(newText));
                } else {

                    if (task != null) {
                        task.cancel(true);
                    }

                    searchView.setIconified(true);
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Swaps fragments in the main content view
     */

    private void selectItem(int position) {
        searchView.setIconified(true);
        if (taskLoad != null) {
            taskLoad.cancel(true);
        }
        Fragment f;
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {

            case 3:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                if (archenemyFragment == null) {
                    archenemyFragment = new ArchenemyFragment();
                }
                f = fragmentManager.findFragmentByTag("CardCarousel");
                if (f != null) {
                    fragmentManager.beginTransaction().remove(f).commit();

                }

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, archenemyFragment).commit();
                currentFragment = archenemyFragment;

                break;
            case 4:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                if (mydeckFragment == null) {
                    mydeckFragment = new MyDeckFragment();
                }
                f = fragmentManager.findFragmentByTag("CardCarousel");
                if (f != null) {
                    fragmentManager.beginTransaction().remove(f).commit();

                }
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, mydeckFragment).commit();
                currentFragment = mydeckFragment;


                break;
            case 6:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                if (advancedSearchFragment == null) {
                    advancedSearchFragment = new AdvancedSearchFragment();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, advancedSearchFragment)
                        .commit();
                currentFragment = advancedSearchFragment;

                break;
            case 2:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                if (planesChaseFragment == null) {
                    planesChaseFragment = new PlaneschaseFragment();

                }
                f = fragmentManager.findFragmentByTag("CardCarousel");
                if (f != null) {
                    fragmentManager.beginTransaction().remove(f).commit();

                }

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, planesChaseFragment).commit();
                currentFragment = planesChaseFragment;


                break;
            case 1:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                if (lifeCounterFragment == null) {
                    lifeCounterFragment = new LifeCounterFragment();
                }
                f = fragmentManager.findFragmentByTag("CardCarousel");
                if (f != null) {
                    fragmentManager.beginTransaction().remove(f).commit();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, lifeCounterFragment).commit();
                currentFragment = lifeCounterFragment;

                break;
            case 5:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (mycartFragment == null) {
                    mycartFragment = new MyCartFragment();
                }
                f = fragmentManager.findFragmentByTag("CardCarousel");
                if (f != null) {
                    fragmentManager.beginTransaction().remove(f).commit();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, mycartFragment).commit();
                currentFragment = mycartFragment;


                break;
            case 0:
                appState.setCardsInCarousel(null);
                Random r = new Random();
                String z = CardList.allCards + r.nextInt(140);
                DeckBrewClient.getAPI().getRandomCards(r.nextInt(140), new Callback<Card[]>() {
                    @Override
                    public void success(Card[] cards, Response response) {
                        appState.setCardsInCarousel(new Card[cards.length]);
                        for (int i = 0; i < cards.length; i++) {
                            appState.getCardsInCarousel()[i] = cards[i];
                        }
                        CardCarouselFragment f = new CardCarouselFragment();
                        getFragmentManager().beginTransaction().replace(R.id.content_frame, f).commit();

                        TCGClient.instantiate();
                        for(int i = 0; i < 15; i++)
                        TCGClient.pricing.getProductPrice("MAGICVIEW", cards[i].getEditions()[0].getSet(), cards[i].getName(), new Callback<Products>(){

                            @Override
                            public void success(Products products, Response response) {
                                Log.i("HERE", products.getProducts().getLowprice());

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                   Log.i("ERROR", error.getMessage());
                                    Log.i("ERROR", error.getUrl());
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
                break;

        }
        mDrawerLayout.closeDrawers();
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
    }

    @Override
    public void onRssItemSelected(String link) {

    }

    class SearchingForNames extends AsyncTask<String, Integer, String> {

        Card[] cardsFromSearching;

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(params[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                } else {
                    // Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                // TODO Handle problems..
            } catch (IOException e) {
                // TODO Handle problems..
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            jsonCards = result;
            StringReader reader = new StringReader(jsonCards);
            jsonParse(reader);
            ArrayList<String> names = new ArrayList<String>();
            ArrayList<String> urls = new ArrayList<String>();
            searchingFragment = new SearchFragment();
            Bundle args = new Bundle();
            CardList.currentCardList = new ArrayList<Card>();
            names = new ArrayList<String>();
            for (int i = 0; i < cardsFromSearching.length; i++) {
                names.add(cardsFromSearching[i].getName());
                urls.add(cardsFromSearching[i].getEditions()[0].getImage_url());
                CardList.currentCardList.add(cardsFromSearching[i]);
            }
            args.putStringArrayList("POTENTIAL", names);
            args.putStringArrayList("URLS", urls);
            searchingFragment.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, searchingFragment).commit();
        }

        private void jsonParse(Reader r) {
            Gson gson = new Gson();
            cardsFromSearching = gson.fromJson(r, Card[].class);
        }
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;

    }

}
