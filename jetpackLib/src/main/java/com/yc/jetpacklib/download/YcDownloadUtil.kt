package com.yc.jetpacklib.download

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import com.yc.jetpacklib.R
import com.yc.jetpacklib.extension.ycIsEmpty
import com.yc.jetpacklib.extension.ycLogDSimple
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.file.YcFileUtils
import com.yc.jetpacklib.utils.YcResources
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x
import java.io.File

object YcDownloadUtil {
    @JvmStatic
    fun urlToEncode(url: String?): String {
        return Uri.encode(url, ":._-$,;~()/+-=*?&") //转码防止中文下载地址导致无法下载
    }

    @JvmStatic
    fun downloadApk(context: Context?, url: String?, apkPath: String?, downloadCall: DownloadCall?) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.setProgressDrawable(YcResources.getDrawable(R.drawable.yc_progessbar))
        progressDialog.setMessage("正在下载中...")
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setProgressNumberFormat("")
        downloadApk(url, apkPath, progressDialog, downloadCall)
    }

    /**
     * 下载文件
     */
    @JvmStatic
    fun downloadApk(url: String?, savePath: String?, progressDialog: ProgressDialog?, downloadCall: DownloadCall?) {
        val urlEncode: String = urlToEncode(url)
        val requestParams = RequestParams(urlEncode) // 下载地址
        requestParams.saveFilePath = savePath // 为RequestParams设置文件下载后的保存路径
        // requestParams.setAutoRename(false); // 下载完成后自动为文件命名
        x.http().get(requestParams, object : Callback.ProgressCallback<File?> {
            override fun onSuccess(result: File?) {
                ycLogE("成功")
                downloadCall?.onSuccess(result)
                progressDialog?.dismiss()
            }

            override fun onError(ex: Throwable, isOnCallback: Boolean) {
                ex.printStackTrace()
                ycLogE("下载失败")
                downloadCall?.onFail("下载失败")
                progressDialog?.dismiss()
            }

            override fun onCancelled(cex: Callback.CancelledException?) {
                ycLogE("取消下载")
                downloadCall?.onFail("下载被取消了")
                progressDialog?.dismiss()
            }

            override fun onFinished() {
                ycLogE("结束下载")
                progressDialog?.dismiss()
            }

            override fun onWaiting() {
                // 网络请求开始的时候调用
                ycLogE("等待下载")
            }

            override fun onStarted() {
                // 下载的时候不断回调的方法
                ycLogE("开始下载")
                if (progressDialog != null && !progressDialog.isShowing) {
                    progressDialog.show()
                }
            }

            override fun onLoading(total: Long, current: Long, isDownloading: Boolean) {
                // 当前的下载进度和文件总大小
                ycLogDSimple("正在下载中......total：$total current：$current")
                if (progressDialog != null) {
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    progressDialog.setMessage("正在下载中......")
                    progressDialog.max = total.toInt()
                    progressDialog.progress = current.toInt()
                    if (!progressDialog.isShowing) {
                        progressDialog.show()
                    }
                }
            }
        })
    }

    fun downloadImg(url: String?, savePath: String?, downloadCall: DownloadCall) {
        if (url.ycIsEmpty()) {
            ycLogE("下载的图片地址为空")
            return
        }
        val requestParams = RequestParams(url) // 下载地址
        val imgFile = File(savePath)
        if (imgFile.exists()) {
            downloadCall.onSuccess(imgFile)
            return
        } else {
            YcFileUtils.createFile(savePath)
        }
        requestParams.saveFilePath = savePath // 为RequestParams设置文件下载后的保存路径
        //        requestParams.setAutoRename(false); // 下载完成后自动为文件命名
        x.http().get(requestParams, object : Callback.ProgressCallback<File?> {
            override fun onSuccess(result: File?) {
                ycLogE("成功")
                downloadCall.onSuccess(result)
            }

            override fun onError(ex: Throwable?, isOnCallback: Boolean) {
                ycLogE("下载失败")
                YcFileUtils.delFile(savePath)
                downloadCall.onFail("下载失败")
            }

            override fun onCancelled(cex: Callback.CancelledException?) {
                ycLogE("取消下载")
                YcFileUtils.delFile(savePath)
                downloadCall.onFail("下载被取消了")
            }

            override fun onFinished() {
                ycLogE("结束下载")
            }

            override fun onWaiting() {
                // 网络请求开始的时候调用
                ycLogE("等待下载")
            }

            override fun onStarted() {
                // 下载的时候不断回调的方法
                ycLogE("开始下载")
            }

            override fun onLoading(total: Long, current: Long, isDownloading: Boolean) {
                // 当前的下载进度和文件总大小
                ycLogE("正在下载中......total：$total current：$current")
            }
        })
    }

    interface DownloadCall {
        fun onSuccess(result: File?)
        fun onFail(msg: String?)
    }
}