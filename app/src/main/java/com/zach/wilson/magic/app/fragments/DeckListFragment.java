package com.zach.wilson.magic.app.fragments;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zach.wilson.magic.app.MainActivity;
import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.R.id;
import com.zach.wilson.magic.app.R.layout;
import com.zach.wilson.magic.app.helpers.JazzyViewPager;
import com.zach.wilson.magic.app.helpers.JazzyViewPager.TransitionEffect;

import com.zach.wilson.magic.app.helpers.TCGClient;

import com.zach.wilson.magic.app.models.CardList;
import com.zach.wilson.magic.app.models.Products;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.os.Parcelable;

import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DeckListFragment extends Fragment {
	ProgressDialog dialog;
	ImagePagerAdapter imgAdapter;
	TextView high;
	TextView mid;
	TextView low;
	TextView foil;
	Context context;
	LayoutInflater inflater;
	Dialog priceDialog;
	Dialog deckDialog;
	ArrayList<String> cardNames;
	ArrayList<String> cardNamesToDisplay;
	ImageView buy, edit, delete, share;
	ArrayList<String> cardURLs;
	ListView list;
	ImageLoader imageLoader;
	ArrayAdapter<String> adapter;
	DrawerLayout mDrawerLayout;
	LinearLayout rightDrawer;
	DisplayImageOptions options;
	JazzyViewPager pager;
	SharedPreferences prefs;
	ImageView shareCard, deleteCard;
	String deckName;
	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(layout.cardsindeck, null);
		this.inflater = inflater;
		context = v.getContext();
		list = (ListView) v.findViewById(id.myDeckLists);
		buy = (ImageView) v.findViewById(id.deckAddToCart);
		share = (ImageView) v.findViewById(id.deckShare);
		delete = (ImageView) v.findViewById(id.deckDelete);
		edit = (ImageView) v.findViewById(id.deckEdit);

		prefs = this.getActivity().getSharedPreferences("DECKSNEW",
				Context.MODE_PRIVATE);

		adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
				android.R.layout.simple_list_item_1, cardNames);
		list.setAdapter(adapter);
		InputMethodManager inputManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		mDrawerLayout = (DrawerLayout) v.findViewById(
				id.drawerLayoutForCards);
		rightDrawer = (LinearLayout) v.findViewById(id.rightDrawerLayout);
		dialog = new ProgressDialog(rightDrawer.getContext());

		shareCard = (ImageView) rightDrawer.findViewById(id.shareCard);
		deleteCard = (ImageView) rightDrawer
				.findViewById(id.removeCardFromDeck);

		deckDialog = new Dialog(rightDrawer.getContext());
		priceDialog = new Dialog(rightDrawer.getContext());

		pager = (JazzyViewPager) rightDrawer.findViewById(id.card_pager);


		imgAdapter = new ImagePagerAdapter(cardURLs, inflater);
		high = (TextView) rightDrawer.findViewById(id.highPrice);
		mid = (TextView) rightDrawer.findViewById(id.meanPrice);
		low = (TextView) rightDrawer.findViewById(id.lowPrice);
		foil = (TextView) rightDrawer.findViewById(id.foilPrice);
		pager.setAdapter(imgAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {


                TCGClient.pricing.getProductPrice("MAGICVIEW", "",cardNamesToDisplay.get(arg2), new Callback<Products>() {
                    @Override
                    public void success(Products products, Response response) {
                        if (!products.getProducts().getFoilavgprice().equals("0")) {
                            foil.setText("$" + products.getProducts().getFoilavgprice());
                        } else {
                            foil.setVisibility(View.GONE);
                            TextView temp = (TextView) rightDrawer
                                    .findViewById(id.foilPriceWords);
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
				shareCard.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

                        Map<String, String> map = new HashMap<String, String>();
                        map.put("CARD", cardNames.get(arg2));
                        FlurryAgent.logEvent("Shared a Card(From decklist fragment)", map);
                        Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("text/plain");

						intent.putExtra(
								Intent.EXTRA_TEXT,
								"Hey check out: "
										+ cardNames.get(arg2).substring(
												0,
												cardNames.get(arg2).indexOf(
														" x "))
										+ "\n"
										+ cardURLs.get(arg2)
										+ "\n\n Shared By: Magic Card Viewer on Google Play at:\n"
										+ CardList.urlToAPP);

						getActivity().startActivity(
								Intent.createChooser(intent, "Share"));
					}

				});
				deleteCard.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);

						// set title
						alertDialogBuilder.setTitle("Are you sure?");

						// set dialog message
						alertDialogBuilder
								.setMessage(
										"Are you sure you want to remove all of "
												+ cardNamesToDisplay.get(arg2)
												+ " from your deck?")
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												Object[] temp = prefs.getAll()
														.keySet().toArray();
												String[] keys = new String[temp.length];
												for (int i = 0; i < keys.length; i++) {
													keys[i] = temp[i]
															.toString();
												}
												ArrayList<String> deckKeys = new ArrayList<String>();
												for (String s : keys) {
													if (s.startsWith(deckName)) {
														deckKeys.add(s);
													}
												}

												for (int i = 0; i < deckKeys
														.size(); i++) {
													if (prefs
															.getAll()
															.get(deckKeys
																	.get(i))
															.toString()
															.contains(
																	cardNamesToDisplay
																			.get(arg2))) {
														Log.i("CARD NAMES",
																cardNamesToDisplay
																		.get(arg2));
														prefs.edit()
																.remove(deckKeys
																		.get(i))
																.commit();
													}
												}
												for (int i = 0; i < cardNames
														.size(); i++) {
													if (cardNames
															.get(i)
															.startsWith(
																	cardNamesToDisplay
																			.get(arg2))) {
														cardURLs.remove(i);
														cardNames.remove(i);
														cardNamesToDisplay
																.remove(arg2);
														break;
													}
												}
												imgAdapter = new ImagePagerAdapter(
														cardURLs, inflater);
												pager.setAdapter(imgAdapter);
												adapter.notifyDataSetChanged();

											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {

												dialog.cancel();
											}
										});

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();

					}

				});
			}

		});

		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String worker = " ";

				worker = "";
				for (String s : cardNames) {
					worker += "\n" + s;
				}
                Map<String, String> map = new HashMap<String, String>();
                int i = 1;
                for(String c : cardNames){
                    map.put("CARD" + i, c);
                    i++;
                }
                FlurryAgent.logEvent("Shared a Deck", map);

				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(
						Intent.EXTRA_TEXT,
						"Here's my decklist:"
								+ worker
								+ "\n\n Shared By: Magic Card Viewer on Google Play at:\n"
								+ CardList.urlToAPP);
				startActivity(Intent.createChooser(intent, "Share"));
			}

		});

		buy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CardList.cardsToOrder == null) {
					CardList.cardsToOrder = new ArrayList<String>();

				}
				ArrayList<String> cardsInDeckToAddToCart = new ArrayList<String>();
				ArrayList<String> cardNamesToAddToCart = new ArrayList<String>();

				for (String c : cardNames) {
					String temp = c.substring(c.indexOf(" x ") + 3);
					cardsInDeckToAddToCart.add(temp + " "
							+ c.substring(0, c.indexOf(" x ")));
				}
				if (CardList.currentOrder == null)
					CardList.currentOrder = CardList.massCardOrderingBaseURL;
				for (String s : cardsInDeckToAddToCart) {
					CardList.currentOrder += s + "||";
				}

				for (String c : cardsInDeckToAddToCart) {
					if (CardList.cardsToOrder == null)
						CardList.cardsToOrder = new ArrayList<String>();

					CardList.cardsToOrder.add(c);

				}
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);

				// set title
				alertDialogBuilder.setTitle("Cards added to cart!");

				// set dialog message
				alertDialogBuilder
						.setMessage("Do you want to view your cart now?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Fragment f = new MyCartFragment();

										getFragmentManager().beginTransaction()
												.replace(R.id.content_frame, f)
												.commit();
										dialog.cancel();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			}

		});

		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Fragment editDeck = new AddDeckFragment();
				Bundle args = new Bundle();
				String z = deckName;
				args.putBoolean("EDITMODE", true);
				args.putString("DECK NAME", deckName);
				Map<String, ?> map = prefs.getAll();
				ArrayList<String> cardsInDeck = new ArrayList<String>();
				Object[] tempArr = prefs.getAll().keySet().toArray();
				String[] keyArr = new String[tempArr.length];
				for (int i = 0; i < tempArr.length; i++) {
					keyArr[i] = tempArr[i].toString();

				}

				for (int i = 0; i < keyArr.length; i++) {
					if (keyArr[i].startsWith(z) && !keyArr[i].contains("#")
							&& !keyArr[i].contains("SIZE")) {
						cardsInDeck.add(map.get(keyArr[i]).toString());
					}
				}
				args.putStringArrayList("CARD NAMES", cardsInDeck);
				editDeck.setArguments(args);

				getFragmentManager().beginTransaction()
						.replace(id.content_frame, editDeck).commit();

			}

		});

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
				Object[] tempArr = prefs.getAll().keySet().toArray();
				final String[] keyArr = new String[tempArr.length];
				for (int i = 0; i < tempArr.length; i++) {
					keyArr[i] = tempArr[i].toString();

				}
				// set title
				alertDialogBuilder.setTitle("Are you sure?");

				// set dialog message
				alertDialogBuilder
						.setMessage(
								"Are you sure you want to delete your deck?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										String b = deckName;
										for (int i = 0; i < keyArr.length; i++) {
											if (keyArr[i].startsWith(b)) {
												prefs.edit().remove(keyArr[i])
														.commit();
											}

										}

										CardCarouselFragment f = new CardCarouselFragment();
										Intent t = new Intent(getActivity(),
												MainActivity.class);

										getActivity().startActivity(t);

									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}
		});

		return v;
	}

	@Override
	public void setArguments(Bundle args) {
		cardNames = new ArrayList<String>();
		cardURLs = new ArrayList<String>();
		deckName = args.getString("DECK NAME");
		cardNamesToDisplay = new ArrayList<String>();
		for (String s : args.getStringArrayList("CARD NAMES")) {
			cardNames.add(s);
		}
		for (String s : args.getStringArrayList("CARD URLS")) {
			cardURLs.add(s);
		}
		for (String s : args.getStringArrayList("CARD NAMES ALSO")) {
			cardNamesToDisplay.add(s);
		}
		super.setArguments(args);
	}

	public void openDrawer() {
		mDrawerLayout.openDrawer(mDrawerLayout);
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
			View imageLayout = inflater.inflate(layout.imagelayout, view,
					false);
			assert imageLayout != null;

			imageLoader = ImageLoader.getInstance();
			ImageView imageView = (ImageView) imageLayout
					.findViewById(id.image);
			imageView.setContentDescription(String.valueOf(position));
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(id.loading);

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
			pager.setTransitionEffect(TransitionEffect.FlipHorizontal);
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


}
