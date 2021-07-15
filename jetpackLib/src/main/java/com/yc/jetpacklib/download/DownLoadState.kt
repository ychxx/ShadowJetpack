package com.yc.jetpacklib.download

import androidx.annotation.StringDef

/**
 * 下载状态
 */
object DownLoadState {
    const val DOWNLOAD_NOT_STARTED = "0" //还未开始
    const val DownLOADING = "1" //下载中
    const val DOWNLOAD_FAIL = "2" //下载失败
    const val DOWNLOAD_SUCCESS = "3" //下载成功
    fun getName(@State state: String?): String {
        return when (state) {
            DOWNLOAD_NOT_STARTED -> "下载"
            DownLOADING -> "下载中"
            DOWNLOAD_FAIL -> "下载失败"
            DOWNLOAD_SUCCESS -> "下载成功"
            else -> "下载"
        }
    }

    @StringDef(DOWNLOAD_NOT_STARTED, DownLOADING, DOWNLOAD_FAIL, DOWNLOAD_SUCCESS)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class State
}