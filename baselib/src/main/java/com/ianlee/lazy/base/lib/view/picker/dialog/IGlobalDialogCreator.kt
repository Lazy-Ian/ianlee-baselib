package com.ianlee.lazy.base.lib.view.picker.dialog

import android.content.Context

interface IGlobalDialogCreator {
    /**
     * 创建IPickerDialog
     */
    fun create(context: Context): IPickerDialog
}