package com.yc.jetpacklib.download

import androidx.annotation.IntDef

/**
 * Creator: yc
 * Date: 2021/9/28 18:24
 * UseDes:
 */


@IntDef(YcDownLoadState.DOWNLOAD_NOT_STARTED, YcDownLoadState.DOWNLOADING, YcDownLoadState.DOWNLOAD_FAIL, YcDownLoadState.DOWNLOAD_SUCCESS)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
internal annotation class YcDownLoadState {
    companion object {
        const val DOWNLOAD_NOT_STARTED = 0 //还未开始
        const val DOWNLOADING = 1 //下载中
        const val DOWNLOAD_FAIL = 2 //下载失败
        const val DOWNLOAD_SUCCESS = 3 //下载成功
    }
}