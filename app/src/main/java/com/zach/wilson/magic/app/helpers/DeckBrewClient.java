package com.zach.wilson.magic.app.helpers;

import com.zach.wilson.magic.app.models.Card;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public class DeckBrewClient {
    private static final String API_URL = "https://api.deckbrew.com";
    public static DeckBrew deckbrew;

    public interface DeckBrew {
        @GET("/mtg/cards/{name}")
        void getCard(
                @Path("name") String name, Callback<Card> callback);

        @GET("/mtg/cards/typeahead")
        void getCardsFromStart(@Query("q") String temp, Callback<List<Card>> card);

        @GET("/mtg/cards")
        void getCardsFromAttributes(@QueryMap Map<String, String> map, Callback<List<Card>> cards);

        @GET("/mtg/cards")
        void getCardsFromAdvAttributes(
                @Query("type") List<String> types,
                @Query("color") List<String> colors,
                @Query("subtype") List<String> subtypes,
                @Query("oracle") List<String> text,
                @Query("format") List<String> format,
                @Query("rarity") List<String> rarity,
                @Query("multicolor") boolean multi,
                Callback<List<Card>> card);


        @GET("/mtg/cards")
        void getRandomCards(@Query("page") int page, Callback<Card[]> card);
    }

    public static DeckBrew getAPI() {
        if (deckbrew == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .build();
            // Create an instance of our GitHub API interface.
            deckbrew = restAdapter.create(DeckBrew.class);
        }



        return deckbrew;
    }
}