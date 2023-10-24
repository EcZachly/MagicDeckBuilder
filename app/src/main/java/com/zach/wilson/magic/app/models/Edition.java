package com.zach.wilson.magic.app.models;

import java.io.Serializable;

public class Edition implements Serializable {
	String set;
	String rarity;
	String artist;
	String multiverse_id;
	String flavor;
	String number;
	String layout;
	Price price;
	String url;
	String image_url;
	String set_url;
	String store_url;
	String set_id;
	
	
	
	public String getSet_id() {
		return set_id;
	}
	public void setSet_id(String set_id) {
		this.set_id = set_id;
	}
	public String getSet() {
		return set;
	}
	public void setSet(String set) {
		this.set = set;
	}
	public String getRarity() {
		return rarity;
	}
	public void setRarity(String rarity) {
		this.rarity = rarity;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getMultiverse_id() {
		return multiverse_id;
	}
	public void setMultiverse_id(String multiverse_id) {
		this.multiverse_id = multiverse_id;
	}
	public String getFlavor() {
		return flavor;
	}
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getLayout() {
		return layout;
	}
	public void setLayout(String layout) {
		this.layout = layout;
	}
	public Price getPrice() {
		return price;
	}
	public void setPrice(Price price) {
		this.price = price;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public String getSet_url() {
		return set_url;
	}
	public void setSet_url(String set_url) {
		this.set_url = set_url;
	}
	public String getStore_url() {
		return store_url;
	}
	public void setStore_url(String store_url) {
		this.store_url = store_url;
	}

	
	
	
	

}
