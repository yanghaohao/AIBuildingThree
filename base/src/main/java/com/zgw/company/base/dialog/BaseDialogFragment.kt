package com.zgw.company.base.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.zgw.company.base.R
import com.zgw.company.base.dialog.interfaces.INegativeButtonDialogListener
import com.zgw.company.base.dialog.interfaces.IPositionButtonDialogListener
import com.zgw.company.base.dialog.interfaces.ISimpleDialogCancelListener
import io.reactivex.disposables.CompositeDisposable

abstract class BaseDialogFragment : DialogFragment() {

    protected var mRequestCode : Int = 0
    private var cancelledOnTouchOutSide : Boolean = false
    private var fullScreen : Boolean = false
    private var mTheme : Int = 0
    private var dimAmount : Float = 0.toFloat()
    private var showBottom : Boolean = false
    private var animStyle : Int = 0
    private var scale : Double = 0.toDouble()
    protected var compositeDisposable = CompositeDisposable()

    var cancelListener : ISimpleDialogCancelListener? = null
    get() = if (field == null) getDialogListener(ISimpleDialogCancelListener::class.java) else field

    var positiveListener: IPositionButtonDialogListener? = null
        get() = if (field == null) getDialogListener(IPositionButtonDialogListener::class.java) else field

    var negativeListener: INegativeButtonDialogListener? = null
        get() = if (field == null) getDialogListener(INegativeButtonDialogListener::class.java) else field

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var args = arguments
        if (args != null){
            cancelledOnTouchOutSide = args.getBoolean(DialogBuilder.ARG_CANCELABLE_ON_TOUCH_OUTSIDE)
            fullScreen = args.getBoolean(DialogBuilder.ARG_FULL_SCREEN)
            showBottom = args.getBoolean(DialogBuilder.ARG_SHOW_BUTTOM)
            mTheme = args.getInt(DialogBuilder.ARG_USE_THEME, R.style.DialogTheme)
            animStyle = args.getInt(DialogBuilder.ARG_ANIM_STYLE)
            dimAmount = args.getFloat(DialogBuilder.ARG_DIM_AMOUNT)
            scale = args.getDouble(DialogBuilder.ARG_SCALE)
        }

        setStyle(STYLE_NO_TITLE,mTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val builder = Builder(inflater,container)
        return build(builder).create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val targetFragment = targetFragment
        if (targetFragment != null){
            mRequestCode = targetRequestCode
        }else{
            val args = arguments
            if (args != null){
                mRequestCode = args.getInt(DialogBuilder.ARG_REQUEST_CODE,0)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initParams()
    }

    private fun initParams() {
        dialog!!.setCanceledOnTouchOutside(cancelledOnTouchOutSide)
        val window = dialog!!.window
        if (null != window){
            val lp = window.attributes
            lp.dimAmount = dimAmount
            if (showBottom){
                lp.gravity = Gravity.BOTTOM
                if (animStyle == 0){
                    animStyle = R.style.AnimBottom
                }
            }

            if (scale != 1.0){
                lp.width = (getScreenWidth(context!!) * scale).toInt()
            }

            if (fullScreen){
                lp.width = getScreenWidth(context!!)
            }

            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.setWindowAnimations(animStyle)
            window.attributes = lp
        }
    }

    override fun onDestroyView() {
        if (dialog != null && retainInstance){
            dialog!!.setDismissMessage(null)
        }
        super.onDestroyView()
        compositeDisposable.clear()
    }

    override fun onDismiss(dialog: DialogInterface) {
        cancelListener?.onCancelled(0)
        super.onDismiss(dialog)
    }

    fun showWithDismissPreDialog(manager: FragmentManager, tag:String, dismissPreDialog:Boolean){
        val ft = manager.beginTransaction()
        val targetFragment = manager.findFragmentByTag(tag)
        if (dismissPreDialog && targetFragment != null && targetFragment is BaseDialogFragment){
            ft.remove(targetFragment).commit()
        }
        show(manager,tag)
    }

    fun showAllowingStateLoss(manager: FragmentManager,tag: String,dismissPreDialog: Boolean){
        val ft = manager.beginTransaction()
        val targetFragment = manager.findFragmentByTag(tag)
        if (dismissPreDialog && targetFragment != null && targetFragment is BaseDialogFragment){
            ft.remove(targetFragment)
        }
        ft.add(this,tag)
        ft.commitAllowingStateLoss()
    }

    fun <T> getDialogListener(listener: Class<T> ) : T?{
        val targetFragment = targetFragment
        if (targetFragment != null && listener.isAssignableFrom(targetFragment.javaClass)){
            return targetFragment as T
        }
        if (activity != null && listener.isAssignableFrom(activity!!.javaClass)){
            return activity as T
        }
        return null
    }

    protected class Builder(val  layoutInflater: LayoutInflater,private val container: ViewGroup?){
        private var mCustomView : View? = null

        fun setView(view: View) : Builder{
            mCustomView = view
            return this
        }

        internal fun create() : View? {
            return mCustomView
        }
    }

    protected abstract fun build(initialBuilder : Builder) : Builder

    companion object{
        fun getScreenWidth(context : Context) : Int{
            val dm = context.resources.displayMetrics
            return dm.widthPixels
        }

        fun getScreenHeight(context: Context) : Int{
            val dm = context.resources.displayMetrics
            return dm.heightPixels
        }
    }
}