package com.ianlee.lazy.base.lib.base.model

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function : 版本更新
 */
data class AppVersionInfoModel(
    var app_info: AppVersionModel,
) {
    data class AppVersionModel(
        var name: String,
        var version: String,
        var versionCode: Int,
        var now_url: String,
        var silent: String, //1是静默安装
        var force: String, // 1 是强制 0 是不强制
        var net_check: String,
        var note: Boolean
    )
}

