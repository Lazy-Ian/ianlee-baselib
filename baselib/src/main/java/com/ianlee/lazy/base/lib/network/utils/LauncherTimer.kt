package com.ianlee.lazy.base.lib.network.utils

import android.util.Log

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
object LauncherTimer {
    var startTime: Long = 0L

    fun logStart() {
        startTime = System.currentTimeMillis()
    }

    fun logEnd(tag: String) {
        Log.d("LauncherTimer", tag + "launcher time = " + (System.currentTimeMillis() - startTime))
    }

    fun cleanTime(){
        startTime = 0L
    }
}