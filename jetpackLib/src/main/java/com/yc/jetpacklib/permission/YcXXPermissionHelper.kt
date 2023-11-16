package com.yc.jetpacklib.permission

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.init.YcJetpack
import com.yc.jetpacklib.manager.YcActivityManager
import com.yc.jetpacklib.widget.dialog.YcIDialog


/**
 * Creator: yc
 * Date: 2021/3/11 20:23
 * UseDes:权限申请帮助类
 */
open class YcXXPermissionHelper {


    private val context: Context
    private val lifecycleOwner: LifecycleOwner

    constructor(context: Context, lifecycleOwner: LifecycleOwner) {
        this.context = context
        this.lifecycleOwner = lifecycleOwner
        initDialog()
    }

    constructor(activity: FragmentActivity) {
        this.context = activity
        this.lifecycleOwner = activity
        initDialog()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    constructor(fragment: Fragment) {
        this.context = fragment.context!!
        this.lifecycleOwner = fragment
        initDialog()
    }

    private var mXXRequestPermission: XXRequestPermission = XXRequestPermission()
    fun addPermission(vararg permissions: String) {
        permissions.forEach {
            if (XXPermissionUtil.LOCATION.contains(it)) {
                mXXRequestPermission.permissionLocation.add(it)
            } else {
                mXXRequestPermission.permission.add(it)
            }
        }
    }

    open var mDialog: YcIDialog<*>? = null

    private fun initDialog() {
        mDialog = YcJetpack.mInstance.mDefaultPermissionDialog.invoke(this.context, this.lifecycleOwner)
    }

    /**
     * 权限全部同意回调
     */
    var mSuccessCall: (() -> Unit)? = null

    /**
     * 用户点击“拒绝"回调
     */
    var mFailCall: ((List<String>) -> Unit) = {
        mDialog?.apply {
            setMsg("软件运行必要权限申请被拒绝!")
            setLeftBtnText("退出")
            setOnLeftClick {
                YcActivityManager.finishCurrentActivity()
            }
            setRightBtnText("重试")
            setOnRightClick {
                start()
            }
            show()
        }
    }

    /**
     * 用户点击“不再询问"回调
     */
    var mNeverAskAgainCall: ((List<String>) -> Unit) = {
        mDialog?.apply {
            setMsg("权限申请被禁止，请手动开启!")
            setLeftBtnText("退出")
            setOnLeftClick {
                YcActivityManager.finishCurrentActivity()
            }
            setRightBtnText("重试")
            setOnRightClick {
                start()
            }
            show()
        }
    }

    /**
     * 开始申请权限
     * @receiver AppCompatActivity  在AppCompatActivity里调用
     * 注：在onCreate之后的生命周期里调用
     */
    fun start() {
        mXXRequestPermission.hasSuccess = mXXRequestPermission.permission.isEmpty() || XXPermissions.isGranted(context, mXXRequestPermission.permission)
        mXXRequestPermission.hasLocationSuccess = mXXRequestPermission.permissionLocation.isEmpty() || XXPermissions.isGranted(context,
                                                                                                                               mXXRequestPermission.permissionLocation)
        if (!mXXRequestPermission.hasSuccess) {
            ycLogE("申请正常权限" + mXXRequestPermission.permission.toString())
            XXPermissions.with(context)
                .permission(mXXRequestPermission.permission)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                        if (allGranted) {
                            mXXRequestPermission.hasSuccess = true
                            if (mXXRequestPermission.hasLocationSuccess) {
                                mSuccessCall?.invoke()
                            } else {
                                start()
                            }
                        } else {
                            mFailCall.invoke(permissions)
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                        if (doNotAskAgain) {
                            mNeverAskAgainCall.invoke(permissions)
                        } else {
                            mFailCall.invoke(permissions)
                        }
                    }
                })
        } else if (!mXXRequestPermission.hasLocationSuccess) {
            ycLogE("申请定位权限" + mXXRequestPermission.permissionLocation.toString())
            XXPermissions.with(context)
                .permission(mXXRequestPermission.permissionLocation)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                        if (allGranted) {
                            mXXRequestPermission.hasLocationSuccess = true
                            if (mXXRequestPermission.hasSuccess) {
                                mSuccessCall?.invoke()
                            } else {
                                start()
                            }
                        } else {
                            mFailCall.invoke(permissions)
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                        if (doNotAskAgain) {
                            mNeverAskAgainCall.invoke(permissions)
                        } else {
                            mFailCall.invoke(permissions)
                        }
                    }
                })
        } else {
            mSuccessCall?.invoke()
        }
    }

    /**
     * 分两次申请权限
     * 由于XXPermission框架定位权限需要单独申请（否则部分机型会出现失败问题）
     */
    private data class XXRequestPermission(
        val permission: ArrayList<String> = arrayListOf(),
        var hasSuccess: Boolean = false,
        val permissionLocation: ArrayList<String> = arrayListOf(),
        var hasLocationSuccess: Boolean = false
    )
}