package com.yc.jetpacklib.ble

import com.yc.jetpacklib.ble.YcBleState.getErrorTip
import com.yc.jetpacklib.exception.YcException

/**
 *  蓝牙异常
 */
class YcBleException : YcException {
    constructor(code: Int, throwable: Throwable?) : super(code, throwable)
    constructor(msg: String?, code: Int, knownCode: Int) : super(msg, code, knownCode)
    constructor(code: Int, msg: String?) : super(code, msg)

    constructor(msg: String?, code: Int, throwable: Throwable?) : super(code, throwable){
        this.msg = msg
    }
    constructor(@YcBleState.ErrorType code: Int) : super(code, getErrorTip(code))
}