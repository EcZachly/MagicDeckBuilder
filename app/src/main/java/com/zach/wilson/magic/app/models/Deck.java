package com.zach.wilson.magic.app.models;


import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jakewilson on 7/25/14.
 */

public class Deck {
    @SerializedName("user_display")
    @Expose
    private String userDisplay;
    @Expose
    private String name;
    @Expose
    private String url;
    @SerializedName("featured_card")
    @Expose
    private String featuredCard;
    @SerializedName("date_updated")
    @Expose
    private Integer dateUpdated;
    @Expose
    private Integer score;
    @Expose
    private List<List<String>> inventory = new ArrayList<List<String>>();
    @SerializedName("resource_uri")
    @Expose
    private String resourceUri;
    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnailUrl;
    @Expose
    private String slug;
    @SerializedName("small_thumbnail_url")
    @Expose
    private String smallThumbnailUrl;
    @Expose
    private String user;

    public String getUserDisplay() {
        return userDisplay;
    }

    public void setUserDisplay(String userDisplay) {
        this.userDisplay = userDisplay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFeaturedCard() {
        return featuredCard;
    }

    public void setFeaturedCard(String featuredCard) {
        this.featuredCard = featuredCard;
    }

    public Integer getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Integer dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<List<String>> getInventory() {
        return inventory;
    }

    public void setInventory(List<List<String>> inventory) {
        this.inventory = inventory;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSmallThumbnailUrl() {
        return smallThumbnailUrl;
    }

    public void setSmallThumbnailUrl(String smallThumbnailUrl) {
        this.smallThumbnailUrl = smallThumbnailUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}