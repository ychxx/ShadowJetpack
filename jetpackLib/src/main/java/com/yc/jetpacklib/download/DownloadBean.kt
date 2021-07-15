package com.yc.jetpacklib.download

import java.io.Serializable

/**
 *
 */
class DownloadBean : Serializable {
    /**
     * 下载地址
     */
    var fileName: String? = null

    /**
     * 下载地址
     */
    var url: String? = null

    /**
     * 当前下载.进度
     */
    var progress = 0

    @DownLoadState.State
    var state = DownLoadState.DOWNLOAD_NOT_STARTED //当前下载状态


    var fileSavePath = "" //文件保存地址
}