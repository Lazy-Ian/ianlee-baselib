package com.ianlee.lazy.base.lib.view

import android.content.Context
import android.view.LayoutInflater
import com.ianlee.lazy.base.lib.databinding.DialogCommonBinding
import com.ianlee.lazy.base.lib.network.utils.dp2px
import com.ianlee.lazy.base.lib.view.xpopup.core.CenterPopupView

/**
 * Created by Ian on 2024/7/9
 * Email: yixin0212@qq.com
 * Function : 普通弹框
 */
class NormalPopup(context: Context) : com.ianlee.lazy.base.lib.view.xpopup.core.CenterPopupView(context) {

    lateinit var binding: DialogCommonBinding
    var dialogListener: DialogListener? = null
    override fun getImplLayoutId(): Any {
        binding = DialogCommonBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }


    interface DialogListener {
        fun onSure()
    }

    override fun onCreate() {
        super.onCreate()

        initListener()
    }

    private fun initListener() {
        binding.tvSure.setOnClickListener {
            dialogListener?.onSure()
            dismiss()
        }

        binding.tvCancel.setOnClickListener {
            dismiss()
        }

    }


    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    fun setContent(content: String) {
        binding.tvContent.text = content
    }

    fun setSureText(text: String) {
        binding.tvSure.text = text
    }

    fun setCancelText(text: String) {
        binding.tvCancel.text = text
    }

    fun setTitle(titleResId: Int) {
        binding.tvTitle.setText(titleResId)
    }

    fun setContent(contentResId: Int) {
        binding.tvContent.setText(contentResId)
    }

    fun setSureText(textResId: Int) {
        binding.tvSure.setText(textResId)
    }

    fun setCancelText(textResId: Int) {
        binding.tvCancel.setText(textResId)
    }

    override fun getMaxWidth(): Int {
        return dp2px(450f).toInt()
    }

}