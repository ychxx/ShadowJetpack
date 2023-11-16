package com.yc.jetpacklib.permission

import android.content.Context
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

/**
 * com.github.getActivity:XXPermissions权限框架适配
 */
object XXPermissionUtil {
    /**
     * 蓝牙权限
     * 由于XXPermissions框架设定（蓝牙权限只需下方三种，申请时候(AndroidManifest.xml还是要加的)不能添加
     * Manifest.permission.BLUETOOTH，android.permission.ACCESS_COARSE_LOCATION，android.permission.ACCESS_FINE_LOCATION
     * 否则会报错）
     */
    val BLUETOOTH = Permission.Group.BLUETOOTH

    /**
     * 外部存储权限
     */
    val STORAGE = arrayOf(Permission.READ_MEDIA_IMAGES, Permission.READ_MEDIA_VIDEO, Permission.READ_MEDIA_AUDIO)

    /**
     * 定位权限
     */
    val LOCATION = arrayOf(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)

    /**
     * 后台定位权限(申请时如果用户未选择“始终允许”，而选择“仅在使用期间”，此情况无法再次申请，只能用户手动修改)
     */
    val LOCATION_BACKGROUND = arrayOf(Permission.ACCESS_BACKGROUND_LOCATION)

    /**
     * 照相机权限
     */
    val CAMERA = Permission.CAMERA

    /**
     * 麦克风权限(即录像和录音需要)
     */
    val RECORD_AUDIO = Permission.RECORD_AUDIO

    /**
     * 联系人权限
     */
    val CONTACTS = Permission.Group.CONTACTS
    fun hasSelfXXPermissions(context: Context, vararg permissions: String): Boolean {
        if (XXPermissions.isGranted(context, *permissions)) {
            return false
        }
        return true
    }
}