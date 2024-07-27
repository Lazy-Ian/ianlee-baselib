package com.ianlee.lazy.base.lib.base.adapter

import android.view.View


/**
 * Created by Ian on 2024/3/22
 * Email: yixin0212@qq.com
 * Function :树形列表项点击事件 加入购物车
 */
interface OnItemTreeClickListener {
    fun onItemClick(
        view: View?,
        position: Int,
        childPosition: Int,
        isAdd: Boolean = false
    )
}