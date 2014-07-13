package com.zach.wilson.magic.app.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zach.wilson.magic.app.models.Card;

import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;

public class MagicAppSettings extends Application {

	// Prefs
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private Activity activity;
	private ArrayList<String> cardsToOrder;	
	private String currentOrder;
	private HashMap<String, Integer> lifeTotals;
	private HashMap<String, Integer> poisonTotals;
	private ArrayList<String> playerNames;
	private Card selectedCard;
	private JazzyViewPager jPager;
	private Card[] cardsInCarousel;
	private int selectedCardIndex;
	private FragmentManager manager;
	private Card[] planesInCarousel;
	private Card[] schemesInCarousel;
	private Card[] cardsFromAPI;
	private Card[] cardsFromAdvSearch;
	private Card cardFromSearch;
	
	
	


	public Card[] getCardsFromAdvSearch() {
		return cardsFromAdvSearch;
	}

	public void setCardsFromAdvSearch(Card[] cardsFromAdvSearch) {
		this.cardsFromAdvSearch = cardsFromAdvSearch;
	}

	public Card getCardFromSearch() {
		return cardFromSearch;
	}

	public void setCardFromSearch(Card cardFromSearch) {
		this.cardFromSearch = cardFromSearch;
	}

	public Card[] getCardsFromAPI() {
		return cardsFromAPI;
	}

	public void setCardsFromAPI(Card[] cardsFromAPI) {
		this.cardsFromAPI = cardsFromAPI;
	}

	public Card[] getPlanesInCarousel() {
		return planesInCarousel;
	}

	public void setPlanesInCarousel(Card[] planesInCarousel) {
		this.planesInCarousel = planesInCarousel;
	}

	public Card[] getSchemesInCarousel() {
		return schemesInCarousel;
	}

	public void setSchemesInCarousel(Card[] schemesInCarousel) {
		this.schemesInCarousel = schemesInCarousel;
	}

	public FragmentManager getManager() {
		return manager;
	}

	public void setManager(FragmentManager manager) {
		this.manager = manager;
	}

	public JazzyViewPager getjPager() {
		return jPager;
	}

	public void setjPager(JazzyViewPager jPager) {
		this.jPager = jPager;
	}


	public Card[] getCardsInCarousel() {
		return cardsInCarousel;
	}

	public void setCardsInCarousel(Card[] cardsInCarousel) {
		this.cardsInCarousel = cardsInCarousel;
	}

	public int getSelectedCardIndex() {
		return selectedCardIndex;
	}

	public void setSelectedCardIndex(int selectedCardIndex) {
		this.selectedCardIndex = selectedCardIndex;
	}

	public void setContextForPreferences(Context activity, Activity a) {
		// Setup preferences
		this.activity = a;
		preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		editor = preferences.edit();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(1500000)
				// 1.5 Mb
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public SharedPreferences.Editor getEditor() {
		return editor;
	}

	public void setEditor(SharedPreferences.Editor editor) {
		this.editor = editor;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public ArrayList<String> getCardsToOrder() {
		return cardsToOrder;
	}

	public void setCardsToOrder(ArrayList<String> cardsToOrder) {
		this.cardsToOrder = cardsToOrder;
	}

	public String getCurrentOrder() {
		return currentOrder;
	}

	public void setCurrentOrder(String currentOrder) {
		this.currentOrder = currentOrder;
	}

	

	public HashMap<String, Integer> getLifeTotals() {
		return lifeTotals;
	}

	public void setLifeTotals(HashMap<String, Integer> lifeTotals) {
		this.lifeTotals = lifeTotals;
	}

	public HashMap<String, Integer> getPoisonTotals() {
		return poisonTotals;
	}

	public void setPoisonTotals(HashMap<String, Integer> poisonTotals) {
		this.poisonTotals = poisonTotals;
	}

	public ArrayList<String> getPlayerNames() {
		return playerNames;
	}

	public void setPlayerNames(ArrayList<String> playerNames) {
		this.playerNames = playerNames;
	}

	public Card getSelectedCard() {
		return selectedCard;
	}

	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}
	
	
	
	

}
