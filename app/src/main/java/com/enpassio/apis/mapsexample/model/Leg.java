package com.enpassio.apis.mapsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Abhishek Raj on 12/27/2018.
 */
public class Leg {

    @SerializedName("distance")
    @Expose
    private Distance distance;
    @SerializedName("duration")
    @Expose
    private Duration duration;
    @SerializedName("end_address")
    @Expose
    private String endAddress;
    @SerializedName("end_location")
    @Expose
    private EndLocation endLocation;
    @SerializedName("start_address")
    @Expose
    private String startAddress;
    @SerializedName("start_location")
    @Expose
    private StartLocation startLocation;
    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;
    @SerializedName("traffic_speed_entry")
    @Expose
    private List<Object> trafficSpeedEntry = null;
    @SerializedName("via_waypoint")
    @Expose
    private List<Object> viaWaypoint = null;

    /**
     * No args constructor for use in serialization
     */
    public Leg() {
    }

    /**
     * @param startAddress
     * @param duration
     * @param distance
     * @param trafficSpeedEntry
     * @param endLocation
     * @param startLocation
     * @param steps
     * @param endAddress
     * @param viaWaypoint
     */
    public Leg(Distance distance, Duration duration, String endAddress, EndLocation endLocation, String startAddress, StartLocation startLocation, List<Step> steps, List<Object> trafficSpeedEntry, List<Object> viaWaypoint) {
        super();
        this.distance = distance;
        this.duration = duration;
        this.endAddress = endAddress;
        this.endLocation = endLocation;
        this.startAddress = startAddress;
        this.startLocation = startLocation;
        this.steps = steps;
        this.trafficSpeedEntry = trafficSpeedEntry;
        this.viaWaypoint = viaWaypoint;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public EndLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(EndLocation endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public StartLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(StartLocation startLocation) {
        this.startLocation = startLocation;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Object> getTrafficSpeedEntry() {
        return trafficSpeedEntry;
    }

    public void setTrafficSpeedEntry(List<Object> trafficSpeedEntry) {
        this.trafficSpeedEntry = trafficSpeedEntry;
    }

    public List<Object> getViaWaypoint() {
        return viaWaypoint;
    }

    public void setViaWaypoint(List<Object> viaWaypoint) {
        this.viaWaypoint = viaWaypoint;
    }

}