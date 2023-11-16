package com.yc.jetpacklib.ble

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.yc.jetpacklib.init.YcJetpack
import com.yc.jetpacklib.permission.XXPermissionUtil
import com.yc.jetpacklib.permission.YcPermissionUtils
/**
 * Creator: yc
 * Date: 2022/4/11 10:58
 * UseDes:用于检测蓝牙模块和蓝牙权限
 */
class YcBleCheck {
    companion object {
        /**
         * 检测蓝牙权限
         */
        val PERMISSION_BLE = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH
        )

        /**
         * 手机是否有蓝牙模块
         */

        fun hasBleModule() = YcJetpack.mInstance.mApplication.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

        /**
         * 手机是否开启蓝牙模块
         */
        fun isOpenBleModule() = (YcJetpack.mInstance.mApplication.getSystemService(AppCompatActivity.BLUETOOTH_SERVICE) as BluetoothManager)
            .adapter.isEnabled
    }

    fun check() {
        if (hasBleModule().not()) {
            throw YcBleException(YcBleState.BLE_ERROR_DISABLED)
        }
        if (isOpenBleModule().not()) {
            throw YcBleException(YcBleState.BLE_ERROR_NO_OPEN_BLE)
        }
//        if (YcPermissionUtils.hasSelfPermissions(YcJetpack.mInstance.mApplication, *PERMISSION_BLE.toTypedArray()).not()) {
//            throw YcBleException(YcBleState.BLE_ERROR_NO_PERMISSION)
//        }
        if (XXPermissionUtil.hasSelfXXPermissions(YcJetpack.mInstance.mApplication, *XXPermissionUtil.BLUETOOTH)) {
            throw YcBleException(YcBleState.BLE_ERROR_NO_PERMISSION)
        }


        //23~30 包含30，还需要开启定位
        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.M until 30) {
            val locationManager = YcJetpack.mInstance.mApplication.getSystemService(LocationManager::class.java)
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER).not()
                && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER).not()
            ) {
                throw YcBleException(YcBleState.BLE_ERROR_NO_OPEN_LOCATION)
            }
        }
    }
}