package com.yc.jetpacklib.mapper

/**
 *  类型转换器接口
 *  用于数据类型的转换，防止网络请求返回json类，数据库类等改变导致UI页面改变问题
 */
interface IMapper<Input, Out> {
    fun map(input: Input): Out
}