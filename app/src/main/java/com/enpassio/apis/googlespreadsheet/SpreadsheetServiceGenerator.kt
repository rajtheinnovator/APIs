package com.enpassio.apis.googlespreadsheet

import android.text.TextUtils
import com.enpassio.apis.AuthenticationInterceptor
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Abhishek Raj on 12/21/2018.
 */
object SpreadsheetServiceGenerator {

    val API_BASE_URL = "https://sheets.googleapis.com/"

    private val httpClient = OkHttpClient.Builder()

    var retrofit: Retrofit? = null
    private val builder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

    fun <S> createService(serviceClass: Class<S>): S {
        return createService(serviceClass, null)
    }

    fun <S> createService(
            serviceClass: Class<S>, clientId: String?, clientSecret: String?): S {
        if (!TextUtils.isEmpty(clientId) && !TextUtils.isEmpty(clientSecret)) {
            val authToken = Credentials.basic(clientId, clientSecret)
            return createService(serviceClass, authToken)
        }

        return createService(serviceClass, null, null)
    }

    fun <S> createService(
            serviceClass: Class<S>, authToken: String?): S {
        if (!TextUtils.isEmpty(authToken)) {
            val interceptor = AuthenticationInterceptor(authToken!!)

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor)

                builder.client(httpClient.build())
                retrofit = builder.build()
            }
        }

        return retrofit!!.create(serviceClass)
    }
}