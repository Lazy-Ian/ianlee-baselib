package com.ianlee.lazy.base.lib.network

import com.ianlee.lazy.base.lib.base.model.AppVersionInfoModel
import com.ianlee.lazy.base.lib.base.model.LanguageListBean
import com.ianlee.lazy.base.lib.base.model.NetResponse
import com.ianlee.lazy.base.lib.utils.MMKVUtil
import palm.catering.kds.network.params.MultilingualInfoParams
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
interface ApiServiceLib {

    /**
     *获取更新版本
     */
    @GET("getAppVersion")
    suspend fun checkAppVersion(
        @Query("version_code") version_code: Int,
        @Query("type") type: Int,
        @Query("language") lang: String = MMKVUtil.getLanguage()
    ): NetResponse<AppVersionInfoModel>

    @GET("language_index")
    suspend fun getLanguageIndex(
        @Query("language") lang: String = MMKVUtil.getLanguage()
    ): NetResponse<LanguageListBean>

    /**
     * http://192.168.3.15:8069/
     * 批量获取多语言记录
     *  {
    "program": "云餐", //id:1
    "client": "后厨端APP", //id:5
    "line": []
    }
     */
    @POST("international_api/android/batch_download")
    suspend fun batch_download(
        @Body body: MultilingualInfoParams
    ): NetResponse<MultilingualInfoParams>

    /**
     * 批量上传多语言记录
     *    {"program": "云餐","client": "后厨端APP","line": [{"index":""} ]
    }
     */
    @POST("international_api/android/batch_upload")
    suspend fun batch_upload(
        @Body body: MultilingualInfoParams
    ): MultilingualInfoParams


}