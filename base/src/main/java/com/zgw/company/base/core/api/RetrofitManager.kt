package com.zgw.company.base.core.api

import com.safframework.http.interceptor.LoggingInterceptor
import com.zgw.company.base.BuildConfig
import com.zgw.company.base.core.http.interceptor.HeaderInterceptor
import com.zgw.company.base.core.http.interceptor.HttpLoginInterceptor
import com.zgw.company.base.core.utils.GSONFactory
import com.zgw.company.base.core.utils.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class RetrofitManager private constructor(){

    private val apiService: ApiService

    private val okHttpClient: OkHttpClient

    init {
        val loggingInterceptor = LoggingInterceptor.Builder()
            .hideVerticalLine()
            .request()
            .requestTag("Request")
            .response()
            .responseTag("Response")
            .loggable(BuildConfig.DEBUG)

        okHttpClient = OkHttpClient.Builder()
            .writeTimeout((10 * 1000).toLong(), TimeUnit.MILLISECONDS)
            .readTimeout((10 * 1000).toLong(), TimeUnit.MILLISECONDS)
            .connectTimeout((10 * 1000).toLong(), TimeUnit.MILLISECONDS)
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(HttpLoginInterceptor())
            .addInterceptor(loggingInterceptor.build())
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(ApiService.MAGIC_BOX_API)
            .addConverterFactory(GSONFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(okHttpClient)
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun retrofit(): Retrofit = retrofit

    fun apiService(): ApiService = apiService

    fun okHttpClient(): OkHttpClient = okHttpClient

    private object Holder {
        val MANAGER = RetrofitManager()
    }

    companion object {

        private lateinit var retrofit: Retrofit

        @JvmStatic
        fun get(): RetrofitManager {
            return Holder.MANAGER
        }
    }

}