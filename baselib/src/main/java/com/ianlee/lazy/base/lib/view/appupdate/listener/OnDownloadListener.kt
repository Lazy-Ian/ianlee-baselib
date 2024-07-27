package com.ianlee.lazy.base.lib.view.appupdate.listener

import java.io.File

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function : 正在下载的回调
 */
interface OnDownloadListener {
    /**
     * 开始下载
     */
    fun start()

    /**
     * 下载中
     *
     * @param max      总进度
     * @param progress 当前进度
     */
    fun downloading(max: Int, progress: Int)

    /**
     * 下载完成
     *
     * @param apk 下载好的apk
     */
    fun done(apk: File?)

    /**
     * 取消下载
     */
    fun cancel()

    /**
     * 下载出错
     *
     * @param e 错误信息
     */
    fun error(e: Exception?)
}