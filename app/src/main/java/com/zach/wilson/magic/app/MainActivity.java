package com.zach.wilson.magic.app;

import android.app.Fragment;
import android.app.FragmentManager;

import android.app.SearchManager;
import android.content.Context;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;

import android.os.AsyncTask;
import android.os.Build;
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
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;


import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zach.wilson.magic.app.adapters.MyDrawerAdapter;
import com.zach.wilson.magic.app.fragments.AddDeckFragment;
import com.zach.wilson.magic.app.fragments.AdvancedSearchFragment;
import com.zach.wilson.magic.app.fragments.ArchenemyFragment;
import com.zach.wilson.magic.app.fragments.CardCarouselFragment;
import com.zach.wilson.magic.app.fragments.DiscoveryFragment;
import com.zach.wilson.magic.app.fragments.LifeCounterFragment;
import com.zach.wilson.magic.app.fragments.MyCartFragment;
import com.zach.wilson.magic.app.fragments.MyDeckFragment;
import com.zach.wilson.magic.app.fragments.PlaneschaseFragment;
import com.zach.wilson.magic.app.fragments.SearchFragment;
import com.zach.wilson.magic.app.helpers.AppRater;
import com.zach.wilson.magic.app.helpers.DeckBrewClient;
import com.zach.wilson.magic.app.helpers.TCGClient;
import com.zach.wilson.magic.app.models.Card;
import com.zach.wilson.magic.app.models.CardList;
import com.zach.wilson.magic.app.models.Deck;
import com.zach.wilson.magic.app.models.Set;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
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
    Deck[] decks;
    MenuItem lifeCounter;

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

        if (!this.getSharedPreferences("DECKS", Context.MODE_PRIVATE).getAll()
                .isEmpty()) {
            this.getSharedPreferences("DECKS", Context.MODE_PRIVATE).edit()
                    .clear().commit();
        }

        AppRater.app_launched(this);
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


        setUpTintBar();
        if (getFragmentManager().findFragmentByTag("PLANESCHASEFRAGMENT") == null) {
            Random ra = new Random();
            int za = ra.nextInt(140);
            DeckBrewClient.getAPI();
            DeckBrewClient.deckbrew.getRandomCards(za, new Callback<Card[]>() {
                @Override
                public void success(Card[] cards, Response response) {
                    CardCarouselFragment f = CardCarouselFragment.newInstance(cards, false, false);
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, f).commit();
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                }

            });

        } else {
            PlaneschaseFragment f = (PlaneschaseFragment) getFragmentManager().findFragmentByTag("PLANESCHASEFRAGMENT");

            getFragmentManager().beginTransaction().replace(R.id.content_frame, f, "PLANESCHASEFRAGMENT").commit();
        }
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
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        lifeCounter = menu
                .findItem(R.id.filteringActionBar);


        lifeCounter.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (CardCarouselFragment.getFilterLayout().getVisibility() == View.GONE) {

                    expand(CardCarouselFragment.getFilterLayout());


                } else {
                    collapse(CardCarouselFragment.getFilterLayout());
                }


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
                        .replace(R.id.content_frame, mycartFragment, "MYCARTFRAGMENT").commit();
                return false;
            }

        });

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setOnSearchClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               Intent t = new Intent(context, SearchActivity.class);
                startActivity(t);
                searchView.setIconified(true);

