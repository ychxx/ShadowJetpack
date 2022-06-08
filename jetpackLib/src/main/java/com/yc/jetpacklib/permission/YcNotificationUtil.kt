package com.yc.jetpacklib.permission

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import android.os.Build
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.yc.jetpacklib.permission.YcNotificationUtil
import java.lang.Exception

/**
 * Creator: yc
 * Date: 2021/5/28 17:01
 * UseDes:
 */
object YcNotificationUtil {
    /**
     * 检测是否开启消息通知功能
     * @param context Context
     * @return Boolean
     */
    @JvmStatic
    fun isNotificationEnabled(context: Context): Boolean {
        val manager = NotificationManagerCompat.from(context)
        return manager.areNotificationsEnabled()
    }

    /**
     * 跳转到开启消息通知页面
     */
    @JvmStatic
    fun toOpenNotification(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                val intent = Intent()
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.applicationInfo.uid)
                context.startActivity(intent)
            } else {
                toPermissionSetting(context)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun toPermissionSetting(context: Context) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            toSystemConfig(context)
        } else {
            try {
                toApplicationInfo(context)
            } catch (e: Exception) {
                e.printStackTrace()
                toSystemConfig(context)
            }
        }
    }

    @JvmStatic
    fun toApplicationInfo(context: Context) {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        localIntent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        localIntent.data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(localIntent)
    }

    @JvmStatic
    fun toSystemConfig(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_SETTINGS)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}