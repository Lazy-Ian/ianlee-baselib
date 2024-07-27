package com.ianlee.lazy.base.lib.utils

import android.util.Log

/**
 * Created by Ian on 2024/4/17
 * Email: yixin0212@qq.com
 * Function : 点击限制
 */
object ClickUtils {
    private const val MIN_CLICK_DELAY_TIME = 200 // 两次点击间隔不能少于1000m
    private var lastClickTime: Long = 0
    val isFastClick: Boolean
        get() {
            var flag = true
            val curClickTime = System.currentTimeMillis()
            if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME) {
                flag = false
            }
            Log.e("22222", "间隔时间" + (curClickTime - lastClickTime))
            lastClickTime = curClickTime
            return flag
        }
}
