package com.yc.jetpacklib.data.entity

/**
 * Creator: yc
 * Date: 2022/2/22 16:17
 * UseDes:
 */
data class YcLoadingBean(
    val isShow: Boolean,
    val msg: String? = null
) {
    companion object {
        @JvmStatic
        fun show(showMsg: String?) = YcLoadingBean(true, showMsg)

        @JvmStatic
        fun hide() = YcLoadingBean(false, null)
    }
}