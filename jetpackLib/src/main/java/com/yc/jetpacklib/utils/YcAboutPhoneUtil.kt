package com.yc.jetpacklib.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.yc.jetpacklib.extension.showToast

/**
 * SimpleDes:
 * Creator: Sindi
 * Date: 2022-06-09
 * UseDes:关于手机工具
 */
object YcAboutPhoneUtil {


    /*
    * 跳到拨打电话页面
    * */
    @JvmStatic
    fun toCallPhone(context: Context, phoneNum: String?) {
        if (phoneNum?.length == 11) {
            try {
                val intent = Intent(Intent.ACTION_DIAL)
                val uri = Uri.parse("tel:${phoneNum}")
                intent.data = uri
                context.startActivity(intent)
            } catch (e: Exception) {
                context.showToast("您的手机不支持电话功能!")
            }
        } else {
            context.showToast("无效手机号，请联系管理人员")
        }
    }
}