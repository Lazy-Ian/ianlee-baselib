package com.ianlee.lazy.base.lib.utils

/**
 * Created by Ian on 2024/4/7
 * Email: yixin0212@qq.com
 * Function : 图片工具
 */
object ImageUtil {


    fun getImg(url: String): String {
        return getImg(url, "280")
    }

    fun getImg(url: String, w: String): String {
        return "$url"
//        return "$url?imageMogr2/thumbnail/${w}x"
    }


}