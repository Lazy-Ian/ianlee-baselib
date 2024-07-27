package com.ianlee.lazy.base.lib.view.recyclerview.decoration

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.DimenRes


/**
 * Created by Ian on 2021/7/21
 * Email: yixin0212@qq.com
 * Function :
 */
class StaggeredGridBuilder(context: Context?) :
    DividerDecoration.Builder(context!!) {
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

    //是否忽略fullSpan的情况
    var isIgnoreFullSpan = false
        private set

    /**
     * 设置分割线间距
     *
     * @param dpValueSpacing
     * @return
     */
    fun setSpacing(dpValueSpacing: Float): StaggeredGridBuilder {
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
    fun setSpacing(@DimenRes dimenResId: Int): StaggeredGridBuilder {
        spacing = Resources.getSystem().getDimensionPixelSize(dimenResId)
        return this
    }

    /**
     * 设置竖直线间距
     *
     * @param dpValueVLineSpacing
     * @return
     */
    fun setVLineSpacing(dpValueVLineSpacing: Float): StaggeredGridBuilder {
        vLineSpacing =
            DividerHelper.applyDimension(dpValueVLineSpacing, TypedValue.COMPLEX_UNIT_DIP)
                .toInt()
        return this
    }

    fun setVLineSpacing(@DimenRes dimenResId: Int): StaggeredGridBuilder {
        vLineSpacing = Resources.getSystem().getDimensionPixelSize(dimenResId)
        return this
    }

    /**
     * 设置水平线间距
     *
     * @param dpValueHLineSpacing
     * @return
     */
    fun setHLineSpacing(dpValueHLineSpacing: Float): StaggeredGridBuilder {
        hLineSpacing =
            DividerHelper.applyDimension(dpValueHLineSpacing, TypedValue.COMPLEX_UNIT_DIP)
                .toInt()
        return this
    }

    fun setHLineSpacing(@DimenRes dimenResId: Int): StaggeredGridBuilder {
        hLineSpacing = Resources.getSystem().getDimensionPixelSize(dimenResId)
        return this
    }

    /**
     * 设置是否包含边界
     *
     * @param includeEdge
     * @return
     */
    fun setIncludeEdge(includeEdge: Boolean): StaggeredGridBuilder {
        isIncludeEdge = includeEdge
        return this
    }

    /**
     * 设置是否忽略fullSpan的情况
     *
     * @param ignoreFullSpan
     * @return
     */
    fun setIgnoreFullSpan(ignoreFullSpan: Boolean): StaggeredGridBuilder {
        isIgnoreFullSpan = ignoreFullSpan
        return this
    }
}
