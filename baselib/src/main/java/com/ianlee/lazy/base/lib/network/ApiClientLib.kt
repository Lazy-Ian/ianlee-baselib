package com.ianlee.lazy.base.lib.network

import android.os.Build
import com.google.gson.GsonBuilder
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.RequestBody
import org.apache.http.conn.ssl.AllowAllHostnameVerifier
import com.ianlee.lazy.base.lib.network.logging.LogInterceptor
import com.ianlee.lazy.base.lib.network.utils.LogUtils
import com.ianlee.lazy.base.lib.network.utils.TimeUtils
import com.ianlee.lazy.base.lib.utils.MMKVUtil
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
object ApiClientLib {

    private const val TAG = "--ApiClient--"

    //多语言请求接口 正式
    private const val BASE_URL_LANGUAGE = "https://admin.palmnet.co/"

    private var BASE_URL_CHECK_VERSION = "https://food.thepalmnet.com/api/common.index/"

    fun getCheckVersionBaseUrl(url: String) {
        if (url.contains("api/")) {
            BASE_URL_CHECK_VERSION = url + "common.index/"
        } else {
            BASE_URL_CHECK_VERSION = url + "api/common.index/"
        }
    }

    //多语言相关
    val apiServiceLanguage: ApiServiceLib by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_LANGUAGE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
        return@lazy retrofit.create(ApiServiceLib::class.java)
    }

    //版本检查更新功能
    val apiServiceCheckVersion: ApiServiceLib by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_CHECK_VERSION)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
        return@lazy retrofit.create(ApiServiceLib::class.java)
    }

    /**
     * 批量删除请求
     *
     * @param tag 标签
     */
    fun cancelCallWithTag(tag: String) {
        // 等待队列
        for (call in client.dispatcher().queuedCalls()) {
            // 注意，不能用 tag()
            if (call.request().tag(String::class.java) == tag) {
                call.cancel()
            }
        }
        // 请求队列
        for (call in client.dispatcher().runningCalls()) {
            // 注意，不能用 tag()
            if (call.request().tag(String::class.java) == tag) {
                call.cancel()

            }
        }
    }


    /**
     * 全局 Retrofit 对象
     */
    val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .protocols(listOf(Protocol.HTTP_1_1))
            .addInterceptor(LogInterceptor())   // 日志拦截器
            .addInterceptor(myHeadInterceptor())  //示例：添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
            .hostnameVerifier(AllowAllHostnameVerifier())
            .addInterceptor(addQueryParameterInterceptor())  //示例：添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
            .build()
    }
    val enc = "UTF-8"


    private fun myHeadInterceptor(): Interceptor {

        return Interceptor { chain: Interceptor.Chain ->
            val builder = chain.request().newBuilder()
                .addHeader("osVersion", Build.VERSION.RELEASE)
                .addHeader("mobileBrand", Build.BRAND)
                .addHeader("Token", MMKVUtil.getToken())
                .addHeader("language", MMKVUtil.getLanguage())
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", getUserAgent()).build()
            chain.proceed(builder)
        }

    }

    private fun getUserAgent(): String {
        return System.getProperty("http.agent")!! + " ksd/1.0.0"
    }

    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val timestamp = TimeUtils.currentTimeInLong.toString()//时间戳
            var request = chain.request()
            val oldBody: RequestBody? = request.body()

            if (request.method() == "POST") {
                if (oldBody is FormBody) {
                    val formBodyBuilder = FormBody.Builder(Charset.forName(enc))
                    for (index in 0 until oldBody.size()) {
                        if (oldBody.encodedName(index).equals("imageDate") || oldBody.encodedName(
                                index
                            ).equals("userIcon")
                        ) {
                            formBodyBuilder.addEncoded(
                                oldBody.encodedName(index),
                                oldBody.encodedValue(index)
                            )
                        } else {
                            formBodyBuilder.addEncoded(
                                oldBody.encodedName(index),
                                URLDecoder.decode(oldBody.encodedValue(index), enc)
                            )
                        }

                    }
                    formBodyBuilder.addEncoded("platform", "Android") //平台
                    formBodyBuilder.addEncoded("timestamp", timestamp) //时间戳


                    request = request.newBuilder()
                        .header("timestamp", timestamp) //时间戳

                        .post(formBodyBuilder.build()).build()
                    LogUtils.debugInfo("-----formBodyBuilder-------", formBodyBuilder.toString())
                    chain.proceed(request)
                } else {
                    LogUtils.debugInfo("-----POST-------", request.toString())
                    chain.proceed(request)
                }
            } else {

                chain.proceed(request)
            }
        }

    }


}