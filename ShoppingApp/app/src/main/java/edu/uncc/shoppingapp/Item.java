package edu.uncc.shoppingapp;

import java.io.Serializable;

/**
 * Created by NANDU on 23-02-2017.
 */

public class Item implements Serializable {
    private String name;
    private float discount;
    private float msrp_price;
    private float sale_price;
    private String imageUrl;
    private String id;

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", discount=" + discount +
                ", msrp_price=" + msrp_price +
                ", sale_price=" + sale_price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public float getMsrp_price() {
        return msrp_price;
    }

    public void setMsrp_price(float msrp_price) {
        this.msrp_price = msrp_price;
    }

    public float getSale_price() {
        return sale_price;
    }

    public void setSale_price(float sale_price) {
        this.sale_price = sale_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDiscount() {
        return ((msrp_price-sale_price)/msrp_price)*100;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
