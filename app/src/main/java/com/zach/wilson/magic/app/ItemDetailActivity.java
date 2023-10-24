package com.zach.wilson.magic.app;

import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zach.wilson.magic.app.fragments.ItemDetailFragment;
import com.zach.wilson.magic.app.helpers.JazzyViewPager;
import com.zach.wilson.magic.app.helpers.TCGClient;
import com.zach.wilson.magic.app.models.Card;
import com.zach.wilson.magic.app.models.CardList;
import com.zach.wilson.magic.app.models.Deck;
import com.zach.wilson.magic.app.models.Products;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ItemDetailActivity extends FragmentActivity {

    Deck d;
    TextView high;
    TextView mid;
    TextView low;
    TextView foil;
    DrawerLayout mDrawerLayout;
    ArrayList<String> cards;
    JazzyViewPager pager;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    Context context;
    ListView list;
    ListView sideBoard;
    SharedPreferences prefs;
    ArrayAdapter<String> adapter;
    String[] cardNames;
    ImageView shareCard, deleteCard;
    LinearLayout rightDrawer;
    ProgressDialog dialog;
    ImagePagerAdapter imgAdapter;
    ImagePagerAdapter sideImgAdapter;

    boolean fromMyDecks = false;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        setContentView(R.layout.deckdetaillayout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        setUpTintBar();
        context = this.getApplicationContext();
        list = (ListView)this.findViewById(R.id.myDeckLists);
        TextView t = new TextView(this);
        t.setText("Main Board");

        list.addHeaderView(t);


        sideBoard = (ListView) this.findViewById(R.id.mySideBoard);
        TextView tx = new TextView(this);
        t.setText("Side Board");

        sideBoard.addHeaderView(tx);

        prefs = this.getSharedPreferences("DECKSNEW",
                Context.MODE_PRIVATE);
        if(getIntent().getExtras().containsKey("FROMMYDECKS")){
            fromMyDecks = true;
        }
        mDrawerLayout = (DrawerLayout)this.findViewById(
                R.id.drawerLayoutForCards);
        rightDrawer = (LinearLayout) this.findViewById(R.id.rightDrawerLayout);
        dialog = new ProgressDialog(rightDrawer.getContext());
        shareCard = (ImageView) rightDrawer.findViewById(R.id.shareCard);
        deleteCard = (ImageView) rightDrawer
                .findViewById(R.id.removeCardFromDeck);
        pager = (JazzyViewPager) rightDrawer.findViewById(R.id.card_pager);
        high = (TextView) rightDrawer.findViewById(R.id.highPrice);
        mid = (TextView) rightDrawer.findViewById(R.id.meanPrice);
        low = (TextView) rightDrawer.findViewById(R.id.lowPrice);
        foil = (TextView) rightDrawer.findViewById(R.id.foilPrice);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .resetViewBeforeLoading(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        d = (Deck) getIntent().getSerializableExtra("DECK");
        ArrayList<String> urls = new ArrayList<String>();
        ArrayList<String> sideURLs = new ArrayList<String>();

        for(Card c : d.getMainBoard()){
            urls.add(c.getEditions()[0].getImage_url());
        }
        for(Card c : d.getSideBoard()){
            sideURLs.add(c.getEditions()[0].getImage_url());
        }

        imgAdapter = new ImagePagerAdapter(urls, getLayoutInflater());
        sideImgAdapter = new ImagePagerAdapter(sideURLs, getLayoutInflater());
        pager.setAdapter(imgAdapter);
        cards = (ArrayList<String>) getIntent().getSerializableExtra("CARDNAMES");

        Log.i("d", d.getName());
        if(!fromMyDecks) {
            if (d.getSideBoard() != null)
                cardNames = new String[d.getMainBoard().length + d.getSideBoard().length];
        }
        else{
            cardNames = new String[d.getMainBoard().length];

        }

        int i = 0;

        for(; i < d.getMainBoard().length; i++){
          if(!fromMyDecks) {
              if (d.getMainBoard()[i] != null)
                  cardNames[i] = d.getMainBoard()[i].getQuantity() + "x " + d.getMainBoard()[i].getName();
          }
            else {
              cardNames[i] = cards.get(i);
          }
        }
        int j = 0;
        int total;
        if(d.getSideBoard() != null)
        total = d.getMainBoard().length  + d.getSideBoard().length;
        else
        total = d.getMainBoard().length;

        for(; i < total; i++){
            cardNames[i] = d.getSideBoard()[j].getQuantity() + "x " + d.getSideBoard()[j].getName();
            j++;
        }


        adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_list_item_1, cardNames);


        list.setAdapter(adapter);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {

                pager.setAdapter(imgAdapter);

                TCGClient.pricing.getProductPrice("MAGICVIEW", "", d.getMainBoard()[arg2].getName(), new Callback<Products>() {
                    @Override
                    public void success(Products products, Response response) {
                        if (!products.getProducts().getFoilavgprice().equals("0")) {
                            foil.setText("$" + products.getProducts().getFoilavgprice());
                        } else {
                            foil.setVisibility(View.GONE);
                            TextView temp = (TextView) rightDrawer
                                    .findViewById(R.id.foilPriceWords);
                            temp.setVisibility(View.GONE);

                        }
                        high.setText("$" + products.getProducts().getHiprice());
                        mid.setText("$" + products.getProducts().getAvgprice());
                        low.setText("$" + products.getProducts().getLowprice());
                        mDrawerLayout.openDrawer(rightDrawer);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("Error", error.getUrl());


                    }
                } );
                pager.setCurrentItem(arg2);


            }

        });

        sideBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {


                pager.setAdapter(sideImgAdapter);

                TCGClient.pricing.getProductPrice("MAGICVIEW", "", d.getSideBoard()[arg2].getName(), new Callback<Products>() {
                    @Override
                    public void success(Products products, Response response) {
                        if (!products.getProducts().getFoilavgprice().equals("0")) {
                            foil.setText("$" + products.getProducts().getFoilavgprice());
                        } else {
                            foil.setVisibility(View.GONE);
                            TextView temp = (TextView) rightDrawer
                                    .findViewById(R.id.foilPriceWords);
                            temp.setVisibility(View.GONE);

                        }
                        high.setText("$" + products.getProducts().getHiprice());
                        mid.setText("$" + products.getProducts().getAvgprice());
                        low.setText("$" + products.getProducts().getLowprice());
                        mDrawerLayout.openDrawer(rightDrawer);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("Error", error.getUrl());


                    }
                } );
                pager.setCurrentItem(arg2);


            }

        });

	}


    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			NavUtils.navigateUpTo(this,
					new Intent(this, ItemListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



    private class ImagePagerAdapter extends PagerAdapter {

        private String[] images;
        private LayoutInflater inflater;

        ImagePagerAdapter(ArrayList<String> images, LayoutInflater inflater) {
            this.images = new String[images.size()];
            for (int i = 0; i < images.size(); i++)
                this.images[i] = images.get(i);

            this.inflater = inflater;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.imagelayout, view,
                    false);
            assert imageLayout != null;

            imageLoader = ImageLoader.getInstance();
            ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.image);
            imageView.setContentDescription(String.valueOf(position));
            final ProgressBar spinner = (ProgressBar) imageLayout
                    .findViewById(R.id.loading);

            imageLoader.displayImage(images[position], imageView, options,
                    new SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            spinner.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            String message = null;
                            switch (failReason.getType()) {
                                case IO_ERROR:
                                    message = "Input/Output error";
                                    break;
                                case DECODING_ERROR:
                                    message = "Image can't be decoded";
                                    break;
                                case NETWORK_DENIED:
                                    message = "Downloads are denied";
                                    break;
                                case OUT_OF_MEMORY:
                                    message = "Out Of Memory error";
                                    break;
                                case UNKNOWN:
                                    message = "Unknown error";
                                    break;
                            }

                            spinner.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            spinner.setVisibility(View.GONE);
                        }
                    });

            view.addView(imageLayout, 0);
            pager.setObjectForPosition(imageLayout, position);
            pager.setTransitionEffect(JazzyViewPager.TransitionEffect.FlipHorizontal);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
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
