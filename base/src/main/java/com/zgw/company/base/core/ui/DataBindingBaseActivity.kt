package com.zgw.company.base.core.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner

abstract class DataBindingBaseActivity<B : ViewDataBinding> : BaseActivity() , LifecycleOwner{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:B = DataBindingUtil.setContentView(this,setLayoutId())

        binding.lifecycleOwner = this

        initView()
    }

    abstract fun setLayoutId() : Int

    open fun initView(){

    }
}