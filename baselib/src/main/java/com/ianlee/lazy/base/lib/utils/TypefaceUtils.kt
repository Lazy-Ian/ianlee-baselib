package com.ianlee.lazy.base.lib.utils

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils

/**
 * Created by Ian on 2024/3/20
 * Email: yixin0212@qq.com
 * Function :
 */
object TypefaceUtils {
    private const val GOTHAM_BOOK_OTF = "ali55.ttf"
    private const val GOTHAM_MEDIUM_OTF = "ali85.ttf"
    private const val DIN_COND_BOLD_OTF = "ali115.ttf"

    private const val GOTHAM_BOLD_OTF = "ali85.ttf"
    const val GOTHAM_LIGHT = 1
    const val GOTHAM_MEDIUM = 2
    const val DIN_COND_BOLD = 3
    const val GOTHAM_BOLD = 4

    /**
     * 获取字体
     *
     * @param typefaceName
     * @return typeface对象
     */
    fun getTypeface(context: Context, typefaceName: Int): Typeface? {
        var path = ""
        when (typefaceName) {
            GOTHAM_LIGHT -> path = "font/" + GOTHAM_BOOK_OTF
            GOTHAM_MEDIUM -> path = "font/" + GOTHAM_MEDIUM_OTF
            DIN_COND_BOLD -> path = "font/" + DIN_COND_BOLD_OTF
            GOTHAM_BOLD -> path = "font/" + GOTHAM_BOLD_OTF
        }
        return try {
            if (!TextUtils.isEmpty(path)) Typeface.createFromAsset(
                context.assets,
                path
            ) else null
        } catch (e: Exception) {
            null
        }
    }
}
