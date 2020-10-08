package com.zgw.company.base.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.text.format.DateFormat
import com.zgw.company.base.core.MyApplication
import com.zgw.company.base.R
import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("SimpleDateFormat")
object DateUtils {
    const val DATETIME_PATTERN_1_1 = "yyyy-MM-dd HH:mm:ss.SSS"
    const val DATETIME_PATTERN_2_1 = "yyyy-MM-dd HH:mm"
    const val DATETIME_PATTERN_2_2 = "yyyy/MM/dd HH:mm"
    const val DATETIME_PATTERN_2_3 = "yyyy年MM月dd日 HH:mm"
    const val DATETIME_PATTERN_3_1 = "yyyy-MM-dd"
    const val DATETIME_PATTERN_3_2 = "yyyy/MM/dd"
    const val DATETIME_PATTERN_3_3 = "yyyy年MM月dd日"
    const val DATETIME_PATTERN_3_4 = "yyyyMMdd"
    const val DATETIME_PATTERN_4_1 = "yyyy"
    const val DATETIME_PATTERN_5_1 = "MM-dd"
    const val DATETIME_PATTERN_5_2 = "yyyy-MM"
    const val DATETIME_PATTERN_6_1 = "yyyy-MM-dd HH:mm:ss"
    const val DATETIME_PATTERN_6_2 = "HH:mm"
    const val DATETIME_PATTERN_6_3 = "HH时mm分"
    private const val INTERVAL_IN_MILLISECONDS = 30000L
    private val weekDayStrings = arrayOf("日", "一", "二", "三", "四", "五", "六")
    private val constellations = arrayOf(arrayOf("摩羯座", "水瓶座"), arrayOf("水瓶座", "双鱼座"), arrayOf("双鱼座", "白羊座"), arrayOf("白羊座", "金牛座"), arrayOf("金牛座", "双子座"), arrayOf("双子座", "巨蟹座"), arrayOf("巨蟹座", "狮子座"), arrayOf("狮子座", "处女座"), arrayOf("处女座", "天秤座"), arrayOf("天秤座", "天蝎座"), arrayOf("天蝎座", "射手座"), arrayOf("射手座", "摩羯座"))
    private val date = intArrayOf(20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22)

