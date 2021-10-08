package com.yc.jetpacklib.release

import android.view.View
import com.yc.jetpacklib.R
import com.yc.jetpacklib.data.constant.YcNetErrorCode
import com.yc.jetpacklib.databinding.YcSpecialReleaseBinding
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.extension.ycLoadImageRes

/**
 *  通用的一些参数
 */
open class YcSpecialViewBuild {
    companion object {
        fun create() = lazy {
            return@lazy YcSpecialViewBuild()
        }
    }

    var mIsShowTitle: Boolean = false
    var mTitleName: String = ""
    var mTitleLeftClick: ((View) -> Unit)? = null
    var mTitleRightClick: ((View) -> Unit)? = null
    var mTitleRightTv: String? = null
    var mTitleRightIv: Int? = null
    var mSpecialClickListener: ((View) -> Unit)? = null
    fun YcSpecialReleaseBinding.onBuildUpdate(specialState: Int) {
        when (specialState) {
            YcSpecialState.DATA_EMPTY -> {
                releaseContentTv.text = "暂无内容"
                releaseButtonBtn.visibility = View.GONE
                releaseButtonBtn.setOnClickListener {
                    mSpecialClickListener?.invoke(it)
                }
                releaseIv.ycLoadImageRes(R.drawable.yc_icon_special_data_empty)
            }
            YcSpecialState.DATA_ERROR -> {
                releaseContentTv.text = "数据异常"
                releaseButtonBtn.visibility = View.VISIBLE
                releaseButtonBtn.text = "刷新"
                releaseButtonBtn.setOnClickListener {
                    mSpecialClickListener?.invoke(it)
                }
                releaseIv.ycLoadImageRes(R.drawable.yc_icon_special_data_error)
            }
            YcSpecialState.NET_ERROR -> {
                releaseContentTv.text = "暂无网络"
                releaseButtonBtn.visibility = View.VISIBLE
                releaseButtonBtn.text = "刷新"
                releaseButtonBtn.setOnClickListener {
                    mSpecialClickListener?.invoke(it)
                }
                releaseIv.ycLoadImageRes(R.drawable.yc_icon_special_net_error)
            }
        }
        if (mIsShowTitle) {
            releaseActionBar.root.visibility = View.VISIBLE
            releaseActionBar.apply {
                tvActionbarMid.text = mTitleName
                ivActionbarLeft.setOnClickListener {
                    mTitleLeftClick?.invoke(it)
                }
                mTitleRightTv?.apply {
                    tvActionbarRight.visibility = View.VISIBLE
                    tvActionbarRight.text = this
                }
                tvActionbarRight.setOnClickListener {
                    mTitleRightClick?.invoke(it)
                }
                mTitleRightIv?.apply {
                    ivActionbarRight.visibility = View.VISIBLE
                    ivActionbarRight.ycLoadImageRes(this)
                }
                ivActionbarRight.setOnClickListener {
                    mTitleRightClick?.invoke(it)
                }
            }

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

    fun YcSpecialViewBase<YcSpecialReleaseBinding>.buildShow(exception: YcException) {
        when (exception.code) {
            YcNetErrorCode.NETWORK_ERROR, YcNetErrorCode.TIME_OUT_ERROR -> {
                show(YcSpecialState.NET_ERROR)
            }
            YcNetErrorCode.JSON_ERROR, YcNetErrorCode.DATE_NULL_ERROR -> {
                mCustomUiProtected = {
                    releaseContentTv.text = "${exception.msg}"
                }
                show(YcSpecialState.DATA_ERROR)
            }
            else -> {
                mCustomUiProtected = {
                    releaseContentTv.text = "${exception.msg}"
                }
                show(YcSpecialState.DATA_ERROR)
            }
        }
    }
}