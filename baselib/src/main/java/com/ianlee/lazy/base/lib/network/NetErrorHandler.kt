package com.ianlee.lazy.base.lib.network

import retrofit2.HttpException
import java.io.EOFException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
object NetErrorHandler {

    fun errorHandle(
        error: java.lang.Exception,
    ) {
        when (error) {
            is HttpException -> {
            }
            is UnknownHostException -> {
            }
            is SocketTimeoutException -> {
            }
            is ConnectException -> {
            }
            is SocketException -> {
//                ToastUtils.showLong("服务器异常连接关闭")
            }
            is EOFException -> {
//                ToastUtils.showLong("服务器异常连接关闭")
            }
            is IllegalArgumentException -> {
            }
            is SSLException -> {
//                ToastUtils.showLong("证书错误")
            }
            is NullPointerException -> {
//                ToastUtils.showLong("数据为空")
            }
            is ServiceErrorException -> {
            }
            is IOException -> {//取消api会发生这个异常
            }
            else -> {
            }
        }
    }
}