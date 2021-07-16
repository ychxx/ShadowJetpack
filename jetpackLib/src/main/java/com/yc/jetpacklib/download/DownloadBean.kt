package com.yc.jetpacklib.download

import java.io.Serializable

/**
 *
 */
data class DownloadBean(
    /**
     * 下载地址
     */
    var fileName: String? = null,

    /**
     * 下载地址
     */
    var url: String? = null,

    /**
     * 当前下载.进度
     */
    var progress: Int = 0,
    /**
     * 当前下载状态
     */
    @DownLoadState.State
    var state: String = DownLoadState.DOWNLOAD_NOT_STARTED,

    /**
     * 文件保存地址
     */
    var fileSavePath: String = "",
) : Serializable