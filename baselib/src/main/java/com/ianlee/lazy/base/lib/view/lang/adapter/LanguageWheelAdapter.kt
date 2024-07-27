package com.ianlee.lazy.base.lib.view.lang.adapter

import com.ianlee.lazy.base.lib.base.model.LanguageListBean
import com.ianlee.lazy.base.lib.view.picker.adapter.WheelAdapter


/**
 * 语言选择adapter
 */
class LanguageWheelAdapter(var data :MutableList<LanguageListBean.LanguageInfoBean>) :
    WheelAdapter<LanguageListBean.LanguageInfoBean> {
    override val itemCount: Int
        get() = data.size

    override fun getItem(index: Int): LanguageListBean.LanguageInfoBean {
        return data[index]
    }
}