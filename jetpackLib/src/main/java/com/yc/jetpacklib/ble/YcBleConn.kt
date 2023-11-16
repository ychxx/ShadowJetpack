package com.yc.jetpacklib.ble

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import com.yc.jetpacklib.extension.ycLogESimple
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.*

/**
 * Creator: yc
 * Date: 2022/4/11 15:31
 * UseDes:蓝牙连接
 */
@SuppressLint("MissingPermission")
class YcBleConn(var mServiceUuid: UUID? = null, var mReceiveUuid: UUID? = null, var mSendUuid: UUID? = null) {
    var mConnSuccess: (() -> Unit)? = null
    var mConnError: ((YcBleException) -> Unit)? = null
    var mReceiveData: ((YcBleReceiveDataBean) -> Unit)? = null
    private var mConnTimeOut: Job? = null


    private var mSendCharacteristic: BluetoothGattCharacteristic? = null
    private var mBluetoothGatt: BluetoothGatt? = null
    private val mBleGattCall = object : BluetoothGattCallback() {
        //蓝牙连接变动
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            ycLogESimple("gatt = $gatt status = $status, newstate = $newState")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (BluetoothProfile.STATE_CONNECTED == newState) {
                    gatt?.discoverServices()
                } else if (BluetoothProfile.STATE_DISCONNECTED == newState) {
                    mConnTimeOut?.cancel()
                    mConnError?.invoke(YcBleException(YcBleState.BLE_ERROR_CONN_FAIL, "连接失败:newState(${newState})"))
                    gatt?.disconnect()
                    gatt?.close()
                }
            } else {
                mConnTimeOut?.cancel()
                mConnError?.invoke(YcBleException(YcBleState.BLE_ERROR_CONN_FAIL, "连接失败:gatt(${status})"))
                gatt?.disconnect()
                gatt?.close()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            try {
                ycLogESimple("onServicesDiscovered status :$status")
                mBluetoothGatt = gatt
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val bleService: BluetoothGattService =
                        gatt!!.getService(mServiceUuid) ?: throw YcBleException(YcBleState.BLE_ERROR_CONN_FAIL, "蓝牙连接失败：服务uuid错误")
                    val receiveCharacteristic: BluetoothGattCharacteristic? =
                        bleService.getCharacteristic(mReceiveUuid) ?: throw YcBleException(YcBleState.BLE_ERROR_CONN_FAIL, "蓝牙连接失败：接收uuid错误")
                    gatt.setCharacteristicNotification(receiveCharacteristic, true)
                    val listDescriptor = receiveCharacteristic!!.descriptors
                    for (descriptor in listDescriptor) {
                        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(descriptor)
                    }
                    //TODO 由于设备不需发送，故发送信息功能未测试过
                    mSendUuid?.apply {
                        mSendCharacteristic =
                            bleService.getCharacteristic(mSendUuid) ?: throw YcBleException(YcBleState.BLE_ERROR_CONN_FAIL, "蓝牙连接失败：写入uuid错误")
                    }
                    mConnTimeOut?.cancel()
                    mConnSuccess?.invoke()
                    ycLogESimple("蓝牙连接成功")
                } else {
                    throw YcBleException(YcBleState.BLE_ERROR_CONN_DISCONNECT, "蓝牙无法连接")
                }
            } catch (e: YcBleException) {
                e.printStackTrace()
                mConnError?.invoke(e)
            } catch (e: Exception) {
                e.printStackTrace()
                mConnError?.invoke(YcBleException(YcBleState.BLE_ERROR_CONN_FAIL, "蓝牙连接失败：${e.cause}"))
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic, status: Int) {
            super.onCharacteristicRead(gatt, characteristic, status)
            ycLogESimple("onCharacteristicRead status :$status")
            val data = characteristic.value
            val dataNew = String(data)
            ycLogESimple("Read 接收到 原始数据:${Arrays.toString(data)}")
            ycLogESimple("Read 接收到 转码数据:$dataNew")
            mReceiveData?.invoke(YcBleReceiveDataBean(dataNew, data))
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            ycLogESimple("onCharacteristicWrite:$status  -- $status")
        }

        //数据变化
        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic) {
            val data = characteristic.value
            val dataNew = String(data)
            ycLogESimple("Changed 接收到 原始数据:${Arrays.toString(data)}")
            ycLogESimple("Changed 接收到 转码数据:$dataNew")
//            val test = TypedArray() { -5, 0, 40, 0, 27, 67, -6 }
            mReceiveData?.invoke(YcBleReceiveDataBean(dataNew, data))
        }
    }

    fun connDevice(context: Context, deviceBle: BluetoothDevice, uuidService: UUID, uuidReceive: UUID, uuidSend: UUID? = null) {
        mServiceUuid = uuidService
        mReceiveUuid = uuidReceive
        mSendUuid = uuidSend
        ycLogESimple("蓝牙连接开始")
        mConnTimeOut?.cancel()
        mConnTimeOut = GlobalScope.launch(Dispatchers.IO) {
            delay(15000)
            mConnError?.invoke(YcBleException(YcBleState.BLE_ERROR_CONN_FAIL, "连接超时"))
            onDestroy()
        }
        deviceBle.connectGatt(context, false, mBleGattCall, BluetoothDevice.TRANSPORT_LE)
    }

    @Synchronized
    fun sendData(sendData: String) {
        ycLogESimple("触发了发送数据")
        mSendCharacteristic?.apply {
            val sendDataNew = sendData.toByteArray()
            ycLogESimple("发送数据：${sendData} \n 转码后：${sendDataNew.contentToString()}")
            value = sendDataNew
            mBluetoothGatt?.writeCharacteristic(this)
        }
    }

    fun onDestroy() {
        mConnTimeOut?.cancel()
        mBluetoothGatt?.disconnect()
        mBluetoothGatt?.close()
    }
}