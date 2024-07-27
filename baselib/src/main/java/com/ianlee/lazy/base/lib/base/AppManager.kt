package com.ianlee.lazy.base.lib.base

import android.app.Activity
import java.util.*


/**
 * Created by Ian on 2021/5/18
 * Email: yixin0212@qq.com
 * Function :
 */
class AppManager private constructor() {
    private object SingleHolder {
        val instance = com.ianlee.lazy.base.lib.base.AppManager()
    }

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity?) {
        if (com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack == null) {
            com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack = Stack()
        }
        com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack!!.add(activity)
    }

    /**
     * 移除指定的Activity
     */
    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack!!.remove(activity)
        }
    }

    /**
     * 是否有activity
     */
    val isActivity: Boolean
        get() = if (com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack != null) {
            !com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack!!.isEmpty()
        } else false

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        return com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack!!.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack!!.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack!!) {
            if (activity!!.javaClass == cls) {
                finishActivity(activity)
                break
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack!!.size
        while (i < size) {
            if (null != com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack!![i]) {
                finishActivity(com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack!![i])
            }
            i++
        }
        com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack!!.clear()
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    fun getActivity(cls: Class<*>): Activity? {
        if (com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack != null) {
            for (activity in com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack!!) {
                if (activity!!.javaClass == cls) {
                    return activity
                }
            }
        }
        return null
    }

    /**
     * 退出应用程序
     */
    fun AppExit() {
        try {
            finishAllActivity()
        } catch (e: Exception) {
            com.ianlee.lazy.base.lib.base.AppManager.Companion.activityStack!!.clear()
            e.printStackTrace()
        }
    }

    companion object {
        private var activityStack: Stack<Activity?>? = null
        val instance: com.ianlee.lazy.base.lib.base.AppManager
            get() = com.ianlee.lazy.base.lib.base.AppManager.SingleHolder.instance
    }
}

