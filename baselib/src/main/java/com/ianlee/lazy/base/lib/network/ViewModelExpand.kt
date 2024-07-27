package com.ianlee.lazy.base.lib.network

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.ianlee.lazy.base.lib.base.viewmodel.BaseViewModel
import com.ianlee.lazy.base.lib.network.utils.ContextHelper
import com.ianlee.lazy.base.lib.network.utils.ExceptionHandle
import com.ianlee.lazy.base.lib.network.utils.LogUtils
import com.ianlee.lazy.base.lib.network.utils.showToast
import java.lang.reflect.ParameterizedType
import java.net.ContentHandler

fun <T> ViewModel.requestT(
    block: suspend () -> T,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
): Job {
    //如果需要弹窗 通知Activity/fragment弹窗
    return viewModelScope.launch {
        runCatching {
            //请求体
            block()
        }.onSuccess {
            //成功回调
            success(it)
        }.onFailure {
            //失败回调
            try {
//                ContextHelper.getApplication().showToast(it.message)
                LogUtils.debugInfo("onFailure::"+it.toString())
                error(ExceptionHandle.handleException(it))
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    NetErrorHandler.errorHandle(e)
                }
            }
        }
    }
}


@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}


/**
 * 隐藏软键盘
 */
fun hideSoftKeyboard(context: Context?) {
    context?.let { act ->
        val view = (act as Activity).currentFocus
        view?.let {
            val inputMethodManager =
                act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}


