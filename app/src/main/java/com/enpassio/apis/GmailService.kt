package com.enpassio.apis

import retrofit2.Call
import retrofit2.http.*

interface TokenService {
    @FormUrlEncoded
    @POST("/oauth2/v4/token/")
    fun getAccessTokenFromAuthCode(
            @Field("scope") scope: String,
            @Field("code") code: String,
            @Field("client_id") client_id: String,
            @Field("redirect_uri") redirect_uri: String,
            @Field("grant_type") grant_type: String): Call<AccessToken>
}

interface RefreshTokenService {
    @FormUrlEncoded
    @POST("/oauth2/v4/token/")
    fun getAccessTokenFromRfreshToken(
            @Field("refresh_token") refreshToken: String,
            @Field("client_id") client_id: String,
            @Field("grant_type") grant_type: String): Call<AccessToken>
}

interface GmailService {
    @GET("gmail/v1/users/{userId}/messages")
    fun getListOfEmails(@Path("userId") userId: String,
                        @Query("scope") scope: String,
                        @Query("client_id") client_id: String,
                        @Query("redirect_uri") redirect_uri: String): Call<ListOfMailIds>
}

interface ParticularItemService {
    @GET("gmail/v1/users/{userId}/messages/{id}")
    fun getParticularEmail(@Path("userId") userId: String,
                           @Path("id") messageId: String,
                           @Query("scope") scope: String,
                           @Query("client_id") client_id: String,
                           @Query("redirect_uri") redirect_uri: String
    ): Call<Message>
}