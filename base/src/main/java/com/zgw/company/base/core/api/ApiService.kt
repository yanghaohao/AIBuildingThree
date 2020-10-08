package com.zgw.company.base.core.api

import androidx.lifecycle.LiveData
import com.zgw.company.base.core.http.entity.request.LoginParams
import com.zgw.company.base.core.http.entity.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    companion object{
        const val MAGIC_BOX_API = ""
    }


    @GET
    fun <T>  get(
        @Url url: String?,
        @QueryMap params: Map<String?, Any?>?
    ): LiveData<ApiResponse<T>>

    @FormUrlEncoded
    @POST
    fun <T> post(
        @Url url: String?,
        @FieldMap params: Map<String?, Any?>?
    ): LiveData<ApiResponse<T>>

    @POST()
    fun logins(@Url url: String?,@Body param: LoginParams): Call<LoginResponse>

    @FormUrlEncoded
    @PUT
    fun <T> put(
        @Url url: String?,
        @FieldMap params: Map<String?, Any?>?
    ): LiveData<ApiResponse<T>>

    @DELETE
    fun <T> delete(
        @Url url: String?,
        @QueryMap params: Map<String?, Any?>?
    ): LiveData<ApiResponse<T>>

    @Streaming
    @GET
    fun <T> download(
        @Url url: String?,
        @QueryMap params: Map<String?, Any?>?
    ): LiveData<ApiResponse<T>>

    @Multipart
    @POST
    fun <T> upload(
        @Url url: String?,
        @Part file: MultipartBody.Part?
    ): LiveData<ApiResponse<T>>

    @POST
    fun <T> postRaw(
        @Url url: String?,
        @Body body: RequestBody?
    ): LiveData<ApiResponse<T>>

    @PUT
    fun <T> putRaw(
        @Url url: String?,
        @Body body: RequestBody?
    ): LiveData<ApiResponse<T>>
}