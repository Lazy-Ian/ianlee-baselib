package com.ianlee.lazy.base.lib.view.speedrecycler

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.ianlee.lazy.base.lib.network.utils.dp2px


/**
 * Created by Ian on 2021/12/2
 * Email: yixin0212@qq.com
 * Function :
 */
class CardAdapterHelper {
    private var mPagePadding = 15f
    private var mShowLeftCardWidth = 15f
    private var mShowRightCardWidth = 15f
    fun onCreateViewHolder(parent: ViewGroup, itemView: View) {
        val lp = itemView.layoutParams as RecyclerView.LayoutParams
        lp.width = (parent.width / 2) - dp2px(2f * (mPagePadding + mShowLeftCardWidth)).toInt()
        itemView.layoutParams = lp
    }

    fun onBindViewHolder(itemView: View, position: Int, itemCount: Int) {
        val padding: Int = dp2px(mPagePadding).toInt()
        itemView.setPadding(padding, 0, padding, 0)
        val leftMarin = if (position == 0) padding + dp2px(
            mShowLeftCardWidth
        ) else 0
        val rightMarin: Int = padding + dp2px(mShowLeftCardWidth).toInt()
        setViewMargin(itemView, leftMarin.toInt(), 0, rightMarin, 0)
    }

    private fun setViewMargin(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        val lp = view.layoutParams as MarginLayoutParams
        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom)
            view.layoutParams = lp
        }
    }

    fun setPagePadding(pagePadding: Int) {
        mPagePadding = pagePadding.toFloat()
    }

    fun setShowLeftCardWidth(showLeftCardWidth: Int) {
        mShowLeftCardWidth = showLeftCardWidth.toFloat()
    }

    fun setShowRightCardWidth(showRightCardWidth: Int) {
        mShowRightCardWidth = showRightCardWidth.toFloat()
    }
}
