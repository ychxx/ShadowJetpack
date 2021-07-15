package com.yc.jetpacklib.recycleView

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *
 */
class YcViewPagerAdapter : FragmentStateAdapter {
    private val mFragments: MutableList<Fragment>

    constructor(fragments: MutableList<Fragment>, fragmentActivity: FragmentActivity) : super(fragmentActivity) {
        mFragments = fragments
    }

    constructor(fragments: MutableList<Fragment>, fragment: Fragment) : super(fragment) {
        mFragments = fragments
    }

    constructor(fragments: MutableList<Fragment>, fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(fragmentManager, lifecycle) {
        mFragments = fragments
    }

    override fun getItemCount(): Int {
        return mFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }
}