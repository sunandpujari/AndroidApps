package edu.uncc.itunestoppaidapps;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by NANDU on 23-02-2017.
 */

public class App implements Serializable {
    private String name;
    private String thumbnailURL;
    private float price;
    private String currencyType;

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    @Override
    public String toString() {
        return "App{" +
                ", name='" + name + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                ", price=" + price +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

}
