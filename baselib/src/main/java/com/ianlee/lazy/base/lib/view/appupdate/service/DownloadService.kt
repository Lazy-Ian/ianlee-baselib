package com.ianlee.lazy.base.lib.view.appupdate.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import com.ianlee.lazy.base.lib.R
import com.ianlee.lazy.base.lib.network.utils.ContextHelper
import com.ianlee.lazy.base.lib.network.utils.LogUtils
import com.ianlee.lazy.base.lib.network.utils.showToast
import com.ianlee.lazy.base.lib.utils.log.Logger
import com.ianlee.lazy.base.lib.view.appupdate.base.BaseHttpDownloadManager
import com.ianlee.lazy.base.lib.view.appupdate.config.UpdateConfiguration
import com.ianlee.lazy.base.lib.view.appupdate.listener.OnDownloadListener
import com.ianlee.lazy.base.lib.view.appupdate.manager.AppDownloadManager
import com.ianlee.lazy.base.lib.view.appupdate.manager.HttpDownloadManager
import com.ianlee.lazy.base.lib.view.appupdate.utils.ApkUtil
import com.ianlee.lazy.base.lib.view.appupdate.utils.Constant
import com.ianlee.lazy.base.lib.view.appupdate.utils.FileUtil
import com.ianlee.lazy.base.lib.view.appupdate.utils.NotificationUtil
import java.io.File

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function : apk 下载服务
 */
class DownloadService : Service(), OnDownloadListener {
    private var smallIcon = 0
    private var apkUrl: String? = null
    private var apkName: String? = null
    private var downloadPath: String? = null
    private var listeners: List<OnDownloadListener>? = null
    private var showNotification = false
    private var showBgdToast = false
    private var jumpInstallPage = false
    private var lastProgress = 0
    private var appDownloadManager: AppDownloadManager? = null
    private var httpManager: BaseHttpDownloadManager? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (null == intent) {
            return START_STICKY
        }
        init()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun init() {
        appDownloadManager = AppDownloadManager.instance
        if (appDownloadManager == null) {
            LogUtils.debugInfo(
                TAG,
                "init DownloadManager.getInstance() = null ,请先调用 getInstance(Context context) !"
            )
            return
        }
        apkUrl = appDownloadManager!!.getApkUrl()
        apkName = appDownloadManager!!.getApkName()
        downloadPath = appDownloadManager!!.getDownloadPath()
        smallIcon = appDownloadManager!!.getSmallIcon()
        //创建apk文件存储文件夹
        FileUtil.createDirDirectory(downloadPath)
        val configuration = appDownloadManager!!.getConfiguration()
        listeners = configuration!!.getOnDownloadListener()
        showNotification = configuration.isShowNotification()
        showBgdToast = configuration.isShowBgdToast()
        jumpInstallPage = configuration.isJumpInstallPage()
        //获取app通知开关是否打开
        val enable: Boolean = NotificationUtil.notificationEnable(this)
        LogUtils.debugInfo(
            TAG,
            if (enable) "应用的通知栏开关状态：已打开" else "应用的通知栏开关状态：已关闭"
        )
        if (checkApkMD5()) {
            LogUtils.debugInfo(TAG, "文件已经存在直接进行安装")
            //直接调用完成监听即可
            done(FileUtil.createFile(downloadPath, apkName))
        } else {
            LogUtils.debugInfo(TAG, "文件不存在开始下载")
            download(configuration)
        }
    }

    /**
     * 校验Apk是否已经下载好了，不重复下载
     *
     * @return 是否下载完成
     */
    private fun checkApkMD5(): Boolean {
        if (FileUtil.fileExists(downloadPath, apkName)) {
//            val fileMD5: String =
//                FileUtil.getFileMD5(FileUtil.createFile(downloadPath, apkName))
//            return fileMD5.equals(appDownloadManager!!.getApkMD5(), ignoreCase = true)
            //todo  先用文件是否存在的状态
            return true
        }
        return false
    }

