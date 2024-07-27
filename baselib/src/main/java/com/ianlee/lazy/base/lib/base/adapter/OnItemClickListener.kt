package com.ianlee.lazy.base.lib.base.adapter

import android.view.View

/**
 * Created by Ian on 2024/3/22
 * Email: yixin0212@qq.com
 * Function : BaseBindAdapter 点击监听事件
 */
interface OnItemClickListener {
    fun onItemClick(view: View, position: Int)

}