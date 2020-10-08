package com.zgw.company.base.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.zgw.company.base.R
import com.zgw.company.base.core.config.Config
import java.io.Serializable

open class DialogBuilder(val context: FragmentActivity, private val mClass: Class<out BaseDialogFragment>) {

    private var mTag = DEFAULT_TAG
    var mRequestCode = DEFAULT_REQUEST_CODE
    private var mTargetFragment: Fragment? = null
    private var mCancelable = true
    private var mCancelableOnTouchOutside = true
    private var mfullScreen = false
    private var mTheme = R.style.DialogTheme
    private var mShowBottom: Boolean = false
    private var mDimAmount = 0.5f
    private var mAnimStyle: Int = 0
    private var mScale = Config.DIALOG_BIG_SCALE
    private var args: Bundle? = null
    private var mDismissPreDialog: Boolean = true

    open fun prepareArguments(): Bundle {
        if (args == null) {
            args = Bundle()
        }
        return args!!
    }

    fun putArgs(key: String, value: Serializable): DialogBuilder {
        prepareArguments().putSerializable(key, value)
        return this
    }

    fun setCancelable(cancelable: Boolean): DialogBuilder {
        mCancelable = cancelable
        return this
    }

    fun setCancelableOnTouchOutside(cancelable: Boolean): DialogBuilder {
        mCancelableOnTouchOutside = cancelable
        if (cancelable) {
            mCancelable = cancelable
        }
        return this
    }

    fun setFullScreen(fullScreen: Boolean): DialogBuilder {
        mfullScreen = fullScreen
        return this
    }

    fun setDimAmount(dimAmount: Float): DialogBuilder {
        mDimAmount = dimAmount
        return this
    }

    fun setScale(scale: Double): DialogBuilder {
        mScale = scale
        return this
    }

    fun setShowBottom(showBottom: Boolean): DialogBuilder {
        mShowBottom = showBottom
        return this
    }

    fun setAnimStyle(animStyle: Int): DialogBuilder {
        mAnimStyle = animStyle
        return this
    }

    fun setTargetFragment(fragment: Fragment, requestCode: Int): DialogBuilder {
        mTargetFragment = fragment
        mRequestCode = requestCode
        return this
    }

    fun setRequestCode(requestCode: Int): DialogBuilder {
        mRequestCode = requestCode
        return this
    }

    fun setTag(tag: String): DialogBuilder {
        mTag = tag
        return this
    }

    fun setTheme(theme: Int): DialogBuilder {
        mTheme = theme
        return this
    }

    fun setDismissPreDialog(dismissPreDialog: Boolean): DialogBuilder {
        mDismissPreDialog = dismissPreDialog
        return this
    }

    private fun create(): BaseDialogFragment {
        val args = prepareArguments()
        val fragment = Fragment.instantiate(context, mClass.name, args) as BaseDialogFragment
        args.putBoolean(ARG_CANCELABLE_ON_TOUCH_OUTSIDE, mCancelableOnTouchOutside)
        args.putBoolean(ARG_FULL_SCREEN, mfullScreen)
        args.putBoolean(ARG_SHOW_BUTTOM, mShowBottom)
        args.putInt(ARG_USE_THEME, mTheme)
        args.putFloat(ARG_DIM_AMOUNT, mDimAmount)
        args.putInt(ARG_ANIM_STYLE, mAnimStyle)
        args.putDouble(ARG_SCALE, mScale)

        if (mTargetFragment != null) {
            fragment.setTargetFragment(mTargetFragment, mRequestCode)
        } else {
            args.putInt(ARG_REQUEST_CODE, mRequestCode)
        }
        fragment.isCancelable = mCancelable
        return fragment
    }

    fun show(): BaseDialogFragment {
        val fragment = create()
        fragment.showWithDismissPreDialog(context.supportFragmentManager, mTag, mDismissPreDialog)
        return fragment
    }

    fun showAllowingStateLoss(): BaseDialogFragment {
        val fragment = create()
        fragment.showAllowingStateLoss(context.supportFragmentManager, mTag, mDismissPreDialog)
        return fragment
    }


    companion object {

        const val ARG_REQUEST_CODE = "request_code"
        const val ARG_FULL_SCREEN = "arg_full_screen"
        const val ARG_SHOW_BUTTOM = "arg_show_buttom"
        const val ARG_CANCELABLE_ON_TOUCH_OUTSIDE = "cancelable_oto"
        var ARG_USE_THEME = "arg_use_theme_type"
        var ARG_DIM_AMOUNT = "arg_dim_amount"
        var ARG_ANIM_STYLE = "arg_anim_style"
        var ARG_SCALE = "arg_scale"
        const val DEFAULT_TAG = "simple_dialog"
        const val DEFAULT_REQUEST_CODE = -42
    }
}
