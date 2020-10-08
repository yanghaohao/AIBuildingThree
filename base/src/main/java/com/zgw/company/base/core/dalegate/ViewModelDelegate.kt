package com.zgw.company.base.core.dalegate

import androidx.lifecycle.ViewModelProviders
import com.zgw.company.base.core.ui.BaseActivity
import com.zgw.company.base.core.ui.BaseFragment
import com.zgw.company.base.core.ViewModel.BaseViewModel
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ViewModelDelegate <out T : BaseViewModel>(private val clazz:KClass<T>, private val fromActivity:Boolean){
    private var viewModel: T? = null

    operator fun getValue(thisRef: BaseActivity, property: KProperty<*>) = buildViewModel(activity = thisRef)

    operator fun getValue(thisRef: BaseFragment, property: KProperty<*>) = if (fromActivity)
        buildViewModel(activity = thisRef.activity as? BaseActivity
            ?: throw IllegalStateException("Activity must be as BaseActivity"))
    else buildViewModel(fragment = thisRef)

    private fun buildViewModel(activity: BaseActivity? = null, fragment: BaseFragment? = null): T {
        if (viewModel != null) return viewModel!!

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(clazz.java)
        } ?: fragment?.let {
            viewModel = ViewModelProviders.of(it).get(clazz.java)
        } ?: throw IllegalStateException("Activity and Fragment null! =(")

        return viewModel!!
    }
}

fun <T : BaseViewModel> BaseActivity.viewModelDelegate(clazz: KClass<T>) = ViewModelDelegate(clazz, true)

fun <T : BaseViewModel> BaseFragment.viewModelDelegate(clazz: KClass<T>, fromActivity: Boolean = true) = ViewModelDelegate(clazz, fromActivity)