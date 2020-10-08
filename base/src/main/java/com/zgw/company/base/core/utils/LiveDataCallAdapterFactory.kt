package com.zgw.company.base.core.utils

import androidx.lifecycle.LiveData
import com.zgw.company.base.core.api.ApiResponse
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataCallAdapterFactory : Factory() {

    override fun get(returnType : Type,annotation: Array<Annotation>,retrofit: Retrofit) : CallAdapter<*, *>?{
        if (getRawType(returnType) != LiveData::class.java){
            return null
        }

        val liveDataType = getParameterUpperBound(0,returnType as ParameterizedType)
        val rawLiveDataType = getRawType(liveDataType)
        if (rawLiveDataType != ApiResponse::class.java){
            throw IllegalArgumentException("类型必须是一个资源")
        }

        if (liveDataType !is ParameterizedType){
            throw IllegalArgumentException("资源必须是一个参数类型")
        }

        val body =  getParameterUpperBound(0,liveDataType)
        return LiveDataCallAdapter<Any>(body)
    }
}