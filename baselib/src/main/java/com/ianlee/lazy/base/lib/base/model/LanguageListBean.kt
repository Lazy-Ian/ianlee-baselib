package com.ianlee.lazy.base.lib.base.model

/**
 * Created by Ian on 2024/6/25
 * Email: yixin0212@qq.com
 * Function :
 */
data class LanguageListBean(var model: ArrayList<LanguageInfoBean> = arrayListOf()) {


    data class LanguageInfoBean(
        var code: String = "",
        var new_name: String = "",
        var icon: String = "",
        var isSelected: Boolean = false,
    )
}
