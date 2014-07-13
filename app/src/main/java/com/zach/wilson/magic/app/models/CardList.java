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
	public static String base = "https://deckbrew.com/api/";
	public static String allCards = "https://api.deckbrew.com/mtg/cards?page=";
	public static Card[] cards;
	public static String[] types = { "Artifact", "Creature", "Enchantment", "Instant",
			"Land", "Planeswalker", "Sorcery" };
	public static String[] standard = {"GTC", "THS", "RTR", "DGM", "JOU" ,"BNG"}; 
	public static String[] modern = { 
	"Eighth Edition",
	"Mirrodin",
	"Darksteel",
	"Fifth Dawn",
	"Champions of Kamigawa",
	"Betrayers of Kamigawa",
	"Saviors of Kamigawa",
	"Ninth Edition",
	"Ravnica: City of Guilds",
	"Guildpact",
	"Dissension",
	"Coldsnap",
	"Time Spiral",
	"Planar Chaos",
	"Future Sight",
	"Tenth Edition",
	"Lorwyn",
	"Morningtide",
	"Shadowmoor",
	"Eventide",
	"Shards of Alara",
	"Conflux",
	"Alara Reborn",
	"Magic 2010",
	"Zendikar",
	"Worldwake",
	"Rise of the Eldrazi",
	"Magic 2011",
	"Scars of Mirrodin",
	"Mirrodin Besieged",
	"New Phyrexia",
	"Magic 2012",
	"Innistrad",
	"Dark Ascension",
	"Avacyn Restored",
	"Magic 2013",
	"Return to Ravnica",
	"Gatecrash",
	"Dragon\'s Maze",
	"Magic 2014",
	"Theros",
	"Born of the Gods"};
	
	
}
