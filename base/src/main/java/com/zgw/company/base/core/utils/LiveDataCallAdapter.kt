package com.zgw.company.base.core.utils

import androidx.lifecycle.LiveData
import com.zgw.company.base.core.api.ApiResponse
import com.zgw.company.base.core.api.HttpResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataCallAdapter<R> (private val responseType : Type) : CallAdapter<HttpResponse<R>,LiveData<ApiResponse<R>>>{
    override fun adapt(call: Call<HttpResponse<R>>): LiveData<ApiResponse<R>> {
        return object : LiveData<ApiResponse<R>>(){
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false,true)){
                    call.enqueue(object : Callback<HttpResponse<R>>{
                        override fun onFailure(call: Call<HttpResponse<R>>, t: Throwable) {
                            postValue(ApiResponse.create(t))
                        }

                        override fun onResponse(
                            call: Call<HttpResponse<R>>,
                            response: Response<HttpResponse<R>>
                        ) {
                            postValue(ApiResponse.create(response))
                        }
                    })
                }
            }
        }
    }

    override fun responseType(): Type = responseType

}