package com.ianlee.lazy.base.lib.view.lang

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.PopupWindow
import com.ianlee.lazy.base.lib.R
import com.ianlee.lazy.base.lib.base.model.LanguageListBean
import com.ianlee.lazy.base.lib.databinding.PopLanguageBinding
import com.ianlee.lazy.base.lib.view.picker.widget.BasePickerView
import com.ianlee.lazy.base.lib.view.lang.adapter.LanguageWheelAdapter

/**
 * Created by Ian on 2024/3/21
 * Email: yixin0212@qq.com
 * Function :
 */

class LanguagePopWindow(
    var context: Context,
    var data :MutableList<LanguageListBean.LanguageInfoBean>
) : PopupWindow(context) {


    //布局文件使用viewBinding
    var binding: PopLanguageBinding = PopLanguageBinding.inflate(LayoutInflater.from(context))


    var languageWheelAdapter: LanguageWheelAdapter? = null

    var onSelectedLanguageListener: OnSelectedLanguageListener? = null
    lateinit var curLanguage: LanguageListBean.LanguageInfoBean
    private fun init() {



        curLanguage = data[0]
        binding.viewTop.setOnClickListener { v: View? -> dismiss() }
        binding.tvCancel.setOnClickListener { v: View? -> dismiss() }
        binding.tvConfirm.setOnClickListener { v: View? ->
            if (onSelectedLanguageListener != null) {
                onSelectedLanguageListener?.onSelectedLanguage(curLanguage)
            }
            dismiss()
        }

        languageWheelAdapter = LanguageWheelAdapter(data)

        binding.pickerview.adapter = languageWheelAdapter
        binding.pickerview.listener = object : BasePickerView.OnSelectedListener {
            override fun onSelected(pickerView: BasePickerView<*>, position: Int) {
                curLanguage = data[position]
            }

        }
        binding.pickerview.apply {
            // 覆盖xml中的水平方向
            isHorizontal = false
            setTextSize(18, 20)
            setIsCirculation(true)
            //setAlignment(Layout.Alignment.ALIGN_CENTER);
            isCanTap = false
            isDisallowInterceptTouch = false
            // 覆盖xml设置的7
            visibleItemCount = 5
            // 格式化内容
            formatter =
                BasePickerView.Formatter { _, position, _ -> data[position].new_name }

        }


    }


    var view: View = binding.root

    //关闭动画
    var isShowDismissAni: Boolean = false

    /**
     * 弹出popWindow
     *
     * @param mainView
     */
    open fun show(mainView: View) {
        // 产生背景变暗效果
        showAtLocation(
            mainView,
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
            0,
            0
        ) //设置layout在PopupWindow中显示的位置

        val animation = AnimationUtils.loadAnimation(context, R.anim.popwindow_up)
        view.startAnimation(animation)
    }


    //关闭动画
    private val duration = 300
    override fun dismiss() {
        if (isShowDismissAni) {
            animateOut(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    superDismiss()
                }
            })
        } else {
            superDismiss()
        }

    }

    private fun animateOut(listener: Animator.AnimatorListener) {
        try {
            val height = view.height
            view.animate().translationY(height.toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        listener.onAnimationEnd(animation)
                        view.animate().setListener(null)
                    }
                }).setDuration(duration.toLong()).start()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    /**
     * 直接关闭PopupWindow，没有动画效果
     */
    fun superDismiss() {
        super.dismiss()
    }

    init {
        this.contentView = view
        //设置SelectPicPopupWindow弹出窗体的宽
        this.width = ViewGroup.LayoutParams.MATCH_PARENT
        //设置SelectPicPopupWindow弹出窗体的高
        this.height = ViewGroup.LayoutParams.MATCH_PARENT
        //设置SelectPicPopupWindow弹出窗体可点击
        this.isFocusable = true
        this.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.pw_anim_style);
        //实例化一个ColorDrawable颜色为半透明
        val dw = ColorDrawable(-0x70000000)
        //设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw)
        init()
    }


    interface OnSelectedLanguageListener {
        fun onSelectedLanguage(language: LanguageListBean.LanguageInfoBean)
    }
}