package com.zgw.company.base.core.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
//        requestBuilder.addHeader("Accept-Language", com.zgw.company.base.core.utils.LanguageUtil.getHttpLanguageHeader())
        requestBuilder.addHeader("os_type", "0")
//        requestBuilder.addHeader("Authorization", User.currentUser.accessToken)

        return chain.proceed(requestBuilder.build())
    }
}