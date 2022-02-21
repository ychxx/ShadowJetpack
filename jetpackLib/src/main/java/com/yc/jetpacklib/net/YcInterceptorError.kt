package com.yc.jetpacklib.net

import android.text.TextUtils
import com.yc.jetpacklib.data.constant.YcNetErrorCode
import com.yc.jetpacklib.exception.YcIoException
import com.yc.jetpacklib.init.YcJetpack
import okhttp3.*
import okio.BufferedSource
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

/**
 * 日志拦截器
 * 功能：一些格式问题的json拦截处理
 */
class YcInterceptorError : YcInterceptor {
    private val UTF8 = Charset.forName("UTF-8")
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val response: Response = chain.proceed(request)
        val body = getFullResponse(response)
        try {
            if (body == null) {
                throw YcIoException("请求返回为空", YcNetErrorCode.REQUEST_NULL)
            } else {
                val jsonObject = JSONObject(body)
                val code: Int = when {
                    jsonObject.has("code") -> {
                        jsonObject.optInt("code")
                    }
                    jsonObject.has("status") -> {
                        jsonObject.optInt("status")
                    }
                    jsonObject.has("result") -> {
                        jsonObject.optInt("result")
                    }
                    else -> {
                        -2333
                    }
                }
                val msg = when {
                    jsonObject.has("message") -> {
                        jsonObject.optString("message")
                    }
                    jsonObject.has("msg") -> {
                        jsonObject.optString("msg")
                    }
                    else -> {
                        "接口异常! 无返回数据"
                    }
                }
                if (code != -2333) {
                    if (YcJetpack.mInstance.mNetSuccessCode != null) {
                        var isFail = true
                        for (successCode in YcJetpack.mInstance.mNetSuccessCode!!) {
                            if (successCode == code) {
                                isFail = false
                            }
                        }
                        if (isFail) {
                            throw YcIoException(msg, code)
                        }
                    }
//                    return response.newBuilder().body(msg.toResponseBody(response.body!!.contentType())).build()
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return response
    }

    /**
     * 获取到完整的响应数据(服务器返回数据)
     *
     * @param response
     * @return
     */
    private fun getFullResponse(response: Response): String? {
        val responseBody: ResponseBody? = response.body
        if (responseBody != null) {
            val source: BufferedSource = responseBody.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer
            var charset = UTF8
            val contentType = responseBody.contentType()
            if (contentType != null) {
                charset = try {
                    contentType.charset(UTF8)
                } catch (e: UnsupportedCharsetException) {
                    e.printStackTrace()
                    UTF8
                }
            }
            return buffer.clone().readString(charset)
        } else {
            return null
        }
    }
}