package com.enpassio.apis

import io.reactivex.Observable
import retrofit2.http.GET

interface GmailService {
    @GET("gmail/v1/users/userId/messages/list")
    fun getListOfEmails(): Observable<List<GmailData>>
}