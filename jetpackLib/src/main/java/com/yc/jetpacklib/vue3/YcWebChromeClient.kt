package com.yc.jetpacklib.vue3

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.yc.jetpacklib.extension.ycLogE

/**
 * Creator: yc
 * Date: 2022/10/13 16:48
 * UseDes:
 */
open class YcWebChromeClient : WebChromeClient() {
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
}