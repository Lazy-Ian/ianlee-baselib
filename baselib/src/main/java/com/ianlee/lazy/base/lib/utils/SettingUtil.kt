package com.ianlee.lazy.base.lib.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.preference.PreferenceManager
import androidx.core.content.ContextCompat
import com.ianlee.lazy.base.lib.R


/**
 * Created by Ian on 2021/7/7
 * Email: yixin0212@qq.com
 * Function :
 */
object SettingUtil {

    /**
     * 获取当前主题颜色
     */
    fun getColor(context: Context): Int {
        val setting = PreferenceManager.getDefaultSharedPreferences(context)
        val defaultColor = ContextCompat.getColor(context, R.color.color_white)
        val color = setting.getInt("color", defaultColor)
        return if (color != 0 && Color.alpha(color) != 255) {
            defaultColor
        } else {
            color
        }

    }


    fun getOneColorStateList(context: Context): ColorStateList {
        val colors = intArrayOf(getColor(context))
        val states = arrayOfNulls<IntArray>(1)
        states[0] = intArrayOf()
        return ColorStateList(states, colors)
    }

    fun getOneColorStateList(color: Int): ColorStateList {
        val colors = intArrayOf(color)
        val states = arrayOfNulls<IntArray>(1)
        states[0] = intArrayOf()
        return ColorStateList(states, colors)
    }


}