package com.yc.jetpacklib.download

import android.app.ProgressDialog
import java.io.File

/**
 * Creator: yc
 * Date: 2021/9/28 17:55
 * UseDes:
 */
data class YcDownloadConfig(
    /**
     * 下载地址
     */
    val url: String,
    /**
     * 文件保存地址
     */
    val saveFilePath: String,
    var onSuccess: ((result: File?) -> Unit)? = null,
    var onFail: ((error: String?) -> Unit)? = null,
    var onLoading: ((total: Long, current: Long) -> Unit)? = null,
    val progressDialog: ProgressDialog? = null,
)