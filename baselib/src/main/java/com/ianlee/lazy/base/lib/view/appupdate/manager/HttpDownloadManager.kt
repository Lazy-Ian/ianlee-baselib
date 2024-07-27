package com.ianlee.lazy.base.lib.view.appupdate.manager

import com.ianlee.lazy.base.lib.network.utils.LogUtils
import com.ianlee.lazy.base.lib.view.appupdate.base.BaseHttpDownloadManager
import com.ianlee.lazy.base.lib.view.appupdate.listener.OnDownloadListener
import com.ianlee.lazy.base.lib.view.appupdate.utils.Constant
import com.ianlee.lazy.base.lib.view.appupdate.utils.FileUtil
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function : 默认的下载管理
 */

class HttpDownloadManager(downloadPath: String) :
    BaseHttpDownloadManager() {
    private var shutdown = false
    private var apkUrl: String? = null
    private var apkName: String? = null
    private val downloadPath: String = downloadPath
    private var listener: OnDownloadListener? = null
    override fun download(apkUrl: String?, apkName: String?, listener: OnDownloadListener?) {
        this.apkUrl = apkUrl
        this.apkName = apkName
        this.listener = listener
        val executor = ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.SECONDS,
            LinkedBlockingQueue()
        ) { r ->
            val thread = Thread(r)
            thread.name = Constant.THREAD_NAME
            thread
        }
        executor.execute(runnable)
    }

    override fun cancel() {
        shutdown = true
    }

    override fun release() {
        listener = null
    }

    private val runnable = Runnable { //删除之前的安装包
        if (FileUtil.fileExists(downloadPath, apkName)) {
            FileUtil.delete(downloadPath, apkName)
        }
        fullDownload()
    }

    /**
     * 全部下载
     */
    private fun fullDownload() {
        listener!!.start()
        try {
            val url = URL(apkUrl)
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.readTimeout = Constant.HTTP_TIME_OUT
            con.connectTimeout = Constant.HTTP_TIME_OUT
            con.setRequestProperty("Accept-Encoding", "identity")
            when (con.responseCode) {
                HttpURLConnection.HTTP_OK -> {
                    val `is` = con.inputStream
                    val length = con.contentLength
                    var len: Int
                    //当前已下载完成的进度
                    var progress = 0
                    val buffer = ByteArray(1024 * 2)
                    val file: File = FileUtil.createFile(downloadPath, apkName)
                    val stream = FileOutputStream(file)
                    while (`is`.read(buffer).also { len = it } != -1 && !shutdown) {
                        //将获取到的流写入文件中
                        stream.write(buffer, 0, len)
                        progress += len
                        listener!!.downloading(length, progress)
                    }
                    if (shutdown) {
                        //取消了下载 同时再恢复状态
                        shutdown = false
                        LogUtils.debugInfo(TAG, "fullDownload: 取消了下载")
                        listener!!.cancel()
                    } else {
                        listener!!.done(file)
                    }
                    //完成io操作,释放资源
                    stream.flush()
                    stream.close()
                    `is`.close()
                    //重定向
                }
                HttpURLConnection.HTTP_MOVED_PERM, HttpURLConnection.HTTP_MOVED_TEMP -> {
                    apkUrl = con.getHeaderField("Location")
                    con.disconnect()
                    LogUtils.run {
                        debugInfo(
                            TAG,
                            "fullDownload: 当前地址是重定向Url，定向后的地址：$apkUrl"
                        )
                    }
                    fullDownload()
                }
                else -> {
                    if (FileUtil.fileExists(downloadPath, apkName)) {
                        FileUtil.delete(downloadPath, apkName)
                    }
                    listener!!.error(SocketTimeoutException("下载失败：Http ResponseCode = " + con.responseCode))


                }
            }
            con.disconnect()
        } catch (e: Exception) {
            if (FileUtil.fileExists(downloadPath, apkName)) {
                FileUtil.delete(downloadPath, apkName)
            }
            listener!!.error(e)
            e.printStackTrace()
        }
    }

    companion object {
        private val TAG: String = Constant.TAG.toString() + "HttpDownloadManager"
    }

}
