package com.yc.shadowjetpack.net

import retrofit2.http.*

/**
 * Creator: yc
 * Date: 2022/9/29 11:09
 * UseDes:
 */
interface ApiService {
    /**
     * 获取已完成任务列表
     */
    @GET("clientPackage/getClientVersion")
    suspend fun getVersion(
        @Query("clientType") clientType: String?,
        @Query("channel") channel: String?
    ): Any
}