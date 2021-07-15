package com.yc.jetpacklib.net

import com.yc.jetpacklib.init.YcJetpack
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Creator: yc
 * Date: 2021/6/8 10:45
 * UseDes:网络请求
 */
class YcRetrofitUtil private constructor() {
    companion object {
        val Instance = RetrofitUtilHolder.holder
    }

    private object RetrofitUtilHolder {
        val holder = YcRetrofitUtil()
    }

    private var mRetrofit: Retrofit? = null
    private fun createClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(YcInterceptorError())
            addInterceptor(YcInterceptorLog())
            for (interceptor in YcJetpack.mInstance.mInterceptor) {
                addInterceptor(interceptor)
            }
            connectTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
        }.build()
    }

    private fun createRetrofit(baseUrl: String = YcJetpack.mInstance.mDefaultBaseUrl): Retrofit {
        return Retrofit.Builder()
            .client(createClient())
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }


    fun <T> createRetrofitAndGetApiService(service: Class<T>?, url: String): T {
        return createRetrofit(url).create(service)
    }

    fun <T> getApiService(service: Class<T>?): T {
        if (mRetrofit == null) {
            mRetrofit = createRetrofit()
        }
        return mRetrofit!!.create(service)
    }
}