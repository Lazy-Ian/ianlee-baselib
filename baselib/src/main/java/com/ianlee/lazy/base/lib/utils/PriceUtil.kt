package com.ianlee.lazy.base.lib.utils

import android.text.TextUtils
import com.ianlee.lazy.base.lib.network.utils.LogUtils
import java.math.BigDecimal

/**
 * Created by Ian on 2024/7/10
 * Email: yixin0212@qq.com
 * Function :
 */
object PriceUtil {
    fun formatPrice(d: String): String {
        return if (TextUtils.isEmpty(d)) {
            ""
        } else d.replace(".", ",") + MMKVUtil.getMoneyUnit()
    }

    fun formatPrice(price: Double): String {
        val bdAmount: BigDecimal = BigDecimal.valueOf(price)
        val newPrice = bdAmount.setScale(2, BigDecimal.ROUND_HALF_UP)
        return formatPrice(newPrice.toString())
    }
    fun formatNewPrice(price: Double): BigDecimal {
        val bdAmount: BigDecimal = BigDecimal.valueOf(price)
        return bdAmount.setScale(2, BigDecimal.ROUND_HALF_UP)
    }

    fun formatPriceAdd(d: String): String {
        return if (TextUtils.isEmpty(d)) {
            ""
        } else "+" + d.replace(".", ",") + MMKVUtil.getMoneyUnit()
    }

    fun formatPriceSub(d: String): String {
        return if (TextUtils.isEmpty(d)) {
            ""
        } else "-" + d.replace(".", ",") + MMKVUtil.getMoneyUnit()
    }

    fun isNotZero(str: String): Boolean {
        LogUtils.debugInfo("isNotZero:" + str)
        LogUtils.debugInfo(
            "isNotZero:" + !(TextUtils.equals(str, "0.00") || TextUtils.equals(
                str,
                "0"
            ))
        )
        return !(TextUtils.equals(str, "0.00") || TextUtils.equals(str, "0"))
    }
}