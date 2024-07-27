package com.ianlee.lazy.base.lib.base

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Process
import android.text.TextUtils
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.ianlee.lazy.base.lib.language.MultiLanguages
import com.ianlee.lazy.base.lib.network.utils.LauncherTimer
import com.ianlee.lazy.base.lib.network.utils.LogUtils
import com.ianlee.lazy.base.lib.utils.DeviceUtils
import com.ianlee.lazy.base.lib.utils.MMKVUtil
import java.util.Locale
import kotlin.system.exitProcess

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
open class BaseApp : Application(), ViewModelStoreOwner , Thread.UncaughtExceptionHandler {


    private lateinit var mAppViewModelStore: ViewModelStore

    private var mFactory: ViewModelProvider.Factory? = null

    private var sDebug = false



    /**
     * 当前语言
     */
    private var curLocal: String? = null

    override fun attachBaseContext(base: Context?) {
        LauncherTimer.logStart()

        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        MMKVUtil.initialize()
        // 初始化语种切换框架
        com.ianlee.lazy.base.lib.language.MultiLanguages.init(this)
        setLanguage()
        curLocal = resources.configuration.locale.language
        getLanguage()
        LogUtils.debugInfo(
            "语言：" + curLocal +
                    Build.DEVICE + ",BaseApp dpi==" + DeviceUtils.getDpi(this) +
                    "高=" + DeviceUtils.getRealHeight(this) + "屏幕高=" + DeviceUtils.getScreenHeight(
                this
            ) + "==宽=" + DeviceUtils.getScreenWidth(this) +
                    "==最小宽度=" + resources.configuration.smallestScreenWidthDp + "，getCurrentProcessName=" + com.ianlee.lazy.base.lib.base.BaseApp.Companion.getCurrentProcessName(
                this
            )
        )


        mAppViewModelStore = ViewModelStore()
        object : Thread() {
            override fun run() {
                //界面加载管理 初始化
//                LoadSir.beginBuilder()
//                    .addCallback(LoadingCallback()) //加载
//                    .addCallback(ErrorCallback()) //错误
//                    .addCallback(EmptyCallback()) //空
//                    .setDefaultCallback(SuccessCallback::class.java) //设置默认加载状态页
//                    .commit()
            }
        }.start()

    }

    private fun setLanguage() {
        val languageNoDef: String = MMKVUtil.getLanguage()
        if (TextUtils.isEmpty(languageNoDef)) {
            val locale = Locale.getDefault().language
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {

        //监听属性变化，避免activity切换时dpi变化的问题
//        if (AppInitManager.getInstance().isAutoSize())
//        AnyFitAutoAdaptStrategy.setCustomDensity(this);
        getLanguage()
        super.onConfigurationChanged(newConfig)

    }

    private fun getLanguage() {
        val metrics = resources.displayMetrics
        val configuration = resources.configuration
        val language = MMKVUtil.getLanguage()
        val locale: Locale
        locale = when (language) {
            "zh" -> Locale.SIMPLIFIED_CHINESE
            "de" -> Locale.GERMAN
            else -> Locale.ENGLISH
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }
        LogUtils.debugInfo("语言设置" + locale.language)
        resources.updateConfiguration(configuration, metrics)
        com.ianlee.lazy.base.lib.network.utils.ContextHelper.setApplication(this)
    }


    companion object {

        var activityFragmentManagerOnResumeMap = HashMap<String, Long>() //activity fragment 进入管理器
        var activityFragmentManagerOnPauseMap = HashMap<String, Long>() //activity fragment 离开管理器

        val UPDATE_STATUS_ACTION = "callback.action.UPDATE_STATUS"
        private var sInstance: com.ianlee.lazy.base.lib.base.BaseApp? = null
        private var sDebug = false

        /**
         * 获得当前app运行的Application
         */
        val instance: com.ianlee.lazy.base.lib.base.BaseApp
            get() {
                if (com.ianlee.lazy.base.lib.base.BaseApp.Companion.sInstance == null) {
                    throw NullPointerException(
                        "please inherit BaseApplication or call setApplication."
                    )
                }
                return com.ianlee.lazy.base.lib.base.BaseApp.Companion.sInstance as com.ianlee.lazy.base.lib.base.BaseApp
            }

        /**
         * 获取进程名
         *
         * @param context
         * @return
         */
        fun getCurrentProcessName(context: Context): String? {
            val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            val runningApps = am.runningAppProcesses ?: return null
            for (proInfo in runningApps) {
                if (proInfo.pid == Process.myPid()) {
                    if (proInfo.processName != null) {
                        return proInfo.processName
                    }
                }
            }
            return null
        }


    }



    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }

    open fun startActivity(activity: Activity, cls: Class<*>?) {
        val intent = Intent(activity, cls)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
        activity.finish()
    }

    override val viewModelStore: ViewModelStore
        get() = mAppViewModelStore

    override fun uncaughtException(t: Thread, e: Throwable) {
        exitProcess(0)

    }

}