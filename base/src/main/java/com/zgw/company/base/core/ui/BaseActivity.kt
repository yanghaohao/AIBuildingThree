package com.zgw.company.base.core.ui

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jaeger.library.StatusBarUtil
import com.safframework.utils.support
import com.zgw.company.base.R
import com.zgw.company.base.core.config.IActivityManager
import com.zgw.company.base.core.ext.finishWithAnim
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    protected var composite = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar()
        IActivityManager.instance.registerActivity(this)
    }

    protected open fun setStatusBar(){
        support(Build.VERSION_CODES.M,{
            StatusBarUtil.setLightMode(this)
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.page_bg))
        },{
            StatusBarUtil.setColor(this,ContextCompat.getColor(this, R.color.page_bg))
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> finish()
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        composite.clear()
        IActivityManager.instance.registerActivity(this)
    }

    fun addFragment(fragment : BaseFragment, layoutId : Int, tag : String){
        supportFragmentManager.beginTransaction().add(layoutId,fragment,tag).commit()
    }

    fun addFragmentWithBackStack(fragment: BaseFragment, layoutResId: Int, tag: String){
        supportFragmentManager.beginTransaction().add(layoutResId,fragment,tag).addToBackStack(tag).commit()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean = if (KeyEvent.KEYCODE_BACK == keyCode) {
        if (supportFragmentManager.backStackEntryCount == 1 && supportFragmentManager.backStackEntryCount == 0){
            finishWithAnim()
            true
        }else{
            supportFragmentManager.popBackStackImmediate()
            true
        }
    }else{
        super.onKeyDown(keyCode, event)
    }
}