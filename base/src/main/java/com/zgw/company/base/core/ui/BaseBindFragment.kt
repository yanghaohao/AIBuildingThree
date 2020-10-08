package com.zgw.company.base.core.ui

import android.graphics.ColorSpace
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

abstract class BaseBindFragment<DB : ViewDataBinding,VM : ViewModel>: BaseFragment(){


    protected lateinit var db:DB

    abstract fun getLayoutId():Int


    protected fun initView(){

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        db = DataBindingUtil.inflate(inflater,getLayoutId(),container,false)
        db.lifecycleOwner = this
        initView()
        return db.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}