package com.ianlee.lazy.base.lib.network
import com.ianlee.lazy.base.lib.network.utils.Error

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
class AppException : Exception {

    var msg: String ="" //错误消息
    var code: Int = 200 //错误码
    var detail: String? //错误日志

    constructor(errCode: Int, error: String?, errorLog: String? = "") : super(error) {
        this.msg = error ?: "请求失败，请稍后再试"
        this.code = errCode
        this.detail = errorLog?:this.msg
    }

    constructor(error: Error, e: Throwable?) {
        code = error.getKey()
        msg = error.getValue()
        detail = e?.message
    }
}