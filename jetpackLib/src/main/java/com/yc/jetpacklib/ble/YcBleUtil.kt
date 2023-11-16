package com.yc.jetpacklib.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LifecycleCoroutineScope
import com.yc.jetpacklib.ble.YcBleState.BLE_STATE_DEFAULT
import com.yc.jetpacklib.ble.YcBleState.BLE_STATE_ERROR
import com.yc.jetpacklib.extension.ycIsNotEmpty
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.extension.ycLogESimple
import com.yc.jetpacklib.init.YcJetpack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

/**
 *
 */
@SuppressLint("MissingPermission")
class YcBleUtil(private val lifecycleScope: LifecycleCoroutineScope) {
    var mError: ((e: YcBleException) -> Unit)? = {
        it.printStackTrace()
    }

    var mScanDevice: ((YcBleScanDeviceBean) -> Unit)? = null
    var mReceiveData: ((YcBleReceiveDataBean) -> Unit)? = null
    var mConnSuccess: (() -> Unit)? = null
    var mScanDeviceList = HashMap<String, ScanResult>()

    @YcBleState.BleState
    private var mState: Int = YcBleState.BLE_STATE_DEFAULT

    /**
     * 监听蓝牙状态的广播接收器
     */
    private var mBleCloseReceiver: BroadcastReceiver? = null
    private val mBleCheck = YcBleCheck()
    private val mBleScan = YcBleScan().apply {
        mScanDevice = {
            if (it != null && it.device != null) {
                lifecycleScope.launch(Dispatchers.Main) {
                    mScanDeviceList[it.device.name + it.device.address] = it
                    this@YcBleUtil.mScanDevice?.invoke(YcBleScanDeviceBean("" + it.device.name + it.device.address, "" + it.device.name))
                }
            }
        }
    }

    private val mBleConn = YcBleConn().apply {
        mReceiveData = { data ->
            lifecycleScope.launch(Dispatchers.Main) {
                this@YcBleUtil.mReceiveData?.invoke(data)
            }
        }
        mConnError = {
            onError(it)
        }
        mConnSuccess = {
            ycLogE("蓝牙连接成功!")
            lifecycleScope.launch(Dispatchers.Main) {
                this@YcBleUtil.mConnSuccess?.invoke()
            }
        }
    }

    /**
     * 启动蓝牙扫描
     */
    fun start() {
        try {
            mState = BLE_STATE_DEFAULT
            mScanDeviceList.clear()
            check()
            if (mState != BLE_STATE_ERROR)
                mBleScan.scanStart()
        } catch (bleExc: YcBleException) {
            bleExc.printStackTrace()
            onError(bleExc)
        } catch (e: Exception) {
            e.printStackTrace()
            onError(YcBleException("蓝牙启动出错：${e.cause.toString()}", BLE_STATE_ERROR, e))
        }
    }

    fun check() {
        try {
            mState = BLE_STATE_DEFAULT
            mBleCheck.check()
            if (mBleCloseReceiver == null) {
                mBleCloseReceiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        val bleState = intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                        //关闭蓝牙后，会接收到两次广播bleState分别为BLE_STATE_NO_OPEN_BLE和STATE_TURNING_OFF
                        if (mState != BLE_STATE_ERROR && bleState == BluetoothAdapter.STATE_TURNING_OFF || bleState == BluetoothAdapter.STATE_OFF) {
                            onError(YcBleException(YcBleState.BLE_ERROR_NO_OPEN_BLE))
                        }
                    }
                }
                YcJetpack.mInstance.mApplication.registerReceiver(mBleCloseReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
            }
        } catch (bleExc: YcBleException) {
            bleExc.printStackTrace()
            onError(bleExc)
        } catch (e: Exception) {
            e.printStackTrace()
            onError(YcBleException("蓝牙启动出错：${e.cause.toString()}", BLE_STATE_ERROR, e))
        }
    }

    fun connDevice(context: Context, bleDeviceBean: YcBleConnDeviceBean?) {
        stopScan()
        if (bleDeviceBean != null) {
            val bleScanDevice = mScanDeviceList[bleDeviceBean.deviceKey]
            if (bleScanDevice != null) {
                mBleConn.connDevice(context,
                                    bleScanDevice.device,
                                    UUID.fromString(bleDeviceBean.uuidService),
                                    UUID.fromString(bleDeviceBean.uuidReceive),
                                    if (bleDeviceBean.uuidSend.ycIsNotEmpty()) {
                                        UUID.fromString(bleDeviceBean.uuidSend)
                                    } else {
                                        null
                                    })
            } else {
                onError(YcBleException(YcBleState.BLE_ERROR, "蓝牙设备不存在"))
            }
        } else {
            onError(YcBleException(YcBleState.BLE_ERROR, "连接的蓝牙设备信息为空"))
        }
    }

    private fun onError(e: YcBleException) {
        stopScan()
        ycLogESimple("蓝牙异常--${e.msg}")
        mState = BLE_STATE_ERROR
        lifecycleScope.launch(Dispatchers.Main) {
            mError?.invoke(e)
        }
    }

    fun stopScan() {
        mBleScan.stopScan()
    }

    fun onDestroy() {
        mBleScan.stopScan()
        mBleConn.onDestroy()
        if (mBleCloseReceiver != null) {
            YcJetpack.mInstance.mApplication.unregisterReceiver(mBleCloseReceiver)
            mBleCloseReceiver = null
        }
    }
}