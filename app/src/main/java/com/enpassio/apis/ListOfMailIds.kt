package com.enpassio.apis

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ListOfMailIds {

    @SerializedName("messages")
    @Expose
    var messages: List<Message>? = null
    @SerializedName("nextPageToken")
    @Expose
    var nextPageToken: String? = null
    @SerializedName("resultSizeEstimate")
    @Expose
    var resultSizeEstimate: Int? = null

}

class Message {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("threadId")
    @Expose
    var threadId: String? = null

}