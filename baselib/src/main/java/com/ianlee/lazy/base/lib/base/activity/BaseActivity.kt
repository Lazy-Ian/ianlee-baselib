package com.ianlee.lazy.base.lib.base.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.jessyan.autosize.AutoSizeCompat
import com.ianlee.lazy.base.lib.base.action.ActivityAction
import com.ianlee.lazy.base.lib.base.action.BundleAction
import com.ianlee.lazy.base.lib.base.action.ClickAction
import com.ianlee.lazy.base.lib.base.action.HandlerAction
import com.ianlee.lazy.base.lib.base.action.KeyboardAction
import com.ianlee.lazy.base.lib.language.MultiLanguages
import com.ianlee.lazy.base.lib.network.params.StyleColorBean
import com.ianlee.lazy.base.lib.network.utils.LogUtils
import com.ianlee.lazy.base.lib.utils.MMKVUtil
import com.ianlee.lazy.base.lib.utils.glide.GlideUtils
import com.ianlee.lazy.base.lib.view.LoadingDialog
import com.ianlee.lazy.base.lib.view.NormalPopup
import pub.devrel.easypermissions.EasyPermissions
import java.util.Locale
import java.util.Random
import kotlin.math.pow


/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */

abstract class BaseActivity : AppCompatActivity(), ActivityAction, ClickAction, HandlerAction,
    BundleAction, KeyboardAction, EasyPermissions.PermissionCallbacks {
    var TAG = "onKeyDown"

    lateinit var normalPopup: NormalPopup

    companion object {
        var styleColorBean: StyleColorBean? = null

        /** 错误结果码 */
        const val RESULT_ERROR: Int = -2
    }

    /** Activity 回调集合 */
    private val activityCallbacks: SparseArray<com.ianlee.lazy.base.lib.base.activity.BaseActivity.OnActivityCallback?> by lazy {
        SparseArray(
            1
        )
    }
    lateinit var loadingDialog: LoadingDialog
    lateinit var launcher: ActivityResultLauncher<Intent>

    open fun showLoadingDialog(timeMillis: Long = 4000) {
        MainScope().launch {
            loadingDialog.show()
            delay(timeMillis)
            dismissLoadingDialog()
        }
    }

    open fun dismissLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog(this)
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
                if (result?.resultCode == RESULT_OK) {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        initActivity()
        if (MMKVUtil.getStyleColor() != null) {
            styleColorBean = MMKVUtil.getStyleColor()
        }
        normalPopup = NormalPopup(this)

    }

    override fun onResume() {
        super.onResume()

        LogUtils.debugInfo("onResume::" + this@BaseActivity.localClassName)
    }

    override fun onStart() {
        super.onStart()
//        getLanguage()
    }

    protected open fun initActivity() {
        initLayout()
        initView()
        initData()
    }

    fun setLogo(ivLogo: ImageView) {
        if (styleColorBean != null && styleColorBean!!.info != null && styleColorBean!!.info.logo_big != null) {
            com.ianlee.lazy.base.lib.utils.glide.GlideUtils.lImg(getContext(), styleColorBean!!.info.logo_big.url, ivLogo)

        }
    }

    fun getLanguage() {
        val language: String = MMKVUtil.getLanguage()
        val locale: Locale = when (language) {
            "zh" -> Locale.SIMPLIFIED_CHINESE
            "es" -> Locale("es")
            "ca" -> Locale("ca")
            else -> Locale.ENGLISH
        }
        // 本地语言设置
        val res = resources
        val dm = res!!.displayMetrics
        val conf = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        val metrics = resources!!.displayMetrics
        val configuration = resources!!.configuration
        val language = MMKVUtil.getLanguage()
        val locale: Locale = when (language) {
            "zh" -> Locale.SIMPLIFIED_CHINESE
            "es" -> Locale("es")
            else -> Locale.ENGLISH
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }
        LogUtils.debugInfo("语言设置" + locale.language)
        resources!!.updateConfiguration(newConfig, metrics)

        super.onConfigurationChanged(newConfig)

    }

    override fun attachBaseContext(newBase: Context?) {
        // 绑定语种
        super.attachBaseContext(com.ianlee.lazy.base.lib.language.MultiLanguages.attach(newBase))
    }


    override fun getResources(): Resources? {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources()) //如果没有自定义需求用这个方法
        }
        //需要升级到 v1.1.2 及以上版本才能使用 AutoSizeCompat
        return super.getResources()
    }

    /**
     * 获取布局，支持两种，1 layoutID，2 View
     */
    protected abstract fun getLayout(): Any

    /**
     * 初始化控件
     */
    protected abstract fun initView()

    /**
     * 初始化数据
     */
    protected abstract fun initData()


    /**
     * 初始化布局
     */
    protected open fun initLayout() {
        try {
            if (getLayout() is Int) {
                setContentView(getLayout() as Int)
            } else {
                setContentView(getLayout() as View)
            }
            initSoftKeyboard()
        } catch (e: Exception) {

        }
    }

    /**
     * 初始化软键盘
     */
    protected open fun initSoftKeyboard() {
        // 点击外部隐藏软键盘，提升用户体验
        getContentView()?.setOnClickListener {
            LogUtils.debugInfo("hideKeyboard(currentFocus)")
            // 隐藏软键，避免内存泄漏
            hideKeyboard(currentFocus)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeCallbacks()
    }

    fun finishFade() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
        // 隐藏软键，避免内存泄漏
        hideKeyboard(currentFocus)
    }

    /**
     * 如果当前的 Activity（singleTop 启动模式） 被复用时会回调
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // 设置为当前的 Intent，避免 Activity 被杀死后重启 Intent 还是最原先的那个
        setIntent(intent)
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }

    /**
     * 和 setContentView 对应的方法
     */
    open fun getContentView(): ViewGroup? {
        return findViewById(Window.ID_ANDROID_CONTENT)
    }

    override fun getContext(): Context {
        return this
    }

    override fun startActivity(intent: Intent) {
        return super<AppCompatActivity>.startActivity(intent)
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，
     * 来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return if (event.x > left && event.x < right && event.y > top && event.y < bottom) {
                // 点击EditText的事件，忽略它。
                false
            } else {
                true
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    private fun hideKeyboard(token: IBinder?) {
        if (token != null) {
            val im: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v?.windowToken)
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    @Suppress("deprecation")
    override fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        // 隐藏软键，避免内存泄漏
        hideKeyboard(currentFocus)
        // 查看源码得知 startActivity 最终也会调用 startActivityForResult
        super.startActivityForResult(intent, requestCode, options)
    }

    /**
     * startActivityForResult 方法优化
     */
    open fun startActivityForResult(
        clazz: Class<out Activity>,
        callback: com.ianlee.lazy.base.lib.base.activity.BaseActivity.OnActivityCallback?
    ) {
        startActivityForResult(Intent(this, clazz), null, callback)
    }

    open fun startActivityForResult(
        intent: Intent,
        callback: com.ianlee.lazy.base.lib.base.activity.BaseActivity.OnActivityCallback?
    ) {
        startActivityForResult(intent, null, callback)
    }

    @Suppress("deprecation")
    open fun startActivityForResult(
        intent: Intent,
        options: Bundle?,
        callback: com.ianlee.lazy.base.lib.base.activity.BaseActivity.OnActivityCallback?
    ) {
        // 请求码必须在 2 的 16 次方以内
        val requestCode: Int = Random().nextInt(2.0.pow(16.0).toInt())
        activityCallbacks.put(requestCode, callback)
        startActivityForResult(intent, requestCode, options)
    }

    @Suppress("deprecation")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var callback: com.ianlee.lazy.base.lib.base.activity.BaseActivity.OnActivityCallback?
        if ((activityCallbacks.get(requestCode).also { callback = it }) != null) {
            callback?.onActivityResult(resultCode, data)
            activityCallbacks.remove(requestCode)
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    interface OnActivityCallback {

        /**
         * 结果回调
         *
         * @param resultCode        结果码
         * @param data              数据
         */
        fun onActivityResult(resultCode: Int, data: Intent?)
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }
}