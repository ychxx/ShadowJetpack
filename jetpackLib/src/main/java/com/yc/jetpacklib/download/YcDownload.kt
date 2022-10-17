package com.yc.jetpacklib.download

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import com.yc.jetpacklib.R
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.extension.ycLogDSimple
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.file.YcFileUtils
import com.yc.jetpacklib.init.YcJetpack
import com.yc.jetpacklib.utils.YcRandom
import com.yc.jetpacklib.utils.YcResources
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x
import java.io.File

/**
 * Creator: yc
 * Date: 2021/9/28 17:02
 * UseDes:
 */
class YcDownload(val mConfig: YcDownloadConfig) {
    companion object {
        @JvmStatic
        fun urlToEncode(url: String?): String {
            return Uri.encode(url, ":._-$,;~()/+-=*?&") //转码防止中文下载地址导致无法下载
        }

        /**
         * 下载png图片
         */
        @JvmStatic
        fun createDownloadPng(
            url: String,
            onSuccess: ((result: File?) -> Unit)? = null,
            onFail: ((error: String?) -> Unit)? = null,
            pngSaveFileName: String = YcRandom.nameImgOfPNG
        ) = createDownloadFile(url, pngSaveFileName, onSuccess, onFail)

        /**
         * 下载jpg图片
         */
        @JvmStatic
        fun createDownloadJpg(
            url: String,
            onSuccess: ((result: File?) -> Unit)? = null,
            onFail: ((error: String?) -> Unit)? = null,
            jpgSaveFileName: String = YcRandom.nameImgOfJPG
        ) = createDownloadFile(url, jpgSaveFileName, onSuccess, onFail)

        /**
         * 下载文件
         */
        @JvmStatic
        fun createDownloadFile(
            url: String,
            saveFileName: String = "",
            onSuccess: ((result: File) -> Unit)? = null,
            onFail: ((error: String?) -> Unit)? = null
        ) = YcDownload(YcDownloadConfig(url, YcJetpack.mInstance.mDefaultSaveDirPath + saveFileName, onSuccess, onFail))

        /**
         * 下载文件（有加载进度框）
         */
        @JvmStatic
        fun createDownloadApkHasProgress(
            context: Context,
            url: String,
            saveFilePath: String,
            onSuccess: ((result: File) -> Unit)? = null,
            onFail: ((error: String?) -> Unit)? = null
        ) = YcDownload(YcDownloadConfig(url, saveFilePath, onSuccess, onFail, progressDialog = ProgressDialog(context).apply {
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            setProgressDrawable(YcResources.getDrawable(R.drawable.yc_progessbar))
            setMessage("正在下载中...")
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setProgressNumberFormat("")
        }))
    }

    private var callback: Callback.Cancelable? = null

    @YcDownLoadState
    var mDownloadState: Int = YcDownLoadState.DOWNLOAD_NOT_STARTED
        private set

    fun stop() {
        mDownloadState = YcDownLoadState.DOWNLOAD_NOT_STARTED
        callback?.apply {
            if (!isCancelled) {
                cancel()
            }
        }
    }

    fun start(changeRequestParams: ((params: RequestParams) -> Unit)? = null) {
        stop()
        val urlEncode: String = urlToEncode(mConfig.url)
        val requestParams = RequestParams(urlEncode) // 下载地址
        changeRequestParams?.invoke(requestParams)
        YcFileUtils.createFile(mConfig.saveFilePath)
        requestParams.saveFilePath = mConfig.saveFilePath // 为RequestParams设置文件下载后的保存路径
        callback = x.http().get(requestParams, object : Callback.ProgressCallback<File?> {
            override fun onSuccess(result: File?) {
                if (result == null) {
                    onError(YcException(400, "文件为空!"), false)
                } else {
                    ycLogE("下载成功：${mConfig.saveFilePath}")
                    mDownloadState = YcDownLoadState.DOWNLOAD_SUCCESS
                    mConfig.progressDialog?.apply {
                        progress = max
                        dismiss()
                    }
                    mConfig.onSuccess?.invoke(result)
                }

            }

            override fun onError(ex: Throwable, isOnCallback: Boolean) {
                ex.printStackTrace()
                ycLogE("下载失败：${mConfig.saveFilePath}")
                mDownloadState = YcDownLoadState.DOWNLOAD_FAIL
                mConfig.progressDialog?.dismiss()
                YcFileUtils.delFile(mConfig.saveFilePath)
                mConfig.onFail?.invoke("下载失败")
            }

            override fun onCancelled(cex: Callback.CancelledException?) {
                ycLogE("取消下载：${mConfig.saveFilePath}")
                mDownloadState = YcDownLoadState.DOWNLOAD_FAIL
                mConfig.progressDialog?.dismiss()
                YcFileUtils.delFile(mConfig.saveFilePath)
                mConfig.onFail?.invoke("下载被取消了")
            }

            override fun onFinished() {
                ycLogE("结束下载")
                mConfig.progressDialog?.dismiss()
            }

            override fun onWaiting() {
                // 网络请求开始的时候调用
                ycLogE("等待下载")
            }

            override fun onStarted() {
                // 下载的时候不断回调的方法
                ycLogE("开始下载：${mConfig.saveFilePath}")
                mConfig.progressDialog?.show()
            }

            override fun onLoading(total: Long, current: Long, isDownloading: Boolean) {
                // 当前的下载进度和文件总大小
                ycLogDSimple("正在下载中......total：$total current：$current")
                mDownloadState = YcDownLoadState.DOWNLOADING
                mConfig.onLoading?.invoke(total, current)
                mConfig.progressDialog?.apply {
                    max = total.toInt()
                    progress = current.toInt()
                }
            }
        })
    }
}