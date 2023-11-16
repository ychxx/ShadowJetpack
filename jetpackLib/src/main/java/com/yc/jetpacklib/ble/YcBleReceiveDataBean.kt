package com.yc.jetpacklib.ble

import java.io.Serializable


/**
 *
 */
data class YcBleReceiveDataBean(
    /**
     * 转换后的数据
     */
    val data: String,
    /**
     * 原始蓝牙接收的数据
     */
    val originalData: ByteArray
) : Serializable