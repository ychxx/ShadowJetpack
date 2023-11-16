package com.yc.jetpacklib.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import androidx.appcompat.app.AppCompatActivity
import com.yc.jetpacklib.extension.ycLogESimple
import com.yc.jetpacklib.init.YcJetpack

/**
 * Creator: yc
 * Date: 2022/4/11 10:58
 * UseDes:用于蓝牙扫描设备
 */
@SuppressLint("MissingPermission")
class YcBleScan {
    var mScanDevice: ((ScanResult?) -> Unit)? = null
    var mError: ((YcBleException) -> Unit)? = null
    private var mScanCall = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            mScanDevice?.invoke(result)
            ycLogESimple("onScanResult:$callbackType ${result?.device?.name ?: "-"} ${result?.device?.address}")
        }

        override fun onBatchScanResults(results: List<ScanResult?>?) {
            if (results != null) {
                for (item in results) {
                    ycLogESimple("onScanResultBatch: ${item?.device?.name ?: "-"} ${item?.device?.address}")
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            mError?.invoke(YcBleException(YcBleState.BLE_ERROR_SCAN_FAIL))
            ycLogESimple("onScanFailed: $errorCode")
        }
    }
    private var mBluetoothLeScanner: BluetoothLeScanner? = null

    @SuppressLint("MissingPermission")
    fun scanStart() {
        val mBleManager = (YcJetpack.mInstance.mApplication.getSystemService(AppCompatActivity.BLUETOOTH_SERVICE) as BluetoothManager)
        mBleManager.adapter.startDiscovery()
        val adapter = mBleManager.adapter
        val scanSetting = ScanSettings.Builder()
        scanSetting.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)//高占比扫描模式(官方建议只在前台页面使用)
        mBluetoothLeScanner = adapter.bluetoothLeScanner
        mBluetoothLeScanner?.startScan(mScanCall)
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        try {
            mBluetoothLeScanner?.stopScan(mScanCall)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}