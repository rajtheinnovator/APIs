package com.enpassio.apis.mapsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Abhishek Raj on 12/27/2018.
 */
public class GeocodedWaypoint {

    @SerializedName("geocoder_status")
    @Expose
    private String geocoderStatus;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("types")
    @Expose
    private List<String> types = null;

    /**
     * No args constructor for use in serialization
     */
    public GeocodedWaypoint() {
    }

    /**
     * @param geocoderStatus
     * @param placeId
     * @param types
     */
    public GeocodedWaypoint(String geocoderStatus, String placeId, List<String> types) {
        super();
        this.geocoderStatus = geocoderStatus;
        this.placeId = placeId;
        this.types = types;
    }

    public String getGeocoderStatus() {
        return geocoderStatus;
    }

    public void setGeocoderStatus(String geocoderStatus) {
        this.geocoderStatus = geocoderStatus;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

}