package com.ianlee.lazy.base.lib.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.WindowManager
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.Locale

/**
 * Created by Ian on 2024/3/20
 * Email: yixin0212@qq.com
 * Function :
 */
object DeviceUtils {
    /**
     * 获得设备的固件版本号
     */
    val releaseVersion: String
        get() = makeSafe(Build.VERSION.RELEASE)

    private fun makeSafe(s: String?): String {
        return s ?: ""
    }

    /**
     * 获取手机设备名
     *
     * @return  手机设备名
     */
    val systemDevice: String
        get() = Build.DEVICE

    /**
     * 获取设备名
     *
     * @return  设备名
     */
    val systemProduct: String
        get() = Build.PRODUCT

    /**
     * 检测是否是中兴机器
     */
    val isZte: Boolean
        get() = deviceModel.lowercase(Locale.getDefault()).indexOf("zte") != -1

    /**
     * 判断是否是三星的手机
     */
    val isSamsung: Boolean
        get() = manufacturer.lowercase(Locale.getDefault()).indexOf("samsung") != -1

    /**
     * 检测是否HTC手机
     */
    val isHTC: Boolean
        get() = manufacturer.lowercase(Locale.getDefault()).indexOf("htc") != -1

    /**
     * 检测当前设备是否是特定的设备
     *
     * @param devices
     * @return
     */
    fun isDevice(vararg devices: String?): Boolean {
        val model = deviceModel.lowercase(Locale.getDefault())
        if (devices != null && model != null) {
            for (device in devices) {
                if (model.indexOf(device!!) != -1) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 检测当前设备是否是特定厂商
     *
     * @param devices
     * @return
     */
    fun isManufacturer(vararg devices: String?): Boolean {
        val model = manufacturer
        if (devices != null && model != null) {
            for (device in devices) {
                if (model.indexOf(device!!) != -1) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 获得设备型号
     *
     * @return
     */
    val deviceModel: String
        get() = trim(Build.MODEL)

    /**
     * 获取厂商信息
     */
    val manufacturer: String
        get() = trim(Build.MANUFACTURER)

    const val EMPTY = ""

    private fun trim(str: String?): String {
        return str?.trim { it <= ' ' } ?: EMPTY
    }

    /**
     * 判断是否是平板电脑
     *
     * @param context
     * @return
     */
    fun isTablet(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    /**
     * 检测是否是平板电脑
     *
     * @param context
     * @return
     */
    fun isHoneycombTablet(context: Context): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && isTablet(context)
    }

    /**
     * 获取CPU的信息
     *
     * @return
     */
    val cpuInfo: String?
        get() {
            var cpuInfo = ""
            try {
                if (File("/proc/cpuinfo").exists()) {
                    val fr = FileReader("/proc/cpuinfo")
                    val localBufferedReader = BufferedReader(fr, 8192)
                    cpuInfo = localBufferedReader.readLine()
                    localBufferedReader.close()
                    if (cpuInfo != null) {
                        cpuInfo = cpuInfo.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()[1].trim { it <= ' ' }
                            .split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()[0]
                    }
                }
            } catch (e: IOException) {
            } catch (e: Exception) {
            }
            return cpuInfo
        }

    /**
     * 判断是否支持闪光灯
     */
    fun isSupportCameraLedFlash(pm: PackageManager?): Boolean {
        if (pm != null) {
            val features = pm.systemAvailableFeatures
            if (features != null) {
                for (f in features) {
                    if (f != null && PackageManager.FEATURE_CAMERA_FLASH == f.name) //判断设备是否支持闪光灯
                        return true
                }
            }
        }
        return false
    }

    /**
     * 检测设备是否支持相机
     */
    fun isSupportCameraHardware(context: Context?): Boolean {
        return if (context != null && context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            true
        } else {
            // no camera on this device
            false
        }
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(context: Context): Int {
        val manager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val manager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels
        return outMetrics.heightPixels
    }

    fun getRealHeight(context: Context): Int {
        val manager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        manager.defaultDisplay.getRealMetrics(outMetrics)
        return outMetrics.heightPixels
    }

//    val androidID: String
//        get() = Settings.System.getString(
//            ContextHelper.getApplication().contentResolver,
//            Settings.System.ANDROID_ID
//        )

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    fun getVersionName(mContext: Context): String {
        var versionName = "1.0.0"
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionName =
                mContext.packageManager.getPackageInfo(mContext.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }

    /**
     * 获得软件的版本号
     *
     * @param context
     * @return
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

    fun getDpi(context: Context): Int {
        val dm = context.resources.displayMetrics
        //        int width=dm.widthPixels;
//        int height=dm.heightPixels;
//        double x = Math.pow(width,2);
//        double y = Math.pow(height,2);
//        double diagonal = Math.sqrt(x+y);
        //        double screenInches = diagonal/(double)dens;//对角线
        return dm.densityDpi
    }


    /***
     * 获取 当前应用包名
     */
    fun getAppProcessName(context: Context): String {

//当前应用pid
        val pid = Process.myPid()
        //任务管理类
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //遍历所有应用
        val infos = manager.runningAppProcesses
        for (info in infos) {
            if (info.pid == pid) //得到当前应用
                return info.processName //返回包名
        }
        return ""
    }


}