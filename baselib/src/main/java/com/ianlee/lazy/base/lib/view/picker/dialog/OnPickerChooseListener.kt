package com.ianlee.lazy.base.lib.view.picker.dialog

interface OnPickerChooseListener {
    /**
     * @return 是否回调选中关闭dialog
     */
    fun onConfirm(): Boolean
    fun onCancel()
}