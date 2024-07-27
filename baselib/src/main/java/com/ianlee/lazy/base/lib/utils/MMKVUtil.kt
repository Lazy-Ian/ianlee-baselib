package com.ianlee.lazy.base.lib.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import com.ianlee.lazy.base.lib.network.params.StyleColorBean
import com.ianlee.lazy.base.lib.network.utils.ContextHelper
import com.ianlee.lazy.base.lib.network.utils.LogUtils
import java.util.concurrent.ConcurrentHashMap


/**
 * Created by Ian on 2024/3/20
 * Email: yixin0212@qq.com
 * Function :
 */
object MMKVUtil {

    val Setting = "Setting"

    val CartListCache = "CartListCache"//购物车列表

    val SHOP_INFO_CACHE = "Shop_info"//店铺
    val PAY_TYPE_INFO = "PAY_TYPE_INFO"//店铺

    val GoodsListCache = "GoodsListCache"//商品列表
    val GoodsDetailCache = "GoodsDetailCache"//商品详情

    val OrderListCache = "OrderListCache"//订单列表
    val OrderDetailCache = "OrderDetailCache"//订单详情

    val TableListCache = "OrderListCache"//桌台列表

    private val ENABLE_NETWORK_MODE = "enable_network_mode" // 开启网络模式
    private val LANGUAGE_SELECT = "language_select" //多语言 en es zh

    private val LANGUAGE_SELECT_NAME = "language_select_name" //多语言  名称
    private val LANGUAGE_SELECT_ICON = "language_select_icon" //多语言 图片

    private val USER_ACCOUNT = "user_account" //
    private val USER_PSD = "user_psd" //
    private val TOKEN = "token" //
    private val USER_INFO_STRING = "userInfo" //
    private val APPLET_ID = "applet_id" // 店铺id
    private val MONEY_UNIT = "money_unit" // 金额


    private val USER_NAME = "userName" //
    private val IS_LAVEL_PRINTER = "isLabelPrinter" // 是小票打印机不是80
    private val PRINTER_TYPE = "PrinterType" // 打印类型 标签60 标签40  小票0
    private val SHOP_ID = "shop_id" // 店铺id
    private val DEVICE_ID = "device_id" // 设备id
    private val ORDER_TYPES = "order_types" // 订单类型
    private val PAGE_MODE = "pageMode" // 订单类型

    private val KDS_BASE_URL = "base_url" //

    private val STYLE_COLORL = "style_color" //


    private var rootDir: String? = null
    private var mmkvHashMap: ConcurrentHashMap<String, MMKV>? = null


    fun initialize() {
        initialize(com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication())
        mmkvHashMap = ConcurrentHashMap<String, MMKV>()
        LogUtils.debugInfo("mmkv root: $rootDir")
    }


    private fun initialize(context: Context?) {
        rootDir = MMKV.initialize(
            context,
            com.ianlee.lazy.base.lib.network.utils.ContextHelper.getApplication().getExternalFilesDir(null)?.absolutePath
        )
        LogUtils.debugInfo("mmkv root: $rootDir")
    }

    fun mmkvWithID(mmapID: String): MMKV {
        var mmkv: MMKV? = mmkvHashMap?.get(mmapID)
        if (mmkv == null) {
            mmkv = MMKV.mmkvWithID(mmapID, MMKV.MULTI_PROCESS_MODE)
            mmkvHashMap?.put(mmapID, mmkv)
        }
        return mmkv!!
    }

    /**
     * 单线程
     *
     * @param mmapID
     * @return
     */
    fun mmkvWithIDSolo(mmapID: String): MMKV? {
        var mmkv: MMKV? = mmkvHashMap?.get(mmapID)
        if (mmkv == null) {
            mmkv = MMKV.mmkvWithID(mmapID, MMKV.SINGLE_PROCESS_MODE)
            mmkvHashMap?.put(mmapID, mmkv)
        }
        return mmkv
    }

    /**
     * 快捷设置语言
     *
     * @param language zh en es
     */
    fun putLanguage(language: String?, languageName: String, languageIcon: String): Boolean {
        var language = language
        if (language == null) {
            language = "en"
        }
        mmkvWithID(Setting).encode(LANGUAGE_SELECT_ICON, languageIcon)
        mmkvWithID(Setting).encode(LANGUAGE_SELECT_NAME, languageName)
        return mmkvWithID(Setting).encode(LANGUAGE_SELECT, language)
    }


    /**
     * 快捷获取 语言
     *
     * @return
     */
    fun getLanguage(): String {
        return mmkvWithID(Setting).decodeString(
            LANGUAGE_SELECT,
            "zh"
        )!!
    }

    fun getLanguageName(): String {
        return mmkvWithID(Setting).decodeString(
            LANGUAGE_SELECT_NAME,
            "ENGLISH"
        )!!
    }

    fun getLanguageIcon(): String {
        return mmkvWithID(Setting).decodeString(
            LANGUAGE_SELECT_ICON,
            ""
        )!!
    }

    /**
     * 保存token
     */
    fun putToken(token: String?): Boolean {
        return mmkvWithID(Setting).encode(TOKEN, token)
    }

    fun putUserAccountPsd(userAccount: String?, userPsd: String?): Boolean {
        mmkvWithID(Setting).encode(USER_PSD, userPsd)
        return mmkvWithID(Setting).encode(USER_ACCOUNT, userAccount)
    }

