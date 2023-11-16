package com.yc.shadowjetpack

import androidx.lifecycle.lifecycleScope
import com.yc.jetpacklib.ble.YcBleUtil
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.ble.YcBleConnDeviceBean
import com.yc.jetpacklib.ble.YcBleScanDeviceBean
import com.yc.jetpacklib.extension.showToast
import com.yc.jetpacklib.extension.ycInitLinearLayoutManage
import com.yc.jetpacklib.permission.XXPermissionUtil
import com.yc.jetpacklib.permission.YcXXPermissionHelper
import com.yc.jetpacklib.recycleView.YcRecyclerViewAdapter
import com.yc.shadowjetpack.databinding.TestBleActivityBinding
import com.yc.shadowjetpack.databinding.TestItem2Binding


class TestBleActivity : YcBaseActivityPlus<TestBleActivityBinding>(TestBleActivityBinding::inflate) {
    private val mYcPermissionHelper by lazy { YcXXPermissionHelper(this) }
    private val mYcBleUtil by lazy { YcBleUtil(this@TestBleActivity.lifecycleScope) }
    private val mAdapter by YcRecyclerViewAdapter.ycLazyInitApply<YcBleScanDeviceBean, TestItem2Binding>(TestItem2Binding::inflate) {
        mOnUpdate = {
            tvTestItem.text = it.deviceName
        }
        mItemClick = {
            mYcBleUtil.connDevice(this@TestBleActivity, YcBleConnDeviceBean(it.deviceKey, "", "", ""))
        }
    }

    override fun TestBleActivityBinding.initView() {

        val permission: MutableList<String> = mutableListOf()
        permission.addAll(XXPermissionUtil.BLUETOOTH)
        permission.addAll(XXPermissionUtil.STORAGE)
        permission.addAll(XXPermissionUtil.LOCATION)
        mYcPermissionHelper.addPermission(*permission.toTypedArray())
        mYcPermissionHelper.mSuccessCall = {
            showToast("权限申请成功")
        }
        openBle.setOnClickListener {
            mYcPermissionHelper.start()
        }
        mYcBleUtil.mScanDevice = {
            mAdapter.addData(it, true)
        }
        rv.adapter = mAdapter
        rv.ycInitLinearLayoutManage()
        startScan.setOnClickListener {
            mYcBleUtil.start()
        }
    }
}