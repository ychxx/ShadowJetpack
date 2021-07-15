package com.yc.jetpacklib.exception


/**
 *  网络请求异常类
 */
public open class YcException : Exception {
    var code: Int
    var msg: String? = null
        get() {
            return if (field == null) {
                ""
            } else {
                field
            }
        }

    constructor(code: Int, throwable: Throwable?) : super(throwable) {
        this.code = code
    }

    constructor(msg: String?, code: Int) {
        this.msg = msg
        this.code = code
    }

    constructor(code: Int, msg: String?) {
        this.msg = msg
        this.code = code
    }
}