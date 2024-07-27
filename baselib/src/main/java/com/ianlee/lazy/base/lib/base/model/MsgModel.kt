package com.ianlee.lazy.base.lib.base.model

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
open class MsgModel(
    val msg: String = "",
    val code: String = "",
    val detail: String = "",
    val time: String = "",

    ) {
      fun isSuccess(): Boolean = code == "1"

}