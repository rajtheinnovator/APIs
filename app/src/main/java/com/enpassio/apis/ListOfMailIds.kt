package com.enpassio.apis

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ListOfMailIds {

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

class MessageWithIdAndThreadId {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("threadId")
    @Expose
    var threadId: String? = null

}