package com.ianlee.lazy.base.lib.view.appupdate.manager

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.ianlee.lazy.base.lib.R
import com.ianlee.lazy.base.lib.base.model.AppVersionInfoModel
import com.ianlee.lazy.base.lib.network.utils.LogUtils
import com.ianlee.lazy.base.lib.network.utils.TimeUtils
import com.ianlee.lazy.base.lib.network.utils.getString4Id
import com.ianlee.lazy.base.lib.network.utils.showToast
import com.ianlee.lazy.base.lib.view.appupdate.config.UpdateConfiguration
import com.ianlee.lazy.base.lib.view.appupdate.dialog.UpdateDialog
import com.ianlee.lazy.base.lib.view.appupdate.service.DownloadService
import com.ianlee.lazy.base.lib.view.appupdate.utils.ApkUtil
import com.ianlee.lazy.base.lib.view.appupdate.utils.Constant
import java.lang.ref.SoftReference

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function : apk下载管理
 */
class AppDownloadManager {
    /**
     * 要更新apk的下载地址
     */
    private var apkUrl = ""

    /**
     * apk下载好的名字 .apk 结尾
     */
    private var apkName = ""

    /**
     * apk 下载存放的位置
     */
    private var downloadPath: String? = null

    /**
     * 是否提示用户 "当前已是最新版本"
     *
     *
     * [.download]
     */
    private var showNewerToast = false

    /**
     * 通知栏的图标 资源路径
     */
    private var smallIcon = -1

    /**
     * 整个库的一些配置属性，可以从这里配置
     */
    private var configuration: UpdateConfiguration? = null

    /**
     * 要更新apk的versionCode
     */
    private var apkVersionCode = Int.MIN_VALUE

    /**
     * 显示给用户的版本号
     */
    private var apkVersionName = ""

    /**
     * 更新描述
     */
    private var apkDescription = ""

    /**
     * 安装包大小 单位 M
     */
    private var apkSize = ""

    /**
     * 新安装包md5文件校验（32位)，校验重复下载
     */
    private var apkMD5 = ""

    /**
     * 当前下载状态
     */
    private var state = false

    /**
     * 内置对话框
     */
    private var dialog: UpdateDialog? = null

    /**
     * 获取apk下载地址
     */
    fun getApkUrl(): String {
        return apkUrl
    }


    /**
     * @param appVersionBean app版本
     * @param smallIcon app通知栏的小图标
     */
    fun setAppVersionBean(
        appVersionBean: AppVersionInfoModel.AppVersionModel,
        smallIcon: Int = -1
    ): AppDownloadManager {
        configuration = UpdateConfiguration()
        setApkName(
            getString4Id(
                R.string.str_set_apk_name,
                appVersionBean.version + "_" + TimeUtils.currentTimeInLong
            )
        )
            .setApkUrl(appVersionBean.now_url)
            .setSmallIcon(smallIcon)
            .setShowNewerToast(true)
            .setApkVersionCode(appVersionBean.versionCode)
            .setApkVersionName(appVersionBean.version)
            .setApkDescription(
                getString4Id(
                    R.string.v_xx_ya_est_disponible_para_actualizar,
                    appVersionBean.version
                )
            ) //
            .setConfiguration(configuration)
        //.setApkMD5("DC501F04BBAA458C9DC33008EFED5E7F")
        //是否强制更新
        if (appVersionBean.force == "1") {
            configuration!!.setForcedUpgrade(true)
        } else {
            configuration!!.setForcedUpgrade(false)
        }
        return this
    }


    /**
     * 设置apk下载地址
     */
    fun setApkUrl(apkUrl: String): AppDownloadManager {
        this.apkUrl = apkUrl
        return this
    }

    /**
     * 获取apk的VersionCode
     */
    fun getApkVersionCode(): Int {
        return apkVersionCode
    }

    /**
     * 设置apk的VersionCode
     */
    fun setApkVersionCode(apkVersionCode: Int): AppDownloadManager {
        this.apkVersionCode = apkVersionCode
        return this
    }

    /**
     * 获取apk的名称
     */
    fun getApkName(): String {
        return apkName
    }

    /**
     * 设置apk的名称
     */
    fun setApkName(apkName: String): AppDownloadManager {
        this.apkName = apkName
        return this
    }


    /**
     * 获取apk的保存路径
     */
    fun getDownloadPath(): String? {
        return downloadPath
    }

    /**
     * 设置apk的保存路径
     * 由于Android Q版本限制应用访问外部存储目录，所以不再支持设置存储目录
     * 使用的路径为:/storage/emulated/0/Android/data/ your packageName /cache
     */
    @Deprecated("", ReplaceWith("this"))
    fun setDownloadPath(downloadPath: String): AppDownloadManager {
        this.downloadPath = downloadPath
        return this
    }

    /**
     * 设置是否提示用户"当前已是最新版本"
     */
    fun setShowNewerToast(showNewerToast: Boolean): AppDownloadManager {
        this.showNewerToast = showNewerToast
        return this
    }

    /**
     * 获取是否提示用户"当前已是最新版本"
     */
    fun isShowNewerToast(): Boolean {
        return showNewerToast
    }

    /**
     * 获取通知栏图片资源id
     */
    fun getSmallIcon(): Int {
        return smallIcon
    }

    /**
     * 设置通知栏图片资源id
     */
    fun setSmallIcon(smallIcon: Int): AppDownloadManager {
        this.smallIcon = smallIcon
        return this
    }

    /**
     * 设置这个库的额外配置信息
     *
     * @see UpdateConfiguration
     */
    fun setConfiguration(configuration: UpdateConfiguration?): AppDownloadManager {
        this.configuration = configuration
        return this
    }

