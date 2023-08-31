package com.yc.shadowjetpack

import androidx.appcompat.app.AppCompatActivity
import com.alipay.mobile.android.verify.sdk.BizCode
import com.alipay.mobile.android.verify.sdk.interfaces.IService
import com.yc.jetpacklib.extension.ycLogD

/**
 * SimpleDes:
 * Creator: Sindi
 * Date: 2023-08-29
 * UseDes:/*阿里活体检测*/
 */
class AliveFaceUtil {
    fun startFace(pageUrl: String?, certifyId: String?, mAliService: IService, block: (response: Map<String, String>) -> Unit) {
        val requestInfo = com.alibaba.fastjson.JSONObject()
        requestInfo["url"] = pageUrl
        requestInfo["certifyId"] = certifyId
        requestInfo["bizCode"] = BizCode.Value.LIVINGNESS_SDK
        val map = mapOf(Pair("url", pageUrl), Pair("certifyId", certifyId), Pair("bizCode", BizCode.Value.LIVINGNESS_SDK))

        ycLogD("======================================================\n" +
                "====================================\n" +
                "======================================================\n")//TODO
        mAliService.startService(map, true) { response ->
            block(response)
        }
    }
}