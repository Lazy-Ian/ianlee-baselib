package com.ianlee.lazy.base.lib.view.recyclerview.decoration

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat


/**
 * Created by Ian on 2021/7/21
 * Email: yixin0212@qq.com
 * Function :  线性item分割线修饰类
 */
    class Decoration(private val mContext: Context) : ILDecoration {
        //    @Override
        //    public int getDividerSpace() {
        //        return mSpanSpace;
        //    }
        //分割线左右内边距（垂直）
        override var leftPadding = 0
            private set
        override var rightPadding = 0
            private set

        //分割线上下内边距（水平）
        override var topPadding = 0
            private set
        override var bottomPadding = 0
            private set

        //分割线颜色或背景
        private var mColor = 0
        private var mDividerDrawable: Drawable? = null

        //默认对边界不处理
        private var isDrawLeft: Boolean = false
        private var isDrawTop: Boolean = false
        private var isDrawRight: Boolean = false
        private var isDrawBottom: Boolean = false

        /**
         * 设置绘制item左上右下属性
         *
         * @param isDrawLeft
         * @param isDrawTop
         * @param isDrawRight
         * @param isDrawBottom
         * @return
         */
        fun setAroundEdge(
            isDrawLeft: Boolean?,
            isDrawTop: Boolean?,
            isDrawRight: Boolean?,
            isDrawBottom: Boolean?
        ): Decoration {
            if (isDrawLeft != null) {
                this.isDrawLeft = isDrawLeft
            }
            if (isDrawTop != null) {
                this.isDrawTop = isDrawTop
            }
            if (isDrawRight != null) {
                this.isDrawRight = isDrawRight
            }
            if (isDrawBottom != null) {
                this.isDrawBottom = isDrawBottom
            }
            return this
        }

    fun getAroundEdge(): Array<Boolean> {
        return arrayOf(isDrawLeft, isDrawTop, isDrawRight, isDrawBottom)
    }
        //
        //    @Override
        //    protected Decoration setDividerSpace(float dpValueSpanSpace) {
        //        mSpanSpace = (int) DividerHelper.applyDimension(dpValueSpanSpace, TypedValue.COMPLEX_UNIT_DIP);
        //        return this;
        //    }
        //
        //    @Override
        //    protected Decoration setDividerSpace(int dimenResId) {
        //        mSpanSpace = Resources.getSystem().getDimensionPixelSize(dimenResId);
        //        return this;
        //    }
        override fun setPadding(dpValuePadding: Float): Decoration {
            setLeftPadding(dpValuePadding)
            setRightPadding(dpValuePadding)
            setTopPadding(dpValuePadding)
            setBottomPadding(dpValuePadding)
            return this
        }

        override fun setPadding(dimenResId: Int): Decoration {
            setLeftPadding(dimenResId)
            setRightPadding(dimenResId)
            setTopPadding(dimenResId)
            setBottomPadding(dimenResId)
            return this
        }

        override fun setLeftPadding(dpValuePadding: Float): Decoration {
            leftPadding = DividerHelper.applyDimension(dpValuePadding, TypedValue.COMPLEX_UNIT_DIP).toInt()
            return this
        }

        override fun setRightPadding(dpValuePadding: Float): Decoration {
            rightPadding =
                DividerHelper.applyDimension(dpValuePadding, TypedValue.COMPLEX_UNIT_DIP).toInt()
            return this
        }

        override fun setLeftPadding(dimenResId: Int): Decoration {
            leftPadding = Resources.getSystem().getDimensionPixelSize(dimenResId)
            return this
        }

        override fun setRightPadding(dimenResId: Int): Decoration {
            rightPadding = Resources.getSystem().getDimensionPixelSize(dimenResId)
            return this
        }

        override fun setTopPadding(dpValuePadding: Float): Decoration {
            topPadding = DividerHelper.applyDimension(dpValuePadding, TypedValue.COMPLEX_UNIT_DIP).toInt()
            return this
        }

        override fun setBottomPadding(dpValuePadding: Float): Decoration {
            bottomPadding =
                DividerHelper.applyDimension(dpValuePadding, TypedValue.COMPLEX_UNIT_DIP).toInt()
            return this
        }

        override fun setTopPadding(dimenResId: Int): Decoration {
            topPadding = Resources.getSystem().getDimensionPixelSize(dimenResId)
            return this
        }

        override fun setBottomPadding(dimenResId: Int): Decoration {
            bottomPadding = Resources.getSystem().getDimensionPixelSize(dimenResId)
            return this
        }

        override fun setColorRes(colorResId: Int): Decoration {
            setColor(ContextCompat.getColor(mContext, colorResId))
            return this
        }

        override fun setColor(color: Int): Decoration {
            mColor = color
            return this
        }

        override fun setDrawableRes(drawableResId: Int): Decoration {
            setDrawable(ContextCompat.getDrawable(mContext, drawableResId))
            return this
        }

        override fun setDrawable(drawable: Drawable?): Decoration {
            mDividerDrawable = drawable
            return this
        }

        //创建Drawable
        override val dividerDrawable: Drawable
            get() {
                //创建Drawable
                if (mDividerDrawable == null) {
                    mDividerDrawable = ColorDrawable(mColor)
                }
                return mDividerDrawable!!
            }

    }

