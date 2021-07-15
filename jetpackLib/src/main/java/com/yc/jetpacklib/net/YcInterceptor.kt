package com.yc.jetpacklib.net

import okhttp3.Interceptor

/**
 * Creator: yc
 * Date: 2021/6/15 14:56
 * UseDes:让插件也能使用过滤器
 */
open fun interface YcInterceptor: Interceptor {
}