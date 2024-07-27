package com.ianlee.lazy.base.lib.utils.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Created by Ian on 2023/11/22
 * Email: yixin0212@qq.com
 * Function :自定义的String类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 *
 */
class StringLiveData(value:String ="") : MutableLiveData<String>(value) {

    override fun getValue(): String {
        return super.getValue() ?: ""
    }

}