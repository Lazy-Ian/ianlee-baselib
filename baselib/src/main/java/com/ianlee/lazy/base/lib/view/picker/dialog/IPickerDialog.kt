package com.ianlee.lazy.base.lib.view.picker.dialog

import com.ianlee.lazy.base.lib.view.picker.picker.BasePicker

interface IPickerDialog {
    /**
     * picker create 时回调
     * @see BasePicker.BasePicker
     */
    fun onCreate(picker: BasePicker)

    /**
     * 其实可以不提供这个方法，为了方便在[BasePicker.show]
     */
    fun showDialog()
}