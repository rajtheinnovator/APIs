package com.enpassio.apis

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthenticationInterceptor(private val authToken: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val builder = original.newBuilder()
                .header("Authorization", "Bearer " + authToken)

        val request = builder.build()
        return chain.proceed(request)
    }
}
