package com.ianlee.lazy.base.lib.utils.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Created by Ian on 2023/11/24
 * Email: yixin0212@qq.com
 * * Function :自定义的Int类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 *
 */
class IntLiveData(value :Int = 0) : MutableLiveData<Int>(value) {

    override fun getValue(): Int {
        return super.getValue() ?: 0
    }
}