package com.yc.jetpacklib.release

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yc.jetpacklib.R
import com.yc.jetpacklib.databinding.YcSpecialReleaseBinding
import com.yc.jetpacklib.extension.ycLoadImageRes

/**
 *  通用的网络异常，空数据等展示页
 */
class YcSpecialViewSimple : YcSpecialView<YcSpecialReleaseBinding> {
    var mIsShowTitle: Boolean = false
    var mTitleName: String = ""
    var mTitleLeftClick: ((View) -> Unit)? = null
    var mTitleRightClick: ((View) -> Unit)? = null
    var mTitleRightTv: String? = null
    var mTitleRightIv: Int? = null

    constructor() : super(YcSpecialReleaseBinding::inflate)
    constructor(originalView: View) : super(originalView, YcSpecialReleaseBinding::inflate)
    constructor(activity: Activity) : super(activity, YcSpecialReleaseBinding::inflate)

    override fun YcSpecialReleaseBinding.onUpdate(specialState: Int) {
        when (specialState) {
            YcSpecialState.DATA_EMPTY -> {
                releaseContentTv.text = "暂无内容"
                releaseButtonBtn.visibility = View.GONE
                releaseIv.ycLoadImageRes(R.drawable.yc_icon_special_data_empty)
            }
            YcSpecialState.DATA_ERROR -> {
                releaseContentTv.text = "数据异常"
                releaseButtonBtn.visibility = View.VISIBLE
                releaseButtonBtn.text = "刷新"
                releaseIv.ycLoadImageRes(R.drawable.yc_icon_special_data_error)
            }
            YcSpecialState.NET_ERROR -> {
                releaseContentTv.text = "哎呀,网络开小差啦~"
                releaseButtonBtn.visibility = View.VISIBLE
                releaseButtonBtn.text = "重新加载"
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
    fun setTitle(
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
}