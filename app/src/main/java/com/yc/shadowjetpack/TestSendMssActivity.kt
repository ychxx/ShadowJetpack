package com.yc.shadowjetpack

import android.Manifest
import android.telephony.SmsManager
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.extension.showToast
import com.yc.jetpacklib.permission.YcPermissionHelper
import com.yc.shadowjetpack.databinding.TestSendSmssActivityBinding


class TestSendMssActivity : YcBaseActivityPlus<TestSendSmssActivityBinding>(TestSendSmssActivityBinding::inflate) {

    override fun TestSendSmssActivityBinding.initView() {
        val per = YcPermissionHelper(this@TestSendMssActivity)
        per.mPermission = listOf(Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE).toTypedArray()
        per.startOnCreate()
        btnTestSend.setOnClickListener {
            val phoneNum = edtPhoneNum.text.toString()
            val msg = edtMsg.text.toString()
            val manager = SmsManager.getDefault()
            val strings = manager.divideMessage(msg)
            strings.forEach { item ->
                manager.sendTextMessage(phoneNum, null, item, null, null)
            }
            showToast("发送成功")
        }
    }
}