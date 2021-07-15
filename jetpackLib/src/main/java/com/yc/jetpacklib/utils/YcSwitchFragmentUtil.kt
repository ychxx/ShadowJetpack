package com.yc.jetpacklib.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 *
 */
class YcSwitchFragmentUtil {
    private lateinit var mFragmentMap: Map<Int, Fragment>
    private var mFragmentShowItemId: Int = 0
    private var mContainerViewId: Int = 0
    private lateinit var mFragmentManager: FragmentManager

    /**
     * 设置Fragment与按钮的关系
     */
    fun setFragments(fragmentMap: Map<Int, Fragment>) {
        mFragmentMap = fragmentMap
    }

    /**
     * 显示默认Fragment
     */
    fun showFirstFragment(fragmentManager: FragmentManager, containerViewId: Int, firstFragmentId: Int) {
        mFragmentShowItemId = firstFragmentId
        mFragmentManager = fragmentManager
        mContainerViewId = containerViewId
        // 添加显示第一个fragment
        val fragment: Fragment = mFragmentMap[mFragmentShowItemId]!!
        fragmentManager.beginTransaction().add(containerViewId, fragment).show(fragment).commit()
    }

    /**
     * Fragment切换
     */
    fun switchFragment(itemId: Int): Boolean {
        return try {
            val fragment = mFragmentMap[itemId]
            val trx = mFragmentManager.beginTransaction()
            trx.hide(mFragmentMap[mFragmentShowItemId]!!)
            if (!fragment!!.isAdded) {
                trx.add(mContainerViewId, fragment)
            }
            trx.show(fragment).commit()
            mFragmentShowItemId = itemId
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}