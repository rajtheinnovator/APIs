package com.enpassio.apis

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GmailData {
    @SerializedName("id")
    @Expose
    var idOfMail: String? = null
    @SerializedName("threadId")
    @Expose
    var idOfThread: Int? = null
}
