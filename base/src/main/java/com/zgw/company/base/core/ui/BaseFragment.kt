package com.zgw.company.base.core.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.zgw.company.base.core.ext.goFragment
import com.zgw.company.base.core.ext.routerWithAnim
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment(){

    lateinit var mContext:Context
    var compositeDisposable = CompositeDisposable()
    private var mContainerView : View? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    protected fun openPage(context: BaseFragment, path: String, @IdRes id: Int, addToBackStack: Boolean = true) = routerWithAnim(path).goFragment(context,id,addToBackStack)
}