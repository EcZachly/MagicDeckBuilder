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

import com.flurry.android.FlurryAgent;
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
import com.zach.wilson.magic.app.helpers.AppRater;
import com.zach.wilson.magic.app.helpers.DeckBrewClient;
import com.zach.wilson.magic.app.helpers.MagicAppSettings;
import com.zach.wilson.magic.app.helpers.TCGClient;
import com.zach.wilson.magic.app.models.Card;
import com.zach.wilson.magic.app.models.CardList;
import com.zach.wilson.magic.app.models.Product;
import com.zach.wilson.magic.app.models.Products;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends FragmentActivity implements
        SearchFragment.OnItemSelectedListener {
    static ArrayList<String> cardCart;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private SearchView searchView;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence title;
    AsyncTask<String, Integer, String> task;
    Fragment currentFragment;
    Fragment archenemyFragment;
    SearchFragment searchFragment;
    SearchFragment searchingFragment;
    Fragment mydeckFragment;
    Fragment mycartFragment;
    Fragment addDeckFragment;
    Fragment planesChaseFragment;
    Fragment advancedSearchFragment;
    Fragment lifeCounterFragment;
    LinearLayout rightDrawer;
    Context context;
    MagicAppSettings appState;

    @Override
    protected void onStop() {
        FlurryAgent.onEndSession(this);
        super.onStop();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FlurryAgent.onStartSession(this, "4P837N5N2QZSC2BGC3V3");
        appState = (MagicAppSettings) this.getApplicationContext();
        appState.setContextForPreferences(this.getBaseContext(), this);
        if (!this.getSharedPreferences("DECKS", Context.MODE_PRIVATE).getAll()
                .isEmpty()) {
            this.getSharedPreferences("DECKS", Context.MODE_PRIVATE).edit()
                    .clear().commit();
        }

        AppRater.app_launched(this);
        appState.setManager(getFragmentManager());
        cardCart = new ArrayList<String>();
        rightDrawer = (LinearLayout) findViewById(R.id.rightDrawerLayout);
        context = this.getBaseContext();
        searchFragment = new SearchFragment();
        archenemyFragment = new ArchenemyFragment();
        addDeckFragment = new AddDeckFragment();

        advancedSearchFragment = new AdvancedSearchFragment();
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
                R.drawable.ic_navigation_drawer, /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(title);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
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
                 DeckBrewClient.getAPI();
                    DeckBrewClient.deckbrew.getCardsFromStart(newText, new Callback<List<Card>>(){

                        @Override
                        public void success(List<Card> cards, Response response) {
                                searchingFragment = new SearchFragment();
                            Bundle args = new Bundle();
                            CardList.currentCardList = new ArrayList<Card>();
                            ArrayList<String>names = new ArrayList<String>();
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
        FlurryAgent.logEvent("Drawer Selected Item: " + position);

        searchView.setIconified(true);
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
