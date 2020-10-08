package com.zgw.company.base.core.ext

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.chenenyu.router.Router
import com.zgw.company.base.core.config.Config
import com.zgw.company.base.core.ext.Interceptor

fun NavController.interceptor(interceptor: Interceptor): NavController {
    if (interceptor.skip()) {
        navigate(interceptor.getResId())
    }
    return this
}

/**
 * 跳转到MainActivity
 * 由于MainActivity是SingleTask的 因此会关闭上层其他界面
 */
fun Fragment.routerToMain() = Router.build(Config.ROUTER_MAIN_ACTIVITY)
    .anim(0, 0)
    .go(activity)

/**
 * 跳转到MainActivity
 * 由于MainActivity是SingleTask的 因此会关闭上层其他界面
 */
fun Activity.routerToMain() = Router.build(Config.ROUTER_MAIN_ACTIVITY)
    .anim(0, 0)
    .go(this)