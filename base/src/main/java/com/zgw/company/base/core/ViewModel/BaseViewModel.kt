package com.zgw.company.base.core.ViewModel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel(){

    protected var compositeDisposable : CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        cancel()
    }

    protected fun cancel(){
        compositeDisposable.clear()
    }
}