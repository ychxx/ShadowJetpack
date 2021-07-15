package com.yc.jetpacklib.manager

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.*

/**
 * Creator: yc
 * Date: 2021/2/8 15:09
 * UseDes:Activity管理类
 */
object YcActivityManager {
    private val mActivityStack: Stack<FragmentActivity> by lazy {
        Stack<FragmentActivity>()
    }

    /**
     * 添加一个Activity到堆栈中
     */
    @JvmStatic
    fun addActivity(activity: FragmentActivity) {
        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                finishActivity(activity)
            }
        })
        mActivityStack.push(activity)
    }

    /**
     * 从堆栈中移除指定的Activity
     */
    @JvmStatic
    fun finishActivity(activity: FragmentActivity) {
        mActivityStack.remove(activity)
    }

    /**
     * 从堆栈中移除指定的Activity
     */
    @JvmStatic
    fun finishActivity(activity: Class<FragmentActivity>) {
        for (itemActivity in mActivityStack) {
            if (activity == itemActivity::class.java) {
                itemActivity.finish()
            }
        }
    }

    /**
     * 结束除当前传入以外所有Activity
     */
    @JvmStatic
    fun returnToActivity(activity: Class<FragmentActivity>) {
        for (i in mActivityStack.size - 1..0) {
            if (activity == mActivityStack[i]::class.java) {
                mActivityStack[i].finish()
            }
        }
    }

    /**
     * 从堆栈中移除所有Activity
     */
    @JvmStatic
    fun finishAllActivity() {
        for (activity in mActivityStack) {
            activity.finish()
        }
        mActivityStack.clear()
    }

    /**
     * 结束除当前传入以外所有Activity
     */
    @JvmStatic
    fun finishOthersActivity(activity: FragmentActivity) {
        for (itemActivity in mActivityStack) {
            if (activity !== itemActivity) {
                itemActivity.finish()
            }
        }
    }


    /**
     * 获取到当前显示Activity（堆栈中最后一个传入的activity）
     */
    @JvmStatic
    fun getCurrentActivity(): FragmentActivity? {
        return if (!mActivityStack.isEmpty()) mActivityStack.lastElement() else null
    }
}