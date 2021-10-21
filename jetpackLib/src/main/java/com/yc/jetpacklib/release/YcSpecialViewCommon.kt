package com.yc.jetpacklib.release

import android.app.Activity
import android.view.View
import com.yc.jetpacklib.databinding.YcSpecialReleaseBinding
import com.yc.jetpacklib.exception.YcException
/**
 *  通用的网络异常，空数据等展示页
 */
class YcSpecialViewCommon : YcSpecialViewBase<YcSpecialReleaseBinding> {
    val mBuild: YcSpecialViewBuild by YcSpecialViewBuild.create()
    
    constructor(originalView: View) : super(originalView, YcSpecialReleaseBinding::inflate)
    constructor(activity: Activity) : super(activity, YcSpecialReleaseBinding::inflate)

    override fun YcSpecialReleaseBinding.onUpdate(specialState: Int) {
        mBuild.apply {
            onBuildUpdate(specialState)
        }
    }

    fun show(exception: YcException) {
        mBuild.apply {
            show(exception)
        }
    }

    /**
     * 设置标题
     * @param titleName String                  标题名
     * @param leftClick Function1<View, Unit>?  左侧点击事件，空则默认finish
     * @param rightClick Function1<View, Unit>? 右侧点击事件
     * @param rightTv String?                   右侧文字
     * @param rightIv Int?                      右侧图片
     */
    fun setTitle(
        titleName: String = "",
        leftClick: ((View) -> Unit)? = null,
        rightClick: ((View) -> Unit)? = null,
        rightTv: String? = null,
        rightIv: Int? = null
    ) {
        mBuild.mTitleName = titleName
        mBuild.mTitleLeftClick = leftClick
        mBuild.mTitleRightClick = rightClick
        mBuild.mTitleRightTv = rightTv
        mBuild.mTitleRightIv = rightIv
        mBuild.mIsShowTitle = true
    }
}