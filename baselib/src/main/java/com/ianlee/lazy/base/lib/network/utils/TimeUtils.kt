package com.ianlee.lazy.base.lib.network.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


/**
 * Created by Ian on 2021/8/25
 * Email: yixin0212@qq.com
 * Function :时间处理共同类
 */
object TimeUtils {
    /**
     * 默认的时间样式
     */
    val DEFAULT_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val DEFAULT2_DATE_FORMAT = SimpleDateFormat("yyyy年MM月dd日")
    val DEFAULT_DATE_FORMAT_3 = SimpleDateFormat("yyyy-MM-dd")
    val DEFAULT_DATE_FORMAT_4 = SimpleDateFormat("HH:mm")

    /**
     * 将时间戳转化成制定的格式
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    fun getTime(timeInMillis: Long, dateFormat: SimpleDateFormat): String {
        var timeInMillis = timeInMillis
        val t = 1000000000000L
        if (timeInMillis - t < 0) {
            timeInMillis = timeInMillis * 1000
        }
        return dateFormat.format(Date(timeInMillis))
    }

    /**
     * 获取当前时间戳
     *
     * @return 时间戳
     */
    val currentTimeInLong: Long
        get() = System.currentTimeMillis() / 1000

    /**
     * 请求刷新token 7天后时间戳
     */
    val timestampAfter7DaysLong: Long
        get() = getTimeAfterDays(currentTimeInLong, 7)

    /**
     * 定制格式时间转 long
     */
    fun getDateStringToLong(datestring: String): Long {
        val date = DEFAULT_DATE_FORMAT.parse(datestring)
        val timestamp = date.time
        return timestamp
    }

    fun getCurrentLastMinute(datestring: String): Int {
        try {
            var oldTime: Long = datestring.toLong()
            return compareTimeForMinute(currentTimeInLong * 1000, oldTime * 1000)

        } catch (e: Exception) {
            return -1
        }

    }

    fun getCurrentLastMinute(date: Long): Int {
        try {
            var oldTime: Long = date
            return compareTimeForMinute(currentTimeInLong * 1000, oldTime * 1000)

        } catch (e: Exception) {
            return -1
        }

    }

    /**
     * 获取制定格式的当前时间
     *
     * @param dateFormat  DEFAULT_DATE_FORMAT
     * @return 带格式的时间字符串
     **
     */
    fun getCurrentTimeInString(dateFormat: SimpleDateFormat = DEFAULT_DATE_FORMAT): String {
        return getTime(currentTimeInLong, dateFormat)
    }

    /**
     * 获取当月第一天
     *
     * @param m 0当月 -1上月 1下月 类推
     * @return 带格式的时间字符串 yyyy-MM-dd
     */
    fun getFirstDayOfMonth(m: Int): String {
        var str = ""
        val lastDate = Calendar.getInstance()
        lastDate.add(Calendar.MONTH, m)
        lastDate[Calendar.DATE] = 1 // 设为当前月的1号
        str = DEFAULT_DATE_FORMAT.format(lastDate.time)
        return str
    }

    /**
     * 获取当月最后一天
     *
     * @param m 0当月 -1上月 1下月 类推
     * @return 带格式的时间字符串 yyyy-MM-dd
     */
    fun getDefaultDay(m: Int): String {
        var str = ""
        val lastDate = Calendar.getInstance()
        lastDate.add(Calendar.MONTH, m)
        lastDate[Calendar.DATE] = 1 // 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1) // 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1) // 减去一天，变为当月最后一天
        str = DEFAULT_DATE_FORMAT.format(lastDate.time)
        return str
    }

    /**
     * 获取当天的指定的时间
     *
     * @param hour   小时24小时制
     * @param minute 分钟
     * @param second 秒
     * @return
     */
    fun getTimeOfDay(hour: Int, minute: Int, second: Int): Long {
        val lastDate = Calendar.getInstance()
        lastDate[Calendar.HOUR_OF_DAY] = hour
        lastDate[Calendar.MINUTE] = minute
        lastDate[Calendar.SECOND] = second
        return lastDate.timeInMillis
    }

    /**
     * 获取给定时间戳之后xx天的时间戳
     *
     * @param time 给定的时间戳
     * @param day  之后xx天
     * @return
     */
    fun getTimeAfterDays(time: Long, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        calendar.add(Calendar.DAY_OF_MONTH, day)
        return calendar.timeInMillis
    }

    /**
     * 获取给定时间戳之后xx分钟的时间戳
     *
     * @param time 给定的时间戳
     * @param day  之后xx天
     * @return
     */
    fun getTimeAfterMinutes(time: Long, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        calendar.add(Calendar.MINUTE, day)
        return calendar.timeInMillis
    }

    /**
     * 获取两个时间戳之间的间隔天数
     *
     * @param time1 开始时间，时间戳
     * @param time2 结束时间，时间戳
     * @return
     */
    fun compareTimeForDay(time1: Long, time2: Long): Int {
        val time = Math.abs(time1 - time2)
        return (time / 1000 / 60 / 60 / 24).toInt()
    }

    /**
     * 获取两个时间戳之间的间隔分钟数
     *
     * @param time1 开始时间，时间戳
     * @param time2 结束时间，时间戳
     * @return
     */
    fun compareTimeForMinute(time1: Long, time2: Long): Int {
        val time = Math.abs(time1 - time2)
        return (time / 1000 / 60).toInt()
    }

    /**
     * 获取某个时间是今天、明天、或者其他
     *
     * @param time
     * @return 0:今天  1:明天  2:其他
     */
    fun compareTimeForDay(time: Long): Int {
        val date = Date()
        date.hours = 24
        return if (time - date.time < 0) {
            0
        } else if (time - date.time - 24 * 60 * 60 * 1000 < 0) {
            1
        } else {
            2
        }
    }

    /**
     * 获取时间
     *
     * @param time
     * @return
     */
    fun getTime(time: Long): String {
        var time = time
        var timeStr = ""
        val day = time / 60 / 60 / 24
        time -= day * 24 * 60 * 60
        val hour = time / 60 / 60
        time -= hour * 60 * 60
        val minute = time / 60
        time -= minute * 60
        val second = time
        timeStr = if (day > 0) {
            day.toString() + "天 " + getFormat(hour) + ":" + getFormat(minute) + ":" + getFormat(
                second
            )
        } else if (hour > 0) {
            getFormat(hour) + ":" + getFormat(minute) + ":" + getFormat(second)
        } else if (minute > 0) {
            getFormat(minute) + ":" + getFormat(second)
        } else {
            getFormat(second)
        }
        return timeStr
    }

    fun getTimeStr(time: Long): String {
        var time = time
        var timeStr = ""
        val day = time / 60 / 60 / 24
        time -= day * 24 * 60 * 60
        val hour = time / 60 / 60
        time -= hour * 60 * 60
        val minute = time / 60
        time -= minute * 60
        val second = time
        timeStr = if (day > 0) {
            day.toString() + "天 " + getFormat(hour) + "小时" + getFormat(minute) + "分" + getFormat(
                second
            ) + "秒"
        } else if (hour > 0) {
            getFormat(hour) + "小时" + getFormat(minute) + "分" + getFormat(second) + "秒"
        } else if (minute > 0) {
            getFormat(minute) + "分" + getFormat(second) + "秒"
        } else {
            getFormat(second) + "秒"
        }
        return timeStr
    }

    /**
     * @param value
     * @return
     */
    fun getFormat(value: Long): String {
        return if (value < 10) "0$value" else "" + value
    }
}
