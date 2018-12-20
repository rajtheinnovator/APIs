package com.enpassio.apis.googlespreadsheet.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Abhishek Raj on 12/20/2018.
 */
class SpreadsheetData : Parcelable {

    @SerializedName("spreadsheetId")
    @Expose
    var spreadsheetId: String? = null
    @SerializedName("valueRanges")
    @Expose
    var valueRanges: List<ValueRange>? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param valueRanges
     * @param spreadsheetId
     */
    constructor(spreadsheetId: String, valueRanges: List<ValueRange>) : super() {
        this.spreadsheetId = spreadsheetId
        this.valueRanges = valueRanges
    }


    protected constructor(`in`: Parcel) {
        spreadsheetId = `in`.readString()
        if (`in`.readByte().toInt() == 0x01) {
            valueRanges = ArrayList()
            `in`.readList(valueRanges, ValueRange::class.java.classLoader)
        } else {
            valueRanges = null
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(spreadsheetId)
        if (valueRanges == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeList(valueRanges)
        }
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<SpreadsheetData> = object : Parcelable.Creator<SpreadsheetData> {
            override fun createFromParcel(`in`: Parcel): SpreadsheetData {
                return SpreadsheetData(`in`)
            }

            override fun newArray(size: Int): Array<SpreadsheetData?> {
                return arrayOfNulls(size)
            }
        }
    }
}