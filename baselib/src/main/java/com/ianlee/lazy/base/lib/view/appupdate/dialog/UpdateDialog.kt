package com.ianlee.lazy.base.lib.view.appupdate.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.ianlee.lazy.base.lib.R
import com.ianlee.lazy.base.lib.databinding.DialogUpdateBinding
import com.ianlee.lazy.base.lib.network.utils.LogUtils
import com.ianlee.lazy.base.lib.network.utils.showToast
import com.ianlee.lazy.base.lib.utils.DeviceUtils.getScreenHeight
import com.ianlee.lazy.base.lib.utils.DeviceUtils.getScreenWidth
import com.ianlee.lazy.base.lib.view.appupdate.config.UpdateConfiguration
import com.ianlee.lazy.base.lib.view.appupdate.listener.OnButtonClickListener
import com.ianlee.lazy.base.lib.view.appupdate.listener.OnDownloadListener
import com.ianlee.lazy.base.lib.view.appupdate.manager.AppDownloadManager
import com.ianlee.lazy.base.lib.view.appupdate.service.DownloadService
import com.ianlee.lazy.base.lib.view.appupdate.utils.ApkUtil
import com.ianlee.lazy.base.lib.view.appupdate.utils.Constant
import com.ianlee.lazy.base.lib.view.appupdate.utils.FileUtil
import java.io.File

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function : app更新弹框
 */
class UpdateDialog(private var mContext: Context) :
    Dialog(mContext, R.style.DialogStyle), OnDownloadListener, View.OnClickListener {
    private lateinit var binding: DialogUpdateBinding

    private var manager: AppDownloadManager? = null
    private var forcedUpgrade = false
    private val install = 0x45F
    private var apk: File? = null

    private var buttonClickListener: OnButtonClickListener? = null


    fun setOnButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }


    /**
     * 初始化布局
     */
    private fun init(context: Context) {
        manager = AppDownloadManager.instance
        val configuration: UpdateConfiguration? = manager!!.getConfiguration()
        if (configuration != null) {
            configuration.setOnDownloadListener(this)
            forcedUpgrade = configuration.isForcedUpgrade()
            buttonClickListener = configuration.getOnButtonClickListener()

        }
        binding = DialogUpdateBinding.inflate(LayoutInflater.from(context), null, false)


        setContentView(binding.root)
        setCancelable(false)
        initView()
    }

    private fun initView() {
        val params = binding.mainView.layoutParams
        params.width = (getScreenWidth(context) * 0.7f).toInt()
        params.height = (getScreenHeight(context) * 0.9f).toInt()
        binding.mainView.layoutParams = params
        binding.tvNowUpdate.setOnClickListener(this)
        binding.tvCancel.setOnClickListener(this)
        binding.tvNowUpdate.tag = 0

        if (forcedUpgrade) {
            binding.tvCancel.visibility = View.GONE
            binding.npBar.visibility = View.VISIBLE
            binding.tvNowUpdate.setBackgroundResource(R.drawable.shape_17radius_4e5969)
            setOnKeyListener { _: DialogInterface?, keyCode: Int, _: KeyEvent? -> keyCode == KeyEvent.KEYCODE_BACK }
        } else {
            binding.npBar.visibility = View.INVISIBLE
        }
        //设置界面更新数据
        if (!TextUtils.isEmpty(manager!!.getApkDescription())) {
            binding.tvUpdateContent.text = manager!!.getApkDescription()
        }
    }


    init {
        init(mContext)
    }

    override fun start() {


    }

    override fun downloading(max: Int, progress: Int) {
        LogUtils.debugInfo("UpdateDialog:::", "apk已下载：$progress  文件总共:$max")
        if (max != -1 && binding.npBar.visibility == View.VISIBLE) {
            val curr = (progress / max.toDouble() * 100.0).toInt()
            binding.npBar.setProgress(curr)
        } else {
            binding.npBar.visibility = View.GONE
        }
    }

    override fun done(apk: File?) {
        this.apk = apk
        if (forcedUpgrade) {
            binding.tvNowUpdate.tag = install
            binding.tvNowUpdate.isEnabled = true
            binding.tvNowUpdate.setText(R.string.click_hint)
        }
    }

    override fun error(e: Exception?) {
    }

    override fun onClick(v: View?) {
        val id = v!!.id
        if (id == R.id.tv_cancel) {
            if (!forcedUpgrade) {
                dismiss()
            }
            //回调点击事件
            if (buttonClickListener != null) {
                buttonClickListener!!.onButtonClick(OnButtonClickListener.CANCEL)
            }
        } else if (id == R.id.tv_now_update) {
            startUpdate()
        }

    }

    override fun show() {
        super.show()
        MainScope().launch {
            delay(600)
            startUpdate()
        }
    }

    /**
     * 开始更新
     */
    private fun startUpdate() {
        if (binding.tvNowUpdate.tag as Int == install) {
            installApk()
            return
        }
        if (forcedUpgrade) {
            binding.tvNowUpdate.isEnabled = false
            binding.tvNowUpdate.setText(R.string.background_downloading)
            //强制升级回调点击事件
            if (buttonClickListener != null) {
                buttonClickListener!!.onButtonClick(OnButtonClickListener.UPDATE_FORCED)
            }

        } else {
            if (manager != null) {
                if (FileUtil.fileExists(manager?.getDownloadPath(), manager?.getApkName())) {

                } else {
                    context.showToast(R.string.start_download_back)
                }
            }
            dismiss()
            //普通升级回调点击事件
            if (buttonClickListener != null) {
                buttonClickListener!!.onButtonClick(OnButtonClickListener.UPDATE)
            }

        }
        mContext.startService(Intent(mContext, DownloadService::class.java))
    }


    /**
     * 强制更新，点击进行安装
     */
    private fun installApk() {
        ApkUtil.installApk(mContext, Constant.AUTHORITIES, apk)
    }

}