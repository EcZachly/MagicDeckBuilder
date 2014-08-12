package com.zach.wilson.magic.app.models;

import java.io.Serializable;

/**
 * Created by zachwilson on 8/12/14.
 */
public class Set implements Serializable{

    String id;
    String name;
    String border;
    String type;
    String url;
    String cards_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCards_url() {
        return cards_url;
    }

    public void setCards_url(String cards_url) {
        this.cards_url = cards_url;
    }
}
