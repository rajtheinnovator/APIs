package com.enpassio.apis.googlespreadsheet.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Abhishek Raj on 12/20/2018.
 */

class File : Parcelable {

    @SerializedName("mimeType")
    @Expose
    var mimeType: String? = null
    @SerializedName("kind")
    @Expose
    var kind: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param id
     * @param name
     * @param kind
     * @param mimeType
     */
    constructor(mimeType: String, kind: String, id: String, name: String) : super() {
        this.mimeType = mimeType
        this.kind = kind
        this.id = id
        this.name = name
    }


    protected constructor(`in`: Parcel) {
        mimeType = `in`.readString()
        kind = `in`.readString()
        id = `in`.readString()
        name = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(mimeType)
        dest.writeString(kind)
        dest.writeString(id)
        dest.writeString(name)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<File> = object : Parcelable.Creator<File> {
            override fun createFromParcel(`in`: Parcel): File {
                return File(`in`)
            }

            override fun newArray(size: Int): Array<File?> {
                return arrayOfNulls(size)
            }
        }
    }
}