package com.ianlee.lazy.base.lib.network.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.ianlee.lazy.base.lib.R


/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
inline fun <reified T> T?.notNull(notNullAction: (T) -> Unit, nullAction: () -> Unit = {}) {
    if (this != null) {
        notNullAction.invoke(this)
    } else {
        nullAction.invoke()
    }
}

//color
fun getColorId(context: Context, colorId: Int): Int {
    return ContextCompat.getColor(context, colorId)
}

//string
fun getString4Id(id: Int): String {
    return com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication().resources.getString(id)
}

fun getString4Id(id: Int, value: String): String {
    return com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication().resources.getString(id, value)
}

fun getString4Id(id: Int, value: Int): String {
    return com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication().resources.getString(id, value)
}


/**
 * dp转px
 *
 * @param context
 * @param dpVal
 * @return pxVal
 */
fun dp2px(dpVal: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dpVal, getDisplayMetrics()
    )
}


//像素转换
fun getDisplayMetrics(): DisplayMetrics {
    return com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication().resources.displayMetrics
}

fun getDpi(): Int {
    val dm = getDisplayMetrics()
    return dm.densityDpi
}


/**
 * 获取屏幕的宽 尺寸像素px
 *
 * @param cont
 * @return int
 */
fun getScreenWidth(cont: Context?): Int {
    return cont?.resources?.displayMetrics!!.widthPixels
}

/**
 * 获取屏幕的高 尺寸像素px
 *
 * @param cont
 * @return int
 */
fun getScreenHeight(cont: Context): Int {
    return cont.resources?.displayMetrics!!.heightPixels

}
fun getScreenHeight(): Int {
    return getDisplayMetrics().heightPixels

}


/**
 * 获取屏幕的宽 尺寸像素px
 */
fun getScreenWidth(): Int {
    return getDisplayMetrics().widthPixels
}


//toast
var mToast: Toast? = null

@SuppressLint("ShowToast")
fun Context.showToast(content: Int): Toast {
    val toast = Toast(com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication())
    val view: View =
        LayoutInflater.from(com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication())
            .inflate(R.layout.layout_toast, null)
    val tvMessage: TextView = view.findViewById(R.id.tv_toast_message)
    tvMessage.setText(content)

    //根据自己需要对view设置text或其他样式
    toast.setView(view)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.duration = Toast.LENGTH_SHORT
    return toast!!
}

@SuppressLint("ShowToast")
fun Context.showToast(content: String): Toast {
    val toast = Toast(com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication())
    val view: View =
        LayoutInflater.from(com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication())
            .inflate(R.layout.layout_toast, null)
    //根据自己需要对view设置text或其他样式
    val tvMessage: TextView = view.findViewById(R.id.tv_toast_message)
    tvMessage.setText(content)
    toast.setView(view)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.duration = Toast.LENGTH_SHORT
    toast.show()
    return toast!!
}

fun Context.showToastLong(content: String): Toast {
    val toast = Toast(com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication())
    val view: View =
        LayoutInflater.from(com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication())
            .inflate(R.layout.layout_toast, null)
    val tvMessage: TextView = view.findViewById(R.id.tv_toast_message)
    tvMessage.setText(content)
    //根据自己需要对view设置text或其他样式
    toast.setView(view)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.duration = Toast.LENGTH_SHORT
    toast.show()
    return toast!!
}

fun Context.showSnackbarLong(view: View, content: String) {
    Snackbar.make(view, content, Snackbar.LENGTH_LONG)
        .setAnchorView(view)
        .setAction("Action", null).show()
}


/**
 * 取消Toast显示
 */
fun clearToast() {
    if (mToast != null) {
        mToast!!.cancel()
        mToast = null
    }
}

//全局 token
var token_id = ""
var language = ""