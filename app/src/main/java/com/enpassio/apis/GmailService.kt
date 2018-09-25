package com.enpassio.apis

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GmailService {
    @GET("gmail/v1/users/{userId}/messages/list")
    fun getListOfEmails(@Path("userId") userId: String): Call<ListOfMailIds>
}