    /**
     * 获取这个库的额外配置信息
     *
     * @see UpdateConfiguration
     */
    fun getConfiguration(): UpdateConfiguration? {
        return configuration
    }

    /**
     * 获取apk的versionName
     */
    fun getApkVersionName(): String {
        return apkVersionName
    }

    /**
     * 设置apk的versionName
     */
    fun setApkVersionName(apkVersionName: String): AppDownloadManager {
        this.apkVersionName = apkVersionName
        return this
    }

    /**
     * 获取新版本描述信息
     */
    fun getApkDescription(): String {
        return apkDescription
    }

    /**
     * 设置新版本描述信息
     */
    fun setApkDescription(apkDescription: String): AppDownloadManager {
        this.apkDescription = apkDescription
        return this
    }

    /**
     * 获取新版本文件大小
     */
    fun getApkSize(): String {
        return apkSize
    }

    /**
     * 设置新版本文件大小
     */
    fun setApkSize(apkSize: String): AppDownloadManager {
        this.apkSize = apkSize
        return this
    }

    /**
     * 新安装包md5文件校验
     */
    fun setApkMD5(apkMD5: String): AppDownloadManager {
        this.apkMD5 = apkMD5
        return this
    }

    /**
     * 新安装包md5文件校验
     */
    fun getApkMD5(): String {
        return apkMD5
    }

    /**
     * 设置当前状态
     *
     * @hide
     */
    fun setState(state: Boolean) {
        this.state = state
    }

    /**
     * 当前是否正在下载
     */
    fun isDownloading(): Boolean {
        return state
    }

    /**
     * 获取内置对话框
     */
    fun getDefaultDialog(): UpdateDialog? {
        return dialog
    }

    /**
     * 开始下载
     */
    fun download() {
        if (!checkParams()) {
            LogUtils.debugInfo("参数设置出错....")
            //参数设置出错....
            return
        }
        if (checkVersionCode()) {
            context!!.get()!!.startService(
                Intent(
                    context!!.get(),
                    DownloadService::class.java
                )
            )
        } else {
            //对版本进行判断，是否显示升级对话框
            LogUtils.debugInfo("apkVersionCode:::" + apkVersionCode  + " ::: appCode::" +ApkUtil.getVersionCode(context!!.get()!!) )

            if (apkVersionCode > ApkUtil.getVersionCode(context!!.get()!!)) {
                if (context != null) {
                    dialog = UpdateDialog(context!!.get()!!)
                    dialog!!.show()
                }
            } else {
                if (showNewerToast) {
                    com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication().showToast(R.string.latest_version)
                }
                LogUtils.debugInfo(TAG, "当前已是最新版本")
            }
        }
    }

    /**
     * 取消下载
     */
    fun cancel() {
        if (configuration == null) {
            LogUtils.debugInfo(TAG, "还未开始下载")
            return
        }
        val httpManager = configuration!!.getHttpManager()
        if (httpManager == null) {
            LogUtils.debugInfo(TAG, "还未开始下载")
            return
        }
        httpManager.cancel()
    }

    /**
     * 检查参数
     */
    private fun checkParams(): Boolean {
        if (TextUtils.isEmpty(apkUrl)) {
            LogUtils.debugInfo(TAG, "apkUrl can not be empty!")
            return false
        }
        if (TextUtils.isEmpty(apkName)) {
            LogUtils.debugInfo(TAG, "apkName can not be empty!")
            return false
        }
        if (!apkName.endsWith(Constant.APK_SUFFIX)) {
            LogUtils.debugInfo(TAG, "apkName must endsWith .apk!")
            return false
        }

        if (smallIcon == -1) {
            LogUtils.debugInfo(TAG, "smallIcon can not be empty!")
            return false
        }
        if (context != null) {
            downloadPath = context!!.get()!!.externalCacheDir!!.path
            Constant.AUTHORITIES = context!!.get()!!.packageName + ".fileProvider"
        }
        //加载用户设置的authorities
        //如果用户没有进行配置，则使用默认的配置
        if (configuration == null) {
            configuration = UpdateConfiguration()
        }
        return true
    }

    /**
     * 检查设置的[this.apkVersionCode] 如果不是默认值则使用内置的对话框
     * 如果是默认值[Integer.MIN_VALUE]直接启动服务下载
     */
    private fun checkVersionCode(): Boolean {
        if (apkVersionCode == Int.MIN_VALUE) {
            return true
        }
        //设置了 VersionCode 则库中进行对话框逻辑处理
        if (TextUtils.isEmpty(apkDescription)) {
            LogUtils.debugInfo(TAG, "apkDescription can not be empty!")
        }
        return false
    }

    /**
     * 释放资源
     */
    fun release() {
        context!!.clear()
        context = null
        instance = null
        if (configuration != null) {
            configuration!!.getOnDownloadListener()!!.clear()
        }
    }

    companion object {
        private val TAG: String = Constant.TAG.toString() + "DownloadManager"

        /**
         * 上下文
         */
        private var context: SoftReference<Context>? = null

        /**
         * 供此依赖库自己使用.
         *
         * @return [AppDownloadManager]
         * @hide
         */
        var instance: AppDownloadManager? = null
            private set

        /**
         * 框架初始化
         *
         * @param context 上下文
         * @return [AppDownloadManager]
         */
        fun getInstance(context: Context): AppDownloadManager? {
            Companion.context = SoftReference(context)
            if (instance == null) {
                synchronized(AppDownloadManager::class.java) {
                    if (instance == null) {
                        instance = AppDownloadManager()
                    }
                }
            }
            return instance
        }
    }
}