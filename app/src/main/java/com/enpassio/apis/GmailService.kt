package com.enpassio.apis

import retrofit2.Call
import retrofit2.http.*

interface LoginService {
    @FormUrlEncoded
    @POST("/oauth2/v4/token/")
    fun getAccessToken(
            @Field("scope") scope: String,
            @Field("code") code: String,
            @Field("client_id") client_id: String,
            @Field("redirect_uri") redirect_uri: String,
            @Field("grant_type") grant_type: String): Call<AccessToken>
}

interface GmailService {
    @GET("gmail/v1/users/{userId}/messages/list")
    fun getListOfEmails(@Path("userId") userId: String): Call<ListOfMailIds>
}