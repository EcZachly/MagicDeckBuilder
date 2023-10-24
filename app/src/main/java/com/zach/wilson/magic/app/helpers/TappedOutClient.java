package com.zach.wilson.magic.app.helpers;

import com.zach.wilson.magic.app.models.Deck;
import com.zach.wilson.magic.app.models.DeckList;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by jakewilson on 7/25/14.
 */
public class TappedOutClient {
    private static final String API_KEY = "7f82494ebbac4fd3f38f26f2bf52d5569fb8d061";
    private static final String API_URL = "http://tappedout.net/api/";
    public static TappedOut tappedOut;

    public interface TappedOut {
        @GET("/deck/latest/{name}/")
        void getDecks(
                @Path("name") String name, Callback<List<DeckList>> callback);

        @GET("/collection/collection:deck/{name}/?key=" + API_KEY)
        void getDeckDetail(
                @Path("name") String name, Callback<Deck> callback);
    }

    public static TappedOut getAPI() {
        if (tappedOut == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .build();
            // Create an instance of our GitHub API interface.
            tappedOut = restAdapter.create(TappedOut.class);
        }
        return tappedOut;
    }
}
