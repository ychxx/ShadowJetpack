package com.yc.jetpacklib.release

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.yc.jetpacklib.R
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.extension.ycLoadImageRes

/**
 * 刷新标题栏状态
 * @param vActionbar View
 * @param tvActionbarMid TextView
 * @param ivActionbarLeft ImageView
 * @param tvActionbarRight TextView
 * @param ivActionbarRight ImageView
 */
fun YcSpecialBean.refreshActionbar(
    vActionbar: View,
    tvActionbarMid: TextView,
    ivActionbarLeft: ImageView,
    tvActionbarRight: TextView,
    ivActionbarRight: ImageView
) {
    if (mIsShowActionbar) {
        vActionbar.visibility = View.VISIBLE
        tvActionbarMid.text = mActionbarName
        ivActionbarLeft.setOnClickListener {
            mActionbarLeftClick?.invoke(it)
        }
        mActionbarRightText?.apply {
            tvActionbarRight.visibility = View.VISIBLE
            tvActionbarRight.text = this
        }
        tvActionbarRight.setOnClickListener {
            mActionbarRightClick?.invoke(it)
        }
        mActionbarRightImgId?.apply {
            ivActionbarRight.visibility = View.VISIBLE
            ivActionbarRight.ycLoadImageRes(this)
        }
        ivActionbarRight.setOnClickListener {
            mActionbarRightClick?.invoke(it)
        }
    } else {
        vActionbar.visibility = View.GONE
    }
}

/**
 * 刷新内容
 * @param specialState Int
 * @param tvContent TextView
 * @param btn Button
 * @param iv ImageView
 */
@SuppressLint("SetTextI18n")
fun YcSpecialBean.refreshContext(specialState: Int, exception: YcException? = null, tvContent: TextView, btn: Button, iv: ImageView) {
    when (specialState) {
        YcSpecialState.DATA_EMPTY -> {
            tvContent.text = "暂无内容"
            btn.visibility = View.GONE
            iv.ycLoadImageRes(R.drawable.yc_icon_special_data_empty)
        }
        YcSpecialState.DATA_EMPTY_ERROR -> {
            if (exception == null) {
                tvContent.text = "空数据异常"
            } else {
                tvContent.text = "空数据异常:${exception.msg}"
            }
            btn.visibility = View.VISIBLE
            btn.text = mContextBtnText
            btn.setOnClickListener {
                mSpecialClickListener?.invoke(it)
            }
            iv.ycLoadImageRes(R.drawable.yc_icon_special_data_empty)
        }
        YcSpecialState.NETWORK_ERROR -> {
            if (exception == null) {
                tvContent.text = "数据异常"
            } else {
                tvContent.text = "数据异常:${exception.msg}"
            }
            btn.visibility = View.VISIBLE
            btn.text = mContextBtnText
            btn.setOnClickListener {
                mSpecialClickListener?.invoke(it)
            }
            iv.ycLoadImageRes(R.drawable.yc_icon_special_data_error)
        }
        YcSpecialState.NETWORK_NO -> {
            tvContent.text = "暂无网络"
            btn.visibility = View.VISIBLE
            btn.text = mContextBtnText
            btn.setOnClickListener {
                mSpecialClickListener?.invoke(it)
            }
            iv.ycLoadImageRes(R.drawable.yc_icon_special_net_error)
        }
        else -> {
            if (exception == null) {
                tvContent.text = "出现异常"
            } else {
                tvContent.text = "出现异常:${exception.msg}"
            }
            btn.visibility = View.VISIBLE
            btn.text = mContextBtnText
            btn.setOnClickListener {
                mSpecialClickListener?.invoke(it)
            }
            iv.ycLoadImageRes(R.drawable.yc_icon_special_data_error)
        }
    }
}

/**
 *
 */
class YcSpecialBean {

    /**
     * 是否有标题栏
     */
    var mIsShowActionbar: Boolean = false

    /**
     * 标题栏-标题名称
     */
    var mActionbarName: String = ""

    /**
     * 标题栏-左侧按钮事件
     */
    var mActionbarLeftClick: ((View) -> Unit)? = null

    /**
     * 标题栏-右侧按钮事件
     */
    var mActionbarRightClick: ((View) -> Unit)? = null

    /**
     * 标题栏-右侧文字
     */
    var mActionbarRightText: String? = null

    /**
     * 标题栏-右侧图片资源id
     */
    var mActionbarRightImgId: Int? = null

//    /**
//     * 内容部分-图片资源id
//     */
//    var mContextImgId: Int? = null
//
//    /**
//     * 内容部分-展示的文字
//     */
//    var mContextText: String? = null
//
    /**
     * 内容部分-展示的按钮文字
     */
    var mContextBtnText: String? = "刷新"

    /**
     * 内容部分-展示的按钮点击事件
     */
    var mSpecialClickListener: ((View) -> Unit)? = null
}
