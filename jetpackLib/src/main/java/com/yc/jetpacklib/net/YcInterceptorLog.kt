package com.yc.jetpacklib.net

import com.yc.jetpacklib.init.YcJetpack
import com.yc.jetpacklib.extension.ycLogE
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okio.Buffer
import okio.BufferedSource
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

/**
 * 日志拦截器
 * 功能：
 * 1.打印网络请求和响应日志
 * 2.切换BaseUrl
 */
class YcInterceptorLog : YcInterceptor {
    private val UTF8 = Charset.forName("UTF-8")

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val realBaseUrl = getRealBaseUrl(request)
        realBaseUrl?.apply {
            request = newRequest(request, this)
        }
        ycLogE("请求方式:${request.method}\n网络请求:${getFullRequest(request)}")
        val response: Response = chain.proceed(request)
        ycLogE("请求方式:${request.method}\n返回数据:${getFullResponse(response)}")
        return response
    }

    /**
     * 获取表头里的baseUrl地址
     *
     * @param request
     * @return
     */
    private fun getRealBaseUrl(request: Request): String? {
        //获取request的创建者builder
        val builder: Request.Builder = request.newBuilder()
        //从request中获取headers，通过给定的键url_name
        val headerValues = request.headers(YcJetpack.OTHER_BASE_URL)
        return if (headerValues.isNotEmpty()) {
            //将配置的header删除，因为header仅用作app和okhttp之间使用
            builder.removeHeader(YcJetpack.OTHER_BASE_URL)
            headerValues[0]
        } else {
            null
        }
    }

    /**
     * 在旧的基础上创建新的Request
     *
     * @param oldRequest 旧的Request
     * @param newBaseUrl 新的Request地址
     * @return
     */
    private fun newRequest(oldRequest: Request, newBaseUrl: String): Request {
        //获取旧的HttpUrl
        val builder: Request.Builder = oldRequest.newBuilder()
        val oldHttpUrl: HttpUrl = oldRequest.url

        //根据newBaseUrl创建临时HttpUrl
        val tempHttpUrl: HttpUrl? = newBaseUrl.toHttpUrlOrNull()
        if (tempHttpUrl == null) {
            ycLogE("baseUrl替换失败：${newBaseUrl}--格式有误")
            return oldRequest
        }

        //通过tempHttpUrl修改旧的oldHttpUrl，生成新的HttpUrl
        val newHttpUrl: HttpUrl = oldHttpUrl
            .newBuilder()
            .scheme(tempHttpUrl.scheme)
            .host(tempHttpUrl.host)
            .port(tempHttpUrl.port)
            .build()
        //重建这个request
        return builder.url(newHttpUrl).build()
    }

    /**
     * 获取到完整的请求(地址+参数)
     *
     * @param request
     * @return
     */
    private fun getFullRequest(request: Request): String {
        val requestBody: RequestBody? = request.body
        if (requestBody != null) {
            val body: String
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            var charset: Charset? = null
            val contentType: MediaType? = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            if (charset == null) charset = UTF8
            body = charset?.let { buffer.readString(it) }.toString()
            return request.url.toString() + "&" + body
        } else {
            return request.url.toString()
        }
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
            return "主体为空"
        }
    }
}