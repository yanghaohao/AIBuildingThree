package com.zgw.company.base.core

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.multidex.MultiDex
import com.safframework.log.L
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.taobao.sophix.SophixManager
import com.zgw.company.base.R
import io.reactivex.Observable
import kotlin.properties.Delegates

open class MyApplication : BaseApplication(){

    override fun onCreate() {
        super.onCreate()

        SophixManager.getInstance().queryAndLoadNewPatch();
        instance = this
        initConfig()

    }

    private fun initConfig() {
        val initLogObservable = Observable.create<Any>{
            L.header(getHeader())
        }
    }

    private fun getHeader(): String {
        val stringBuffer = StringBuffer().append("(appName:"+getString(R.string.app_name))
        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName,0)
        if (info != null) stringBuffer.append("app version" + info.versionName)
        return stringBuffer.append("model:"+Build.MODEL).append("osVersion:"+Build.VERSION.RELEASE).append(")").toString()
    }

    companion object{
        var instance : BaseApplication by Delegates.notNull()
        private set
    }

    init {
        initRefreshLayout()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        initRefreshLayout()
    }

    private fun initRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator{ context, layout ->
            layout.setHeaderHeight(40F)
            layout.setEnableOverScrollDrag(true)
            layout.setEnableAutoLoadMore(true)
            layout.setEnableFooterFollowWhenLoadFinished(true)
            ClassicsHeader.REFRESH_HEADER_REFRESHING = getString(R.string.refreshing)
            ClassicsHeader(context).setEnableLastTime(false).setTextSizeTitle(14F).setSpinnerStyle(SpinnerStyle.Translate)
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator{ context, layout ->
            layout.setHeaderHeight(40F)
            layout.setEnableOverScrollDrag(true)
            ClassicsFooter(context).setDrawableSize(20f).setTextSizeTitle(14F)
        }
    }
}