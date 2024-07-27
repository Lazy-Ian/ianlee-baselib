package com.ianlee.lazy.base.lib.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout

/**
 * Created by Ian on 2024/7/16
 * Email: yixin0212@qq.com
 * Function :
 */
class MyDrawerLayout : DrawerLayout {

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
    }


    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (this.isDrawerOpen(Gravity.RIGHT)) {
            return true
        } else {
            return super.onTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//        when (ev!!.action) {
//            MotionEvent.ACTION_DOWN -> {
//                var x = ev.x
//                var y = ev.y
//                var view = findTopChildUnder(x,y)
//                if (view!= null && isContentView(view)&& this.isDrawerOpen(GravityCompat.START)) {
//                    return true
//                }
//
//
//                if (this.isDrawerOpen(Gravity.RIGHT)){
//                    return true
//                }
//            }
//        }

            return super.onInterceptTouchEvent(ev)
    }

    fun findTopChildUnder(x: Float, y: Float): View? {
        var childCount = childCount

        for (i in childCount - 1 downTo 0) {
            var child = getChildAt(i);
            if (x >= child.left && x < child.right && y >= child.top && y < child.bottom) {
                return child
            }
        }
        return null
    }

    fun isContentView(child: View): Boolean {
        return (child.layoutParams as (LayoutParams)).gravity == Gravity.NO_GRAVITY;
    }
}