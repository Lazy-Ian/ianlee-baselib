package com.ianlee.lazy.base.lib.network.params

/**
 * Created by Ian on 2024/5/13
 * Email: yixin0212@qq.com
 * Function :
 */
data class StyleColorBean(var info: InfoBean) {

    data class InfoBean(
        var p5: String = "",
        var p4: String = "",
        var p3: String = "",
        var p2: String = "",
        var p1: String = "",
        var f2: String = "",
        var f1: String = "",
        var s2: String = "",
        var s1: String = "",
        var w2: String = "",
        var w1: String = "",
        var e2: String = "",
        var e1: String = "",
        var image_h1: ImageH_WBean = ImageH_WBean(),
        var image_h2: ImageH_WBean = ImageH_WBean(),
        var image_h3: ImageH_WBean = ImageH_WBean(),
        var image_w1: ImageH_WBean = ImageH_WBean(),
        var image_w2: ImageH_WBean = ImageH_WBean(),
        var image_w3: ImageH_WBean = ImageH_WBean(),
        var logo_small: ImageH_WBean = ImageH_WBean(),
        var logo_big: ImageH_WBean = ImageH_WBean()
    )


    data class ImageH_WBean(
        var image_id: Int = 0,
        var url: String = "",
    )
}