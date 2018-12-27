package com.enpassio.apis.mapsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.Route;

/**
 * Created by Abhishek Raj on 12/27/2018.
 */
public class Direction {

    @SerializedName("geocoded_waypoints")
    @Expose
    private List<GeocodedWaypoint> geocodedWaypoints = null;
    @SerializedName("routes")
    @Expose
    private List<Route> routes = null;
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * No args constructor for use in serialization
     */
    public Direction() {
    }

    /**
     * @param status
     * @param routes
     * @param geocodedWaypoints
     */
    public Direction(List<GeocodedWaypoint> geocodedWaypoints, List<Route> routes, String status) {
        super();
        this.geocodedWaypoints = geocodedWaypoints;
        this.routes = routes;
        this.status = status;
    }

    public List<GeocodedWaypoint> getGeocodedWaypoints() {
        return geocodedWaypoints;
    }

    public void setGeocodedWaypoints(List<GeocodedWaypoint> geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
