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
import com.ianlee.lazy.base.lib.view.recyclerview.decoration.Decoration


/**
 * Created by Ian on 2021/7/21
 * Email: yixin0212@qq.com
 * Function : LinearLayoutManager分割线构造器
 */
class LinearBuilder(context: Context?) : DividerDecoration.Builder(
    context!!
),
    ILDecoration {
    /**
     * 获取分割线宽（高）度
     *
     * @return
     */
    //默认分割线的宽（高）度,单位像素px
    var spacing = DividerHelper.dp2px(1f)
        private set

    /**
     * 是否最后一条显示分割线
     *
     * @return
     */
    //是否绘制最后一条分割线
    var isShowLastLine = false
        private set

    /**
     * 是否展示顶部分割线
     *
     * @return
     */
    //是否绘制第一个item的顶部分割线
    var isShowFirstTopLine = false
        private set

    /**
     * 是否忽略RecyclerView的padding
     *
     * @return
     */
    //是否绘制RecyclerView的左右padding（竖直）或上下padding（水平）
    var isIncludeParentLTPadding = false
        private set
    var isIncludeParentRBPadding = false
        private set

    /**
     * 获取分割线左内边距
     *
     * @return
     */
    //分割线左右内边距（垂直）
    override var leftPadding = 0
        private set

    /**
     * 获取分割线右内边距
     *
     * @return
     */
    override var rightPadding = 0
        private set

    /**
     * 获取分割线上内边距
     *
     * @return
     */
    //分割线上下内边距（水平）
    override var topPadding = 0
        private set

    /**
     * 获取分割线下内边距
     *
     * @return
     */
    override var bottomPadding = 0
        private set

    /**
     * 获取颜色值
     *
     * @return
     */
    //分割线颜色或背景
    protected var color = 0
        private set
    private var mDividerDrawable: Drawable? = null

    /**
     * 获取回调
     *
     * @return
     */
    //不画分割线position的回调
    var onItemNoDivider: OnNoDividerPosition? = null
        private set

    /**
     * 获取分割线绘制的回调
     *
     * @return
     */
    //item分割线绘制回调
    var itemDividerDecoration: OnItemDivider? = null
        private set

    /**
     * 设置分割线宽（高）度
     */
    fun setSpacing(dpValueSpanSpace: Float): LinearBuilder {
        spacing =
            DividerHelper.applyDimension(dpValueSpanSpace, TypedValue.COMPLEX_UNIT_DIP).toInt()
        return this
    }

    /**
     * 设置分割线宽（高）度
     */
    fun setSpacing(@DimenRes dimenResId: Int): LinearBuilder {
        spacing = Resources.getSystem().getDimensionPixelSize(dimenResId)
        return this
    }

    /**
     * 设置左右间距
     */
    override fun setPadding(dpValuePadding: Float): LinearBuilder {
        setLeftPadding(dpValuePadding)
        setRightPadding(dpValuePadding)
        setTopPadding(dpValuePadding)
        setBottomPadding(dpValuePadding)
        return this
    }

    /**
     * 设置左右间距
     */
    override fun setPadding(@DimenRes dimenResId: Int): LinearBuilder {
        setLeftPadding(dimenResId)
        setRightPadding(dimenResId)
        setTopPadding(dimenResId)
        setBottomPadding(dimenResId)
        return this
    }

    /**
     * 设置左间距
     */
    override fun setLeftPadding(dpValuePadding: Float): LinearBuilder {
        leftPadding =
            DividerHelper.applyDimension(dpValuePadding, TypedValue.COMPLEX_UNIT_DIP).toInt()
        return this
    }

    /**
     * 设置右间距
     */
    override fun setRightPadding(dpValuePadding: Float): LinearBuilder {
        rightPadding =
            DividerHelper.applyDimension(dpValuePadding, TypedValue.COMPLEX_UNIT_DIP).toInt()
        return this
    }

    /**
     * 通过资源id设置左间距
     */
    override fun setLeftPadding(@DimenRes dimenResId: Int): LinearBuilder {
        leftPadding = Resources.getSystem().getDimensionPixelSize(dimenResId)
        return this
    }

    /**
     * 通过资源id设置右间距
     */
    override fun setRightPadding(@DimenRes dimenResId: Int): LinearBuilder {
        rightPadding = Resources.getSystem().getDimensionPixelSize(dimenResId)
        return this
    }

    /**
     * 设置上间距
     */
    override fun setTopPadding(dpValuePadding: Float): LinearBuilder {
        topPadding =
            DividerHelper.applyDimension(dpValuePadding, TypedValue.COMPLEX_UNIT_DIP).toInt()
        return this
    }

    /**
     * 设置下间距
     */
    override fun setBottomPadding(dpValuePadding: Float): LinearBuilder {
        bottomPadding =
            DividerHelper.applyDimension(dpValuePadding, TypedValue.COMPLEX_UNIT_DIP).toInt()
        return this
    }

    /**
     * 通过资源id设置上间距
     */
    override fun setTopPadding(@DimenRes dimenResId: Int): LinearBuilder {
        topPadding = Resources.getSystem().getDimensionPixelSize(dimenResId)
        return this
    }

    /**
     * 通过资源id设置下间距
     */
    override fun setBottomPadding(@DimenRes dimenResId: Int): LinearBuilder {
        bottomPadding = Resources.getSystem().getDimensionPixelSize(dimenResId)
        return this
    }

    /**
     * 设置是否展示最后分割线
     *
     * @param showLastLine
     * @return
     */
    fun setShowLastLine(showLastLine: Boolean): LinearBuilder {
        isShowLastLine = showLastLine
        return this
    }

    /**
     * 设置是否展示第一个item顶部分割线
     *
     * @param showFirstTopLine
     * @return
     */
    fun setShowFirstTopLine(showFirstTopLine: Boolean): LinearBuilder {
        isShowFirstTopLine = showFirstTopLine
        return this
    }

    /**
     * 设置是否忽略RecyclerView的padding
     *
     * @param includeParentLTPadding
     * @param includeParentRBPadding
     * @return
     */
    fun setIncludeParentHVPadding(
        includeParentLTPadding: Boolean,
        includeParentRBPadding: Boolean
    ): LinearBuilder {
        isIncludeParentLTPadding = includeParentLTPadding
        isIncludeParentRBPadding = includeParentRBPadding
        return this
    }

    /**
     * 通过资源id设置颜色
     */
    override fun setColorRes(@ColorRes colorResId: Int): LinearBuilder {
        setColor(ContextCompat.getColor(mContext, colorResId))
        return this
    }

    /**
     * 设置颜色
     */
    override fun setColor(@ColorInt color: Int): LinearBuilder {
        this.color = color
        return this
    }

    /**
     * 通过资源id设置Drawable
     */
    override fun setDrawableRes(@DrawableRes drawableResId: Int): LinearBuilder {
        setDrawable(ContextCompat.getDrawable(mContext, drawableResId))
        return this
    }

    /**
     * 设置分割线Drawable
     */
    override fun setDrawable(drawable: Drawable?): LinearBuilder {
        mDividerDrawable = drawable
        return this
    }//创建Drawable

    /**
     * 获取分割线Drawable
     *
     * @return
     */
    override val dividerDrawable: Drawable
        get() {
            //创建Drawable
            if (mDividerDrawable == null) {
                mDividerDrawable = ColorDrawable(color)
            }
            return mDividerDrawable!!
        }

    /**
     * 设置不画分割线position的回调
     *
     * @param onNoDividerPosition
     * @return
     */
    fun setOnItemNoDivider(onNoDividerPosition: OnNoDividerPosition?): LinearBuilder {
        onItemNoDivider = onNoDividerPosition
        return this
    }

    /**
     * 是否画分割线的回调
     */
    interface OnNoDividerPosition {
        val noDividerPosition: IntArray?
    }

    /**
     * 设置分割线绘制监听
     *
     * @param onItemDivider
     * @return
     */
    fun setOnItemDividerDecoration(onItemDivider: OnItemDivider?): LinearBuilder {
        itemDividerDecoration = onItemDivider
        return this
    }

    interface OnItemDivider {
        fun getItemDividerDecoration(position: Int): Decoration?
    }
}
