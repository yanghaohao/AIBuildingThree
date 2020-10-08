package com.zgw.company.base.core.ui

import android.content.Context
import com.zgw.company.base.core.ui.BaseFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class InjectFragment : BaseFragment() , HasAndroidInjector{

    @Inject
    @JvmField
    var childFragmentInjector : DispatchingAndroidInjector<Any>? = null

//    override fun supportFragmentInjector(): AndroidInjector<Fragment>? = childFragmentInjector

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun androidInjector(): AndroidInjector<Any>? = childFragmentInjector
}