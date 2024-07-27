package com.ianlee.lazy.base.lib.view.appupdate.listener

import androidx.annotation.IntDef

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function : 点击事件
 */
interface OnButtonClickListener {
    @IntDef(UPDATE, UPDATE_FORCED, CANCEL)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ID

    /**
     * 按钮点击回调
     *
     * @param id [OnButtonClickListener.UPDATE]
     * [OnButtonClickListener.CANCEL]
     */
    open fun onButtonClick(@ID id: Int) {}

    companion object {
        /**
         * 强制升级按钮点击事件
         */
        const val UPDATE_FORCED = 2
        /**
         * 强制升级按钮点击事件
         */
        const val UPDATE = 0

        /**
         * 取消按钮点击事件
         */
        const val CANCEL = 1
    }
}