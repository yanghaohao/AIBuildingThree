package com.zgw.company.base.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Point
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.WindowManager
import com.zgw.company.base.core.MyApplication
import java.util.*

object DeviceInfoUtils {
    private const val CHINA_CARRIER_UNKNOWN = "0"
    private const val CHINA_MOBILE = "1"
    private const val CHINA_UNICOM = "2"
    private const val CHINA_TELECOM = "3"
    private const val CHINA_TIETONG = "4"
    private const val TAG = "DeviceInfo"
    const val os = "0"
    private const val FIRST_TAG = "0"
    private const val NO_FIRST_TAG = "1"
    private var VERSION_NAME: String = "1.0"
    private var VERSION_CODE: Int = 0

    private

    val osVersion: String
        get() = Build.VERSION.RELEASE

    val branding: String
        get() = Build.BRAND

    val manufacturer: String
        get() = Build.MANUFACTURER

    val device: String
        get() = Build.MODEL


    val local: String
        get() {
            val locale = Locale.getDefault()
            return locale.language + "_" + locale.country
        }


    @SuppressLint("HardwareIds", "MissingPermission", "PrivateApi")
    fun getDeviceId(context: Context): String? {

        if (!TextUtils.isEmpty(SPHelper.create(context).getString(SPHelper.TRACKING_DEVICE_ID))) {
            return SPHelper.create(context).getString(SPHelper.TRACKING_DEVICE_ID)

        }

        var result: String? = ""
        try {
            if (Util.checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
                var deviceId = ""
                if (context.getSystemService(
                        Context
                        .TELEPHONY_SERVICE) != null) {
                    val telephonyManager = context.getSystemService(
                        Context
                        .TELEPHONY_SERVICE) as TelephonyManager
                    deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        telephonyManager.imei
                    } else {
                        telephonyManager.deviceId
                    }
                }
                var backId = ""
                if (!TextUtils.isEmpty(deviceId) && !deviceId.contains("00000000000")) {
                    backId = deviceId
                    backId = backId.replace("0", "")
                }

                if (TextUtils.isEmpty(deviceId) || TextUtils.isEmpty(backId)) {
                    deviceId = try {
                        val c = Class.forName("android.os.SystemProperties")
                        val get = c.getMethod("com.zgw.company.base.core.ext.get", String::class.java, String::class.java)
                        get.invoke(c, "ro.serialno", "unknown") as String
                    } catch (t: Exception) {
                        ""
                    }

                }

                result = if (deviceId.isNotBlank()) {
                    deviceId
                } else {
                    Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                }
            } else {
                result = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            }
        } catch (ignored: Exception) {

        }

        SPHelper.create(context).putString(SPHelper.TRACKING_DEVICE_ID, result!!)
        return result
    }

    @SuppressLint("HardwareIds", "MissingPermission", "PrivateApi")
    fun getIMEI(context: Context): String? {
        return if (Util.checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
            if (context.getSystemService(Context.TELEPHONY_SERVICE) != null) {
                val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    telephonyManager.imei
                } else {
                    telephonyManager.deviceId
                }
            } else {
                null
            }
        } else {
            null
        }


    }

    @SuppressLint("ObsoleteSdkInt")
    fun getScreenSize(context: Context): String {
        var result = ""
        try {
            when {
                Build.VERSION.SDK_INT in 13..16 -> {
                    val windowManager = context.getSystemService(
                        Context
                            .WINDOW_SERVICE) as WindowManager
                    val display = windowManager.defaultDisplay
                    val size = Point()
                    display.getSize(size)

                    val screenWidth = size.x
                    val screenHeight = size.y
                    result = if (context.resources.configuration.orientation == Configuration
                            .ORIENTATION_PORTRAIT) {
                        screenWidth.toString() + "x" + screenHeight
                    } else {
                        screenHeight.toString() + "x" + screenWidth
                    }
                }
                Build.VERSION.SDK_INT >= 17 -> {
                    val windowManager = context.getSystemService(
                        Context
                            .WINDOW_SERVICE) as WindowManager
                    val display = windowManager.defaultDisplay
                    val size = Point()

                    display.getRealSize(size)

                    val screenWidth = size.x
                    val screenHeight = size.y
                    result = if (context.resources.configuration.orientation == Configuration
                            .ORIENTATION_PORTRAIT) {
                        screenWidth.toString() + "x" + screenHeight
                    } else {
                        screenHeight.toString() + "x" + screenWidth
                    }
                }
                else -> {
                    val dm2 = context.resources.displayMetrics

                    result = if (context.resources.configuration.orientation == Configuration
                            .ORIENTATION_PORTRAIT) {
                        dm2.widthPixels.toString() + "x" + dm2.heightPixels
                    } else {
                        dm2.heightPixels.toString() + "x" + dm2.widthPixels
                    }
                }
            }
        } catch (ignored: Exception) {

        }

        return result
    }

    fun getPackageName(context: Context): String {
        return context.packageName
    }

    fun getAppName(context: Context): String {
        var appName = ""
        try {
            appName = context.packageManager.getApplicationLabel(context.applicationInfo) as String
        } catch (ignored: Exception) {
        }

        return appName
    }

    @Synchronized
    fun getAppVersionName(context: Context): String {
        if (!TextUtils.isEmpty(VERSION_NAME)) {
            return VERSION_NAME
        }

        var info: PackageInfo? = null
        try {
            info = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (ignored: PackageManager.NameNotFoundException) {
        }

        if (info != null) {
            VERSION_NAME = info.versionName
            VERSION_CODE = info.versionCode
        }
        return VERSION_NAME
    }

    @Synchronized
    fun getAppVersionCode(context: Context): Int {
        if (VERSION_CODE != 0) {
            return VERSION_CODE
        }
        var info: PackageInfo? = null
        try {
            info = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (ignored: PackageManager.NameNotFoundException) {
        }

        if (info != null) {
            VERSION_CODE = info.versionCode
            VERSION_NAME = info.versionName
        }
        return VERSION_CODE
    }


    private fun carryByOperator(manager: TelephonyManager?): String {
        val operatorString = manager!!.simOperator

        return if ("46000" == operatorString || "46002" == operatorString || "46007" == operatorString) {
            CHINA_MOBILE
        } else if ("46001" == operatorString || "46006" == operatorString) {
            CHINA_UNICOM
        } else if ("46003" == operatorString || "46005" == operatorString) {
            CHINA_TELECOM
        } else if ("46020" == operatorString) {
            CHINA_TIETONG
        } else {
            CHINA_CARRIER_UNKNOWN
        }
    }

    private fun isTablet(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration
            .SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    fun getDeviceType(context: Context): Int {
        return if (isTablet(context)) {
            1
        } else {
            0
        }
    }

    private var MWChannel: String = ""

    fun isNetworkAvailable(): Boolean {
        val manager = MyApplication.instance.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false
        val networkinfo = manager.activeNetworkInfo
        return !(networkinfo == null || !networkinfo.isAvailable)
    }

    fun isAPPInstalled(packageName: String): Boolean {
        val pm = MyApplication.instance.packageManager
        val pinfo = pm.getInstalledPackages(0)
        pinfo.forEachIndexed { index, _ ->
            if (pinfo[index].packageName == packageName) {
                return true
            }
        }
        return false
    }
}