    /**
     * 获取下载管理者
     */
    @Synchronized
    private fun download(configuration: UpdateConfiguration?) {
        if (appDownloadManager!!.isDownloading()) {
            com.ianlee.lazy.base.lib.utils.log.Logger.e(TAG, "download: 当前正在下载，请务重复下载！")
            return
        }
        httpManager = configuration!!.getHttpManager()
        //使用自己的下载
        if (httpManager == null) {
            httpManager = HttpDownloadManager(downloadPath!!)
            configuration.setHttpManager(httpManager)
        }
        //如果用户自己定义了下载过程
        httpManager!!.download(apkUrl, apkName, this)
        appDownloadManager!!.setState(true)
    }

    override fun start() {
        if (showNotification) {
            if (showBgdToast) {
                handler.sendEmptyMessage(0)
            }
            val startDownload = resources.getString(R.string.start_download)
            val startDownloadHint = resources.getString(R.string.start_download_hint)
            NotificationUtil.showNotification(
                this,
                smallIcon,
                startDownload,
                startDownloadHint
            )
        }
        handler.sendEmptyMessage(1)
    }

    override fun downloading(max: Int, progress: Int) {
        if (showNotification) {
            //优化通知栏更新，减少通知栏更新次数
            val curr = (progress / max.toDouble() * 100.0).toInt()
            if (curr != lastProgress) {
                lastProgress = curr
                val downloading = resources.getString(R.string.start_downloading) + apkName
                val content = if (curr < 0) "" else "$curr%"
                NotificationUtil.showProgressNotification(
                    this, smallIcon, downloading,
                    content, if (max == -1) -1 else 100, curr
                )
            }
        }
        handler.obtainMessage(2, max, progress).sendToTarget()
    }

    override fun done(apk: File?) {
        LogUtils.debugInfo(TAG, "done: 文件已下载至$apk")
        appDownloadManager!!.setState(false)
        //如果是android Q（api=29）及其以上版本showNotification=false也会发送一个下载完成通知
        if (showNotification || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val downloadCompleted = resources.getString(R.string.download_completed)
            val clickHint = resources.getString(R.string.click_hint)
            NotificationUtil.showDoneNotification(
                this, smallIcon, downloadCompleted,
                clickHint, Constant.AUTHORITIES, apk
            )
        }
        if (jumpInstallPage) {
            ApkUtil.installApk(this, Constant.AUTHORITIES, apk)
        }
        //如果用户设置了回调 则先处理用户的事件 在执行自己的
        handler.obtainMessage(3, apk).sendToTarget()
    }

    override fun cancel() {
        appDownloadManager!!.setState(false)
        if (showNotification) {
            NotificationUtil.cancelNotification(this)
        }
        handler.sendEmptyMessage(4)
    }

    override fun error(e: Exception?) {
        com.ianlee.lazy.base.lib.utils.log.Logger.e(TAG, "error: $e")
        appDownloadManager!!.setState(false)
        if (showNotification) {
            val downloadError = resources.getString(R.string.download_error)
            val conDownloading = resources.getString(R.string.continue_downloading)
            NotificationUtil.showErrorNotification(
                this,
                smallIcon,
                downloadError,
                conDownloading
            )
        }
        handler.obtainMessage(5, e).sendToTarget()
    }

    @SuppressLint("HandlerLeak")
        private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0 -> com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication().showToast(
                    R.string.background_downloading)
                1 -> for (listener in listeners!!) {
                    listener.start()
                }

                2 -> for (listener in listeners!!) {
                    listener.downloading(msg.arg1, msg.arg2)
                }

                3 -> {
                    for (listener in listeners!!) {
                        listener.done(msg.obj as File)
                    }
                    //执行了完成开始释放资源
                    releaseResources()
                }

                4 -> for (listener in listeners!!) {
                    listener.cancel()
                }

                5 -> for (listener in listeners!!) {
                    listener.error(msg.obj as Exception)
                }

                else -> {
                }
            }
        }
    }

    /**
     * 下载完成释放资源
     */
    private fun releaseResources() {
        handler.removeCallbacksAndMessages(null)
        if (httpManager != null) {
            httpManager!!.release()
        }
        stopSelf()
        appDownloadManager!!.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val TAG: String = Constant.TAG.toString() + "DownloadService"
    }
}
