package com.yc.jetpacklib.release

import android.view.View
import androidx.viewbinding.ViewBinding
import com.yc.jetpacklib.R
import com.yc.jetpacklib.data.constant.YcNetErrorCode
import com.yc.jetpacklib.databinding.YcSpecialReleaseBinding
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.extension.ycLoadImageRes

/**
 *  通用的一些参数
 */
interface YcISpecialViewBuild<VB : ViewBinding> {
    var mIsShowTitle: Boolean
    var mTitleName: String
    var mTitleLeftClick: ((View) -> Unit)?
    var mTitleRightClick: ((View) -> Unit)?
    var mTitleRightTv: String?
    var mTitleRightIv: Int?
    var mSpecialClickListener: ((View) -> Unit)?


    /**
     * 设置标题
     * @param titleName String                  标题名
     * @param leftClick Function1<View, Unit>?  左侧点击事件，空则默认finish
     * @param rightClick Function1<View, Unit>? 右侧点击事件
     * @param rightTv String?                   右侧文字
     * @param rightIv Int?                      右侧图片
     */
    fun setBuildTitle(
        titleName: String = "",
        leftClick: ((View) -> Unit)? = null,
        rightClick: ((View) -> Unit)? = null,
        rightTv: String? = null,
        rightIv: Int? = null
    ) {
        mTitleName = titleName
        mTitleLeftClick = leftClick
        mTitleRightClick = rightClick
        mTitleRightTv = rightTv
        mTitleRightIv = rightIv
        mIsShowTitle = true
    }

    fun VB.onBuildUpdate(specialState: Int)
    fun YcSpecialViewBase<VB>.buildShow(exception: YcException)
}