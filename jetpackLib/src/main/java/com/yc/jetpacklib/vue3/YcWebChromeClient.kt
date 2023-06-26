package com.yc.jetpacklib.vue3

import android.net.Uri
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.core.net.toUri
import com.google.gson.Gson
import com.yc.jetpacklib.extension.ycIsEmpty
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.file.YcFileUtils
import com.yc.jetpacklib.init.YcJetpack
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

/**
 * Creator: yc
 * Date: 2022/10/13 16:48
 * UseDes:
 */
open class YcWebChromeClient : WebChromeClient() {
    companion object {
        val TEMP_DATA_FILE_PATH = YcJetpack.mInstance.mApplication.filesDir.path + File.pathSeparator + "webTempFile.txt"
    }

    protected var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    var mToNext: ((selectorType: Int?) -> Unit)? = null

    @YcVueSelectorUtil.SelectorType
    var mSelectorType = YcVueSelectorUtil.ERROR
    override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
        mFilePathCallback = filePathCallback
        mSelectorType = YcVueSelectorUtil.acceptVueToAndroid(fileChooserParams)
        mToNext?.invoke(mSelectorType)
        return true
    }

    open fun setOpenImgResult(data: Uri?) {
        if (data == null) {
            mFilePathCallback?.onReceiveValue(null)
            ycLogE("mFilePathCallback为空")
        } else {
            mFilePathCallback?.onReceiveValue(arrayOf(data))
        }
        mFilePathCallback = null
        return
    }

    open fun setOpenImgResultMore(data: Uri?, data2: Any?) {
        val array = mutableListOf<Uri>();
        data?.apply {
            array.add(this)
        }
        data2?.apply {
            val file = YcFileUtils.createFile(TEMP_DATA_FILE_PATH)!!
            val output = FileOutputStream(file)
            val osw = OutputStreamWriter(output)
            osw.append(Gson().toJson(this))
            osw.flush()
            osw.close()
            array.add(file.toUri())
        }
        if (array.ycIsEmpty()) {
            mFilePathCallback?.onReceiveValue(null)
            ycLogE("mFilePathCallback为空")
        } else {
            mFilePathCallback?.onReceiveValue(array.toTypedArray())
        }
        mFilePathCallback = null
        return
    }

    override fun onPermissionRequest(request: PermissionRequest?) {
        ycLogE("webView申请权限" + request?.resources.contentToString())
        request?.grant(request.resources)
    }
}