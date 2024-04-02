package com.yc.jetpacklib.vue3

import androidx.annotation.IntDef
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.permission.XXPermissionUtil

/**
 *
 */
object YcVuePermission {

    /**
     * 申请权限成功
     */
    const val SUCCESS = 0

    /**
     * 申请权限失败
     */
    const val FAIL = 1

    /**
     * 申请权限拒绝且不再提示
     */
    const val NEVER_ASK_AGAIN = 2

    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
    @IntDef(SUCCESS, FAIL, NEVER_ASK_AGAIN)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class YcVuePhonePermissionResult

    /**
     * 蓝牙权限
     */
    const val BLUETOOTH = 0

    /**
     * 外部存储权限
     */
    const val STORAGE = 1

    /**
     * 前台定位权限
     */
    const val LOCATION = 2

    /**
     * 后台定位权限(申请时如果用户未选择“始终允许”，而选择“仅在使用期间”，此情况无法再次申请，只能用户手动修改)
     */
    const val LOCATION_BACKGROUND = 3

    /**
     * 照相机权限
     */
    const val CAMERA = 4

    /**
     * 麦克风权限(即录像和录音需要)
     */
    const val RECORD_AUDIO = 5

    /**
     * 联系人权限
     */
    const val CONTACTS = 6

    /**
     * 读取电话状态
     */
    const val READ_PHONE_STATE = 7

    /**
     * 通知权限权限
     */
    const val POST_NOTIFICATIONS = 8

    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
    @IntDef(BLUETOOTH, STORAGE, LOCATION, LOCATION_BACKGROUND, CAMERA, RECORD_AUDIO, CONTACTS, READ_PHONE_STATE, POST_NOTIFICATIONS)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class PhonePermissionType

    @JvmStatic
    fun PermissionTypeToString(list: List<@PhonePermissionType Int>): MutableList<String> {
        val realPermission: MutableList<String> = mutableListOf()
        list.forEach {
            when (it) {
                BLUETOOTH -> {
                    realPermission.addAll(XXPermissionUtil.BLUETOOTH)
                }

                STORAGE -> {
                    realPermission.addAll(XXPermissionUtil.STORAGE)
                }

                LOCATION -> {
                    realPermission.addAll(XXPermissionUtil.LOCATION)
                }

                LOCATION_BACKGROUND -> {
                    realPermission.addAll(XXPermissionUtil.LOCATION_BACKGROUND)
                }

                CAMERA -> {
                    realPermission.add(XXPermissionUtil.CAMERA)
                }

                RECORD_AUDIO -> {
                    realPermission.add(XXPermissionUtil.RECORD_AUDIO)
                }

                CONTACTS -> {
                    realPermission.addAll(XXPermissionUtil.CONTACTS)
                }

                else -> {
                    ycLogE("动态申请权限类型异常！未找到对应类型：${it}")
                }
            }
        }
        return realPermission
    }
}
