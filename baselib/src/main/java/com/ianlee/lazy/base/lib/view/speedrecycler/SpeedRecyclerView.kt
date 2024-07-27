package com.ianlee.lazy.base.lib.view.speedrecycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

/**
 * Created by Ian on 2024/7/26
 * Email: yixin0212@qq.com
 * Function :
 */
class SpeedRecyclerView : RecyclerView {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    )

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        var velocityX = velocityX
        var velocityY = velocityY
        velocityX = solveVelocity(velocityX)
        velocityY = solveVelocity(velocityY)
        return super.fling(velocityX, velocityY)
    }

    private fun solveVelocity(velocity: Int): Int {
        return if (velocity > 0) {
            min(velocity.toDouble(), FLING_MAX_VELOCITY.toDouble())
                .toInt()
        } else {
            max(velocity.toDouble(), -FLING_MAX_VELOCITY.toDouble())
                .toInt()
        }
    }

    companion object {
        private const val FLING_SCALE_DOWN_FACTOR = 0.5f // 减速因子
        private const val FLING_MAX_VELOCITY = 5000 // 最大顺时滑动速度
    }
}

