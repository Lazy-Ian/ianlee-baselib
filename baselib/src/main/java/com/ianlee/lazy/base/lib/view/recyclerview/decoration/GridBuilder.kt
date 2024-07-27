package com.ianlee.lazy.base.lib.view.recyclerview.decoration

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.ianlee.lazy.base.lib.view.recyclerview.decoration.DividerHelper.Companion.NO_COLOR


/**
 * Created by Ian on 2021/7/21
 * Email: yixin0212@qq.com
 * Function :GridLayoutManager分割线构造器
 */
class GridBuilder(context: Context?) : DividerDecoration.Builder(
    context!!
) {
    //分割线宽或高,mVLineSpacing||mHLineSpacing > mSpacing
    var vLineSpacing = 0
        private set
    var hLineSpacing = 0
        private set
    var spacing = 0
        private set

    //是否需要画边界
    var isIncludeEdge = false
        private set

    //竖直方向分割线是否包括item角边的距离
    var isVerticalIncludeEdge = false
        private set

    //分割线颜色,mVLineColor||mHLineColor > mColor
    var vLineColor: Int = NO_COLOR
        private set
    var hLineColor: Int = NO_COLOR
        private set

    /**
     * 获取颜色值
     *
     * @return
     */
    protected var color: Int = NO_COLOR
        private set

    //分割线drawable,mVLineDividerDrawable||mHLineDividerDrawable > mDividerDrawable
    private var mVLineDividerDrawable: Drawable? = null
    private var mHLineDividerDrawable: Drawable? = null
    private var mDividerDrawable: Drawable? = null

    /**
     * 设置分割线间距
     *
     * @param dpValueSpacing
     * @return
     */
    fun setSpacing(dpValueSpacing: Float): GridBuilder {
        spacing =
            DividerHelper.applyDimension(dpValueSpacing, TypedValue.COMPLEX_UNIT_DIP).toInt()
        return this
    }

    /**
     * 设置分割线间距
     *
     * @param dimenResId
     * @return
     */
    fun setSpacing(@DimenRes dimenResId: Int): GridBuilder {
        spacing = Resources.getSystem().getDimensionPixelSize(dimenResId)
        return this
    }

    /**
     * 设置竖直线间距
     *
     * @param dpValueVLineSpacing
     * @return
     */
    fun setVLineSpacing(dpValueVLineSpacing: Float): GridBuilder {
        vLineSpacing =
            DividerHelper.applyDimension(dpValueVLineSpacing, TypedValue.COMPLEX_UNIT_DIP)
                .toInt()
        return this
    }

    fun setVLineSpacing(@DimenRes dimenResId: Int): GridBuilder {
        vLineSpacing = Resources.getSystem().getDimensionPixelSize(dimenResId)
        return this
    }

    /**
     * 设置水平线间距
     *
     * @param dpValueHLineSpacing
     * @return
     */
    fun setHLineSpacing(dpValueHLineSpacing: Float): GridBuilder {
        hLineSpacing =
            DividerHelper.applyDimension(dpValueHLineSpacing, TypedValue.COMPLEX_UNIT_DIP)
                .toInt()
        return this
    }

    fun setHLineSpacing(@DimenRes dimenResId: Int): GridBuilder {
        hLineSpacing = Resources.getSystem().getDimensionPixelSize(dimenResId)
        return this
    }

    /**
     * 设置是否包含边界
     *
     * @param includeEdge
     * @return
     */
    fun setIncludeEdge(includeEdge: Boolean): GridBuilder {
        isIncludeEdge = includeEdge
        return this
    }

    /**
     * 设置边界是否包含于竖直方向上的分割线
     *
     * @param verticalIncludeEdge
     * @return
     */
    fun setVerticalIncludeEdge(verticalIncludeEdge: Boolean): GridBuilder {
        isVerticalIncludeEdge = verticalIncludeEdge
        return this
    }

    /**
     * 设置竖直分割线颜色
     *
     * @param vLineColor
     * @return
     */
    fun setVLineColor(@ColorInt vLineColor: Int): GridBuilder {
        this.vLineColor = vLineColor
        return this
    }

    /**
     * 通过资源id设置竖直分割线颜色
     *
     * @param colorResId
     * @return
     */
    fun setVLineColorRes(@ColorRes colorResId: Int): GridBuilder {
        setVLineColor(ContextCompat.getColor(mContext, colorResId))
        return this
    }

    /**
     * 设置水平分割线颜色
     *
     * @param hLineColor
     * @return
     */
    fun setHLineColor(@ColorInt hLineColor: Int): GridBuilder {
        this.hLineColor = hLineColor
        return this
    }

    /**
     * 通过资源id设置水平分割线颜色
     *
     * @param colorResId
     * @return
     */
    fun setHLineColorRes(@ColorRes colorResId: Int): GridBuilder {
        setHLineColor(ContextCompat.getColor(mContext, colorResId))
        return this
    }

    /**
     * 通过资源id设置颜色
     *
     * @param colorResId
     * @return
     */
    fun setColorRes(@ColorRes colorResId: Int): GridBuilder {
        setColor(ContextCompat.getColor(mContext, colorResId))
        return this
    }

    /**
     * 设置颜色，如果不设置mHLineColor的颜色，默认水平方向和竖直共用此颜色
     *
     * @param color
     * @return
     */
    fun setColor(@ColorInt color: Int): GridBuilder {
        this.color = color
        return this
    }

    /**
     * 设置竖直方向分割线的drawable
     *
     * @param vLineDividerDrawable
     * @return
     */
    fun setVLineDrawable(vLineDividerDrawable: Drawable?): GridBuilder {
        mVLineDividerDrawable = vLineDividerDrawable
        return this
    }

    /**
     * 通过资源id设置竖直方向分割线的drawable
     */
    fun setVLineDrawableRes(@DrawableRes drawableResId: Int): GridBuilder {
        setVLineDrawable(ContextCompat.getDrawable(mContext, drawableResId))
        return this
    }

    /**
     * 设置水平方向分割线的drawable
     *
     * @param hLineDividerDrawable
     * @return
     */
    fun setHLineDrawable(hLineDividerDrawable: Drawable?): GridBuilder {
        mHLineDividerDrawable = hLineDividerDrawable
        return this
    }

    /**
     * 通过资源id设置水平方向分割线的drawable
     */
    fun setHLineDrawableRes(@DrawableRes drawableResId: Int): GridBuilder {
        setHLineDrawable(ContextCompat.getDrawable(mContext, drawableResId))
        return this
    }

    /**
     * 通过资源id设置Drawable
     */
    fun setDrawableRes(@DrawableRes drawableResId: Int): GridBuilder {
        setDrawable(ContextCompat.getDrawable(mContext, drawableResId))
        return this
    }

    /**
     * 设置分割线Drawable
     */
    fun setDrawable(drawable: Drawable?): GridBuilder {
        mDividerDrawable = drawable
        return this
    }
    //    /**
    //     * 获取分割线Drawable
    //     *
    //     * @return
    //     */
    //    protected Drawable getDividerDrawable() {
    //        return mDividerDrawable;
    //    }//创建Drawable
    /**
     * 获取分割线Drawable
     *
     * @return
     */
    val vLineDividerDrawable: Drawable?
        get() {
            //创建Drawable
            if (mVLineDividerDrawable == null) {
                if (vLineColor == NO_COLOR) {
                    if (mDividerDrawable == null) {
                        if (color != NO_COLOR) {
                            mVLineDividerDrawable = ColorDrawable(color)
                        }
                    } else {
                        mVLineDividerDrawable = mDividerDrawable
                    }
                } else {
                    mVLineDividerDrawable = ColorDrawable(vLineColor)
                }
            }
            return mVLineDividerDrawable
        }//创建Drawable

    /**
     * 获取分割线Drawable
     *
     * @return
     */
    val hLineDividerDrawable: Drawable?
        get() {
            //创建Drawable
            if (mHLineDividerDrawable == null) {
                if (hLineColor == NO_COLOR) {
                    if (mDividerDrawable == null) {
                        if (color != NO_COLOR) {
                            mHLineDividerDrawable = ColorDrawable(color)
                        }
                    } else {
                        mHLineDividerDrawable = mDividerDrawable
                    }
                } else {
                    mHLineDividerDrawable = ColorDrawable(hLineColor)
                }
            }
            return mHLineDividerDrawable
        }
}
