package com.yc.jetpacklib.release;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import androidx.annotation.IntDef;

/**
 * 特殊状态
 */
@IntDef({YcSpecialState.NET_ERROR, YcSpecialState.DATA_ERROR, YcSpecialState.DATA_EMPTY, YcSpecialState.UPDATE_SUCCESS
        , YcSpecialState.CUSTOM_1, YcSpecialState.CUSTOM_2, YcSpecialState.CUSTOM_3})
@Retention(RetentionPolicy.SOURCE)
public @interface YcSpecialState {
    int NET_ERROR = 0;//网络异常
    int DATA_ERROR = 1;//数据错误
    int DATA_EMPTY = 2;//数据为空
    int UPDATE_SUCCESS = 3;//修改成功
    int CUSTOM_1 = 4;//自定义1
    int CUSTOM_2 = 5;//自定义2
    int CUSTOM_3 = 6;//自定义3
}
