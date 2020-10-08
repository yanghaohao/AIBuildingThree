package com.zgw.company.base.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.text.TextUtils
import com.chenenyu.router.Router
import com.zgw.company.base.core.BaseApplication
import com.zgw.company.base.core.config.Config
import java.util.*

object LanguageUtil {
    private const val AUTO = 0
    private const val CHINESE = 1
    private const val ENGLISH = 2

    fun getHttpLanguageHeader() = combineHeader(getSelectedLocale())


    fun routToMainForce(activity: Activity? ) = Router.build(Config.ROUTER_MAIN_ACTIVITY)
        .anim(0, 0)//不使用跳转动画
        .with(Config.LANGUAGE_CHANGED, true)
        .also {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                //android 28 需要重新初始化界面，否则无法切换语言
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        .go(activity)

    fun needChange(select: Int) = getAppLocale() != getLocalByIndex(select)

    fun saveSelectLanguage(context: Context, select: Int) {
        SPHelper.create().putInt(Config.SELECTED_LANGUAGE, select)
        setApplicationLanguage(context)
    }

    fun setLocal(context: Context): Context {
        return updateResources(context, getSelectedLocale(context))
    }

    fun setApplicationLanguage(context: Context) {
        val resources = context.applicationContext.resources
        val dm = resources.displayMetrics
        val config = resources.configuration
        val locale = getSelectedLocale(context)
        config.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
            context.applicationContext.createConfigurationContext(config)
            Locale.setDefault(locale)
        }
        resources.updateConfiguration(config, dm)
    }

    fun onConfigurationChanged(context: Context) {
        setLocal(context)
        setApplicationLanguage(context)
    }

    fun checkLocalLanguage(activity: Activity) {
        val locale = getSystemLocale() ?: return
        val cacheStr = SPHelper.create().getString(Config.LAST_LANGUAGE)
        val localeStr = combineHeader(locale)
        if (TextUtils.isEmpty(cacheStr)) {
            SPHelper.create().putString(Config.LAST_LANGUAGE, localeStr)
        } else {
            if (cacheStr != localeStr) {
                SPHelper.create().putString(Config.LAST_LANGUAGE, localeStr)
                routToMainForce(activity)
            } else {
                if (getSelectedLanguage(BaseApplication.instance) == AUTO) {
                    val appLocaleStr = combineHeader(getAppLocale())
                    if (appLocaleStr != localeStr) {
                        routToMainForce(activity)
                    }
                }
            }
        }
    }

    fun getSelectedLocale(context: Context = BaseApplication.instance): Locale {
        val selectedLanguage = getSelectedLanguage(context)
        return getLocalByIndex(selectedLanguage)
    }

    fun getSelectedLanguage(context: Context): Int {
        return SPHelper.create(context).getInt(Config.SELECTED_LANGUAGE, AUTO)
    }

    private fun getLocalByIndex(selectedLanguage: Int): Locale {
        return when (selectedLanguage) {
            CHINESE -> Locale.CHINA
            ENGLISH -> Locale.US
            else -> getSystemLocale()
        }
    }

    private fun combineHeader(locale: Locale): String = locale.language + "_" + locale.country

    private fun getAppLocale() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        BaseApplication.instance.resources.configuration.locales[0]
    } else {
        BaseApplication.instance.resources.configuration.locale
    }

    private fun getSystemLocale() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Resources.getSystem().configuration.locales[0]
    } else {
        Resources.getSystem().configuration.locale
    }

    private fun updateResources(context: Context, locale: Locale): Context {
        val res = context.resources
        val config = res.configuration

        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
