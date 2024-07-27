package com.ianlee.lazy.base.lib.view.speedrecycler

import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by Ian on 2021/12/3
 * Email: yixin0212@qq.com
 * Function : 防止卡片在第一页和最后一页因无法"居中"而一直循环调用onScrollStateChanged-->SnapHelper.snapToTargetExistingView-->onScrollStateChanged
 */
class CardLinearSnapHelper : LinearSnapHelper() {
    var mNoNeedToScroll = false
    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray? {
        return if (mNoNeedToScroll) {
            intArrayOf(0, 0)
        } else {
            super.calculateDistanceToFinalSnap(layoutManager, targetView)
        }
    }
}
