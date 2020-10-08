package com.zgw.company.base.core.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.zgw.company.base.core.api.ApiResponse

abstract class NetworkOnlyResource<RequestType> @MainThread constructor() : NetworkBoundResource<RequestType, RequestType>() {

  @WorkerThread
  override fun saveCallResult(item: RequestType) {
    liveData.postValue(item)
  }

  @MainThread
  override fun shouldFetch(data: RequestType?): Boolean {
    return true
  }

  @MainThread
  override fun loadFromDb(): LiveData<RequestType> {
    return liveData
  }

  @MainThread
  abstract override fun createCall(): LiveData<ApiResponse<RequestType>>

  @MainThread
  override fun onFetchFailed() {
  }

}