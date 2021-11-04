package com.yc.jetpacklib.permission

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.yc.jetpacklib.R
import com.yc.jetpacklib.init.YcJetpack
import com.yc.jetpacklib.widget.dialog.YcIDialog


/**
 * Creator: yc
 * Date: 2021/3/11 20:23
 * UseDes:权限申请帮助类
 */
open class YcPermissionHelper(val activity: FragmentActivity) {
    private var mPermissionRegister: ActivityResultLauncher<Array<String>>? = null
    var mDialog: YcIDialog<*>? = YcJetpack.mInstance.mDefaultPermissionDialog.invoke(activity, activity)

    init {
        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                register()
            }
        })
    }

    /**
     * 注册
     */
    fun register() {
        if (mPermissionRegister == null) {
            mPermissionRegister = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                //通过的权限
                val grantedList = it.filterValues { it }.mapNotNull { it.key }
                //是否所有权限都通过
                if (grantedList.size == it.size) {
                    mSuccessCall?.invoke()
                } else {
                    val list = (it - grantedList).map { it.key }
                    //未通过的权限
                    val deniedList = list.filter { YcPermissionUtils.shouldShowRequestPermissionRationale(activity, it) }
                    //拒绝并且点了“不再询问”权限
                    val alwaysDeniedList = list - deniedList
                    if (alwaysDeniedList.isNotEmpty()) {
                        mNeverAskAgainCall.invoke()
                    } else {
                        mFailCall.invoke()
                    }
                }
            }
        }
    }

    /**
     * 需要申请的权限
     */
    var mPermission: Array<String> = arrayOf()

    /**
     * 权限全部同意回调
     */
    var mSuccessCall: (() -> Unit)? = null

    /**
     * 用户点击“拒绝"回调
     */
    var mFailCall: (() -> Unit) = {
        mDialog?.apply {
            setMsg("软件运行必要权限申请被拒绝!")
            setLeftBtnText("退出")
            setOnLeftClick {
                activity.finish()
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
    var mNeverAskAgainCall: (() -> Unit) = {
        mDialog?.apply {
            setMsg("权限申请被禁止，请手动开启!")
            setLeftBtnText("退出")
            setOnLeftClick {
                activity.finish()
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
     * 注：在onCreate生命周期里调用
     */
    fun startOnCreate() {
        register()
        start()
    }

    /**
     * 开始申请权限
     * @receiver AppCompatActivity  在AppCompatActivity里调用
     * 注：在onCreate之后的生命周期里调用
     */
    fun start() {
        if (YcPermissionUtils.hasSelfPermissions(activity, *mPermission)) {
            mSuccessCall?.invoke()
        } else {
            mPermissionRegister?.launch(mPermission)
        }
    }
}