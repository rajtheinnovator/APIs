package com.enpassio.apis.mapsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Raj on 12/27/2018.
 */
public class Polyline {

    @SerializedName("points")
    @Expose
    private String points;

    /**
     * No args constructor for use in serialization
     */
    public Polyline() {
    }

    /**
     * @param points
     */
    public Polyline(String points) {
        super();
        this.points = points;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

}