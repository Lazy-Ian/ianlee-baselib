package com.ianlee.lazy.base.lib.base.adapter

import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ianlee.lazy.base.lib.network.params.StyleColorBean
import com.ianlee.lazy.base.lib.utils.MMKVUtil

/**
 * Created by Ian on 2024/3/22
 * Email: yixin0212@qq.com
 * Function :  BaseAdapter 带ViewBiding
 */

abstract class BaseBindAdapter<T, V : ViewBinding>(data: MutableList<T>) :
    RecyclerView.Adapter<BaseViewHolder<V>>() {
   var  styleColorBean : StyleColorBean? = null
    var onItemClickListener: OnItemClickListener? = null

    open var data: MutableList<T> = mutableListOf()
        set(value) {
            field = value
            notifyItemRangeChanged(0, value.size)
        }

    init {
        this.data = data
        if (MMKVUtil.getStyleColor()!= null){
            styleColorBean = MMKVUtil.getStyleColor()
        }

    }

    open fun setDataList(dataList: Collection<T>) {
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
        notifyDataSetChanged()

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
        this.data.addAll(newData)
        notifyItemRangeInserted(this.data.size - newData.size, newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    private fun compatibilityDataSizeChanged(size: Int) {
        if (this.data.size == size) {
            notifyDataSetChanged()
        }
    }

    @JvmName("setOnItemClickListener1")
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<V>


    override fun onBindViewHolder(holder: BaseViewHolder<V>, position: Int) {
        onBindViewHolder(
            holder,
            holder.adapterPosition,
            holder.binding,
            data[holder.adapterPosition]
        )
        holder.itemView.setOnClickListener {
            if (onItemClickListener != null) {
                onItemClickListener!!.onItemClick(it, holder.adapterPosition)
            }
        }

    }

    abstract fun onBindViewHolder(holder: BaseViewHolder<V>, position: Int, binding: V, item: T)


    override fun getItemCount(): Int = data.size
}


