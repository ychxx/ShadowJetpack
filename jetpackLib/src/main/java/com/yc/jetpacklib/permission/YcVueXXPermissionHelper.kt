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
open class YcVueXXPermissionHelper : YcXXPermissionHelper {
    constructor(context: Context, lifecycleOwner: LifecycleOwner) : super(context, lifecycleOwner) {}

    constructor(activity: FragmentActivity) : super(activity) {}

    @SuppressLint("UseRequireInsteadOfGet")
    constructor(fragment: Fragment) : super(fragment) {
    }

    override fun start() {
        mStartCall?.invoke()
        super.start()
    }
}