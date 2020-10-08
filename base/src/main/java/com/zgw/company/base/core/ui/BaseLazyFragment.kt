package com.zgw.company.base

import android.os.Bundle
import android.view.View
import com.zgw.company.base.core.ui.BaseFragment

abstract class BaseLazyFragment : BaseFragment(){

    private var isPrepared : Boolean = false
    private var isVisibleLocal : Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPrepared = true
        dispatchUserVisibleHint()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint){
            isVisibleLocal = true
            dispatchUserVisibleHint()
        }else{
            isVisibleLocal = false
        }
    }

    private fun dispatchUserVisibleHint() {
        if (!isVisibleLocal || !isPrepared){
            return
        }

        onLazyLoad()

        isPrepared = false
    }

    protected abstract fun onLazyLoad()
}