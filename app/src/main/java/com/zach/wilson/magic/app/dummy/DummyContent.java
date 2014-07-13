package com.zach.wilson.magic.app.dummy;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zach.wilson.magic.app.fragments.AddDeckFragment;
import com.zach.wilson.magic.app.fragments.AdvancedSearchFragment;
import com.zach.wilson.magic.app.fragments.ArchenemyFragment;
import com.zach.wilson.magic.app.fragments.SearchFragment;

import android.app.Fragment;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
	static String[] options = {"My Decks", "Add Deck", "Advanced Search", "Archenemy", "Planeschase"};

	static {
		for(int i = 0; i < options.length; i++){
			addItem(new DummyItem(String.valueOf(i), options[i]));
			
		}
	}

	private static void addItem(DummyItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class DummyItem {
		public String id;
		public String content;

		public DummyItem(String id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
