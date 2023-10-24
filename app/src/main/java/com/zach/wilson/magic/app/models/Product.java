package com.zach.wilson.magic.app.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by Zach on 7/13/2014.
 */
@Root
public class Product implements Serializable
{

    @Element(required = false)
    String id;
    @Element
    String hiprice;
    @Element
    String lowprice;
    @Element
    String avgprice;
    @Element
    String foilavgprice;
    @Element
    String link;

    public Product(){

    }
    public String getHiprice() {
        return hiprice;
    }

    public void setHiprice(String hiprice) {
        this.hiprice = hiprice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
