package com.zgw.company.base.core.api

import androidx.lifecycle.LiveData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RxRestService {

    @GET
    fun <T>  get(
        @Url url: String?,
        @QueryMap params: Map<String?, Any?>?
    ): LiveData<T>

    @FormUrlEncoded
    @POST
    fun <T> post(
        @Url url: String?,
        @FieldMap params: Map<String?, Any?>?
    ): LiveData<T>

    @FormUrlEncoded
    @PUT
    fun <T> put(
        @Url url: String?,
        @FieldMap params: Map<String?, Any?>?
    ): LiveData<T>

    @DELETE
    fun <T> delete(
        @Url url: String?,
        @QueryMap params: Map<String?, Any?>?
    ): LiveData<T>

    @Streaming
    @GET
    fun <T> download(
        @Url url: String?,
        @QueryMap params: Map<String?, Any?>?
    ): LiveData<T>

    @Multipart
    @POST
    fun <T> upload(
        @Url url: String?,
        @Part file: MultipartBody.Part?
    ): LiveData<T>

    @POST
    fun <T> postRaw(
        @Url url: String?,
        @Body body: RequestBody?
    ): LiveData<T>

    @PUT
    fun <T> putRaw(
        @Url url: String?,
        @Body body: RequestBody?
    ): LiveData<T>
}