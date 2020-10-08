package com.zgw.company.base.core.ext

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.chenenyu.router.IRouter
import com.chenenyu.router.Router
import com.zgw.company.base.core.ui.BaseActivity
import com.zgw.company.base.core.ui.BaseFragment
import com.zgw.company.base.R

fun BaseActivity.finishWithAnim(enterAnim:Int = R.anim.slide_in_form_left, exitAnim:Int = R.anim.slide_in_form_right){
    finish()
    overridePendingTransition(enterAnim,exitAnim)
}

fun routerWithAnim(path : String) : IRouter = Router.build(path).anim(R.anim.slide_in_form_right,R.anim.slide_in_form_left)

fun IRouter.goFragment(targetFragment: BaseFragment, @IdRes containerViewId : Int, addToBackStack : Boolean = true, backStackName : String? = null){
    val fm = targetFragment.fragmentManager
    val transaction = fm?.fragmentTranslation()
    val fragment= getFragment(targetFragment.context!!) as BaseFragment
    transaction?.add(containerViewId,fragment)
    if (addToBackStack) transaction?.addToBackStack(backStackName)
    transaction?.commit()
}

fun IRouter.goFragment(activity: BaseActivity, @IdRes containerViewId: Int, addToBackStack: Boolean = true, backStackName: String? = null) {
    val fm = activity.supportFragmentManager
    val transaction = fm.beginTransaction()
    val fragment = getFragment(activity) as BaseFragment
    transaction.add(containerViewId, fragment)
    if (addToBackStack) transaction.addToBackStack(backStackName)
    transaction.commit()
}

fun FragmentManager.fragmentTranslation(): FragmentTransaction = beginTransaction().setCustomAnimations(R.anim.slide_in_form_right,R.anim.slide_in_form_left)