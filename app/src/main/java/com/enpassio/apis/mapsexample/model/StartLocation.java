package com.enpassio.apis.mapsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Raj on 12/27/2018.
 */
public class StartLocation {

    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lng")
    @Expose
    private double lng;

    /**
     * No args constructor for use in serialization
     */
    public StartLocation() {
    }

    /**
     * @param lng
     * @param lat
     */
    public StartLocation(double lat, double lng) {
        super();
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

}
