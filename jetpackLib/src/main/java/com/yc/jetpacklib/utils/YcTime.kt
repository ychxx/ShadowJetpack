package com.yc.jetpacklib.utils

import android.text.TextUtils
import android.util.Log
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Creator: yc
 * Date: 2021/2/3 13:58
 * UseDes:
 */
object YcTime {
    const val FORMAT_TIME = "yyyy-MM-dd"
    const val FORMAT_TIME_YEAR = "yyyy"
    const val FORMAT_TIME_MONTH = "yyyy-MM"
    const val FORMAT_TIME_SECOND = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_TIME_MONTH_DAY = "MM-dd"
    const val FORMAT_TIME_SECOND_WEEK = "yyyy-MM-dd E HH:mm:ss" //E代表星期，会根据时区显示中文或英文
    const val FORMAT_TIME_HOUR_MINUTE = "HH:mm"//只显示时分


    @JvmStatic
    fun getSimpleDateFormat(formatTime: String = FORMAT_TIME) = SimpleDateFormat(formatTime, Locale.getDefault())

    /**
     * 获取系统当前时间
     */
    @JvmStatic
    fun getCurrentTime(formatTime: String = FORMAT_TIME): String {
        return calendarToString(Calendar.getInstance(), formatTime)
    }

    /**
     * 将Date类型格式化成String yyyy-MM-dd
     *
     * @param date 时间
     * @return
     */
    @JvmStatic
    fun dateToString(date: Date?, formatTime: String = FORMAT_TIME): String {
        return if (date == null) {
            ""
        } else {
            getSimpleDateFormat(formatTime).format(date)
        }
    }


    @JvmStatic
    fun dateToLong(date: Date): Long {
        val str = dateToString(date)
        val date = stringToDate(str)
        return date?.time!!
    }


    /**
     * 将Date ——>星期E
     *
     * @param date 时间
     * @return
     */
    @JvmStatic
    fun dateToOnlyWeekStr(date: Date?): String {
        return if (date == null) {
            ""
        } else {
            String.format("%tA", date)
        }
    }


    /**
     * 将Calendar类型格式化成String yyyy-MM-dd
     *
     * @param date 时间
     * @return
     */
    @JvmStatic
    fun calendarToString(date: Calendar?, formatTime: String = FORMAT_TIME): String {
        return if (date == null) {
            ""
        } else {
            getSimpleDateFormat(formatTime).format(date.time)
        }
    }

    @JvmStatic
    fun stringToDate(formatTime: String): Date? {
        val sdf = SimpleDateFormat(FORMAT_TIME, Locale.getDefault())
        var timeDate: Date? = null
        try {
            timeDate = sdf.parse(formatTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return timeDate
    }

    @JvmStatic
    fun stringToString(time: String, formatTimeInput: String, formatTimeOut: String): String {
        try {
            val df = getSimpleDateFormat(formatTimeInput)
            val date = df.parse(time)
            val calendar = Calendar.getInstance()
            calendar.time = date
            return getSimpleDateFormat(formatTimeOut).format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    @JvmStatic
    fun stringToCalendar(time: String, formatTime: String = FORMAT_TIME): Calendar {
        if (TextUtils.isEmpty(time)) {
            Log.e("FormatUtils", "String转Calender失败，String为空")
            return Calendar.getInstance()
        }
        return try {
            val df = getSimpleDateFormat(formatTime)
            val date = df.parse(time)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar
        } catch (e: ParseException) {
            Log.e("FormatUtils", "String转成Calender失败，" + time + "的格式不是" + FORMAT_TIME)
            Calendar.getInstance()
        } catch (e: Exception) {
            e.printStackTrace()
            Calendar.getInstance()
        }
    }

    /**
     * 将Date类型格式化成Calendar
     *
     * @param date 时间
     * @return
     */
    @JvmStatic
    fun dataToCalendar(date: Date?): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    }

    @JvmStatic
    fun getMonthFirstDay(calendar: Calendar): String {
        //获取某月最小天数
        val firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        //设置日历中月份的最小天数
        calendar[Calendar.DAY_OF_MONTH] = firstDay
        return calendarToString(calendar)
    }

    @JvmStatic
    fun getMonthLastDay(calendar: Calendar): String {
        //获取某月最小天数
        val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        //设置日历中月份的最小天数
        calendar[Calendar.DAY_OF_MONTH] = lastDay
        return calendarToString(calendar)
    }


    //计算两个时间的间隔天数（格式： YYYY-MM-DD HH:MM:SS），时间差值
    fun getDateDifference(date1: String?, date2: String?): String {
        if (TextUtils.isEmpty(date1) && TextUtils.isEmpty(date2)) return "0"
        // 转换为标准时间

        val df: DateFormat = getSimpleDateFormat()
        var diff = 0L //差值
        try {
            val d1 = df.parse(date1!!)?.time
            val d2 = df.parse(date2!!)?.time
            diff = (d2!! - d1!!) / (1000 * 60 * 60 * 24)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return diff.toString()
    }

    //计算两个时间的间隔天数（格式： YYYY-MM-DD HH:MM:SS），时间差值秒
    fun getDateDifferenceToMinu(date1: String, date2: String): Int {
        if (TextUtils.isEmpty(date1) && TextUtils.isEmpty(date2)) return 0
        // 转换为标准时间

        val df: DateFormat = getSimpleDateFormat(FORMAT_TIME_SECOND)
        var diff = 0L //差值
        try {
            val d1 = df.parse(date1)?.time
            val d2 = df.parse(date2)?.time
            diff = (d2!! - d1!!) / 1000
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return diff.toInt()

    }

    //long类型的时间转换成String类型
    fun longToString(time: Long, format: String): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT+00:00")
        return simpleDateFormat.format(Date(time))
    }

    //获取当前时间前几天
    fun getDateInterval(interval: Int): String {
        val format = getSimpleDateFormat()
        val c = Calendar.getInstance()
        c.time = Date()
        c.add(Calendar.DATE, -interval)
        val start = c.time
        return format.format(start)
    }


    /**
     * 秒变小时 格式：hh:mm:ss
     */
    fun secondToHour(mTimer: Int): String {
        val hour: Int = mTimer / 3600 % 24
        val minute: Int = mTimer % 3600 / 60
        return java.lang.String.format("%02d:%02d:%02d", hour, minute, mTimer % 60)
    }

    /**
     * 秒变分钟 格式：mm:ss
     */
    fun secondToMinute(mTimer: Int): String {
        val minute: Int = mTimer % 3600 / 60
        return java.lang.String.format("%02d:%02d", minute, mTimer % 60)
    }

    /**
     * 时间格式转换
     * @param time String?          输入的时间
     * @param defaultOut String     默认值
     * @param formatInput String    输入的格式
     * @param formatOut String      输出的格式
     */
    @JvmStatic
    fun stringToString(
        time: String?,
        defaultOut: String = "",
        formatInput: String = FORMAT_TIME,
        formatOut: String = FORMAT_TIME_MONTH_DAY
    ): String {
        if (time == null) {
            return defaultOut
        }
        val sdf = SimpleDateFormat(formatInput, Locale.getDefault())
        return try {
            val timeDate = sdf.parse(time)
            SimpleDateFormat(formatOut, Locale.getDefault()).format(timeDate)
        } catch (e: ParseException) {
            e.printStackTrace()
            defaultOut
        }
    }
}