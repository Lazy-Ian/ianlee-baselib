package com.ianlee.lazy.base.lib.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

/**
 * Created by Ian on 2024/5/14
 * Email: yixin0212@qq.com
 * Function :
 */
class AutoPollRecyclerView(context: Context?, attrs: AttributeSet?) :
    RecyclerView(context!!, attrs), LifecycleObserver {
    var autoPollTask: AutoPollTask = AutoPollTask(this)
    private var running = false //标示是否正在自动轮询
    private var canRun = false //标示是否可以自动轮询,可在不需要的是否置false

    class AutoPollTask(reference: AutoPollRecyclerView?) : Runnable {
        private val mReference: WeakReference<*>

        //使用弱引用持有外部类引用->防止内存泄漏
        init {
            mReference = WeakReference<Any?>(reference)
        }

        override fun run() {
            val recyclerView = mReference.get() as AutoPollRecyclerView?
            if (recyclerView != null && recyclerView.running && recyclerView.canRun) {
                recyclerView.scrollBy(1, 1)
                recyclerView.postDelayed(recyclerView.autoPollTask, TIME_AUTO_POLL)
            }
        }
    }

    //开启:如果正在运行,先停止->再开启
    fun start() {
        if (running && context != null) stop()
        canRun = true
        running = true
        postDelayed(autoPollTask, TIME_AUTO_POLL)
    }

    fun stop() {
        if (context != null) {
            running = false
            removeCallbacks(autoPollTask)
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> if (running) stop()
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> if (canRun) start()
        }
        return super.onTouchEvent(e)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if (canRun) start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        stop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        stop()
    }

    companion object {
        private const val TIME_AUTO_POLL: Long = 50
    }
}