    fun getUserAccount(): String {
        return mmkvWithID(Setting).decodeString(
            USER_ACCOUNT,
            ""
        )!!
    }

    fun getUserPsd(): String {
        return mmkvWithID(Setting).decodeString(
            USER_PSD,
            ""
        )!!
    }

    /**
     * 获取token
     */
    fun getToken(): String {
        return mmkvWithID(Setting).decodeString(
            TOKEN,
            ""
        )!!
    }


    fun getStyleColor(): StyleColorBean? {
        var string = mmkvWithID(Setting).decodeString(STYLE_COLORL, "")!!
        if (string.isNotEmpty()) {
            return Gson().fromJson(string, StyleColorBean::class.java)
        }
        return null
    }

    fun putStyleColor(value: Any): Boolean {
        LogUtils.debugInfo("---putStyleColor:" + value.toString())
        val json: String = Gson().toJson(value)
        return mmkvWithID(Setting).encode(STYLE_COLORL, json)
    }


    /**
     * 保存用户信息
     */
    fun putUserInfoString(userInfoString: String): Boolean {
        return mmkvWithID(Setting).encode(USER_INFO_STRING, userInfoString)
    }

    /**
     *  获取用户信息
     */
    fun getUserInfoString(): String {
        return mmkvWithID(Setting).decodeString(
            USER_INFO_STRING,
            ""
        )!!
    }


    fun putOrderTypes(value: Any): Boolean {
        val json: String = Gson().toJson(value)
        return mmkvWithID(Setting).encode(ORDER_TYPES, json)
    }

    fun getOrderTypes(): MutableList<String> {
        var string = mmkvWithID(Setting).decodeString(ORDER_TYPES, "")!!
        if (string.isNotEmpty()) {
            val result: MutableList<String> =
                Gson().fromJson(string, object : TypeToken<MutableList<String>>() {}.type)
            return result
        }
        return arrayListOf()
    }

    fun putUserName(token: String?): Boolean {
        return mmkvWithID(Setting).encode(USER_NAME, token)
    }

    /**
     * 获取名称
     */
    fun getUserName(): String {
        return mmkvWithID(Setting).decodeString(
            USER_NAME,
            ""
        )!!
    }


    /**
     * 设置网络模式
     */
    fun putEnableNetworkMode(isEnable: Boolean): Boolean {
        return mmkvWithID(Setting).encode(ENABLE_NETWORK_MODE, isEnable)
    }

    /**
     * 获取网络模式
     */
    fun isEnableNetworkMode(): Boolean {
        return mmkvWithID(Setting).decodeBool(
            ENABLE_NETWORK_MODE,
            true
        )!!
    }


    /**
     * 设置店铺id
     */
    fun putAppletId(applet_id: String): Boolean {
        return mmkvWithID(Setting).encode(APPLET_ID, applet_id)
    }

    /**
     *  获取店铺id
     */
    fun getAppletId(): String {
        return mmkvWithID(Setting).decodeString(
            APPLET_ID,
            ""
        )!!
    }

    /**
     * 金额
     */
    fun putMoneyUnit(applet_id: String): Boolean {
        return mmkvWithID(Setting).encode(MONEY_UNIT, applet_id)
    }

    /**
     *  金额
     */
    fun getMoneyUnit(): String {
        return mmkvWithID(Setting).decodeString(
            MONEY_UNIT,
            ""
        )!!
    }

    /**
     * 设置店铺id
     */
    fun putShopId(shop_id: String?): Boolean {
        LogUtils.debugInfo("shop_id::" + shop_id)
        return mmkvWithID(Setting).encode(SHOP_ID, shop_id)
    }


    /**
     *  获取店铺id
     */
    fun getShopId(): String {
        return mmkvWithID(Setting).decodeString(
            SHOP_ID,
            ""
        )!!
    }

    fun putDeviceId(device_id: String?): Boolean {
        LogUtils.debugInfo("shop_id::" + device_id)
        return mmkvWithID(Setting).encode(DEVICE_ID, device_id)
    }


    /**
     *  设备id
     */
    fun getDeviceId(): String {
        return mmkvWithID(Setting).decodeString(
            DEVICE_ID,
            ""
        )!!
    }

    fun putCallPageMode(isCallPageMode: Boolean): Boolean {
        LogUtils.debugInfo("isCallPageMode::" + isCallPageMode)
        return mmkvWithID(Setting).encode(PAGE_MODE, isCallPageMode)
    }


    /**
     *
     */
    fun isCallPageMode(): Boolean {
        return mmkvWithID(Setting).decodeBool(
            PAGE_MODE,
            true
        )!!
    }

    /**
     * 打印机为小票
     * @param labelType  60 40 0
     */
    fun putLabelPrinter(labelType: String): Boolean {
        return mmkvWithID(Setting).encode(PRINTER_TYPE, labelType)
    }

    /**
     * 是 标签60 标签40 小票0
     */
    fun labelTypePrinter(): String {
        return mmkvWithID(Setting).decodeString(
            PRINTER_TYPE,
            "30"
        )!!
    }

    fun putBaseUrl(env: String, url: String): Boolean {
        return mmkvWithID(Setting).encode(env + KDS_BASE_URL, url)
    }

    fun getBaseUrl(env: String): String {
        var baseUrl = mmkvWithID(Setting).decodeString(env + KDS_BASE_URL, "")!!

        LogUtils.debugInfo("getBaseUrl::env:$env:   url:$baseUrl")
        return baseUrl
    }




}