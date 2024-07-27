package com.ianlee.lazy.base.lib.view.appupdate.utils

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
object Constant {

    /**
     * 网络连接超时时间
     */
    const val HTTP_TIME_OUT = 30000

    /**
     * Logcat日志输出Tag
     */
    const val TAG = "AppUpdate."

    /**
     * 渠道通知id
     */
    const val DEFAULT_CHANNEL_ID = "appUpdate"

    /**
     * 渠道通知名称
     */
    const val DEFAULT_CHANNEL_NAME = "AppUpdate"

    /**
     * 新版本下载线程名称
     */
    const val THREAD_NAME = "app update thread"

    /**
     * apk文件后缀
     */
    const val APK_SUFFIX = ".apk"

    /**
     * 兼容Android N Uri 授权
     */
    lateinit var AUTHORITIES: String
}
