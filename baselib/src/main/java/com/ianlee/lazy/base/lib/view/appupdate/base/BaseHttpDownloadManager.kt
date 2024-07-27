package com.ianlee.lazy.base.lib.view.appupdate.base

import com.ianlee.lazy.base.lib.view.appupdate.listener.OnDownloadListener

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :下载管理者
 */
abstract class BaseHttpDownloadManager {
    /**
     * 下载apk
     *
     * @param apkUrl   apk下载地址
     * @param apkName  apk名字
     * @param listener 回调
     */
    abstract fun download(apkUrl: String?, apkName: String?, listener: OnDownloadListener?)

    /**
     * 取消下载apk
     */
    abstract fun cancel()

    /**
     * 释放资源
     */
    abstract fun release()
}
