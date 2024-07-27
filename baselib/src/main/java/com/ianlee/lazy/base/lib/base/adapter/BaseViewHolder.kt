package com.ianlee.lazy.base.lib.base.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by Ian on 2024/3/22
 * Email: yixin0212@qq.com
 * Function : BaseViewHolder
 */
class BaseViewHolder<V : ViewBinding>(val context: Context, val binding: V) : RecyclerView.ViewHolder(binding.root) {

}