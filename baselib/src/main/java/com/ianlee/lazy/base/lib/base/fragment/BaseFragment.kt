package com.ianlee.lazy.base.lib.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.ianlee.lazy.base.lib.view.LoadingDialog
import com.ianlee.lazy.base.lib.view.NormalPopup
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function : fragment 基类
 */
abstract class BaseFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    protected var TAG = javaClass.simpleName

    protected lateinit var curActivity: FragmentActivity

    lateinit var loadingDialog: LoadingDialog

    lateinit var normalPopup: NormalPopup

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract fun layout(): Any

    override fun onAttach(context: Context) {
        super.onAttach(context)
        curActivity = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (layout() is Int) {
            inflater.inflate(layout() as Int, container, false)
        } else if (layout() is View) {
            layout() as View
        } else {
            null
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(curActivity)
        normalPopup = NormalPopup(curActivity)
        initView(savedInstanceState)
        initListener()
        createObserver()
        initData()

    }


    /**
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)


    /**
     * 创建观察者
     */
    abstract fun createObserver()

    /**
     * Fragment执行onCreate后触发的方法
     */
    open fun initData() {}
    open fun initListener() {}

    //允许
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

    }

    //拒绝
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @ColorInt
    protected fun getColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(requireActivity(), colorRes)
    }


    open fun showLoadingDialog(isAuto: Boolean = true, msg:String ="") {
        MainScope().launch {
            loadingDialog.setLoading(msg)
            loadingDialog.show()
            if (isAuto) {
                delay(4000)
                dismissLoadingDialog()
            }
        }
    }


    open fun dismissLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }


}