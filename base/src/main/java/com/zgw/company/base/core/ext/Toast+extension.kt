package com.zgw.company.base.core.ext

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zgw.company.base.core.MyApplication
import com.zgw.company.base.R

private fun Toast.setGravityCenter(): Toast {
    setGravity(Gravity.CENTER, 0, 0)
    return this
}

/**
 * 设置Toast字体及背景
 * @param messageColor
 * @param background
 * @return
 */
private fun Toast.setBackground(@ColorInt messageColor: Int, @DrawableRes background: Int): Toast {
    val view = view
    if (view != null) {
        val message = view.findViewById(android.R.id.message) as TextView
        view.setBackgroundResource(background)
        message.setTextColor(messageColor)
    }
    return this
}

@SuppressLint("ShowToast")
fun toast(text: CharSequence?, @ColorInt messageColor: Int = Color.WHITE, @DrawableRes background: Int = R.color.color_bg_trans, vararg views: Pair<View, Int> = arrayOf()) =
        runOnUIThread {
            Toast.makeText(MyApplication.instance, text, Toast.LENGTH_SHORT)
                    .setGravityCenter()
                    .also {
                        if (views.isNotEmpty()) {
                            for (addView in views) {
                                (it.view as LinearLayout).addView(addView.first, addView.second)
                            }
                        }
                    }
                    .show()
        }

@SuppressLint("ShowToast")
fun toast(@StringRes res: Int, @ColorInt messageColor: Int = Color.WHITE, @DrawableRes background: Int = R.color.color_bg_trans, vararg views: Pair<View, Int> = arrayOf()) =
        toast(MyApplication.instance.resources.getString(res), messageColor, background, *views)
