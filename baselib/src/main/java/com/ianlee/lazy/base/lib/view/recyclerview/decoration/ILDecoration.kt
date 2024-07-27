package com.ianlee.lazy.base.lib.view.recyclerview.decoration

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes


/**
 * Created by Ian on 2021/7/21
 * Email: yixin0212@qq.com
 * Function :接口类
 */
interface ILDecoration {
    fun setPadding(dpValuePadding: Float): ILDecoration?
    fun setPadding(@DimenRes dimenResId: Int): ILDecoration?
    fun setLeftPadding(dpValuePadding: Float): ILDecoration?
    fun setRightPadding(dpValuePadding: Float): ILDecoration?
    fun setLeftPadding(@DimenRes dimenResId: Int): ILDecoration?
    fun setRightPadding(@DimenRes dimenResId: Int): ILDecoration?
    fun setTopPadding(dpValuePadding: Float): ILDecoration?
    fun setBottomPadding(dpValuePadding: Float): ILDecoration?
    fun setTopPadding(@DimenRes dimenResId: Int): ILDecoration?
    fun setBottomPadding(@DimenRes dimenResId: Int): ILDecoration?
    fun setColorRes(@ColorRes colorResId: Int): ILDecoration?
    fun setColor(@ColorInt color: Int): ILDecoration?
    fun setDrawableRes(@DrawableRes drawableResId: Int): ILDecoration?
    fun setDrawable(drawable: Drawable?): ILDecoration?

    //    int getDividerSpace();
    val leftPadding: Int

    val rightPadding: Int
    val topPadding: Int
    val bottomPadding: Int
    val dividerDrawable: Drawable?
}
