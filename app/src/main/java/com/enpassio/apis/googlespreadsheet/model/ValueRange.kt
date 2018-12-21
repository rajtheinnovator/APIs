package com.enpassio.apis.googlespreadsheet.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Abhishek Raj on 12/20/2018.
 */
class ValueRange : Parcelable {

    @SerializedName("range")
    @Expose
    var range: String? = null
    @SerializedName("values")
    @Expose
    var values: List<List<String>>? = null
    @SerializedName("majorDimension")
    @Expose
    var majorDimension: String? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param values
     * @param range
     * @param majorDimension
     */
    constructor(range: String, values: List<List<String>>, majorDimension: String) : super() {
        this.range = range
        this.values = values
        this.majorDimension = majorDimension
    }


    protected constructor(`in`: Parcel) {
        range = `in`.readString()
        majorDimension = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(range)
        dest.writeString(majorDimension)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<ValueRange> = object : Parcelable.Creator<ValueRange> {
            override fun createFromParcel(`in`: Parcel): ValueRange {
                return ValueRange(`in`)
            }

            override fun newArray(size: Int): Array<ValueRange?> {
                return arrayOfNulls(size)
            }
        }
    }
}