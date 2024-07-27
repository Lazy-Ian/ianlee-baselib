package com.ianlee.lazy.base.lib.base.adapter

import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by Ian on 2021/7/28
 * Email: yixin0212@qq.com
 * Function :Adapter基类
 * { com.winekar.app.adapter.BaseTreeAdapter}
 * { com.winekar.app.adapter.HeaderFooterAdapter}
 */

abstract class BaseAdapter<T>(data: MutableList<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    var onItemClickListener: OnItemClickListener? = null
    private var isLoadingMore = false


    open var data: MutableList<T> = mutableListOf()
        set(value) {
            field = value
            notifyItemRangeChanged(0, value.size)
        }


    init {
        this.data = data
    }

    @JvmName("setOnItemClickListener1")
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    fun setIsLoadingMore(isLoadingMore: Boolean) {
        this.isLoadingMore = isLoadingMore
    }

    fun isLoadingMore(): Boolean {
        return isLoadingMore
    }

    open fun notifyDataChange() {
        notifyDataSetChanged()
        setIsLoadingMore(false)
    }


    open fun setDataList(dataList: Collection<T>) {
        if (dataList != null) {
            if (dataList !== data) {
                this.data.clear()
                if (!dataList.isNullOrEmpty()) {
                    this.data.addAll(dataList)
                }
            } else {
                if (!dataList.isNullOrEmpty()) {
                    val newList = ArrayList(dataList)
                    this.data.clear()
                    this.data.addAll(newList)
                } else {
                    this.data.clear()
                }
            }
            compatibilityDataSizeChanged(data.size)
        }
    }

    /**
     *是否有头
     */
    var hasHeader = false

    val headerLayoutCount: Int
        get() {
            return if (hasHeader) {
                1
            } else {
                0
            }
        }

    /**
     * add new data in to certain location
     * 在指定位置添加数据
     *
     * @param position the insert position
     * @param newData  the new data collection
     */
    open fun addData(@IntRange(from = 0) position: Int, newData: Collection<T>) {
        this.data.addAll(position, newData)
        notifyItemRangeInserted(position, newData.size)
        compatibilityDataSizeChanged(newData.size)
    }


    open fun addData(@NonNull newData: Collection<T>) {
        if (newData != null) {
            this.data.addAll(newData)
            notifyItemRangeInserted(itemCount - newData.size, newData.size)
            compatibilityDataSizeChanged(newData.size)
        }
    }


    companion object {
        protected const val TYPE_HEADER = 1
        protected const val TYPE_ITEM = 2
    }

    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    private fun compatibilityDataSizeChanged(size: Int) {
        if (this.data.size == size) {
            notifyDataSetChanged()
        }
    }


}
