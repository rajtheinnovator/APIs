package com.enpassio.apis

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "listofmailidstable")
class ListOfMailIds {
    @Expose(deserialize = false, serialize = false)
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @SerializedName("messages")
    @Expose
    var messages: List<MessageWithIdAndThreadId>? = null
    @SerializedName("nextPageToken")
    @Expose
    var nextPageToken: String? = null
    @SerializedName("resultSizeEstimate")
    @Expose
    var resultSizeEstimate: Int? = null

}

@Entity(tableName = "messagewithidandthreadtable")
class MessageWithIdAndThreadId {
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("threadId")
    @Expose
    var threadId: String? = null

}