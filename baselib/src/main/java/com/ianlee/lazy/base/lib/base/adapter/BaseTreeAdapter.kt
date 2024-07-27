package com.ianlee.lazy.base.lib.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by Ian on 2024/3/22
 * Email: yixin0212@qq.com
 * Function : 属性结构adapter
 */
abstract class BaseTreeAdapter<T>(override var data: MutableList<T>) : BaseAdapter<T>(data) {

    var onItemTreeClickListener: OnItemTreeClickListener? = null

    constructor(manager: GridLayoutManager, data: MutableList<T>) : this(data) {
        manager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (getItemViewType(position)) {
                    TYPE_PARENT_HEADER -> manager.spanCount
                    TYPE_PARENT_FOOTER -> manager.spanCount
                    else -> 1
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            //头
            TYPE_PARENT_HEADER -> onCreateParentHeaderViewHolder(parent, viewType)
            //尾
            TYPE_PARENT_FOOTER -> onCreateParentFooterViewHolder(parent, viewType)
            else -> onCreateChildViewHolder(parent, viewType) //中间部分

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_PARENT_HEADER -> onBindParentHeaderView(holder, getParentIndex(position))
            TYPE_PARENT_FOOTER -> onBindParentFooterView(holder, getParentIndex(position))
            TYPE_CHILD -> onBindChildView(holder, getParentIndex(position), getChildIndex(position))
        }
    }

    private fun getParentIndex(position: Int): Int {
        var position = position
        for (i in 0 until getParentCount()) {
            if (position < getChildCount(i) + 2) {
                return i
            }
            position = position - getChildCount(i) - 2
        }
        return -1
    }

    private fun getChildIndex(cPosition: Int): Int {
        var position = cPosition
        for (i in 0 until getParentCount()) {
            if (position < getChildCount(i) + 2) {
                return position - 1
            }
            position = position - getChildCount(i) - 2
        }
        return -1
    }

    override fun getItemCount(): Int {
        var count = 0
        for (i in 0 until getParentCount()) {
            count += 2
            count += getChildCount(i)
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        var position = position
        for (i in 0 until getParentCount()) {
            if (position == 0) {
                return TYPE_PARENT_HEADER
            }
            position -= 1
            if (position < getChildCount(i)) {
                return TYPE_CHILD
            }
            position -= getChildCount(i)
            if (position == 0) {
                return TYPE_PARENT_FOOTER
            }
            position -= 1
        }
        IllegalStateException("BaseTreeAdapter excludes $position.")
        return 0
    }

    abstract fun getParentCount(): Int
    abstract fun getChildCount(parentPosition: Int): Int
    abstract fun onCreateParentHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder

    abstract fun onBindParentHeaderView(holder: RecyclerView.ViewHolder, position: Int)
    abstract fun onCreateParentFooterViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder

    abstract fun onBindParentFooterView(holder: RecyclerView.ViewHolder, position: Int)
    abstract fun onCreateChildViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder

    abstract fun onBindChildView(
        holder: RecyclerView.ViewHolder,
        position: Int,
        childPosition: Int
    )

    companion object {
        private const val TYPE_PARENT_HEADER = Int.MIN_VALUE
        private const val TYPE_PARENT_FOOTER = Int.MIN_VALUE + 2
        private const val TYPE_CHILD = Int.MIN_VALUE + 1

    }
}
