package com.ianlee.lazy.base.lib.view.recyclerview.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu
import com.ianlee.lazy.base.lib.view.recyclerview.decoration.DividerHelper.Companion.getDividerType
import java.util.*


/**
 * Created by Ian on 2021/7/21
 * Email: yixin0212@qq.com
 * Function :
 */
class DividerDecoration private constructor() : ItemDecoration() {
    private var mBuilder: Builder? = null
    private var mFullSpanPosition = -1

    /**
     * 分割线构造器绑定
     *
     * @param builder
     * @return
     */
    private fun bind(builder: Builder): DividerDecoration {
        mBuilder = builder
        return this
    }

    override fun onDraw(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val type: DividerHelper.DividerType = getDividerType(parent)
        when (type) {
            DividerHelper.DividerType.LINEAR_HORIZONTAL -> if (mBuilder is LinearBuilder) {
                drawVDividerForHLinear(c, parent, mBuilder as LinearBuilder?)
            }

            DividerHelper.DividerType.LINEAR_VERTICAL -> if (mBuilder is LinearBuilder) {
                drawDividerForVLinear(c, parent, mBuilder as LinearBuilder?)
            }

            DividerHelper.DividerType.GRID_VERTICAL -> if (mBuilder is GridBuilder) {
                drawDividerForVGrid(c, parent, mBuilder as GridBuilder?)
            }

            DividerHelper.DividerType.GRID_HORIZONTAL -> if (mBuilder is GridBuilder) {
                drawDividerForHGrid(c, parent, mBuilder as GridBuilder?)
            }

            DividerHelper.DividerType.STAGGERED_GRID_HORIZONTAL -> {
            }

            DividerHelper.DividerType.STAGGERED_GRID_VERTICAL -> {
            }

            DividerHelper.DividerType.UNKNOWN -> super.onDraw(c, parent, state)
            else -> super.onDraw(c, parent, state)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val type: DividerHelper.DividerType = getDividerType(parent)
        when (type) {
            DividerHelper.DividerType.LINEAR_HORIZONTAL, DividerHelper.DividerType.LINEAR_VERTICAL -> getItemOffsetsForLinear(
                outRect,
                view,
                parent,
                type
            )

            DividerHelper.DividerType.GRID_VERTICAL, DividerHelper.DividerType.GRID_HORIZONTAL -> if (mBuilder is GridBuilder) {
                getItemOffsetsForGrid(outRect, view, parent, mBuilder as GridBuilder?)
            }

            DividerHelper.DividerType.STAGGERED_GRID_VERTICAL, DividerHelper.DividerType.STAGGERED_GRID_HORIZONTAL -> if (mBuilder is StaggeredGridBuilder) {
                getItemOffsetsForStaggeredGrid(
                    outRect,
                    view,
                    parent,
                    mBuilder as StaggeredGridBuilder?
                )
            }

            DividerHelper.DividerType.UNKNOWN -> super.getItemOffsets(outRect, view, parent, state)
            else -> super.getItemOffsets(outRect, view, parent, state)
        }
    }

    /**
     * 绘制水平方向分割线（对应LayoutManage的竖直方法）
     *
     * @param c
     * @param parent
     * @param builder
     */
    protected fun drawDividerForVLinear(
        c: Canvas,
        parent: RecyclerView,
        builder: LinearBuilder?
    ) {
        c.save()
        Objects.requireNonNull(parent.adapter, "RecyclerView请设置Adapter")
        Objects.requireNonNull<Any>(builder, "LinearLayoutManager分割线必须设置LinearBuilder")
        val count =
            if (builder!!.isShowLastLine) parent.adapter!!.itemCount else parent.adapter!!
                .itemCount - 1
        val parentLPadding = if (builder.isIncludeParentLTPadding) 0 else parent.paddingLeft
        val parentRPadding = if (builder.isIncludeParentRBPadding) 0 else parent.paddingRight
        val childCount = parent.childCount
        var drawable: Drawable? = null
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            //这个才是真正的layout position
            val itemPosition = params.viewLayoutPosition
            val decoration: Decoration? = DividerHelper.getDecoration(builder, itemPosition)
            if (decoration != null) {
                drawable = decoration.dividerDrawable
            } else {
                drawable = builder.dividerDrawable
            }
            val decorationLeftPadding: Int = DividerHelper.getDecorationLeftPadding(decoration)
            val decorationRightPadding: Int =
                DividerHelper.getDecorationRightPadding(decoration)
            val leftPadding =
                if (decorationLeftPadding == 0) builder.leftPadding else decorationLeftPadding
            val rightPadding =
                if (decorationRightPadding == 0) builder.rightPadding else decorationRightPadding
            val left = parentLPadding + leftPadding
            val right = parent.width - parentRPadding - rightPadding
            val top = child.bottom + params.bottomMargin
            val bottom: Int = top + builder.spacing
            val isDrawLeft: Boolean =
                DividerHelper.isDrawEdge(decoration, DividerHelper.Edge.LEFT) == true
            val isDrawRight: Boolean =
                DividerHelper.isDrawEdge(decoration, DividerHelper.Edge.RIGHT) == true
            if (isDrawLeft != null && isDrawLeft) {
                val edgTop = child.top + params.topMargin
                val edgBottom = child.bottom + params.bottomMargin
                val edgRight: Int = left + builder.spacing
                drawable!!.setBounds(left, edgTop, edgRight, edgBottom)
                drawable.draw(c)
            }
            if (isDrawRight != null && isDrawRight) {
                val edgTop = child.top + params.topMargin
                val edgBottom = child.bottom + params.bottomMargin
                val edgLeft: Int = right - builder.spacing
                drawable!!.setBounds(edgLeft, edgTop, right, edgBottom)
                drawable.draw(c)
            }
            if (i == 0 && builder.isShowFirstTopLine) {
                val edgBottom = child.top - params.topMargin
                val edgTop: Int = edgBottom - builder.spacing
                drawable!!.setBounds(left, edgTop, right, edgBottom)
                drawable.draw(c)
            }
            if (i < count) {
                drawable!!.setBounds(left, top, right, bottom)
            } else {
                drawable!!.setBounds(left, top, right, top)
            }
            drawable.draw(c)
        }
        c.restore()
    }

