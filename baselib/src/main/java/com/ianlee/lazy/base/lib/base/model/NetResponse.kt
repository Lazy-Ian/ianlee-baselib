package com.ianlee.lazy.base.lib.base.model

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
data class NetResponse<T>(
    var data: T,
    val error: Error = Error(),
    val code: Int = 0,
    val msg: String = ""
) : BaseResponse<T>() {
    override fun isSuccess(): Boolean = code == 1

    override fun getResponseData(): T = data

    override fun getResponseCode(): Int = code

    override fun getResponseMsg(): String = msg

}


data class Error(
    val code: Int = 0,
    val message: String = ""
)

data class MessageInfo(
    val type: String = "",
    val desc: String = "",
    val code: String = ""
)