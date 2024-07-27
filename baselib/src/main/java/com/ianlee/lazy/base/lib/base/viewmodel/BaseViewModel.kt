package com.ianlee.lazy.base.lib.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.RequestBody
import com.ianlee.lazy.base.lib.base.model.AppVersionInfoModel
import com.ianlee.lazy.base.lib.base.model.LanguageListBean
import com.ianlee.lazy.base.lib.callback.livedata.event.EventLiveData
import com.ianlee.lazy.base.lib.network.ApiClientLib
import com.ianlee.lazy.base.lib.network.requestT
import com.ianlee.lazy.base.lib.network.utils.ContextHelper
import com.ianlee.lazy.base.lib.utils.DeviceUtils
import com.ianlee.lazy.base.lib.utils.MMKVUtil

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
open class BaseViewModel : ViewModel() {


    //页码  1开始
    var pageIndex = 1
    var submitBtn: Boolean = false

    /**基础返回值*/

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }


    var appVersionResult = MutableLiveData<AppVersionInfoModel?>()

    var languageListBeanResult = MutableLiveData<LanguageListBean?>()


    /** 版本检测*/
    fun checkVersion(type: Int = 4) {
        requestT(
            {
                ApiClientLib.apiServiceCheckVersion.checkAppVersion(
                    DeviceUtils.getVersionCode(
                        com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication()
                    ),
                    type = type,
                )
            }, {
                appVersionResult.value = it.data
            }, {
                appVersionResult.value = null

            }
        )
    }


    /**
     * 获取语言列表
     */
    fun getLanguageIndex() {

        requestT(
            {
                ApiClientLib.apiServiceCheckVersion.getLanguageIndex()
            }, {
                languageListBeanResult.value = it.data
            }, {

            }
        )
    }


    /**
     * 内置封装好的可通知Activity/fragment 显示隐藏加载框
     */
    inner class UiLoadingChange {
        //显示加载框
        val showDialog by lazy { EventLiveData<String>() }

        //隐藏
        val dismissDialog by lazy { EventLiveData<Boolean>() }
    }

    fun addParamsLogin(params: MutableMap<String, String>): RequestBody {

        params["language"] = MMKVUtil.getLanguage()

        val body = FormBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(params)
        )
        return body
    }

    fun addParams(params: MutableMap<String, String>): RequestBody {

        params["language"] = MMKVUtil.getLanguage()
        if (MMKVUtil.getAppletId().isNotEmpty()) {
            params["applet_id"] = MMKVUtil.getAppletId()
        }
        if (MMKVUtil.getToken().isNotEmpty()) {
            params["token"] = MMKVUtil.getToken()
        }

        val body = FormBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(params)
        )
        return body
    }
}