//                searchFragment = new SearchFragment();
//                Bundle args = new Bundle();
//                searchFragment.setArguments(args);
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.content_frame, searchFragment, "SEARCHFRAGMENT").commit();

            }

        });
        searchView.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    DeckBrewClient.getAPI();
                    DeckBrewClient.deckbrew.getCardsFromStart(newText, new Callback<List<Card>>() {

                        @Override
                        public void success(List<Card> cards, Response response) {
                            searchingFragment = new SearchFragment();
                            lifeCounter.setVisible(false);
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
                lifeCounter.setVisible(false);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, archenemyFragment, "ARCHENEMYFRAGMENT").commit();
                currentFragment = archenemyFragment;

                break;
            case 4:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                lifeCounter.setVisible(false);
                if (mydeckFragment == null) {
                    mydeckFragment = new MyDeckFragment();
                }
                f = fragmentManager.findFragmentByTag("CardCarousel");
                if (f != null) {
                    fragmentManager.beginTransaction().remove(f).commit();

                }
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, mydeckFragment, "MYDECKFRAGMENT").commit();
                currentFragment = mydeckFragment;


                break;
            case 6:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                lifeCounter.setVisible(false);
                if (advancedSearchFragment == null) {
                    advancedSearchFragment = AdvancedSearchFragment.newInstance();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, advancedSearchFragment, "ADVSEARCHFRAGMENT")
                        .commit();
                currentFragment = advancedSearchFragment;

                break;
            case 2:

                lifeCounter.setVisible(false);
                if (planesChaseFragment == null) {
                    planesChaseFragment = new PlaneschaseFragment();

                }
                f = fragmentManager.findFragmentByTag("CardCarousel");
                if (f != null) {
                    fragmentManager.beginTransaction().remove(f).commit();

                }

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, planesChaseFragment, "PLANESCHASEFRAGMENT").commit();
                currentFragment = planesChaseFragment;
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                break;
            case 1:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                lifeCounter.setVisible(false);
                if (lifeCounterFragment == null) {
                    lifeCounterFragment = new LifeCounterFragment();
                }
                f = fragmentManager.findFragmentByTag("CardCarousel");
                if (f != null) {
                    fragmentManager.beginTransaction().remove(f).commit();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, lifeCounterFragment, "LIFECOUNTERFRAGMENT").commit();
                currentFragment = lifeCounterFragment;

                break;
            case 5:
                lifeCounter.setVisible(false);
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (mycartFragment == null) {
                    mycartFragment = new MyCartFragment();
                }
                f = fragmentManager.findFragmentByTag("CardCarousel");
                if (f != null) {
                    fragmentManager.beginTransaction().remove(f).commit();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, mycartFragment, "MYCARTFRAGMENT").commit();
                currentFragment = mycartFragment;


                break;
            case 0:
                lifeCounter.setVisible(true);

                Random r = new Random();
                String z = CardList.allCards + r.nextInt(140);
                DeckBrewClient.getAPI().getRandomCards(r.nextInt(140), new Callback<Card[]>() {
                    @Override
                    public void success(Card[] cards, Response response) {
                        Bundle args = new Bundle();
                        args.putSerializable("CARDS FROM MAIN", cards);
                        CardCarouselFragment f = CardCarouselFragment.newInstance(cards, false, false);
                        getFragmentManager().beginTransaction().replace(R.id.content_frame, f).commit();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        TCGClient.instantiate();

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
                break;


            case 7:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                new AsyncTask<String, String, String>() {


                    @Override
                    protected String doInBackground(String... strings) {
                       decks =  readInDecks().get(0);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        Fragment f;
                        if (mycartFragment == null) {
                            mycartFragment = DiscoveryFragment.newInstance(decks, "BLANK");
                        }
                        f = getFragmentManager().findFragmentByTag("CardCarousel");
                        if (f != null) {
                            getFragmentManager().beginTransaction().remove(f).commit();
                        }
                        getFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, mycartFragment).commit();
                        currentFragment = mycartFragment;
                    }
                }.execute("");


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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }


    public SearchView getSearchView() {
        return searchView;
    }


    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    public ArrayList<Deck[]> readInDecks() {
        ArrayList<Deck[]> decks = new ArrayList<Deck[]>();
        Gson gson = new Gson();
        try {
            AssetManager as = getAssets();

         InputStream is = as.open("cards.txt");

                String temp = IOUtils.toString(is);
                decks.add(gson.fromJson(temp, Deck[].class));
        } catch (IOException e) {
            Log.i("ERROR", e.getMessage());
            Log.i("ERROR", e.getStackTrace().toString());


        }

        return  decks;
    }



}
