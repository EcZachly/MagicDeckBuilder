package com.zach.wilson.magic.app.models;

import java.io.Serializable;
import java.util.ArrayList;


public class CardList implements Serializable {
	
	
	
	public static ArrayList<String> cardsToOrder;	
	public static String currentOrder;
	public static String massCardOrderingBaseURL = "http://store.tcgplayer.com/list/selectproductmagic.aspx?partner=MAGICVIEW&c=";
	public static String[] rarities = {"Common", "Uncommon", "Rare", "Mythic"};
	
	public static String urlToAPP = "https://play.google.com/store/apps/details?id=com.zach.wilson.magic.app";
	public static ArrayList<Card> currentCardList;
	public static Card selectedCard;
	public static String allCards = "https://api.deckbrew.com/mtg/cards?page=";
	public static Card[] cards;
	public static String[] types = { "Artifact", "Creature", "Enchantment", "Instant",
			"Land", "Planeswalker", "Sorcery" };

	
}
