package com.ianlee.lazy.base.lib.view.appupdate.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
object ApkUtil {
    /**
     * 安装一个apk
     *
     * @param context     上下文
     * @param authorities Android N 授权
     * @param apk         安装包文件
     */
    fun installApk(context: Context, authorities: String?, apk: File?) {
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
        context.startActivity(intent)
    }

    /**
     * 获取当前app的升级版本号
     *
     * @param context 上下文
     */
    fun getVersionCode(context: Context): Int {
        return try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
            1
        }
    }

    /**
     * 删除旧版本apk
     *
     * @param context    上下文
     * @param oldApkPath 旧版本保存的文件路径
     * @return 是否删除成功
     */
    fun deleteOldApk(context: Context, oldApkPath: String?): Boolean {
        val curVersionCode = getVersionCode(context)
        //文件存在
        try {
            val apk = File(oldApkPath)
            if (apk.exists()) {
                val oldVersionCode = getVersionCodeByPath(context, oldApkPath)
                if (curVersionCode > oldVersionCode) {
                    return apk.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 对一个apk文件获取相应的信息
     *
     * @param context 上下文
     * @param path    apk路径
     */
    fun getVersionCodeByPath(context: Context, path: String?): Int {
        val packageManager = context.packageManager
        val packageInfo =
            packageManager.getPackageArchiveInfo(path!!, PackageManager.GET_ACTIVITIES)
        return packageInfo!!.versionCode
    }

    /**
     * 获取当前app的版本号
     *
     * @param context 上下文
     */
    fun getVersionName(context: Context): String {
        return try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
            "1.0.0"
        }

    }

    /**
     * 获取app名字
     *
     * @param context 上下文
     */
    fun getAppName(context: Context): String {
        try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
            return packageManager.getApplicationLabel(applicationInfo) as String
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    fun isApkInDebug(context: Context): Boolean {
        return try {
            val info = context.applicationInfo
            info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: java.lang.Exception) {
            false
        }
    }

}
