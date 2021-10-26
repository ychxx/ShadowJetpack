package com.yc.jetpacklib.release;

import com.yc.jetpacklib.data.constant.YcNetErrorCode;


import androidx.annotation.IntDef;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

/**
 * 特殊状态
 */
@IntDef({YcSpecialState.NETWORK_NO, YcSpecialState.NETWORK_ERROR, YcSpecialState.DATA_EMPTY, YcSpecialState.DATA_EMPTY_ERROR, YcSpecialState.UPDATE_SUCCESS
        , YcSpecialState.CUSTOM_1, YcSpecialState.CUSTOM_2, YcSpecialState.CUSTOM_3})
@Retention(AnnotationRetention.SOURCE)
public @interface YcSpecialState {
    /**
     * 无网络
     */
    int NETWORK_NO = 0;
    /**
     * 接口错误
     */
    int NETWORK_ERROR = 1;
    /**
     * 数据为空(正确)
     */
    int DATA_EMPTY = YcNetErrorCode.DATE_NULL;
    /**
     * 数据为空(错误)
     */
    int DATA_EMPTY_ERROR = YcNetErrorCode.DATE_NULL_ERROR;
    /**
     * 更新成功
     */
    int UPDATE_SUCCESS = 3;
    int CUSTOM_1 = 4;//自定义1
    int CUSTOM_2 = 5;//自定义2
    int CUSTOM_3 = 6;//自定义3
}
