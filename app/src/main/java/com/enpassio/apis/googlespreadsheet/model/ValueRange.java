package com.enpassio.apis.googlespreadsheet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Raj on 1/8/2019.
 */
public class ValueRange implements Parcelable {
    public static final Parcelable.Creator<ValueRange> CREATOR = new Parcelable.Creator<ValueRange>() {
        @Override
        public ValueRange createFromParcel(Parcel source) {
            return new ValueRange(source);
        }

        @Override
        public ValueRange[] newArray(int size) {
            return new ValueRange[size];
        }
    };
    @SerializedName("range")
    @Expose

    String range;
    @SerializedName("values")
    @Expose
    List<List<String>> values;
    @SerializedName("majorDimension")
    @Expose
    String majorDimension;

    public ValueRange() {
    }

    protected ValueRange(Parcel in) {
        this.range = in.readString();
        this.values = new ArrayList<List<String>>();
        in.readList(this.values, List.class.getClassLoader());
        this.majorDimension = in.readString();
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public List<List<String>> getValues() {
        return values;
    }

    public void setValues(List<List<String>> values) {
        this.values = values;
    }

    public String getMajorDimension() {
        return majorDimension;
    }

    public void setMajorDimension(String majorDimension) {
        this.majorDimension = majorDimension;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.range);
        dest.writeList(this.values);
        dest.writeString(this.majorDimension);
    }
}
