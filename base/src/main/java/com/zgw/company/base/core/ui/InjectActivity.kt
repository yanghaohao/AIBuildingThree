package com.zgw.company.base.core.ui

import android.os.Bundle
import com.zgw.company.base.core.ui.BaseActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector

import javax.inject.Inject

open class InjectActivity : BaseActivity() , HasAndroidInjector{

    @Inject
    @JvmField
    var supportFragmentInjector : DispatchingAndroidInjector<Any>? = null

//    override fun supportFragmentInjector() : AndroidInjector<Fragment>? = supportFragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun androidInjector(): AndroidInjector<Any>? = supportFragmentInjector
}