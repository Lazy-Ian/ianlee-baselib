package com.ianlee.lazy.base.lib.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ianlee.lazy.base.lib.R
import com.ianlee.lazy.base.lib.network.utils.dp2px
import com.ianlee.lazy.base.lib.network.utils.getColorId
import com.ianlee.lazy.base.lib.utils.TypefaceUtils
import com.ianlee.lazy.base.lib.utils.TypefaceUtils.GOTHAM_BOLD

/**
 * Created by Ian on 2024/3/20
 * Email: yixin0212@qq.com
 * Function :
 */

class TypefaceTextview @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {
    private var isClip = 0
    private var StrokeWidth = 2
    private var clipColor = 0
    private var cornerRadius = 20f
    private var mStartColor = 0
    private var mEndColor = 0
    private var mult = 1.0f //设置行间距

    /**
     * 是线条还是填充满背景
     */
    private var isFill = false
    private var typefaceName = 4
    private var isBold = false
    public override fun onDraw(canvas: Canvas) {
        onDrawClip(canvas)
        super.onDraw(canvas)
    }

    private fun onDrawClip(canvas: Canvas) {
        var rx = 0f
        var ry = 0f
        when (isClip) {
            0 -> {
                ry = 20f
                rx = ry
            }

            1 -> {
                rx = (measuredHeight / 2).toFloat()
                ry = (measuredHeight / 2).toFloat()
            }
        }
        if (cornerRadius > 0) {
            ry = cornerRadius
            rx = ry
        }
        if (rx != 0f && ry != 0f) {
            val p = Paint()
            p.color = clipColor // 设置红色
            //渐变
            if (mStartColor != 0 && mEndColor != 0) {
                val shader = LinearGradient(
                    0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(),
                    mStartColor, mEndColor, Shader.TileMode.REPEAT
                )
                p.shader = shader // 设置红色
            }
            if (!isFill) p.style = Paint.Style.STROKE //充满
            else p.style = Paint.Style.FILL //充满
            p.isAntiAlias = true // 设置画笔的锯齿效果
            p.strokeWidth = StrokeWidth.toFloat()
            val oval3 = RectF(
                (StrokeWidth / 2).toFloat(),
                (StrokeWidth / 2).toFloat(),
                (measuredWidth - StrokeWidth / 2).toFloat(),
                (measuredHeight - StrokeWidth / 2).toFloat()
            ) // 设置个新的长方形
            canvas.drawRoundRect(oval3, rx, ry, p) //第二个参数是x半径，第三个参数是y半径
        }
    }

    private fun initTypedArray(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextview)
        typefaceName =
            typedArray.getInt(R.styleable.TypefaceTextview_typefaceName, GOTHAM_BOLD)
        mult = typedArray.getFloat(R.styleable.TypefaceTextview_textViewLineSpacing, mult)
        //设置行间距
        setLineSpacing(dp2px( 3f), mult)
        isClip = typedArray.getInt(R.styleable.TypefaceTextview_clipType, -1)
        isFill = typedArray.getBoolean(R.styleable.TypefaceTextview_isFill, false)
        cornerRadius = typedArray.getDimensionPixelOffset(
            R.styleable.TypefaceTextview_textViewCornerRadius,
            0
        ).toFloat()
        StrokeWidth = typedArray.getDimensionPixelOffset(
            R.styleable.TypefaceTextview_textViewStrokeWidth,
            2
        )
        clipColor = typedArray.getColor(
            R.styleable.TypefaceTextview_clipColor,
            getColorId(context,R.color.app_main_color)
        )
        mStartColor = typedArray.getColor(R.styleable.TypefaceTextview_textViewStartColor, 0)
        mEndColor = typedArray.getColor(R.styleable.TypefaceTextview_textViewEndColor, 0)
        isBold = typedArray.getBoolean(R.styleable.TypefaceTextview_textViewIsBold, false)
        val typeface: Typeface? = TypefaceUtils.getTypeface(context, typefaceName)
        typedArray.recycle()
        if (typeface != null) {
            setTypeface(typeface)
        }
        //新隐藏掉字体
    }

    fun setBold(isBold: Boolean) {
        typefaceName = if (isBold) {
            2
        } else {
            1
        }
        val typeface: Typeface? = TypefaceUtils.getTypeface(context, typefaceName)
        if (typeface != null) {
            setTypeface(typeface)
        }
        postInvalidate()
    }

    /**
     * 1常规 2加粗
     *
     * @param type
     */
    fun setTitleType(type: Int) {
        val typeface: Typeface? = TypefaceUtils.getTypeface(context, type)
        if (typeface != null) {
            setTypeface(typeface)
        }
    }

    /**
     * 设置不同字体大小的方法
     *
     * @param highlightVaule 前面字体大小值
     * @param highlightStyle 字体大小
     * @param value          总值
     * @param valueStyle     剩余的字体大小
     */
    fun setSpanText(
        highlightVaule: String,
        highlightStyle: Int,
        value: String,
        valueStyle: Int
    ) {
        val ss = SpannableString(value)
        ss.setSpan(
            TextAppearanceSpan(context, highlightStyle), 0, highlightVaule.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ss.setSpan(
            TextAppearanceSpan(context, valueStyle), highlightVaule.length,
            value.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = ss
    }

    /**
     *
     */
    var drawableRightClick: DrawableRightClickListener? = null

    init {
        initTypedArray(attrs)
        //        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    //为了方便,直接写了一个内部类的接口
    interface DrawableRightClickListener {
        fun onDrawableRightClickListener(view: View?)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (drawableRightClick != null) {
                // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
                val rightDrawable = compoundDrawables[2]
                //判断的依据是获取点击区域相对于屏幕的x值比我(获取TextView的最右边界减去边界宽度)大就可以判断点击在Drawable上
                if (rightDrawable != null && event.rawX >= right - rightDrawable.bounds.width()) {
                    drawableRightClick!!.onDrawableRightClickListener(this)
                }
                //此处不能设置成false,否则drawable不会触发点击事件,如果设置,TextView会处理事件
                return false
            }
        }
        return super.onTouchEvent(event)
    }

    fun setClipColor(clipColor: Int) {
        this.clipColor = clipColor
        invalidate()
    }
}
