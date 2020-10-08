package com.zgw.company.base.core.utils

import android.app.Activity
import android.graphics.Rect
import android.view.View
import androidx.fragment.app.Fragment

class SoftKeyInputVisibleUtils {
    companion object {
        private const val SOFT_KEY_BOARD_MIN_HEIGHT = 200
    }

    private lateinit var callback: (Boolean) -> Unit

    fun registerFragment(fragment: Fragment, callback: (Boolean) -> Unit): SoftKeyInputVisibleUtils {
        return registerView(fragment.view, callback)
    }

    fun registerActivity(activity: Activity, callback: (Boolean) -> Unit): SoftKeyInputVisibleUtils {
        return registerView(activity.window.decorView.findViewById(android.R.id.content), callback)
    }

    fun registerView(view: View?, callback: (Boolean) -> Unit): SoftKeyInputVisibleUtils {
        this.callback = callback
        view?.let {
            it.viewTreeObserver.addOnGlobalLayoutListener {
                val r = Rect()
                view.getWindowVisibleDisplayFrame(r)
                val heightDiff = view.rootView.height - (r.bottom - r.top)
                if (heightDiff > SOFT_KEY_BOARD_MIN_HEIGHT) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
        }
        return this
    }
}