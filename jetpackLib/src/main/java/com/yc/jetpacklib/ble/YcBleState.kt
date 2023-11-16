package com.yc.jetpacklib.ble

import androidx.annotation.IntDef

/**
 *
 */
object YcBleState {
    /**
     * 没有蓝牙功能
     */
    const val BLE_ERROR_DISABLED = -1

    /**
     * 没有蓝牙权限
     */
    const val BLE_ERROR_NO_PERMISSION = -2

    /**
     * 蓝牙模块未开启
     */
    const val BLE_ERROR_NO_OPEN_BLE = -3

    /**
     * 定位模块未开启
     */
    const val BLE_ERROR_NO_OPEN_LOCATION = -4

    /**
     * 蓝牙扫描失败
     */
    const val BLE_ERROR_SCAN_FAIL = -5

    /**
     * 蓝牙连接失败
     */
    const val BLE_ERROR_CONN_FAIL = -6

    /**
     * 蓝牙连接断开
     */
    const val BLE_ERROR_CONN_DISCONNECT = -7

    /**
     * 未知异常
     */
    const val BLE_ERROR = -233

    @IntDef(BLE_ERROR,
            BLE_ERROR_DISABLED,
            BLE_ERROR_NO_PERMISSION,
            BLE_ERROR_NO_OPEN_BLE,
            BLE_ERROR_NO_OPEN_LOCATION,
            BLE_ERROR_SCAN_FAIL,
            BLE_ERROR_CONN_FAIL,
            BLE_ERROR_CONN_DISCONNECT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ErrorType {}

    @JvmStatic
    fun getErrorTip(@ErrorType code: Int) = when (code) {
        BLE_ERROR_DISABLED -> "手机不支持蓝牙功能"
        BLE_ERROR_NO_PERMISSION -> "没有蓝牙权限"
        BLE_ERROR_NO_OPEN_BLE -> "蓝牙模块被关闭"
        BLE_ERROR_NO_OPEN_LOCATION -> "定位模块未开启"
        BLE_ERROR_SCAN_FAIL -> "蓝牙扫描异常"
        BLE_ERROR_CONN_DISCONNECT, BLE_ERROR_CONN_FAIL -> "蓝牙连接失败"
        else -> "蓝牙错误"
    }

    /**
     * 蓝牙状态-默认
     */
    const val BLE_STATE_DEFAULT = 0

    /**
     * 蓝牙状态-扫描蓝牙设备中
     */
    const val BLE_STATE_SCANNING = 1

    /**
     * 蓝牙状态-出错
     */
    const val BLE_STATE_ERROR = -1

    @IntDef(BLE_STATE_DEFAULT,
            BLE_STATE_SCANNING,
            BLE_STATE_ERROR)
    @Retention(AnnotationRetention.SOURCE)
    annotation class BleState {}
} 