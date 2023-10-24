package com.zach.wilson.magic.app.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Card implements Serializable, Comparable<Card> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



    private String quantity = "1";
	private Edition[] editions;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    private Format formats;
	private String name;
	private String cmc;
	private String[] status;
	private String id;
	private String[] colors;
	private String manacost;
	private String[] types;
	private String powerToughness;
	@SerializedName("Text")
	private String text;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Edition[] getEditions() {
		return editions;
	}

	public void setEditions(Edition[] editions) {
		this.editions = editions;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getColors() {
		return colors;
	}

	public void setColors(String[] colors) {
		this.colors = colors;
	}

	public String getManacost() {
		return manacost;
	}

	public void setManacost(String manacost) {
		this.manacost = manacost;
	}

	public String[] getTypes() {
		return types;
	}

	public void setType(String[] type) {
		this.types = type;
	}

	public String getPowerToughness() {
		return powerToughness;
	}

	public void setPowerToughness(String powerToughness) {
		this.powerToughness = powerToughness;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	public Card() {
		name = "";
		manacost = "";
		powerToughness = "";
		text = "";
		
	}

	

	public String[] getStatus() {
		return status;
	}

	public void setStatus(String[] status) {
		this.status = status;
	}

	public String getCmc() {
		return cmc;
	}

	public void setCmc(String cmc) {
		this.cmc = cmc;
	}

	public Format getFormats() {
		return formats;
	}

	public void setFormats(Format formats) {
		this.formats = formats;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}
	@Override
	public boolean equals(Object o) {
		if (o.getClass() != this.getClass()) {
			return false;
		}
		Card d = (Card) o;

		if (d.name.equals(this.name)) {
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(Card another) {
		char a = this.getName().charAt(0);
		Card c = (Card) another;
		char b = c.getName().charAt(0);
		return a - b;
	}

}
