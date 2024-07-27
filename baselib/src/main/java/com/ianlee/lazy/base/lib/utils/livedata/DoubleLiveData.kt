package com.ianlee.lazy.base.lib.utils.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Created by Ian on 2024/7/17
 * Email: yixin0212@qq.com
 * Function :
 */
class DoubleLiveData(value :Double = 0.00) : MutableLiveData<Double>(value) {
    override fun getValue(): Double {
        return super.getValue() ?: 0.00
    }
}