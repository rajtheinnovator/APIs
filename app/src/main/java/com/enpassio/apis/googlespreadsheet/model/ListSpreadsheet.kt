package com.enpassio.apis.googlespreadsheet.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Abhishek Raj on 12/20/2018.
 */
class ListSpreadsheet : Parcelable {

    @SerializedName("incompleteSearch")
    @Expose
    var isIncompleteSearch: Boolean = false
    @SerializedName("files")
    @Expose
    var files: List<File>? = null
    @SerializedName("kind")
    @Expose
    var kind: String? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param files
     * @param incompleteSearch
     * @param kind
     */
    constructor(incompleteSearch: Boolean, files: List<File>, kind: String) : super() {
        this.isIncompleteSearch = incompleteSearch
        this.files = files
        this.kind = kind
    }


    protected constructor(`in`: Parcel) {
        isIncompleteSearch = `in`.readByte().toInt() != 0x00
        if (`in`.readByte().toInt() == 0x01) {
            files = ArrayList()
            `in`.readList(files, File::class.java.classLoader)
        } else {
            files = null
        }
        kind = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte((if (isIncompleteSearch) 0x01 else 0x00).toByte())
        if (files == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeList(files)
        }
        dest.writeString(kind)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<ListSpreadsheet> = object : Parcelable.Creator<ListSpreadsheet> {
            override fun createFromParcel(`in`: Parcel): ListSpreadsheet {
                return ListSpreadsheet(`in`)
            }

            override fun newArray(size: Int): Array<ListSpreadsheet?> {
                return arrayOfNulls(size)
            }
        }
    }
}