package com.zgw.company.base.core.api

open class ApiRepository {

    val apiInterface: ApiService = RetrofitManager.get().apiService()
}