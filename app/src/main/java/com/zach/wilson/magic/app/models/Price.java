package com.zach.wilson.magic.app.models;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.Gson;

public class Price implements Serializable{

	
	
	static String basePriceURL = "https://api.deckbrew.com/mtg/cards/";
	
	String hiprice;
	String avgprice;
	String lowprice;
	String foilavgprice;
	String link;

    public String getHiprice() {
        return hiprice;
    }

    public void setHiprice(String hiprice) {
        this.hiprice = hiprice;
    }

    public String getAvgprice() {
        return avgprice;
    }

    public void setAvgprice(String avgprice) {
        this.avgprice = avgprice;
    }

    public String getLowprice() {
        return lowprice;
    }

    public void setLowprice(String lowprice) {
        this.lowprice = lowprice;
    }

    public String getFoilavgprice() {
        return foilavgprice;
    }

    public void setFoilavgprice(String foilavgprice) {
        this.foilavgprice = foilavgprice;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
