package com.ianlee.lazy.base.lib.utils

import android.os.Handler
import android.os.HandlerThread


/**
 * Created by Ian on 2022/3/31
 * Email: yixin0212@qq.com
 * Function :再按一次退出
 */
class Exit {
    var isExit = false
    private val task = Runnable { isExit = false }
    fun doExitInOneSecond() {
        isExit = true
        val thread = HandlerThread("doExit")
        thread.start()
        Handler(thread.looper).postDelayed(task, 2000)
    }

}
