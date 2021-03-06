package com.zgw.company.base.core.ui

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes

interface LoadingInterface {

    fun setErrorLayoutReloadListener(layoutClickListener: View.OnClickListener)
    fun setEmptyLayoutReloadListener(layoutClickListener: View.OnClickListener)


    fun setLoadingButtonClickListener(loadingButtonClickListener: View.OnClickListener)


    fun setEmptyButtonClickListener(emptyButtonClickListener: View.OnClickListener)


    fun setErrorButtonClickListener(errorButtonClickListener: View.OnClickListener)


    fun setViewType(viewType: Int)

    fun showErrorWithMessage(@DrawableRes resId: Int, text: CharSequence)

    fun showErrorWithLayout(@LayoutRes resId: Int)

    fun showError()

    fun showEmptyWithMessage(@DrawableRes resId: Int, text: CharSequence)

    fun showEmpty()


    fun showLoading()

    fun showContent()

}