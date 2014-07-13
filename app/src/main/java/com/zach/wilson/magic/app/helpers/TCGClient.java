package com.zach.wilson.magic.app.helpers;

import com.zach.wilson.magic.app.models.Product;
import com.zach.wilson.magic.app.models.Products;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Zach on 7/13/2014.
 */
public class TCGClient {

    private static final String API_URL = "http://partner.tcgplayer.com/x3";
    public static Pricing pricing;

    public interface Pricing {
        @GET("/phl.asmx/p")
        void getProductPrice(@Query("pk") String partnerKey, @Query("s") String set, @Query("p") String name, Callback<Products> price);
    }


    public static void instantiate(){
        if (pricing == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL).setConverter(new SimpleXMLConverter())
                    .build();
            // Create an instance of our GitHub API interface.
            pricing = restAdapter.create(Pricing.class);
        }
    }

    public static String formatSet(String set){
        if (set.startsWith("Magic 2")) {
            set = set.substring(0, 10);
            String temp = set.substring(set.length() - 2);
            temp = "(M" + temp + ")";
            set = set + " " + temp;
        }
        if (set.startsWith("Commander 2013")) {
            set = "Commander 2013";
        }
        if (set.startsWith("Magic: The Gathering-Commander")) {
            set = "Commander";
        } else if (set.startsWith("Magic: The Gathering")) {
            set = "Conspiracy";
        }
        if (set.startsWith("Limited Edition Beta")) {
            set = "Beta Edition";
        }
        if (set.startsWith("Limited Edition Alpha")) {
            set = "Alpha Edition";
        }
        if (set.startsWith("Planechase")) {
            if (set.length() > 15)
                set = set.substring(0, 15);
        }
        if (set.startsWith("Ninth")) {
            set = "9th Edition";
        }
        if (set.startsWith("Tenth")) {
            set = "10th Edition";
        }
        if (set.startsWith("Eighth")) {
            set = "8th Edition";
        }
        if (set.startsWith("Seventh")) {
            set = "7th Edition";
        }
        if (set.startsWith("Sixth")) {
            set = "Classic Sixth Edition";
        }
        return set;
    }

}
