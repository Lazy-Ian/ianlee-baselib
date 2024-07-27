package com.ianlee.lazy.base.lib.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.ianlee.lazy.base.lib.R
import com.ianlee.lazy.base.lib.network.utils.dp2px
import com.ianlee.lazy.base.lib.view.background.drawable.DrawableCreator

/**
 * Created by Ian on 2024/7/10
 * Email: yixin0212@qq.com
 * Function :
 */
object DrawableUtil {


    fun getPressedSolidDrawable(
        context: Context,
        cornersRadius: Float = 2f,
        pressedSolidColor: Int = R.color.menu_color,
        unPressedSolidColor: Int = R.color.menu_color
    ): Drawable {
        return com.ianlee.lazy.base.lib.view.background.drawable.DrawableCreator.Builder()
//            .setShape(DrawableCreator.Shape.Oval)
            .setCornersRadius(cornersRadius)
            .setPressedSolidColor(
                ContextCompat.getColor(context, pressedSolidColor),
                ContextCompat.getColor(context, unPressedSolidColor)
            )
            .build()
    }

    fun getSolidColorDrawable(
        context: Context,
        cornersRadius: Float = 6f,
        solidColor: Int = R.color.menu_color,
    ): Drawable {
        return com.ianlee.lazy.base.lib.view.background.drawable.DrawableCreator.Builder()
            .setCornersRadius(cornersRadius)
            .setSolidColor(ContextCompat.getColor(context, solidColor))
            .build()
    }


    fun getSelectedStrokeAndSolidColorDrawable(
        context: Context,
        cornersRadius: Float = 6f,
        selectedSolidColor: Int = R.color.menu_color,
        unSelectedSolidColor: Int = R.color.menu_color,
        selectedStrokeColor: Int = R.color.menu_color,
        unSelectedStrokeColor: Int = R.color.menu_color,
        strokeWidth: Float = 1f
    ): Drawable {
        return com.ianlee.lazy.base.lib.view.background.drawable.DrawableCreator.Builder()
            .setCornersRadius(cornersRadius)
            .setSelectedSolidColor(
                ContextCompat.getColor(context, selectedSolidColor),
                ContextCompat.getColor(context, unSelectedSolidColor)
            )
            .setSelectedStrokeColor(
                ContextCompat.getColor(context, selectedStrokeColor),
                ContextCompat.getColor(context, unSelectedStrokeColor)
            )
            .setStrokeWidth(dp2px(strokeWidth))
            .build()
    }

    fun getSelectedStrokeAndSolidColorDrawable(
        context: Context,
        cornersRadius: Float = 6f,
        selectedSolidColor: String,
        unSelectedSolidColor: String,
        selectedStrokeColor: String,
        unSelectedStrokeColor: String,
        strokeWidth: Float = 1f
    ): Drawable {
        return com.ianlee.lazy.base.lib.view.background.drawable.DrawableCreator.Builder()
            .setCornersRadius(cornersRadius)
            .setStrokeWidth(dp2px(strokeWidth))
            .setSelectedSolidColor(
                Color.parseColor(selectedSolidColor), Color.parseColor(unSelectedSolidColor)
            )
            .setSelectedStrokeColor(
                Color.parseColor(selectedStrokeColor), Color.parseColor(unSelectedStrokeColor)
            )
            .build()
    }
}