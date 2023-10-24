package com.zach.wilson.magic.app.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by Zach on 7/13/2014.
 */
@Root
public class Products implements Serializable {


    @Attribute(required = false)
    int id;

    @Element
    Product product;

    public Product getProducts() {
        return product;
    }

    public void setProducts(Product products) {
        this.product = products;
    }
}
