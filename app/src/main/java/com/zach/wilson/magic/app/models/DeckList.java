package com.zach.wilson.magic.app.models;

/**
 * Created by jakewilson on 7/25/14.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeckList {

    @SerializedName("user_display")
    @Expose
    private String userDisplay;
    @Expose
    private String name;
    @Expose
    private String url;
    @Expose
    private String user;
    @Expose
    private String slug;
    @SerializedName("resource_uri")
    @Expose
    private String resourceUri;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

}

