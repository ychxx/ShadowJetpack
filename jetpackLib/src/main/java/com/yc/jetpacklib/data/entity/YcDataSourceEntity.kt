package com.hc.jatpacklib.data.entity

/**
 *
 */
data class YcDataSourceEntity<T>(
    /**
     * 数据
     */
    val data: List<T>?,
    /**
     * 数据的总页面数（不是数据的总量）
     */
    val pageSum: Int
)