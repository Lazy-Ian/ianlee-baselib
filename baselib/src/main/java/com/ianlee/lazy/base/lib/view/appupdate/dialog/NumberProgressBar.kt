package com.ianlee.lazy.base.lib.view.appupdate.dialog

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import com.ianlee.lazy.base.lib.R

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
class NumberProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private var mMaxProgress = 100

    /**
     * Current progress, can not exceed the max progress.
     */
    private var mCurrentProgress = 0

    /**
     * The progress area bar color.
     */
    private var mReachedBarColor: Int

    /**
     * The bar unreached area color.
     */
    private var mUnreachedBarColor: Int

    /**
     * The progress text color.
     */
    private var mTextColor: Int

    /**
     * The progress text size.
     */
    private var mTextSize: Float

    /**
     * The height of the reached area.
     */
    private var mReachedBarHeight: Float

    /**
     * The height of the unreached area.
     */
    private var mUnreachedBarHeight: Float

    /**
     * The suffix of the number.
     */
    private var mSuffix = "%"

    /**
     * The prefix.
     */
    private var mPrefix = ""
    private val default_text_color = Color.rgb(78, 89, 105)
    private val default_reached_color = Color.rgb(78, 89, 105)
    private val default_unreached_color = Color.rgb(238, 238, 238)
    private val default_progress_text_offset: Float
    private val default_text_size: Float

    /**
     * The width of the text that to be drawn.
     */
    private var mDrawTextWidth = 0f

    /**
     * The drawn text start.
     */
    private var mDrawTextStart = 0f

    /**
     * The drawn text end.
     */
    private var mDrawTextEnd = 0f

    /**
     * The text that to be drawn in onDraw().
     */
    private var mCurrentDrawText: String? = null

    /**
     * The Paint of the reached area.
     */
    private var mReachedBarPaint: Paint? = null

    /**
     * The Paint of the unreached area.
     */
    private var mUnreachedBarPaint: Paint? = null

    /**
     * The Paint of the progress text.
     */
    private var mTextPaint: Paint? = null

    /**
     * Unreached bar area to draw rect.
     */
    private val mUnreachedRectF = RectF(0.0F, 0.0F, 0.0F, 0.0F)

    /**
     * Reached bar area rect.
     */
    private val mReachedRectF = RectF(0.0F, 0.0F, 0.0F, 0.0F)

    /**
     * The progress text offset.
     */
    private val mOffset = 0f

    /**
     * Determine if need to draw unreached area.
     */
    private var mDrawUnreachedBar = true
    private var mDrawReachedBar = true
    private var mIfDrawText = true

    enum class ProgressTextVisibility {
        Visible, Invisible
    }

    override fun getSuggestedMinimumWidth(): Int {
        return mTextSize.toInt()
    }

    override fun getSuggestedMinimumHeight(): Int {
        return Math.max(
            mTextSize.toInt(), Math.max(
                mReachedBarHeight.toInt(),
                mUnreachedBarHeight.toInt()
            )
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false))
    }

    private fun measure(measureSpec: Int, isWidth: Boolean): Int {
        var result: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        val padding = if (isWidth) paddingLeft + paddingRight else paddingTop + paddingBottom
        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = if (isWidth) suggestedMinimumWidth else suggestedMinimumHeight
            result += padding
            if (mode == MeasureSpec.AT_MOST) {
                result = if (isWidth) {
                    Math.max(result, size)
                } else {
                    Math.min(result, size)
                }
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        if (mIfDrawText) {
            calculateDrawRectF()
        } else {
            calculateDrawRectFWithoutProgressText()
        }
        if (mDrawReachedBar) {
            canvas.drawRect(mReachedRectF, mReachedBarPaint!!)
        }
        if (mDrawUnreachedBar) {
            canvas.drawRect(mUnreachedRectF, mUnreachedBarPaint!!)
        }
        if (mIfDrawText) canvas.drawText(
            mCurrentDrawText!!, mDrawTextStart, mDrawTextEnd,
            mTextPaint!!
        )
    }

    private fun initializePainters() {
        mReachedBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mReachedBarPaint!!.color = mReachedBarColor
        mUnreachedBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mUnreachedBarPaint!!.color = mUnreachedBarColor
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.color = mTextColor
        mTextPaint!!.textSize = mTextSize
    }

    private fun calculateDrawRectFWithoutProgressText() {
        mReachedRectF.left = paddingLeft.toFloat()
        mReachedRectF.top = height / 2.0f - mReachedBarHeight / 2.0f
        mReachedRectF.right =
            (width - paddingLeft - paddingRight) / (getMax() * 1.0f) * getProgress() + paddingLeft
        mReachedRectF.bottom = height / 2.0f + mReachedBarHeight / 2.0f
        mUnreachedRectF.left = mReachedRectF.right
        mUnreachedRectF.right = (width - paddingRight).toFloat()
        mUnreachedRectF.top = height / 2.0f + -mUnreachedBarHeight / 2.0f
        mUnreachedRectF.bottom = height / 2.0f + mUnreachedBarHeight / 2.0f
    }

    private fun calculateDrawRectF() {
        mCurrentDrawText = String.format("%d", getProgress() * 100 / getMax())
        mCurrentDrawText = mPrefix + mCurrentDrawText + mSuffix
        mDrawTextWidth = mTextPaint!!.measureText(mCurrentDrawText)
        if (getProgress() == 0) {
            mDrawReachedBar = false
            mDrawTextStart = paddingLeft.toFloat()
        } else {
            mDrawReachedBar = true
            mReachedRectF.left = paddingLeft.toFloat()
            mReachedRectF.top = height / 2.0f - mReachedBarHeight / 2.0f
            mReachedRectF.right =
                (width - paddingLeft - paddingRight) / (getMax() * 1.0f) * getProgress() - mOffset + paddingLeft
            mReachedRectF.bottom = height / 2.0f + mReachedBarHeight / 2.0f
            mDrawTextStart = mReachedRectF.right + mOffset
        }
        mDrawTextEnd =
            (height / 2.0f - (mTextPaint!!.descent() + mTextPaint!!.ascent()) / 2.0f)
        if (mDrawTextStart + mDrawTextWidth >= width - paddingRight) {
            mDrawTextStart = width - paddingRight - mDrawTextWidth
            mReachedRectF.right = mDrawTextStart - mOffset
        }
        val unreachedBarStart = mDrawTextStart + mDrawTextWidth + mOffset
        if (unreachedBarStart >= width - paddingRight) {
            mDrawUnreachedBar = false
        } else {
            mDrawUnreachedBar = true
            mUnreachedRectF.left = unreachedBarStart
            mUnreachedRectF.right = (width - paddingRight).toFloat()
            mUnreachedRectF.top = height / 2.0f + -mUnreachedBarHeight / 2.0f
            mUnreachedRectF.bottom = height / 2.0f + mUnreachedBarHeight / 2.0f
        }
    }

    /**
     * Get progress text color.
     *
     * @return progress text color.
     */
    fun getTextColor(): Int {
        return mTextColor
    }

    /**
     * Get progress text size.
     *
     * @return progress text size.
     */
    fun getProgressTextSize(): Float {
        return mTextSize
    }

    fun getUnreachedBarColor(): Int {
        return mUnreachedBarColor
    }

    fun getReachedBarColor(): Int {
        return mReachedBarColor
    }

    fun getProgress(): Int {
        return mCurrentProgress
    }

    fun getMax(): Int {
        return mMaxProgress
    }

    fun getReachedBarHeight(): Float {
        return mReachedBarHeight
    }

    fun getUnreachedBarHeight(): Float {
        return mUnreachedBarHeight
    }

    fun setProgressTextSize(textSize: Float) {
        mTextSize = textSize
        mTextPaint!!.textSize = mTextSize
        invalidate()
    }

    fun setProgressTextColor(textColor: Int) {
        mTextColor = textColor
        mTextPaint!!.color = mTextColor
        invalidate()
    }

    fun setUnreachedBarColor(barColor: Int) {
        mUnreachedBarColor = barColor
        mUnreachedBarPaint!!.color = mUnreachedBarColor
        invalidate()
    }

    fun setReachedBarColor(progressColor: Int) {
        mReachedBarColor = progressColor
        mReachedBarPaint!!.color = mReachedBarColor
        invalidate()
    }

    fun setReachedBarHeight(height: Float) {
        mReachedBarHeight = height
    }

    fun setUnreachedBarHeight(height: Float) {
        mUnreachedBarHeight = height
    }

    fun setMax(maxProgress: Int) {
        if (maxProgress > 0) {
            mMaxProgress = maxProgress
            invalidate()
        }
    }

    fun setSuffix(suffix: String?) {
        mSuffix = suffix ?: ""
    }

    fun getSuffix(): String {
        return mSuffix
    }

    fun setPrefix(prefix: String?) {
        mPrefix = prefix ?: ""
    }

    fun getPrefix(): String {
        return mPrefix
    }

    fun incrementProgressBy(by: Int) {
        if (by > 0) {
            setProgress(getProgress() + by)
        }
    }

    fun setProgress(progress: Int) {
        if (progress <= getMax() && progress >= 0) {
            mCurrentProgress = progress
            invalidate()
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor())
        bundle.putFloat(INSTANCE_TEXT_SIZE, getProgressTextSize())
        bundle.putFloat(INSTANCE_REACHED_BAR_HEIGHT, getReachedBarHeight())
        bundle.putFloat(INSTANCE_UNREACHED_BAR_HEIGHT, getUnreachedBarHeight())
        bundle.putInt(INSTANCE_REACHED_BAR_COLOR, getReachedBarColor())
        bundle.putInt(INSTANCE_UNREACHED_BAR_COLOR, getUnreachedBarColor())
        bundle.putInt(INSTANCE_MAX, getMax())
        bundle.putInt(INSTANCE_PROGRESS, getProgress())
        bundle.putString(INSTANCE_SUFFIX, getSuffix())
        bundle.putString(INSTANCE_PREFIX, getPrefix())
        bundle.putBoolean(INSTANCE_TEXT_VISIBILITY, getProgressTextVisibility())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            val bundle = state
            mTextColor = bundle.getInt(INSTANCE_TEXT_COLOR)
            mTextSize = bundle.getFloat(INSTANCE_TEXT_SIZE)
            mReachedBarHeight = bundle.getFloat(INSTANCE_REACHED_BAR_HEIGHT)
            mUnreachedBarHeight = bundle.getFloat(INSTANCE_UNREACHED_BAR_HEIGHT)
            mReachedBarColor = bundle.getInt(INSTANCE_REACHED_BAR_COLOR)
            mUnreachedBarColor = bundle.getInt(INSTANCE_UNREACHED_BAR_COLOR)
            initializePainters()
            setMax(bundle.getInt(INSTANCE_MAX))
            setProgress(bundle.getInt(INSTANCE_PROGRESS))
            setPrefix(bundle.getString(INSTANCE_PREFIX))
            setSuffix(bundle.getString(INSTANCE_SUFFIX))
            setProgressTextVisibility(if (bundle.getBoolean(INSTANCE_TEXT_VISIBILITY)) ProgressTextVisibility.Visible else ProgressTextVisibility.Invisible)
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE))
            return
        }
        super.onRestoreInstanceState(state)
    }

    fun dp2px(dp: Float): Float {
        val scale = resources.displayMetrics.density
        return dp * scale + 0.5f
    }

    fun sp2px(sp: Float): Float {
        val scale = resources.displayMetrics.scaledDensity
        return sp * scale
    }

    fun setProgressTextVisibility(visibility: ProgressTextVisibility) {
        mIfDrawText = visibility == ProgressTextVisibility.Visible
        invalidate()
    }

    fun getProgressTextVisibility(): Boolean {
        return mIfDrawText
    }

    companion object {
        /**
         * For save and restore instance of progressbar.
         */
        private const val INSTANCE_STATE = "saved_instance"
        private const val INSTANCE_TEXT_COLOR = "text_color"
        private const val INSTANCE_TEXT_SIZE = "text_size"
        private const val INSTANCE_REACHED_BAR_HEIGHT = "reached_bar_height"
        private const val INSTANCE_REACHED_BAR_COLOR = "reached_bar_color"
        private const val INSTANCE_UNREACHED_BAR_HEIGHT = "unreached_bar_height"
        private const val INSTANCE_UNREACHED_BAR_COLOR = "unreached_bar_color"
        private const val INSTANCE_MAX = "max"
        private const val INSTANCE_PROGRESS = "progress"
        private const val INSTANCE_SUFFIX = "suffix"
        private const val INSTANCE_PREFIX = "prefix"
        private const val INSTANCE_TEXT_VISIBILITY = "text_visibility"
        private const val PROGRESS_TEXT_VISIBLE = 0
    }

    init {
        mReachedBarHeight = dp2px(30f)
        mUnreachedBarHeight = dp2px(30f)
        default_text_size = sp2px(20f)
        default_progress_text_offset = dp2px(4.0f)

        //load styled attributes.
        val attributes = context.theme.obtainStyledAttributes(
            attrs, R.styleable.NumberProgressBar,
            defStyleAttr, 0
        )
        mReachedBarColor = attributes.getColor(
            R.styleable.NumberProgressBar_progress_reached_color,
            default_reached_color
        )
        mUnreachedBarColor = attributes.getColor(
            R.styleable.NumberProgressBar_progress_unreached_color,
            default_unreached_color
        )
        mTextColor = attributes.getColor(
            R.styleable.NumberProgressBar_progress_text_color,
            default_text_color
        )
        mTextSize = attributes.getDimension(
            R.styleable.NumberProgressBar_progress_text_size,
            default_text_size
        )
        attributes.recycle()
        initializePainters()
    }
}
