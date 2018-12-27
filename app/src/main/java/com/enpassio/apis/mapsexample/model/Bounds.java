package com.enpassio.apis.mapsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Raj on 12/27/2018.
 */
public class Bounds {

    @SerializedName("northeast")
    @Expose
    private Northeast northeast;
    @SerializedName("southwest")
    @Expose
    private Southwest southwest;

    /**
     * No args constructor for use in serialization
     */
    public Bounds() {
    }

    /**
     * @param southwest
     * @param northeast
     */
    public Bounds(Northeast northeast, Southwest southwest) {
        super();
        this.northeast = northeast;
        this.southwest = southwest;
    }

    public Northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    public Southwest getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

}