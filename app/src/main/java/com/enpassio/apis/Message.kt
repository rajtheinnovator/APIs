package com.enpassio.apis

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Message {
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("threadId")
    @Expose
    var threadId: String? = null
    @SerializedName("labelIds")
    @Expose
    var labelIds: List<String>? = null
    @SerializedName("snippet")
    @Expose
    var snippet: String? = null
    @SerializedName("historyId")
    @Expose
    var historyId: String? = null
    @SerializedName("internalDate")
    @Expose
    var internalDate: String? = null
    @SerializedName("payload")
    @Expose
    var payload: Payload? = null
    @SerializedName("sizeEstimate")
    @Expose
    var sizeEstimate: Int? = null

}

class Body {

    @SerializedName("size")
    @Expose
    var size: Int? = null

}

class Body_ {

    @SerializedName("size")
    @Expose
    var size: Int? = null
    @SerializedName("data")
    @Expose
    var data: String? = null

}

class Header {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null

}

class Header_ {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null

}


class Part {

    @SerializedName("partId")
    @Expose
    var partId: String? = null
    @SerializedName("mimeType")
    @Expose
    var mimeType: String? = null
    @SerializedName("filename")
    @Expose
    var filename: String? = null
    @SerializedName("headers")
    @Expose
    var headers: List<Header_>? = null
    @SerializedName("body")
    @Expose
    var body: Body_? = null

}

class Payload {

    @SerializedName("partId")
    @Expose
    var partId: String? = null
    @SerializedName("mimeType")
    @Expose
    var mimeType: String? = null
    @SerializedName("filename")
    @Expose
    var filename: String? = null
    @SerializedName("headers")
    @Expose
    var headers: List<Header>? = null
    @SerializedName("body")
    @Expose
    var body: Body? = null
    @SerializedName("parts")
    @Expose
    var parts: List<Part>? = null

}