    /**
     * 绘制竖直方向分割线（对应LayoutManage的水平方法）
     *
     * @param c
     * @param parent
     * @param builder
     */
    protected fun drawVDividerForHLinear(
        c: Canvas,
        parent: RecyclerView,
        builder: LinearBuilder?
    ) {
        c.save()
        Objects.requireNonNull(parent.adapter, "RecyclerView请设置Adapter")
        Objects.requireNonNull<Any>(builder, "LinearLayoutManager分割线必须设置LinearBuilder")
        val count =
            if (builder!!.isShowLastLine) parent.adapter!!.itemCount else parent.adapter!!
                .itemCount - 1
        val parentTPadding = if (builder.isIncludeParentLTPadding) 0 else parent.paddingTop
        val parentBPadding = if (builder.isIncludeParentRBPadding) 0 else parent.paddingBottom
        val childCount = parent.childCount
        var drawable: Drawable? = null
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            //这个才是真正的layout position
            val itemPosition = params.viewLayoutPosition
            val decoration: Decoration? = DividerHelper.getDecoration(builder, itemPosition)
            if (decoration != null) {
                drawable = decoration.dividerDrawable
            } else {
                drawable = builder.dividerDrawable
            }
            val decorationTopPadding: Int = DividerHelper.getDecorationTopPadding(decoration)
            val decorationBottomPadding: Int =
                DividerHelper.getDecorationBottomPadding(decoration)
            val topPadding =
                if (decorationTopPadding == 0) builder.leftPadding else decorationTopPadding
            val bottomPadding =
                if (decorationBottomPadding == 0) builder.rightPadding else decorationBottomPadding
            val top = parentTPadding + topPadding
            val bottom = parent.height - parentBPadding - bottomPadding
            val left = child.right + params.rightMargin
            val right: Int = left + builder.spacing
            val isDrawTop: Boolean =
                DividerHelper.isDrawEdge(decoration, DividerHelper.Edge.TOP) == true
            val isDrawBottom: Boolean =
                DividerHelper.isDrawEdge(decoration, DividerHelper.Edge.BOTTOM) == true
            if (isDrawTop != null && isDrawTop) {
                val edgLeft = child.left + params.leftMargin
                val edgTop: Int = child.top - params.topMargin - builder.spacing
                val edgRight = child.right + params.rightMargin
                val edgBottom: Int = edgTop + builder.spacing
                drawable.setBounds(edgLeft, edgTop, edgRight, edgBottom)
                drawable.draw(c)
            }
            if (isDrawBottom != null && isDrawBottom) {
                val edgTop = child.bottom + params.bottomMargin
                val edgBottom: Int = edgTop + builder.spacing
                val edgLeft = child.left - params.leftMargin
                val edgRight = child.right + params.rightMargin
                drawable.setBounds(edgLeft, edgTop, edgRight, edgBottom)
                drawable.draw(c)
            }
            if (i == 0 && builder.isShowFirstTopLine) {
                val edgRight = child.left - params.leftMargin
                val edgLeft: Int = edgRight - builder.spacing
                drawable.setBounds(edgLeft, top, edgRight, bottom)
                drawable.draw(c)
            }
            if (i < count) {
                drawable.setBounds(left, top, right, bottom)
                drawable.draw(c)
            } else {
                drawable.setBounds(left, top, left, bottom)
            }
        }
        c.restore()
    }

    /**
     * 绘制LinearLayoutManger的分割线
     *
     * @param outRect
     * @param view
     * @param parent
     * @param type
     */
    protected fun getItemOffsetsForLinear(
        outRect: Rect,
        view: View?,
        parent: RecyclerView,
        type: DividerHelper.DividerType
    ) {
        if (mBuilder is LinearBuilder) {
            val builder: LinearBuilder? = mBuilder as LinearBuilder?
            Objects.requireNonNull(parent.adapter, "RecyclerView请设置Adapter")
            //最后分割线
            val count = parent.adapter!!.itemCount
            val itemPosition = parent.getChildAdapterPosition(view!!)
            if (builder!!.onItemNoDivider != null && builder.onItemNoDivider!!
                    .noDividerPosition != null
            ) {
                val noDivider: IntArray = builder.onItemNoDivider!!.noDividerPosition!!
                if (DividerHelper.isContains(noDivider, itemPosition)) {
                    outRect[0, 0, 0] = 0
                    return
                }
            }
            val decoration: Decoration? = DividerHelper.getDecoration(builder, itemPosition)
            val dividerSpace: Int = builder.spacing
            if (type === DividerHelper.DividerType.LINEAR_VERTICAL) {
                var top = 0
                var bottom = dividerSpace
                val isDrawLeft: Boolean =
                    DividerHelper.isDrawEdge(decoration, DividerHelper.Edge.LEFT) == true
                val isDrawRight: Boolean =
                    DividerHelper.isDrawEdge(decoration, DividerHelper.Edge.RIGHT) == true
                val left = if (isDrawLeft == null) 0 else dividerSpace
                val right = if (isDrawRight == null) 0 else dividerSpace
                //第一条顶部分割线
                if (builder.isShowFirstTopLine && itemPosition == 0) {
                    top = dividerSpace
                }
                //最后分割线
                if (!builder.isShowLastLine && itemPosition == count - 1) {
                    bottom = 0
                }
                if (itemPosition < count) {
                    outRect[left, top, right] = bottom
                }
            } else {
                var left = 0
                var right = dividerSpace
                val isDrawTop: Boolean =
                    DividerHelper.isDrawEdge(decoration, DividerHelper.Edge.TOP) == true
                val isDrawBottom: Boolean =
                    DividerHelper.isDrawEdge(decoration, DividerHelper.Edge.BOTTOM) == true
                val top = if (isDrawTop == null) 0 else dividerSpace
                val bottom = if (isDrawBottom == null) 0 else dividerSpace

                //第一条顶部分割线
                if (builder.isShowFirstTopLine && itemPosition == 0) {
                    left = dividerSpace
                }
                //最后分割线
                if (!builder.isShowLastLine && itemPosition == count - 1) {
                    right = 0
                }
                if (itemPosition < count) {
                    outRect[left, top, right] = bottom
                }
            }
        }
    }

    /**
     * 为Grid绘制有Drawable的线(GridLayoutManage方向为竖直V方向)
     * getItemOffsets方法限制了item的偏移量
     */
    protected fun drawDividerForVGrid(c: Canvas, parent: RecyclerView, builder: GridBuilder?) {
        c.save()
        drawHDividerForVGrid(c, parent, builder)
        drawVDividerForVGrid(c, parent, builder)
        c.restore()
    }

    /**
     * 为Grid画水平方向的分割线（GridLinearLayoutManage的布局方向是竖直）
     *
     * @param c
     * @param parent
     * @param builder
     */
    private fun drawHDividerForVGrid(c: Canvas, parent: RecyclerView, builder: GridBuilder?) {
        Objects.requireNonNull(parent.adapter, "RecyclerView请设置Adapter")
        Objects.requireNonNull<Any>(builder, "GridLinearLayoutManage分割线必须设置GridBuilder")
        if (builder!!.hLineDividerDrawable == null) return
        val hLineSpacing: Int =
            if (builder.hLineSpacing === 0) builder!!.spacing else builder.hLineSpacing
        val vLineSpacing: Int =
            if (builder.vLineSpacing === 0) builder!!.spacing else builder.vLineSpacing
        val childCount = parent.childCount
        //        int spanCount = getSpanCount(parent);
        val gridLayoutManager = parent.layoutManager as GridLayoutManager?
        Objects.requireNonNull(
            gridLayoutManager,
            "RecyclerView LayoutManager请设置GridLayoutManager"
        )
        val spanSizeLookup = gridLayoutManager!!.spanSizeLookup
        val spanCount = gridLayoutManager.spanCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            // 当前i的itemSpanSize
            val itemSpanSize = spanSizeLookup.getSpanSize(i)
            // spanIndex = 0 表示是最左边(当前item起始的index)
            val spanIndex = spanSizeLookup.getSpanIndex(i, spanCount)
            // 行
            val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(i, spanCount)
            var left = child.left - params.leftMargin
            val top = child.bottom + params.bottomMargin
            var right = child.right + params.rightMargin
            val bottom = top + hLineSpacing
            val edgeTop = child.top - params.topMargin - hLineSpacing
            val edgeBottom = child.top - params.topMargin
            if (builder.isIncludeEdge) {
                //竖直方向分割线不包括角边
                if (!builder.isVerticalIncludeEdge) {
//                    if (spanIndex == 0) {
//                        left = left - vLineSpacing;
//                    }
                    //会重复绘制
                    left = left - vLineSpacing
                    right = right + vLineSpacing
                }
                //第一行
                if (spanGroupIndex == 0) {
                    builder.hLineDividerDrawable!!
                        .setBounds(left, edgeTop, right, edgeBottom)
                    builder.hLineDividerDrawable!!.draw(c)
                }
                //如果item跨span
                if (spanGroupIndex != 0 && itemSpanSize > 1) {
                    builder.hLineDividerDrawable!!
                        .setBounds(left, edgeTop, right, edgeBottom)
                    builder.hLineDividerDrawable!!.draw(c)
                }
                builder.hLineDividerDrawable!!.setBounds(left, top, right, bottom)
                builder.hLineDividerDrawable!!.draw(c)
            } else {
                //竖直方向分割线不包括角边
                if (!builder.isVerticalIncludeEdge) {
//                    if (spanIndex == 0) {
//                        left = left - vLineSpacing;
//                    }
                    //会重复绘制
                    left -= vLineSpacing
                    right += vLineSpacing
                }
                //如果item跨span
                if (spanGroupIndex != 0 && itemSpanSize > 1) {
                    builder.hLineDividerDrawable!!
                        .setBounds(left, edgeTop, right, edgeBottom)
                    builder.hLineDividerDrawable!!.draw(c)
                }
                //                //最后一行不绘制分割线
//                if (!DividerHelper.isLastRow(parent, childCount, i, itemSpanSize, spanIndex)) {
//                    builder.hLineDividerDrawable!!.setBounds(left, top, right, bottom);
//                    builder.hLineDividerDrawable!!.draw(c);
//                }
                builder.hLineDividerDrawable!!.setBounds(left, top, right, bottom)
                builder.hLineDividerDrawable!!.draw(c)
            }
        }
    }

    /**
     * 为Grid画竖直方向的分割线（GridLinearLayoutManage的布局方向是竖直）
     *
     * @param c
     * @param parent
     * @param builder
     */
    private fun drawVDividerForVGrid(c: Canvas, parent: RecyclerView, builder: GridBuilder?) {
        Objects.requireNonNull(parent.adapter, "RecyclerView请设置Adapter")
        if (builder!!.vLineDividerDrawable == null) return
        val hLineSpacing: Int =
            if (builder.hLineSpacing === 0) builder!!.spacing else builder.hLineSpacing
        val vLineSpacing: Int =
            if (builder.vLineSpacing === 0) builder!!.spacing else builder.vLineSpacing
        val childCount = parent.childCount
        val gridLayoutManager = parent.layoutManager as GridLayoutManager?
        Objects.requireNonNull(
            gridLayoutManager,
            "RecyclerView LayoutManager请设置GridLayoutManager"
        )
        val spanSizeLookup = gridLayoutManager!!.spanSizeLookup
        val spanCount = gridLayoutManager.spanCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            // 当前item的spanSize
            val spanSize = spanSizeLookup.getSpanSize(i)
            // spanIndex = 0 表示是最左边(当前item起始的index)
            val spanIndex = spanSizeLookup.getSpanIndex(i, spanCount)
            val left = child.right + params.rightMargin
            var top = child.top - params.topMargin
            val right = left + vLineSpacing
            var bottom = child.bottom + params.bottomMargin
            if (builder.isIncludeEdge) {
                val edgeLeft = child.left - params.leftMargin - vLineSpacing
                val edgeRight = child.left - params.leftMargin
                if (builder.isVerticalIncludeEdge) {
                    top = top - hLineSpacing
                    bottom = bottom + hLineSpacing
                }
                //第一列
                if (spanIndex == 0) {
                    builder.vLineDividerDrawable!!
                        .setBounds(edgeLeft, top, edgeRight, bottom)
                    builder.vLineDividerDrawable!!.draw(c)
                }
            } else {
                if (builder.isVerticalIncludeEdge) {
                    bottom = bottom + hLineSpacing
                    //                    if (DividerHelper.isLastRow(parent, childCount, i, spanSize, spanIndex)) {
//                        bottom -= hLineSpacing;
//                        HLog.e("lastRaw：" + i);
//                    }
                }
            }
            builder.vLineDividerDrawable!!.setBounds(left, top, right, bottom)
            builder.vLineDividerDrawable!!.draw(c)
        }
    }

    /**
     * 为分割线设置item偏移量
     *
     * @param outRect
     * @param view
     * @param parent
     * @param builder
     */
    fun getItemOffsetsForGrid(
        outRect: Rect,
        view: View?,
        parent: RecyclerView,
        builder: GridBuilder?
    ) {
        Objects.requireNonNull<Any>(builder, " GridLayoutManager分割线必须设置GridBuilder")
        val hLineSpacing: Int =
            if (builder!!.hLineSpacing === 0) builder!!.spacing else builder!!.hLineSpacing
        val vLineSpacing: Int =
            if (builder!!.vLineSpacing === 0) builder.spacing else builder.vLineSpacing
        val lastPosition =
            Objects.requireNonNull(parent.adapter, "RecyclerView请设置Adapter").itemCount - 1
        val position = parent.getChildAdapterPosition(view!!)
        if (position > lastPosition) return
        val layoutManager = parent.layoutManager as GridLayoutManager?
        Objects.requireNonNull(layoutManager, "RecyclerView LayoutManager请设置GridLayoutManager")
        //规则是第n列item的outRect.right + 第n+1列的outRect.left 等于 spacing，以此类推
        val spanSizeLookup = layoutManager!!.spanSizeLookup
        val spanCount = layoutManager.spanCount
        // 当前position的itemSpanSize
        val itemSpanSize = spanSizeLookup.getSpanSize(position)
        // 一行几个
        val rowSpanCount = spanCount / itemSpanSize
        // spanIndex = 0 表示是最左边,即第一列
        val spanIndex = spanSizeLookup.getSpanIndex(position, spanCount)
        // 列
        val column = spanIndex / itemSpanSize
        // 行 可在这控制底部Footer绘制
        val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount)
        val orientation = layoutManager.orientation
        if (builder.isIncludeEdge) {
            //包括边界：第一列的outRect.left = spacing，第column + 1 = realSpanCount列outRect.right = spacing
            if (orientation == com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu.VERTICAL) {
                outRect.left = vLineSpacing - column * vLineSpacing / rowSpanCount
                outRect.right = (column + 1) * vLineSpacing / rowSpanCount
            } else {
                outRect.top = hLineSpacing - column * hLineSpacing / rowSpanCount
                outRect.bottom = (column + 1) * hLineSpacing / rowSpanCount
            }
            //第一行才有间距
            if (spanGroupIndex < 1 && position < rowSpanCount) {
                if (orientation == com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu.VERTICAL) {
                    // 上间距
                    outRect.top = hLineSpacing
                } else {
                    // 左间距
                    outRect.left = vLineSpacing
                }
            }
            if (orientation == com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu.VERTICAL) {
                // 下边偏移量
                outRect.bottom = hLineSpacing
            } else {
                // 右边偏移量
                outRect.right = vLineSpacing
            }
        } else {
            if (orientation == com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu.VERTICAL) {
                outRect.left = column * vLineSpacing / rowSpanCount
                outRect.right = vLineSpacing - (column + 1) * vLineSpacing / rowSpanCount
            } else {
                outRect.top = column * hLineSpacing / rowSpanCount
                outRect.bottom = hLineSpacing - (column + 1) * hLineSpacing / rowSpanCount
            }
            if (spanGroupIndex >= 1) {
                if (orientation == com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu.VERTICAL) {
                    // 超过第0行都显示上间距
                    outRect.top = hLineSpacing
                } else {
                    // 超过第0列都显示左间距
                    outRect.left = vLineSpacing
                }
            }
        }
    }

    /**
     * 为Grid绘制有Drawable的线(GridLayoutManage方向为竖直H方向)
     */
    protected fun drawDividerForHGrid(c: Canvas, parent: RecyclerView, builder: GridBuilder?) {
        //行实际上是LayoutManager的列
        //列实际上是LayoutManager的行
        //layoutManage的方向是水平
        c.save()
        drawHDividerForHGrid(c, parent, builder)
        drawVDividerForHGrid(c, parent, builder)
        c.restore()
    }

    /**
     * 为Grid画水平方向的分割线（GridLinearLayoutManage的布局方向是水平）
     *
     * @param c
     * @param parent
     * @param builder
     */
    private fun drawHDividerForHGrid(c: Canvas, parent: RecyclerView, builder: GridBuilder?) {
        Objects.requireNonNull<Any>(builder, "GridLinearLayoutManage分割线必须设置GridBuilder")
        if (builder!!.hLineDividerDrawable == null) return
        val hLineSpacing: Int =
            if (builder.hLineSpacing === 0) builder!!.spacing else builder.hLineSpacing
        val vLineSpacing: Int =
            if (builder.vLineSpacing === 0) builder!!.spacing else builder.vLineSpacing
        val childCount = parent.childCount
        //        int spanCount = DividerHelper.getSpanCount(parent);
        val gridLayoutManager = parent.layoutManager as GridLayoutManager?
        Objects.requireNonNull(
            gridLayoutManager,
            "RecyclerView LayoutManager请设置GridLayoutManager"
        )
        val spanSizeLookup = gridLayoutManager!!.spanSizeLookup
        val spanCount = gridLayoutManager.spanCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            // 当前i的itemSpanSize
            val itemSpanSize = spanSizeLookup.getSpanSize(i)
            // spanIndex = 0 表示是最左边(当前item起始的index)
            val spanIndex = spanSizeLookup.getSpanIndex(i, spanCount)
            // 行
            val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(i, spanCount)
            var left = child.left - params.leftMargin
            val top = child.bottom + params.bottomMargin
            var right = child.right + params.rightMargin
            val bottom = top + hLineSpacing
            if (builder.isIncludeEdge) {
                val edgeTop = child.top - params.topMargin - hLineSpacing
                val edgeBottom = child.top - params.topMargin
                if (!builder.isVerticalIncludeEdge) {
//                    //第一列
//                    if (spanGroupIndex == 0) {
//                        left = left - vLineSpacing;
//
//                    }
                    //会重复绘制
                    left -= vLineSpacing
                    right += vLineSpacing
                }
                //第一行
                if (spanIndex == 0) {
                    builder.hLineDividerDrawable!!
                        .setBounds(left, edgeTop, right, edgeBottom)
                    builder.hLineDividerDrawable!!.draw(c)
                }
            } else {
                if (!builder.isVerticalIncludeEdge) {
                    //非最最后一列
                    if (!DividerHelper.isLastColumn(parent, childCount, i)) {
                        right += vLineSpacing
                    }
                }
            }
            builder.hLineDividerDrawable!!.setBounds(left, top, right, bottom)
            builder.hLineDividerDrawable!!.draw(c)
        }
    }

    /**
     * 为Grid画竖直方向的分割线（GridLinearLayoutManage的布局方向是水平）
     *
     * @param c
     * @param parent
     * @param builder
     */
    private fun drawVDividerForHGrid(c: Canvas, parent: RecyclerView, builder: GridBuilder?) {
        Objects.requireNonNull<Any>(builder, "GridLinearLayoutManage分割线必须设置GridBuilder")
        if (builder!!.vLineDividerDrawable == null) return
        val hLineSpacing: Int =
            if (builder.hLineSpacing === 0) builder.spacing else builder.hLineSpacing
        val vLineSpacing: Int =
            if (builder.vLineSpacing === 0) builder.spacing else builder.vLineSpacing
        val childCount = parent.childCount
        val gridLayoutManager = parent.layoutManager as GridLayoutManager?
        Objects.requireNonNull(
            gridLayoutManager,
            "RecyclerView LayoutManager请设置GridLayoutManager"
        )
        val spanSizeLookup = gridLayoutManager!!.spanSizeLookup
        val spanCount = gridLayoutManager.spanCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            // 当前i的itemSpanSize
            val itemSpanSize = spanSizeLookup.getSpanSize(i)
            // spanIndex = 0 表示是最左边(当前item起始的index)
            val spanIndex = spanSizeLookup.getSpanIndex(i, spanCount)
            // 行
            val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(i, spanCount)

            //绘制item右边界
            val left = child.right + params.rightMargin
            var top = child.top - params.topMargin
            val right = left + vLineSpacing
            var bottom = child.bottom + params.bottomMargin
            if (builder!!.isIncludeEdge) {
                if (builder.isVerticalIncludeEdge) {
                    //第一行
                    if (spanIndex == 0) {
                        top = top - hLineSpacing
                    }
                    bottom = bottom + hLineSpacing
                }
                //第一列
                if (spanGroupIndex == 0) {
                    val edgeLeft = child.left - params.leftMargin - vLineSpacing
                    val edgeRight = child.left - params.leftMargin
                    builder.vLineDividerDrawable!!
                        .setBounds(edgeLeft, top, edgeRight, bottom)
                    builder.vLineDividerDrawable!!.draw(c)
                }
                builder.vLineDividerDrawable!!.setBounds(left, top, right, bottom)
                builder.vLineDividerDrawable!!.draw(c)
            } else {
                if (builder.isVerticalIncludeEdge) {
                    bottom = bottom + hLineSpacing
                }
                //                //最后一行
//                if (spanIndex + itemSpanSize == spanCount) {
//                    HLog.e("ttt", i + "");
//                    bottom -= hLineSpacing;
//                }
                //非最最后一列
                if (!DividerHelper.isLastColumn(parent, childCount, i)) {
                    builder!!.vLineDividerDrawable!!.setBounds(left, top, right, bottom)
                    builder!!.vLineDividerDrawable!!.draw(c)
                }
            }
            //处理item跨多个span的情况(会重复绘制)
            if (spanGroupIndex != 0) {
                val edgeLeft = child.left - params.leftMargin - vLineSpacing
                val edgeRight = child.left - params.leftMargin
                builder.vLineDividerDrawable!!.setBounds(edgeLeft, top, edgeRight, bottom)
                builder.vLineDividerDrawable!!.draw(c)
            }
        }
    }

    /**
     * 为分割线设置item偏移量
     *
     * @param outRect
     * @param view
     * @param parent
     * @param builder
     */
    fun getItemOffsetsForStaggeredGrid(
        outRect: Rect, view: View,
        parent: RecyclerView,
        builder: StaggeredGridBuilder?
    ) {
        Objects.requireNonNull<Any>(
            builder,
            "GridLinearLayoutManage分割线必须设置StaggeredGridBuilder"
        )
        val hLineSpacing: Int =
            if (builder!!.hLineSpacing === 0) builder!!.spacing else builder!!.hLineSpacing
        val vLineSpacing: Int =
            if (builder!!.vLineSpacing === 0) builder!!.spacing else builder!!.vLineSpacing
        val lastPosition =
            Objects.requireNonNull(parent.adapter, "RecyclerView请设置Adapter").itemCount - 1
        val position = parent.getChildAdapterPosition(view)
        if (position > lastPosition) return
        val layoutManager = parent.layoutManager as StaggeredGridLayoutManager?
        Objects.requireNonNull(
            layoutManager,
            "RecyclerView LayoutManager请设置StaggeredGridLayoutManager"
        )
        // 瀑布流获取列方式不一样
        val params = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        // 瀑布流item处于那一列
        val column = params.spanIndex
        // 瀑布流是否占满一行
        val isFullSpan = params.isFullSpan
        var spanCount = layoutManager!!.spanCount
        //真正的spanCount
        spanCount /= if (isFullSpan) spanCount else 1
        val orientation = layoutManager.orientation
        if (builder!!.isIncludeEdge) {
            if (isFullSpan && !builder!!.isIgnoreFullSpan) {
                outRect.left = 0
                outRect.right = 0
            } else {
                if (orientation == com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu.VERTICAL) {
                    outRect.left = vLineSpacing - column * vLineSpacing / spanCount
                    outRect.right = (column + 1) * vLineSpacing / spanCount
                } else {
                    outRect.top = hLineSpacing - column * hLineSpacing / spanCount
                    outRect.bottom = (column + 1) * hLineSpacing / spanCount
                }
            }

            // 找到头部第一个整行的position，后面的上间距都不显示
            if (mFullSpanPosition == -1 && position < spanCount && isFullSpan) {
                mFullSpanPosition = position
            }
            //显示上间距: 头部没有整行||头部体验整行但是在之前的position
            val isFirstLineStagger =
                (mFullSpanPosition == -1 || position < mFullSpanPosition) && position < spanCount
            if (isFirstLineStagger) {
                if (orientation == com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu.VERTICAL) {
                    // 上间距
                    outRect.top = hLineSpacing
                } else {
                    // 左间距
                    outRect.left = vLineSpacing
                }
            }
            if (orientation == com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu.VERTICAL) {
                // 底部偏移量
                outRect.bottom = hLineSpacing
            } else {
                // 右边偏移量
                outRect.right = vLineSpacing
            }
        } else {
            if (isFullSpan && !builder.isIgnoreFullSpan) {
                outRect.left = 0
                outRect.right = 0
            } else {
                if (orientation == com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu.VERTICAL) {
                    outRect.left = column * vLineSpacing / spanCount
                    outRect.right = vLineSpacing - (column + 1) * vLineSpacing / spanCount
                } else {
                    outRect.top = column * hLineSpacing / spanCount
                    outRect.bottom = hLineSpacing - (column + 1) * hLineSpacing / spanCount
                }
            }

            // 找到头部第一个整行的position
            if (mFullSpanPosition == -1 && position < spanCount && isFullSpan) {
                mFullSpanPosition = position
            }
            // 上间距显示规则
            val isStaggerShowTop =
                position >= spanCount || isFullSpan && position != 0 || mFullSpanPosition != -1 && position != 0
            if (isStaggerShowTop) {
                if (orientation == com.ianlee.lazy.base.lib.view.recyclerview.SwipeMenu.VERTICAL) {
                    // 超过第0行都显示上间距
                    outRect.top = hLineSpacing
                } else {
                    // 超过第0列都显示左间距
                    outRect.left = vLineSpacing
                }
            }
        }
    }

    /**
     * 提供初始化DividerDecoration
     */
    open class Builder protected constructor(  //上下文
        protected var mContext: Context
    ) {
        /**
         * DividerDecoration初始化
         *
         * @return
         */
        fun build(): DividerDecoration {
            return DividerDecoration().bind(this)
        }
    }
}
