package com.yc.jetpacklib.release

import androidx.annotation.IntDef
import com.yc.jetpacklib.data.constant.YcNetErrorCode
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.release.YcSpecialState

/**
 * 特殊状态
 */
@IntDef(
    YcSpecialState.NETWORK_NO,
    YcSpecialState.NETWORK_ERROR,
    YcSpecialState.DATA_EMPTY,
    YcSpecialState.DATA_EMPTY_ERROR,
    YcSpecialState.UPDATE_SUCCESS,
    YcSpecialState.CUSTOM_1,
    YcSpecialState.CUSTOM_2,
    YcSpecialState.CUSTOM_3
)
@Retention(AnnotationRetention.SOURCE)
annotation class YcSpecialState {
    companion object {
        /**
         * 无网络
         */
        const val NETWORK_NO = YcNetErrorCode.NETWORK_NO

        /**
         * 接口错误
         */
        const val NETWORK_ERROR = 1

        /**
         * 数据为空(正确)
         */
        const val DATA_EMPTY = YcNetErrorCode.DATE_NULL

        /**
         * 数据为空(错误)
         */
        const val DATA_EMPTY_ERROR = YcNetErrorCode.DATE_NULL_ERROR

        /**
         * 更新成功
         */
        const val UPDATE_SUCCESS = 3
        const val CUSTOM_1 = 4 //自定义1
        const val CUSTOM_2 = 5 //自定义2
        const val CUSTOM_3 = 6 //自定义3
    }
}