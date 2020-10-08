package com.zgw.company.base.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import com.zgw.company.base.core.MyApplication
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.regex.Pattern
import kotlin.experimental.and

object Util {

    private val COLOR_PATTEN = Pattern.compile("([0-9A-Fa-f]{8})|([0-9A-Fa-f]{6})")
    private val PHONE_NUM_PATTERN = Pattern.compile("^((\\+?86)?\\s?-?)1[0-9]{10}")
    private val PHONE_NUM_FORMAT_PATTERN = Pattern.compile("^((\\+?86)?)\\s?-?")
    private val EMAIL_PATTERN = Pattern.compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]+$", Pattern.CASE_INSENSITIVE)

    @JvmStatic
    fun getCurrentTimeSecond(): Long {
        return System.currentTimeMillis() / 1000
    }

    @JvmStatic
    fun getCurrentTimeSecondStr(): String {
        return (System.currentTimeMillis() / 1000).toString()
    }


    private fun filterColor(colorString: String): String {
        val m = COLOR_PATTEN.matcher(colorString)

        while (m.find()) {
            return m.group()
        }
        return ""
    }

    @JvmStatic
    fun parseColor(colorString: String): Int {
        val filterColor = filterColor(colorString)
        val parsedColorString: String
        parsedColorString = if (!"#".equals(filterColor[0].toString(), ignoreCase = true)) {
            "#$filterColor"
        } else {
            filterColor
        }
        return Color.parseColor(parsedColorString)
    }

    @JvmStatic
    fun isColor(colorString: String): Boolean {
        val m = COLOR_PATTEN.matcher(colorString)

        while (m.find()) {
            return true
        }
        return false
    }

    @JvmStatic
    fun createRoundCornerShapeDrawable(radius: Float, borderColor: Int): ShapeDrawable {
        val outerR = floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
        val roundRectShape = RoundRectShape(outerR, null, null)
        //RectShape rectShape = new RectShape();
        val shapeDrawable = ShapeDrawable(roundRectShape)
        shapeDrawable.paint.color = borderColor
        shapeDrawable.paint.style = Paint.Style.FILL
        return shapeDrawable
    }

    @JvmStatic
    fun bitmap2Base64String(bm: Bitmap): String {
        val bos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 0, bos)// 参数100表示不压缩
        val bytes = bos.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun dip2px(dpValue: Float, context: Context = MyApplication.instance): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dip(pxValue: Float, context: Context = MyApplication.instance): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun dip2pxFloat(dpValue: Float, context: Context = MyApplication.instance): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.resources.displayMetrics)
    }

    fun sp2pxFloat(spValue: Float, context: Context = MyApplication.instance): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.resources.displayMetrics)
    }

    @JvmStatic
    fun checkPermissions(context: Context?, permission: String): Boolean {

        val localPackageManager = context!!.applicationContext.packageManager
        return localPackageManager.checkPermission(permission, context.applicationContext.packageName) == PackageManager.PERMISSION_GRANTED
    }

    @JvmStatic
    fun addBackSplash(input: String): String {
        val result = StringBuilder()
        val chars = input.toCharArray()
        var i = 0
        while (i < chars.size) {
            result.append(chars[i])
            if (i > 0 && i < chars.size && "\\" == chars[i].toString() && "\\" != chars[i - 1].toString() && "\\" != chars[i + 1].toString()) {
                result.append("\\").append("\\")
            }
            i++
        }

        Log.d("addBackSplash:", result.toString())
        return result.toString()
    }

    @JvmStatic
    fun md5with16Byte(str: String): String {
        try {
            val localMessageDigest = MessageDigest.getInstance("MD5")
            localMessageDigest.update(str.toByteArray())
            val arrayOfByte = localMessageDigest.digest()
            val stringBuffer = StringBuilder()
            for (anArrayOfByte in arrayOfByte) {
                val j = 0xFF and anArrayOfByte.toInt()
                if (j < 16) {
                    stringBuffer.append("0")
                }
                stringBuffer.append(Integer.toHexString(j))
            }
            return stringBuffer.toString().toLowerCase().substring(8, 24)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }

    @JvmStatic
    fun md5(str: String): String {
        var reStr = ""
        try {
            val md5 = MessageDigest.getInstance("MD5")
            val bytes = md5.digest(str.toByteArray())
            val stringBuffer = StringBuilder()
            for (b in bytes) {
                val bt = b and 0xff.toByte()
                if (bt < 16) {
                    stringBuffer.append(0)
                }
                stringBuffer.append(Integer.toHexString(bt.toInt()))
            }
            reStr = stringBuffer.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return reStr
    }

    @JvmStatic
    fun formatPhoneNum(phoneNum: String): String {

        val m1 = PHONE_NUM_PATTERN.matcher(phoneNum)
        return if (m1.matches()) {
            val m2 = PHONE_NUM_FORMAT_PATTERN.matcher(phoneNum)
            val sb = StringBuffer()
            while (m2.find()) {
                m2.appendReplacement(sb, "")
            }
            m2.appendTail(sb)
            sb.toString()
        } else {
            ""
        }
    }

    @JvmStatic
    fun checkPhoneNum(phoneNum: String): Boolean {

        val m1 = PHONE_NUM_PATTERN.matcher(phoneNum)
        return m1.matches()
    }

    @JvmStatic
    fun checkEmail(email: String): Boolean {
        val matcher = EMAIL_PATTERN.matcher(email)
        return matcher.matches()
    }

    const val regexCIp = "^192\\.168\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)$"
    const val regexAIp = "^10\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)$"
    const val regexBIp = "^172\\.(1[6-9]|2\\d|3[0-1])\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)$"

    private fun isIPv4RealAddress(address: String): Boolean {
        val p = Pattern.compile("($regexAIp)|($regexBIp)|($regexCIp)")
        val m = p.matcher(address)
        return m.matches()
    }

    @JvmStatic
    fun getHostIp(): String? {
        var networkInterfaces: Enumeration<NetworkInterface>? = null
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces()
        } catch (e: SocketException) {
            e.printStackTrace()
        }

        var address: InetAddress
        while (networkInterfaces!!.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()
            val inetAddresses = networkInterface.inetAddresses
            while (inetAddresses.hasMoreElements()) {
                address = inetAddresses.nextElement()
                val hostAddress = address.hostAddress
                if (!address.isLoopbackAddress && isIPv4RealAddress(hostAddress)) {
                    return hostAddress
                }

            }
        }
        return null
    }

    fun mul(v1: String?, v2: String?): Double {
        val b1 = BigDecimal(if (v1.isNullOrEmpty()) "0" else v1)
        val b2 = BigDecimal(if (v2.isNullOrEmpty()) "0" else v2)
        return b1.multiply(b2).toDouble()
    }

    fun isChineseOrPunctuation(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub === Character.UnicodeBlock.VERTICAL_FORMS)
    }

    fun isChinese(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
    }

    fun isChinesePunctuation(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub === Character.UnicodeBlock.VERTICAL_FORMS
    }

    fun getStatusBarHeight(context: Context = MyApplication.instance): Int {

        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")

        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }

        return result

    }

    fun isLetterOrDigit(str: String): Boolean {
        var isLetterOrDigit = false
        for (element in str) {
            if (Character.isLetterOrDigit(element)) {
                isLetterOrDigit = true
            }
        }
        val regex = "^[a-zA-Z0-9]+$"
        return isLetterOrDigit && str.matches(regex.toRegex())
    }

    fun isLetterDigit(str: String): Boolean {
        var isDigit = false
        var isLetter = false
        for (i in str.indices) {
            if (Character.isDigit(str[i])) {
                isDigit = true
            } else if (Character.isLetter(str[i])) {
                isLetter = true
            }
        }
        val regex = "^[a-zA-Z0-9]+$"
        return isDigit && isLetter && str.matches(regex.toRegex())
    }

    fun isContainAll(str: String): Boolean {
        var isDigit = false
        var isLowerCase = false
        var isUpperCase = false
        for (i in str.indices) {
            when {
                Character.isDigit(str[i]) -> {
                    isDigit = true
                }
                Character.isLowerCase(str[i]) -> {
                    isLowerCase = true
                }
                Character.isUpperCase(str[i]) -> {
                    isUpperCase = true
                }
            }
        }
        val regex = "^[a-zA-Z0-9]+$"
        return isDigit && isLowerCase && isUpperCase && str.matches(regex.toRegex())
    }

    fun isLetter(str: String): Boolean {
        val regex = "^[a-zA-Z]+$"
        return str.matches(regex.toRegex())
    }

    fun isOneLetter(str: String): Boolean {
        val regex = "^[a-zA-Z]$"
        return str.matches(regex.toRegex())
    }
}