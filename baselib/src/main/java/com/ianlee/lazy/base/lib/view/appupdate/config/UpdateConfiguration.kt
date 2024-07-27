package com.ianlee.lazy.base.lib.view.appupdate.config

import android.app.NotificationChannel
import com.ianlee.lazy.base.lib.view.appupdate.base.BaseHttpDownloadManager
import com.ianlee.lazy.base.lib.view.appupdate.listener.OnButtonClickListener
import com.ianlee.lazy.base.lib.view.appupdate.listener.OnDownloadListener

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
class UpdateConfiguration {
    /**
     * 通知栏id
     */
    private var notifyId = 1011

    /**
     * 适配Android O的渠道通知
     */
    private var notificationChannel: NotificationChannel? = null

    /**
     * 用户自定义的下载管理
     */
    private var httpManager: BaseHttpDownloadManager? = null

    /**
     * 是否需要显示通知栏进度
     */
    private var showNotification = true

    /**
     * 下载过程回调
     */
    private val onDownloadListeners: MutableList<OnDownloadListener> =
        ArrayList<OnDownloadListener>()

    /**
     * 按钮点击事件回调
     */
    private var onButtonClickListener: OnButtonClickListener? = null

    /**
     * 下载完成是否自动弹出安装页面 (默认为true)
     */
    private var jumpInstallPage = true

    /**
     * 下载开始时是否提示 "正在更新中…" (默认为true)
     */
    private var showBgdToast = true

    /**
     * 是否强制升级(默认为false)
     */
    private var forcedUpgrade = false


    /**
     * 设置通知栏消息id
     */
    fun setNotifyId(notifyId: Int): UpdateConfiguration? {
        this.notifyId = notifyId
        return this
    }

    /**
     * 获取通知栏消息id
     */
    fun getNotifyId(): Int {
        return notifyId
    }

    /**
     * 设置下载管理器
     */
    fun setHttpManager(httpManager: BaseHttpDownloadManager?): UpdateConfiguration? {
        this.httpManager = httpManager
        return this
    }

    /**
     * 获取下载管理器
     */
    fun getHttpManager(): BaseHttpDownloadManager? {
        return httpManager
    }



    /**
     * 设置下载监听器
     */
    fun setOnDownloadListener(onDownloadListener: OnDownloadListener): UpdateConfiguration? {
        onDownloadListeners.add(onDownloadListener)
        return this
    }

    /**
     * 获取下载监听器
     */
    fun getOnDownloadListener(): MutableList<OnDownloadListener>? {
        return onDownloadListeners
    }

    /**
     * 设置apk下载完成是否跳转至安装界面
     */
    fun setJumpInstallPage(jumpInstallPage: Boolean): UpdateConfiguration? {
        this.jumpInstallPage = jumpInstallPage
        return this
    }

    /**
     * apk下载完成是否跳转至安装界面
     */
    fun isJumpInstallPage(): Boolean {
        return jumpInstallPage
    }

    /**
     * 设置Android O的通知渠道
     */
    fun setNotificationChannel(notificationChannel: NotificationChannel?): UpdateConfiguration? {
        this.notificationChannel = notificationChannel
        return this
    }

    /**
     * 获取Android O的通知渠道
     */
    fun getNotificationChannel(): NotificationChannel? {
        return notificationChannel
    }

    /**
     * 设置是否在通知栏显示信息
     */
    fun setShowNotification(showNotification: Boolean): UpdateConfiguration? {
        this.showNotification = showNotification
        return this
    }

    /**
     * 是否在通知栏显示信息
     */
    fun isShowNotification(): Boolean {
        return showNotification
    }

    /**
     * 设置是否强制升级
     */
    fun setForcedUpgrade(forcedUpgrade: Boolean): UpdateConfiguration? {
        this.forcedUpgrade = forcedUpgrade
        return this
    }

    /**
     * 是否强制升级
     */
    fun isForcedUpgrade(): Boolean {
        return forcedUpgrade
    }

    /**
     * 设置是否提示 "正在后台下载新版本…"
     */
    fun setShowBgdToast(showBgdToast: Boolean): UpdateConfiguration? {
        this.showBgdToast = showBgdToast
        return this
    }

    /**
     * 是否提示 "正在后台下载新版本…"
     */
    fun isShowBgdToast(): Boolean {
        return showBgdToast
    }


    /**
     * 设置内置对话框按钮点击事件监听
     */
    fun setButtonClickListener(onButtonClickListener: OnButtonClickListener): UpdateConfiguration? {
        this.onButtonClickListener = onButtonClickListener
        return this
    }

    /**
     * 获取内置对话框按钮点击事件监听
     */
    fun getOnButtonClickListener(): OnButtonClickListener? {
        return onButtonClickListener
    }
}