    fun getFormatTime(datetime: String?, pattern: String, pattern_new: String): String? {
        if (datetime != null && datetime.trim { it <= ' ' }.isNotEmpty()) {
            try {
                val date = SimpleDateFormat(pattern).parse(datetime)
                val dateFormat = SimpleDateFormat(pattern_new)
                return dateFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }
        return null
    }

    fun getFormatTime(date: Date, pattern: String): String {
        val sdf = SimpleDateFormat(pattern)
        return sdf.format(date)
    }

    fun getFormatTime(datetime: String, pattern: String): Date? {
        val sdf = SimpleDateFormat(pattern, Locale.CHINA)
        try {
            return sdf.parse(datetime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    fun getRefreshTime(lastSaveTime: Long, currentTime: Long): String {
        val time = currentTime - lastSaveTime
        if (time < 60 * (1000 * 60)) {
            return "刚刚刷新"
        } else if (time < 24 * (60 * (1000 * 60))) {
            return (time / (60 * (1000 * 60))).toString() + "小时前刷新"
        }
        return (time / (24 * (60 * (1000 * 60)))).toString() + "天前刷新"
    }

    fun getTime(format: String, data: Int): String {
        val mFormat = SimpleDateFormat(format)
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, data)
        return mFormat.format(calendar.time)
    }

    fun getTimeMonth(format: String, data: Int): String {
        val mFormat = SimpleDateFormat(format)
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.MONTH, data)
        return mFormat.format(calendar.time)
    }

    fun getAge(birthDay: Date): Int {
        val cal = Calendar.getInstance()
        if (cal.before(birthDay)) {
            return -1
        }
        val yearNow = cal.get(Calendar.YEAR)
        val monthNow = cal.get(Calendar.MONTH)
        val dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH)

        cal.time = birthDay
        val yearBirth = cal.get(Calendar.YEAR)
        val monthBirth = cal.get(Calendar.MONTH)
        val dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH)
        var age = yearNow - yearBirth
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--
            } else {
                age--
            }
        }
        return age
    }

    fun millis2String(millis: Long, pattern: String): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(millis))
    }

    fun getWeekDays(startDay: Calendar, endDay: Calendar): ArrayList<ArrayList<String>> {

        val weekDays = arrayListOf<ArrayList<String>>()

        val tempStart = startDay.clone() as Calendar
        while (endDay.compareTo(tempStart) == 1) {
            val arr = arrayListOf<String>()
            arr[0] = weekDayStrings[tempStart.get(Calendar.DAY_OF_WEEK) - 1]
            arr[1] = tempStart.get(Calendar.DAY_OF_MONTH).toString()
            weekDays.add(arr)
            tempStart.add(Calendar.DAY_OF_YEAR, 1)
        }
        return weekDays


    }

    val week: String
        get() {
            val cal = Calendar.getInstance()
            val i = cal.get(Calendar.DAY_OF_WEEK)
            return when (i) {
                1 -> "星期日"
                2 -> "星期一"
                3 -> "星期二"
                4 -> "星期三"
                5 -> "星期四"
                6 -> "星期五"
                7 -> "星期六"
                else -> ""
            }
        }

    fun getWeekOfDate(dt: Date, calendar: Calendar): String {
        val weekDays = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
        calendar.time = dt
        var w = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (w < 0)
            w = 0
        return weekDays[w]
    }

    fun isDayOrNight(context: Context): Boolean {
        val hourFormat = DateFormat.is24HourFormat(context)
        if (hourFormat) {
            val df = SimpleDateFormat("HH:mm:ss")//设置日期格式
            val time = df.format(Date())
            val hour = Integer.parseInt(time.substring(0, 2))
            return hour in 6..17
        } else {
            val mCalendar = Calendar.getInstance()
            return if (mCalendar.get(Calendar.AM_PM) == Calendar.AM) {
                mCalendar.get(Calendar.HOUR) >= 6
            } else {
                mCalendar.get(Calendar.HOUR) < 6

            }
        }
    }

    val currentTime: String
        get() {
            val formatter = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
            val curDate = Date(System.currentTimeMillis())
            return formatter.format(curDate)
        }

    fun getCurrentTime(format: String): String {
        val formatter = SimpleDateFormat(format)
        val curDate = Date(System.currentTimeMillis())
        return formatter.format(curDate)
    }

    fun getTimestamp(user_time: String): String? {
        var reTime: String? = null
        val sdf = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
        val d: Date
        try {
            d = sdf.parse(user_time)
            val l = d.time
            val str = l.toString()
            reTime = str.substring(0, 10)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return reTime
    }

    fun getTime(user_time: String): String? {
        var reTime: String? = null
        val sdf = SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒")
        val d: Date
        try {
            d = sdf.parse(user_time)
            val l = d.time
            val str = l.toString()
            reTime = str.substring(0, 10)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return reTime
    }

    fun getLongTime(time: String): Long {
        var l: Long = 0
        val sdf = SimpleDateFormat(DATETIME_PATTERN_3_3)
        val d: Date
        try {
            d = sdf.parse(time)
            l = d.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return l
    }

    fun getStringTime(ms: Long): String {
        val formatter = SimpleDateFormat(DATETIME_PATTERN_3_1)
        val curDate = Date(ms)
        return formatter.format(curDate)
    }

    fun getStringTime(ms: Long, format: String): String {
        val formatter = SimpleDateFormat(format)
        val curDate = Date(ms)
        return formatter.format(curDate)
    }

    fun formatDataToTodayString(input: String, format: String = DATETIME_PATTERN_3_1): String {
        val calendar = Calendar.getInstance()
        calendar.time = Date(System.currentTimeMillis())

        val formatter = SimpleDateFormat(format, Locale.getDefault())
        val inputCalendar = Calendar.getInstance()
        inputCalendar.time = formatter.parse(input)

        return if (calendar.get(Calendar.YEAR) == inputCalendar.get(Calendar.YEAR)) {
            val day = calendar.get(Calendar.DAY_OF_YEAR)
            val inputDay = inputCalendar.get(Calendar.DAY_OF_YEAR)
            if (day == inputDay) {
                MyApplication.instance.getString(R.string.today)
            } else if (inputDay > day - 1 && inputDay < day) {
                MyApplication.instance.getString(R.string.yesterday)
            } else if (inputDay > day + 1 && inputDay < day + 2) {
                MyApplication.instance.getString(R.string.tomorrow)
            } else {
                input
            }
        } else {
            input
        }
    }

    fun parseLongTime(time: String): Long {
        var l: Long = 0
        val sdf = SimpleDateFormat(DATETIME_PATTERN_3_3)
        val d: Date
        try {
            d = sdf.parse(time)
            l = d.time

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return l
    }

    val simpleDateFormat: SimpleDateFormat
        get() = SimpleDateFormat(DATETIME_PATTERN_3_1, Locale.CHINA)

    fun parseDateTime(time: String): Date {
        var l: Long = 0
        if (!TextUtils.isEmpty(time)) {
            val sdf = SimpleDateFormat(DATETIME_PATTERN_3_1)
            val d: Date
            try {
                d = sdf.parse(time)
                l = d.time

            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }
        return Date(l)
    }

    fun parseDateTime(time: String, pattern: String): Date {
        var l: Long = 0
        if (!TextUtils.isEmpty(time)) {
            val sdf = SimpleDateFormat(pattern)
            val d: Date
            try {
                d = sdf.parse(time)
                l = d.time

            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }
        return Date(l)
    }

    fun parseStrTime(ms: Long): String {
        val formatter = SimpleDateFormat(DATETIME_PATTERN_6_1)
        val curDate = Date(ms)//获取当前时间
        return formatter.format(curDate)
    }

    fun getMonthDays(year: Int, month: Int): Int {
        var year = year
        var month = month
        if (month > 12) {
            month = 1
            year += 1
        } else if (month < 1) {
            month = 12
            year -= 1
        }
        val arr = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var days = 0

        if (isLeapYear(year)) {
            arr[1] = 29
        }

        try {
            days = arr[month - 1]
        } catch (e: Exception) {
            e.stackTrace
        }

        return days
    }


    fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
    }


    fun getMonthDaysArray(year: Int, month: Int): List<String> {
        val dayList = ArrayList<String>()
        val days = getMonthDays(year, month)
        for (i in 1..days) {
            dayList.add(i.toString() + "")
        }
        return dayList
    }


    val year: Int
        get() = Calendar.getInstance().get(Calendar.YEAR)


    val month: Int
        get() = Calendar.getInstance().get(Calendar.MONTH) + 1


    val currentMonthDay: Int
        get() = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)


    val hour: Int
        get() = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)


    val minute: Int
        get() = Calendar.getInstance().get(Calendar.MINUTE)


    val second: Int
        get() = Calendar.getInstance().get(Calendar.SECOND)


    fun getDaySpan(time: Long): Long {
        val tiemzone = TimeZone.getDefault().rawOffset
        return (System.currentTimeMillis() + tiemzone) / 86400000 - (time + tiemzone) / 86400000
    }

    fun isToday(time: Long): Boolean {
        return getDaySpan(time) == 0L
    }

    fun isYestoday(time: Long): Boolean {
        return getDaySpan(time) == 1L
    }

    @JvmOverloads
    fun getDate(format: String = "yyyy-MM-dd HH-mm-ss"): String {
        @SuppressLint("SimpleDateFormat") val sDateFormat = SimpleDateFormat(format)
        return sDateFormat.format(Date())
    }


    fun parseYyyyMM(date: String): Date {
        val l: Long = 0
        @SuppressLint("SimpleDateFormat")
        val sdf = SimpleDateFormat(DATETIME_PATTERN_5_2)
        try {
            return sdf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return Date(l)
    }


    fun getYyyyMM(date: Date): String {
        @SuppressLint("SimpleDateFormat")
        val sdf = SimpleDateFormat(DATETIME_PATTERN_5_2)
        return sdf.format(date)
    }

    fun getDate(date: Date, format: String): String {
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat(format)
        return sdf.format(date)
    }

    fun getHzMonth(month: String): String {
        val map = HashMap<String, String>()
        map["01"] = "1月"
        map["02"] = "2月"
        map["03"] = "3月"
        map["04"] = "4月"
        map["05"] = "5月"
        map["06"] = "6月"
        map["07"] = "7月"
        map["08"] = "8月"
        map["09"] = "9月"
        map["10"] = "10月"
        map["11"] = "11月"
        map["12"] = "12月"
        val seting = map.entries
        for ((key, value) in seting) {
            if (month == key) {
                return value
            }
        }
        return ""
    }

    fun getConstellations(birthday: String): String {
        val data = birthday.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val day = date[Integer.parseInt(data[1]) - 1]
        val cl1 = constellations[Integer.parseInt(data[1]) - 1]
        return if (Integer.parseInt(data[2]) >= day) {
            cl1[1]
        } else {
            cl1[0]
        }
    }

    fun getTimestampString(var0: Date): String {
        var var1: String? = null
        val var2 = Locale.getDefault().language
        val var3 = var2.startsWith("en")
        val var4 = var0.time
        if (isSameDay(var4)) {
            if (var3) {
                var1 = "aa hh:mm"
            } else {
                var1 = "hh:mm aa"
            }
        } else if (isYesterday(var4)) {
            if (!var3) {
                return "Yesterday " + SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(var0)
            }

            var1 = "昨天aa hh:mm"
        } else if (var3) {
            var1 = "M月d日aa hh:mm"
        } else {
            var1 = "MMM dd hh:mm aa"
        }

        return if (var3) SimpleDateFormat(var1, Locale.CHINESE).format(var0) else SimpleDateFormat(var1, Locale.ENGLISH).format(var0)
    }

    fun getTimestamp(var0: Date): String {
        val var4 = var0.time
        return when {
            isSameDay(var4) -> "今天 " + SimpleDateFormat("HH:mm:ss", Locale.CHINESE).format(var0)
            isYesterday(var4) -> "昨天 " + SimpleDateFormat("HH:mm:ss", Locale.CHINESE).format(var0)
            else -> showTime(var0)
        }
    }

    fun isCloseEnough(var0: Long, var2: Long): Boolean {
        var var4 = var0 - var2
        if (var4 < 0L) {
            var4 = -var4
        }

        return var4 < 30000L
    }

    fun showTime(ctime: Date?): String {
        var r = ""
        if (ctime == null) return r
        val nowtimelong = System.currentTimeMillis()
        val ctimelong = ctime.time
        val result = Math.abs(nowtimelong - ctimelong)
        r = when {
            result < 60000 -> { // 一分钟内
                val seconds = result / 1000
                "刚刚"
            }
            result in 60000..3599999 -> {  // 一小时内
                val seconds = result / 60000
                seconds.toString() + "分钟前"
            }
            result in 3600000..86399999 -> { // 一天内
                val seconds = result / 3600000
                seconds.toString() + "小时前"
            }
            else -> // 日期格式
                SimpleDateFormat(DATETIME_PATTERN_6_1, Locale.CHINESE).format(ctime)
        }
        return r
    }

    private fun isSameDay(var0: Long): Boolean {
        val var2 = todayStartAndEndTime
        return var0 > var2.startTime && var0 < var2.endTime
    }

    private fun isYesterday(var0: Long): Boolean {
        val var2 = yesterdayStartAndEndTime
        return var0 > var2.startTime && var0 < var2.endTime
    }

    @SuppressLint("SimpleDateFormat")
    fun stringToDate(var0: String, var1: String): Date? {
        val var2 = SimpleDateFormat(var1)
        var var3: Date? = null

        try {
            var3 = var2.parse(var0)
        } catch (var5: ParseException) {
            var5.printStackTrace()
        }

        return var3
    }

    @SuppressLint("DefaultLocale")
    fun toTime(var0: Int): String {
        var var0 = var0
        var0 /= 1000

        var hour = 0
        var minute = 0
        val second = var0 % 60
        val var1 = var0 / 60
        if (var1 >= 60) {
            hour = var1 / 60
            minute = var1 % 60
        } else if (var1 >= 1) {
            minute = var1 % 60
        }
        return String.format("%02d:%02d:%02d", hour, minute, second)
    }

    @SuppressLint("DefaultLocale")
    fun toTimeBySecond(var0: Int): String {
        var hour = 0
        var minute = 0
        val second = var0 % 60
        val var1 = var0 / 60
        if (var1 >= 60) {
            hour = var1 / 60
            minute = var1 % 60
        } else if (var1 >= 1) {
            minute = var1 % 60
        }
        return String.format("%02d:%02d:%02d", hour, minute, second)
    }

    val yesterdayStartAndEndTime: TimeInfo
        get() {
            val var0 = Calendar.getInstance()
            var0.add(Calendar.DAY_OF_MONTH, -1)
            var0.set(Calendar.HOUR_OF_DAY, 0)
            var0.set(Calendar.MINUTE, 0)
            var0.set(Calendar.SECOND, 0)
            var0.set(Calendar.MILLISECOND, 0)
            val var1 = var0.time
            val var2 = var1.time
            val var4 = Calendar.getInstance()
            var4.add(Calendar.DAY_OF_MONTH, -1)
            var4.set(Calendar.HOUR_OF_DAY, 23)
            var4.set(Calendar.MINUTE, 59)
            var4.set(Calendar.SECOND, 59)
            var4.set(Calendar.MILLISECOND, 999)
            val var5 = var4.time
            val var6 = var5.time
            val var8 = TimeInfo()
            var8.startTime = var2
            var8.endTime = var6
            return var8
        }

    val todayStartAndEndTime: TimeInfo
        get() {
            val var0 = Calendar.getInstance()
            var0.set(Calendar.HOUR_OF_DAY, 0)
            var0.set(Calendar.MINUTE, 0)
            var0.set(Calendar.SECOND, 0)
            var0.set(Calendar.MILLISECOND, 0)
            val var1 = var0.time
            val var2 = var1.time
            val var4 = Calendar.getInstance()
            var4.set(Calendar.HOUR_OF_DAY, 23)
            var4.set(Calendar.MINUTE, 59)
            var4.set(Calendar.SECOND, 59)
            var4.set(Calendar.MILLISECOND, 999)
            val var5 = var4.time
            val var6 = var5.time
            val var8 = TimeInfo()
            var8.startTime = var2
            var8.endTime = var6
            return var8
        }

    val beforeYesterdayStartAndEndTime: TimeInfo
        get() {
            val var0 = Calendar.getInstance()
            var0.add(Calendar.DAY_OF_MONTH, -2)
            var0.set(Calendar.HOUR_OF_DAY, 0)
            var0.set(Calendar.MINUTE, 0)
            var0.set(Calendar.SECOND, 0)
            var0.set(Calendar.MILLISECOND, 0)
            val var1 = var0.time
            val var2 = var1.time
            val var4 = Calendar.getInstance()
            var4.add(Calendar.DAY_OF_MONTH, -2)
            var4.set(Calendar.HOUR_OF_DAY, 23)
            var4.set(Calendar.MINUTE, 59)
            var4.set(Calendar.SECOND, 59)
            var4.set(Calendar.MILLISECOND, 999)
            val var5 = var4.time
            val var6 = var5.time
            val var8 = TimeInfo()
            var8.startTime = var2
            var8.endTime = var6
            return var8
        }

    val currentMonthStartAndEndTime: TimeInfo
        get() {
            val var0 = Calendar.getInstance()
            var0.set(Calendar.DAY_OF_MONTH, 1)
            var0.set(Calendar.HOUR_OF_DAY, 0)
            var0.set(Calendar.MINUTE, 0)
            var0.set(Calendar.SECOND, 0)
            var0.set(Calendar.MILLISECOND, 0)
            val var1 = var0.time
            val var2 = var1.time
            val var4 = Calendar.getInstance()
            val var5 = var4.time
            val var6 = var5.time
            val var8 = TimeInfo()
            var8.startTime = var2
            var8.endTime = var6
            return var8
        }

    val lastMonthStartAndEndTime: TimeInfo
        get() {
            val var0 = Calendar.getInstance()
            var0.add(Calendar.MONTH, -1)
            var0.set(Calendar.DAY_OF_MONTH, 1)
            var0.set(Calendar.HOUR_OF_DAY, 0)
            var0.set(Calendar.MINUTE, 0)
            var0.set(Calendar.SECOND, 0)
            var0.set(Calendar.MILLISECOND, 0)
            val var1 = var0.time
            val var2 = var1.time
            val var4 = Calendar.getInstance()
            var4.add(Calendar.MONTH, -1)
            var4.set(Calendar.DAY_OF_MONTH, 1)
            var4.set(Calendar.HOUR_OF_DAY, 23)
            var4.set(Calendar.MINUTE, 59)
            var4.set(Calendar.SECOND, 59)
            var4.set(Calendar.MILLISECOND, 999)
            var4.roll(Calendar.DAY_OF_MONTH, -1)
            val var5 = var4.time
            val var6 = var5.time
            val var8 = TimeInfo()
            var8.startTime = var2
            var8.endTime = var6
            return var8
        }

    val timestampStr: String
        get() = java.lang.Long.toString(System.currentTimeMillis())

    fun timestampToString(timestamp: Long): String {
        val ts = Timestamp(timestamp)
        @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat(DATETIME_PATTERN_6_1)

        //方法一:优势在于可以灵活的设置字符串的形式。
        return formatter.format(ts)
    }
}


class TimeInfo {
    var startTime: Long = 0
    var endTime: Long = 0
}