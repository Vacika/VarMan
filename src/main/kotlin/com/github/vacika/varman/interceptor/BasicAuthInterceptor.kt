package com.github.vacika.varman.interceptor

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor(private val userName: String, private val password: String): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
                .addHeader("Authorization", Credentials.basic(this.userName, this.password)).build()
        return chain.proceed(authenticatedRequest)
    }

}