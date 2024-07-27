package com.ianlee.lazy.base.lib.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import com.ianlee.lazy.base.lib.R

class LoadingDialog(context: Context) : Dialog(context, R.style.CustomAlertDialog) {

    var mDimAmount: Float = 0.0f
    var tvLoading: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading)

        setCancelable(false)
        val layoutParams = window!!.attributes
        layoutParams.dimAmount = mDimAmount
        window!!.attributes = layoutParams


        window?.setBackgroundDrawableResource(R.drawable.shape_loading_bg)
    }

    fun setDimAmount(dimAmount: Float): LoadingDialog {
        mDimAmount = dimAmount
        return this
    }

    fun setLoading(loading: String) {
        if (loading.isNotEmpty()) {
            tvLoading = findViewById(R.id.tv_loading)
            if (tvLoading != null) {
                tvLoading!!.text = loading
            }
        }
    }


}