package com.ianlee.lazy.base.lib.view.appupdate.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import com.ianlee.lazy.base.lib.view.appupdate.config.UpdateConfiguration
import com.ianlee.lazy.base.lib.view.appupdate.manager.AppDownloadManager
import com.ianlee.lazy.base.lib.view.appupdate.service.DownloadService
import java.io.File

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function : 通知类工具
 */
object NotificationUtil {
    /**
     * 构建一个消息
     *
     * @param context 上下文
     * @param icon    图标id
     * @param title   标题
     * @param content 内容
     */
    private fun builderNotification(
        context: Context,
        icon: Int,
        title: String,
        content: String
    ): NotificationCompat.Builder {
        var channelId = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = getNotificationChannelId()
        }
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setWhen(System.currentTimeMillis())
            .setContentText(content) //不能删除
            .setAutoCancel(false) //正在交互（如播放音乐）
            .setOngoing(true)
    }

    /**
     * 显示刚开始下载的通知
     *
     * @param context 上下文
     * @param icon    图标
     * @param title   标题
     * @param content 内容
     */
    fun showNotification(context: Context, icon: Int, title: String, content: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            afterO(manager)
        }
        val builder = builderNotification(context, icon, title, content)
            .setDefaults(Notification.DEFAULT_SOUND)
        manager.notify(requireManagerNotNull().getNotifyId(), builder.build())
    }

    /**
     * 显示正在下载的通知
     *
     * @param context 上下文
     * @param icon    图标
     * @param title   标题
     * @param content 内容
     */
    fun showProgressNotification(
        context: Context, icon: Int, title: String, content: String,
        max: Int, progress: Int
    ) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = builderNotification(
            context,
            icon,
            title,
            content
        ) //indeterminate:true表示不确定进度，false表示确定进度
            //当下载进度没有获取到content-length时，使用不确定进度条
            .setProgress(max, progress, max == -1)
        manager.notify(requireManagerNotNull().getNotifyId(), builder.build())
    }

    /**
     * 显示下载完成的通知,点击进行安装
     *
     * @param context     上下文
     * @param icon        图标
     * @param title       标题
     * @param content     内容
     * @param authorities Android N 授权
     * @param apk         安装包
     */
    fun showDoneNotification(
        context: Context, icon: Int, title: String, content: String,
        authorities: String?, apk: File?
    ) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //不知道为什么需要先取消之前的进度通知，才能显示完成的通知。
        manager.cancel(requireManagerNotNull().getNotifyId())
        //得到调整安装页面的Intent
        val pi = getPendingIntentActivity(context, authorities, apk)
        //通知栏
        val builder = builderNotification(context, icon, title, content).setContentIntent(pi)
        val notification = builder.build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        manager.notify(requireManagerNotNull().getNotifyId(), notification)
    }

    /**
     * 显示下载错误的通知,点击继续下载
     *
     * @param context 上下文
     * @param icon    图标
     * @param title   标题
     * @param content 内容
     */
    fun showErrorNotification(context: Context, icon: Int, title: String, content: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            afterO(manager)
        }
        val intent = Intent(context, DownloadService::class.java)
        val pi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val builder = builderNotification(context, icon, title, content)
            .setAutoCancel(true)
            .setOngoing(false)
            .setContentIntent(pi)
            .setDefaults(Notification.DEFAULT_SOUND)
        manager.notify(requireManagerNotNull().getNotifyId(), builder.build())
    }

    /**
     * 取消通知
     *
     * @param context 上下文
     */
    fun cancelNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(requireManagerNotNull().getNotifyId())
    }

    /**
     * 获取通知栏开关状态
     *
     * @return true |false
     */
    fun notificationEnable(context: Context?): Boolean {
        val notificationManagerCompat = NotificationManagerCompat.from(
            context!!
        )
        return notificationManagerCompat.areNotificationsEnabled()
    }

    /**
     * 适配 Android O 通知渠道
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun afterO(manager: NotificationManager) {
        val config: UpdateConfiguration = requireManagerNotNull()
        var channel: NotificationChannel? = config.getNotificationChannel()
        //如果用户没有设置
        if (channel == null) {
            //IMPORTANCE_LOW：默认关闭声音与震动、IMPORTANCE_DEFAULT：开启声音与震动
            channel = NotificationChannel(
                Constant.DEFAULT_CHANNEL_ID, Constant.DEFAULT_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            //是否在桌面icon右上角展示小圆点
            channel.enableLights(true)
            //是否在久按桌面图标时显示此渠道的通知
            channel.setShowBadge(true)
            //在Android O 上更新进度 不震动
//            channel.enableVibration(true);
        }
        manager.createNotificationChannel(channel)
    }

    /**
     * 获取设置的通知渠道id
     *
     * @return 如果没有设置则使用默认的 'appUpdate'
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun getNotificationChannelId(): String {
        val channel: NotificationChannel = requireManagerNotNull().getNotificationChannel()
            ?: return Constant.DEFAULT_CHANNEL_ID
        val channelId = channel.id
        return if (TextUtils.isEmpty(channelId)) {
            Constant.DEFAULT_CHANNEL_ID
        } else channelId
    }

    private fun requireManagerNotNull(): UpdateConfiguration {
        return if (AppDownloadManager.instance == null) {
            UpdateConfiguration()
        } else AppDownloadManager.instance!!.getConfiguration()!!
    }


    /**
     * 意图 安装apk页面
     */
    private fun getPendingIntentActivity(
        context: Context,
        authorities: String?,
        apk: File?
    ): PendingIntent? {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        val uri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(
                context, authorities!!,
                apk!!
            )
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(apk)
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        val pi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

        } else {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        }
        return pi
    }
}
