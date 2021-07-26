package com.yc.jetpacklib.release

import android.R
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * 替换布局
 */
object YcReleaseLayoutUtils {
    private const val CONTAINER_TAG = "YC_CONTAINER_TAG"
    fun replace(activity: Activity, releaseView: View?) {
        replace(activity.findViewById<View>(R.id.content), releaseView)
    }

    fun replace(activity: Activity, @LayoutRes releaseLayoutRes: Int) {
        replace(activity.findViewById<View>(R.id.content), createView(activity, releaseLayoutRes))
    }

    fun replace(originalView: View, @LayoutRes layoutRes: Int) {
        replace(originalView, createView(originalView.context, layoutRes))
    }

    /**
     * 隐藏原来的originalView 并显示releaseLayout
     *
     * @param originalView 原来的View
     * @param releaseView  替换显示的View
     */
    fun replace(originalView: View, releaseView: View?) {
        val containerTag = CONTAINER_TAG + originalView.id
        var mContainer: FrameLayout? = null
        if (originalView.parent is ViewGroup) {
            val viewParentGroup = originalView.parent as ViewGroup
            val tag = viewParentGroup.tag?.toString()
            //查找mContainer是否已经创建过
            if (containerTag == tag) {
                mContainer = originalView.parent as FrameLayout
            } else {
                for (i in 0 until viewParentGroup.childCount) {
                    if (containerTag == viewParentGroup.getChildAt(i).tag?.toString()) {
                        mContainer = viewParentGroup.getChildAt(i) as FrameLayout
                        break
                    }
                }
            }
            if (mContainer == null) {
                //创建FrameLayout来存放switchView和Layout生成的View
                mContainer = FrameLayout(originalView.context)
                mContainer.tag = containerTag //设置Tag用于标示
                val originalViewLayoutParams = originalView.layoutParams
                if (originalView is RecyclerView) {
                    mContainer.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                } else {
                    mContainer.layoutParams = originalViewLayoutParams //将布局设置成switchView一样
                }

                //从viewParentGroup移除switchView后，再将switchView添加mContainer里
                val index = viewParentGroup.indexOfChild(originalView)
                viewParentGroup.removeView(originalView)
                originalView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                mContainer.addView(originalView)
                viewParentGroup.addView(mContainer, index)
            }
            if (mContainer.indexOfChild(releaseView) == -1) {
                mContainer.addView(releaseView)
            }
            showAndHideView(mContainer, releaseView)
        } else {
            Log.e("视图隐藏和显示", "该View的getParent()获取到的不是ViewGroup")
        }
    }

    fun recovery(activity: Activity) {
        recovery(activity.findViewById<View>(R.id.content))
    }

    /**
     * 恢复显示原来的View
     */
    fun recovery(originalView: View) {
        val containerTag = CONTAINER_TAG + originalView.id
        var mContainer: FrameLayout? = null
        if (originalView.parent is ViewGroup) {
            val viewParentGroup = originalView.parent as ViewGroup
            //查找mContainer是否已经创建过
            if (containerTag == viewParentGroup.tag?.toString()) {
                mContainer = originalView.parent as FrameLayout
            } else {
                for (i in 0 until viewParentGroup.childCount) {
                    if (containerTag == viewParentGroup.getChildAt(i).tag?.toString()) {
                        mContainer = viewParentGroup.getChildAt(i) as FrameLayout
                        break
                    }
                }
            }
            if (mContainer == null) {
                originalView.visibility = View.VISIBLE
            } else {
                showAndHideView(mContainer, originalView)
            }
        }
    }

    /**
     * 显示containerViewGroup里的showView,并隐藏containerViewGroup里除了showView的其他View
     *
     * @param containerViewGroup 父容器
     * @param showView           显示的View
     */
    fun showAndHideView(containerViewGroup: ViewGroup, showView: View?) {
        for (i in 0 until containerViewGroup.childCount) {
            if (containerViewGroup.indexOfChild(showView) == i) {
                containerViewGroup.getChildAt(i).visibility = View.VISIBLE
            } else {
                containerViewGroup.getChildAt(i).visibility = View.GONE
            }
        }
    }

    fun createView(context: Context?, @LayoutRes layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, null, false)
    }
}