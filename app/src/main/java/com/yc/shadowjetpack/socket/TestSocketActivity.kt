package com.yc.shadowjetpack.socket

import com.google.gson.Gson
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.utils.YcTime
import com.yc.shadowjetpack.databinding.TestSocketActivityBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Creator: yc
 * Date: 2021/10/11 11:31
 * UseDes:
 */
fun toCalendar(minutes: Int): Calendar {
    return stringToCalendar(String.format("%02d:%02d", minutes / 60, minutes % 60), "HH:mm")
}

fun stringToCalendar(time: String, formatTime: String): Calendar {
    val df = SimpleDateFormat(formatTime)
    val date = df.parse(time)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar
}

class TestSocketActivity : YcBaseActivityPlus<TestSocketActivityBinding>(TestSocketActivityBinding::inflate) {
    val mVM: TestSocketVM by ycViewModels()
    val mYcSocketJava by lazy { YcSocketJava() }
    val mSocketServer by lazy { SocketServer() }
    val time = YcTime.getCurrentTime("yyyy-MM-dd HH:mm:ss")
    val SOH: Char = "1".toInt().toChar()
    val sendDataLogin = "8=HC1.0${SOH}9=60${SOH}35=6${SOH}34=1${SOH}52=${time}${SOH}53=admin${SOH}54=123456${SOH}10=3306${SOH}"
    val sendDataDeviceLogin = "8=HC1.0${SOH}9=60${SOH}35=GJ3${SOH}34=8971${SOH}52=${time}${SOH}201=577802505${SOH}202=123456${SOH}203=2${SOH}10=3306${SOH}"
    override fun TestSocketActivityBinding.initView() {
        btnTestServer.setOnClickListener {
            mSocketServer.startServer()
        }
        btnTestCloseClient.setOnClickListener {
            mSocketServer.closeClient()
        }
        mVM.mReceiverData.observe {
            ycLogE("ac收到数据$it")
        }
        btnTestConn.setOnClickListener {
//            mYcSocketJava.create(SocketServer.getLocalIp(this@TestSocketActivity), SocketServer.SERVERPORT)
//            mYcSocketJava.create("119.3.84.189", 16699)
//            mVM.start(SocketServer.getLocalIp(this@TestSocketActivity), SocketServer.SERVERPORT)
//            mVM.start("119.3.84.189", 16699)
//            mVM.start("120.35.11.81", 61105)

            mVM.start("121.204.143.143", 6699)
        }
        btnTestSend.setOnClickListener {
//            mVM.send(sendDataDeviceLogin)

//            mYcSocketJava.send(sendData)
            mVM.send(Gson().toJson(PowerNetSwitch("862273046576302", 3)))
        }
        btnTestSend2.setOnClickListener {
//            mVM.send(sendDataDeviceLogin)

//            mYcSocketJava.send(sendData)
            mVM.send(Gson().toJson(WorkTime("862273046576302", 127, 1, 719, 722, 1438)))
        }
    }

}

data class PowerNetSwitch(
    val DeviceID: String,
    val SwitchFlag: Int,
    val CmdFlag: Int = 3,
    val MsgType: Int = 432
)

data class WorkTime(
    val DeviceID: String,
    val WorkTime: Int,
    val StartDay: Int,
    val StopDay: Int,
    val StartNight: Int,
    val StopNight: Int,
    val CmdFlag: Int = 4,
    val MsgType: Int = 432
)