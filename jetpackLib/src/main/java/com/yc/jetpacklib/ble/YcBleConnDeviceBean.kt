package com.yc.jetpacklib.ble


/**
 *
 */
data class YcBleConnDeviceBean(
    val deviceKey: String,
    val uuidService: String,
    val uuidReceive: String,
    val uuidSend: String? = null
)