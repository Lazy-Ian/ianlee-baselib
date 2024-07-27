package com.ianlee.lazy.base.lib.utils.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Created by Ian on 2023/12/18
 * Email: yixin0212@qq.com
 * Function :自定义的Float类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 */
class FloatLiveData(value :Float = 0.0f) : MutableLiveData<Float>(value) {
    override fun getValue(): Float {
        return super.getValue() ?: 0.0f
    }
}