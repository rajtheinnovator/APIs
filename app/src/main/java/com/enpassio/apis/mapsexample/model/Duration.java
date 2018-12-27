package com.enpassio.apis.mapsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Raj on 12/27/2018.
 */
public class Duration {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("value")
    @Expose
    private long value;

    /**
     * No args constructor for use in serialization
     */
    public Duration() {
    }

    /**
     * @param text
     * @param value
     */
    public Duration(String text, long value) {
        super();
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

}
