package com.ianlee.lazy.base.lib.view.recyclerview.decoration

import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.IntDef
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.*


/**
 * Created by Ian on 2021/7/21
 * Email: yixin0212@qq.com
 * Function : 设置分割线帮助类
 */
class DividerHelper private constructor() {
    enum class DividerType {
        LINEAR_VERTICAL, LINEAR_HORIZONTAL, GRID_VERTICAL, GRID_HORIZONTAL, STAGGERED_GRID_VERTICAL, STAGGERED_GRID_HORIZONTAL, UNKNOWN
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(Edge.LEFT, Edge.TOP, Edge.RIGHT, Edge.BOTTOM)
    annotation class Edge {
        companion object {
            const val LEFT = 0
            const val TOP = 1
            const val RIGHT = 2
            const val BOTTOM = 3
        }
    }

    companion object {
        private const val TAG = "DividerHelper"
        const val NO_COLOR = Color.TRANSPARENT

        /**
         * 是否为最后一行（Vertical）
         * 是否为最后一列（Horizontal）
         *
         * @param parent
         * @param childCount
         * @param childIndex
         * @return
         */
        fun isLastRow(
            parent: RecyclerView,
            childCount: Int,
            childIndex: Int
        ): Boolean {
            val spanCount = getSpanCount(parent)
            return if (getOrientation(parent) == com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu.VERTICAL) {
                isLastItemEdgeValid(
                    childIndex >= childCount - spanCount, parent, childCount, childIndex, true
                )
            } else {
                val spanIndex = getItemSpanIndex(parent, childIndex, true)
                val itemSpanSize = getItemSpanSize(parent, childIndex, true)
                spanIndex + itemSpanSize == spanCount
            }
        }

        /**
         * 是否为最后一列
         *
         * @param parent
         * @return
         */
        fun isLastColumn(
            parent: RecyclerView,
            childCount: Int,
            childIndex: Int
        ): Boolean {
            val spanCount = getSpanCount(parent)
            return if (getOrientation(parent) == com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu.VERTICAL) {
                val spanIndex = getItemSpanIndex(parent, childIndex, false)
                val itemSpanSize = getItemSpanSize(parent, childIndex, false)
                spanIndex + itemSpanSize == spanCount
            } else {
                isLastItemEdgeValid(
                    childIndex >= childCount - spanCount, parent, childCount, childIndex, false
                )
            }
        }

        //    /**
        //     * 是否为边界
        //     *
        //     * @param parent
        //     * @param childCount
        //     * @param childIndex
        //     * @param itemSpanSize
        //     * @param spanIndex
        //     * @return
        //     */
        //    public static boolean isBottomEdge(RecyclerView parent,
        //                                          int childCount,
        //                                          int childIndex,
        //                                          int itemSpanSize,
        //                                          int spanIndex) {
        //        int spanCount = getSpanCount(parent);
        //        if (getOrientation(parent) == VERTICAL) {
        //            return isLastItemEdgeValid((
        //                    childIndex
        //                            >= childCount - spanCount), parent, childCount, childIndex, spanIndex
        //            );
        //        } else {
        //            return (spanIndex + itemSpanSize) == spanCount;
        //        }
        //    }
        protected fun isLastItemEdgeValid(
            isOneOfLastItems: Boolean,
            parent: RecyclerView,
            childCount: Int,
            childIndex: Int, isRow: Boolean
        ): Boolean {
            var totalSpanRemaining = 0
            if (isOneOfLastItems) {
                for (i in childIndex until childCount) {
                    totalSpanRemaining = totalSpanRemaining + getItemSpanSize(parent, i, isRow)
                }
            }
            val spanIndex = getItemSpanIndex(parent, childIndex, isRow)
            return isOneOfLastItems && totalSpanRemaining <= getSpanCount(parent) - spanIndex
        }

        /**
         * 获取RecyclerView布局方向
         *
         * @param parent
         * @return
         */
        protected fun getOrientation(parent: RecyclerView): Int {
            val layoutManager = parent.layoutManager
            if (layoutManager is GridLayoutManager) {
                return layoutManager.orientation
            } else if (layoutManager is LinearLayoutManager) {
                return layoutManager.orientation
            } else if (layoutManager is StaggeredGridLayoutManager) {
                return layoutManager.orientation
            }
            return com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu.VERTICAL
        }

        /**
         * 获取item跨的列数
         *
         * @param parent
         * @param childIndex
         * @return
         */
        protected fun getItemSpanSize(
            parent: RecyclerView,
            childIndex: Int,
            isRow: Boolean
        ): Int {
            val layoutManager = parent.layoutManager
            if (layoutManager is GridLayoutManager) {
                return layoutManager.spanSizeLookup.getSpanSize(childIndex)
            } else if (layoutManager is StaggeredGridLayoutManager) {
                //瀑布流是不规则的，对其区别处理
                return if (isRow) {
                    1
                } else {
                    val view = layoutManager.findViewByPosition(childIndex)
                    Objects.requireNonNull(view, "findViewByPosition for view is null")
                    val params = view!!.layoutParams as StaggeredGridLayoutManager.LayoutParams
                    if (params.isFullSpan) getSpanCount(parent) else 1
                }
            } else if (layoutManager is LinearLayoutManager) {
                return 1
            }
            return -1
        }

        /**
         * 获取item的第一个检索
         *
         * @param parent
         * @param childIndex
         * @return
         */
        protected fun getItemSpanIndex(
            parent: RecyclerView,
            childIndex: Int,
            isRow: Boolean
        ): Int {
            val spanCount = getSpanCount(parent)
            val layoutManager = parent.layoutManager
            if (layoutManager is GridLayoutManager) {
                return layoutManager.spanSizeLookup.getSpanIndex(childIndex, spanCount)
            } else if (layoutManager is StaggeredGridLayoutManager) {
                //瀑布流是不规则的，对其区别处理
                return if (isRow) {
                    childIndex % spanCount
                } else {
                    val view = layoutManager.findViewByPosition(childIndex)
                    Objects.requireNonNull(view, "findViewByPosition for view is null")
                    val params = view!!.layoutParams as StaggeredGridLayoutManager.LayoutParams
                    // 列
                    params.spanIndex
                }
            } else if (layoutManager is LinearLayoutManager) {
                return 0
            }
            return -1
        }

        /**
         * 获取LayoutManager的span
         *
         * @param parent
         * @return
         */
        fun getSpanCount(parent: RecyclerView): Int {
            var spanCount = -1
            val layoutManager = parent.layoutManager
            if (layoutManager is GridLayoutManager) {
                spanCount = layoutManager.spanCount
            } else if (layoutManager is StaggeredGridLayoutManager) {
                spanCount = layoutManager.spanCount
            } else if (layoutManager is LinearLayoutManager) {
                spanCount = 1
            }
            return spanCount
        }

        /**
         * 获取当前RecyclerView分割线类型
         *
         * @param parent
         * @return
         */
        fun getDividerType(parent: RecyclerView): DividerType {
            val layoutManager = parent.layoutManager
            return if (layoutManager is GridLayoutManager) {
                val orientation = layoutManager.orientation
                if (orientation == GridLayoutManager.VERTICAL) DividerType.GRID_VERTICAL else DividerType.GRID_HORIZONTAL
            } else if (layoutManager is LinearLayoutManager) {
                val orientation = layoutManager.orientation
                if (orientation == LinearLayoutManager.VERTICAL) DividerType.LINEAR_VERTICAL else DividerType.LINEAR_HORIZONTAL
            } else if (layoutManager is StaggeredGridLayoutManager) {
                val orientation = layoutManager.orientation
                if (orientation == StaggeredGridLayoutManager.VERTICAL) DividerType.STAGGERED_GRID_VERTICAL else DividerType.STAGGERED_GRID_HORIZONTAL
            } else {
                DividerType.UNKNOWN
            }
        }

        /**
         * 检测数组是否存在某个position
         *
         * @param list
         * @param position
         * @return
         */
        fun isContains(list: IntArray, position: Int): Boolean {
            for (j in list) {
                if (position == j) {
                    return true
                }
            }
            return false
        }

        /**
         * 获取分割线装饰器
         *
         * @param builder
         * @param itemPosition
         * @return
         */
        fun getDecoration(builder: LinearBuilder, itemPosition: Int): Decoration? {
            return if (builder.itemDividerDecoration != null && builder.itemDividerDecoration!!
                    .getItemDividerDecoration(itemPosition) != null
            ) {
                builder.itemDividerDecoration!!.getItemDividerDecoration(itemPosition)
            } else null
        }

        /**
         * 获取Decoration的左内边距
         *
         * @param decoration
         * @return
         */
        fun getDecorationLeftPadding(decoration: Decoration?): Int {
            return if (decoration == null) 0 else decoration.leftPadding
        }

        /**
         * 获取Decoration的右内边距
         *
         * @param decoration
         * @return
         */
        fun getDecorationRightPadding(decoration: Decoration?): Int {
            return if (decoration == null) 0 else decoration.rightPadding
        }

        /**
         * 获取Decoration的顶部内边距
         *
         * @param decoration
         * @return
         */
        fun getDecorationTopPadding(decoration: Decoration?): Int {
            return if (decoration == null) 0 else decoration.topPadding
        }

        /**
         * 获取Decoration的底部内边距
         *
         * @param decoration
         * @return
         */
        fun getDecorationBottomPadding(decoration: Decoration?): Int {
            return if (decoration == null) 0 else decoration.bottomPadding
        }

        /**
         * 是否绘制边界的颜色
         *
         * @param decoration
         * @param edge
         * @return
         */
        fun isDrawEdge(decoration: Decoration?, @Edge edge: Int): Boolean {
            if (decoration == null) return false
            val aroundEdge: Array<Boolean> = decoration.getAroundEdge()
            return aroundEdge[edge]
        }

        /**
         * dp转px
         */
        fun dp2px(dpValue: Float): Int {
            val scale = Resources.getSystem().displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * px转dp
         */
        fun px2dp(pxValue: Float): Int {
            val scale = Resources.getSystem().displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }

        /**
         * sp转px
         */
        fun sp2px(spValue: Float): Int {
            val fontScale = Resources.getSystem().displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }

        /**
         * px转sp
         */
        fun px2sp(pxValue: Float): Int {
            val fontScale = Resources.getSystem().displayMetrics.scaledDensity
            return (pxValue / fontScale + 0.5f).toInt()
        }

        /**
         * 不同单位的值转px
         *
         * @param value
         * @param unit
         * @return
         */
        fun applyDimension(value: Float, unit: Int): Float {
            val metrics = Resources.getSystem().displayMetrics
            when (unit) {
                TypedValue.COMPLEX_UNIT_PX -> return value
                TypedValue.COMPLEX_UNIT_DIP -> return value * metrics.density
                TypedValue.COMPLEX_UNIT_SP -> return value * metrics.scaledDensity
                TypedValue.COMPLEX_UNIT_PT -> return value * metrics.xdpi * (1.0f / 72)
                TypedValue.COMPLEX_UNIT_IN -> return value * metrics.xdpi
                TypedValue.COMPLEX_UNIT_MM -> return value * metrics.xdpi * (1.0f / 25.4f)
            }
            return 0f
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}

