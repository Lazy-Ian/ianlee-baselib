package com.ianlee.lazy.base.lib.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView


/**
 * Created by Ian on 2022/3/22
 * Email: yixin0212@qq.com
 * Function :跑马灯
 */
@SuppressLint("AppCompatCustomView")
class MarqueeTextView : TextView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun isFocused(): Boolean {
        return true
    }
}
