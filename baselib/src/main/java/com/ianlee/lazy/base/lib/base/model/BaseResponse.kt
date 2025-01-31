package com.ianlee.lazy.base.lib.base.model

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
abstract class BaseResponse<T>{
    //抽象方法，用户的基类继承该类时，需要重写该方法
    abstract fun isSuccess(): Boolean

    abstract fun getResponseData(): T

    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String
}