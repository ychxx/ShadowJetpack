package com.yc.jetpacklib.download

import com.yc.jetpacklib.extension.ycIsEmpty
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.utils.YcRandom
import org.xutils.common.Callback
import org.xutils.common.Callback.CommonCallback
import org.xutils.http.RequestParams
import org.xutils.x
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 *
 */
class DownloadManager {
    private var mData: MutableList<DownloadBean> = ArrayList()
    private val resultNum = AtomicInteger(0)
    var mCall: ((data: List<DownloadBean>) -> Unit)? = null
    fun setData(data: MutableList<DownloadBean>) {
        mData = data
    }

    fun addData(bean2: DownloadBean) {
        mData.add(bean2)
    }

    fun start() {
        resultNum.set(0)
        for (i in mData.indices) {
            if (mData[i].state == DownLoadState.DOWNLOAD_SUCCESS) {
                resultNum.set(resultNum.get() + 1)
                if (resultNum.get() == mData.size) {
                    mCall?.invoke(mData)
                }
            } else {
                downloadFile(mData[i])
            }
        }
    }

    private fun downloadFile(downloadBean: DownloadBean) {
        val urlEncode = YcDownloadUtil.urlToEncode(downloadBean.url)
        val requestParams = RequestParams(urlEncode)
        if (downloadBean.fileSavePath.ycIsEmpty()) {
            requestParams.saveFilePath = "temp_" + YcRandom.nameImgOfJPG
        } else {
            requestParams.saveFilePath = downloadBean.fileSavePath
        }
        x.http().get(requestParams, object : CommonCallback<File> {
            override fun onSuccess(result: File) {
                downloadBean.fileSavePath = result.absolutePath
                downloadBean.state = DownLoadState.DOWNLOAD_SUCCESS
            }

            override fun onError(ex: Throwable, isOnCallback: Boolean) {
                ycLogE(ex.message)
                ex.printStackTrace()
                downloadBean.state = DownLoadState.DOWNLOAD_FAIL
            }

            override fun onCancelled(cex: Callback.CancelledException) {}
            override fun onFinished() {
                resultNum.set(resultNum.get() + 1)
                if (resultNum.get() == mData.size) {
                    mCall?.invoke(mData)
                }
            }
        })
    }
}