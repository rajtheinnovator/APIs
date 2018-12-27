package com.enpassio.apis.mapsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Raj on 12/27/2018.
 */
public class Step {

    @SerializedName("distance")
    @Expose
    private Distance_ distance;
    @SerializedName("duration")
    @Expose
    private Duration_ duration;
    @SerializedName("end_location")
    @Expose
    private EndLocation_ endLocation;
    @SerializedName("html_instructions")
    @Expose
    private String htmlInstructions;
    @SerializedName("polyline")
    @Expose
    private Polyline polyline;
    @SerializedName("start_location")
    @Expose
    private StartLocation_ startLocation;
    @SerializedName("travel_mode")
    @Expose
    private String travelMode;
    @SerializedName("maneuver")
    @Expose
    private String maneuver;

    /**
     * No args constructor for use in serialization
     */
    public Step() {
    }

    /**
     * @param duration
     * @param distance
     * @param polyline
     * @param endLocation
     * @param htmlInstructions
     * @param startLocation
     * @param maneuver
     * @param travelMode
     */
    public Step(Distance_ distance, Duration_ duration, EndLocation_ endLocation, String htmlInstructions, Polyline polyline, StartLocation_ startLocation, String travelMode, String maneuver) {
        super();
        this.distance = distance;
        this.duration = duration;
        this.endLocation = endLocation;
        this.htmlInstructions = htmlInstructions;
        this.polyline = polyline;
        this.startLocation = startLocation;
        this.travelMode = travelMode;
        this.maneuver = maneuver;
    }

    public Distance_ getDistance() {
        return distance;
    }

    public void setDistance(Distance_ distance) {
        this.distance = distance;
    }

    public Duration_ getDuration() {
        return duration;
    }

    public void setDuration(Duration_ duration) {
        this.duration = duration;
    }

    public EndLocation_ getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(EndLocation_ endLocation) {
        this.endLocation = endLocation;
    }

    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public StartLocation_ getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(StartLocation_ startLocation) {
        this.startLocation = startLocation;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }

}
