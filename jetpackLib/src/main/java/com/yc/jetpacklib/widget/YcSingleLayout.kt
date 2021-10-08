package com.yc.jetpacklib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yc.jetpacklib.R
import com.yc.jetpacklib.extension.ycGetColorRes
import com.yc.jetpacklib.extension.ycGetDimension
import com.yc.jetpacklib.extension.ycGetDimensionPixelSize
import com.yc.jetpacklib.utils.ycDp

/**
 * Creator: yc
 * Date: 2021/8/3 14:34
 * UseDes:单行布局（左边图标-中间文字-右边图标）
 * 用于“我的页面”单行选择
 */
@SuppressLint("RtlHardcoded")
class YcSingleLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {
    protected val mIvLeft: ImageView
    protected val mIvRight: ImageView
    protected val mTextView: TextView

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        minimumHeight = ycGetDimensionPixelSize(R.dimen.jetpack_single_height)
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.YcSingleLayout)
        val bg = a.getResourceId(R.styleable.YcSingleLayout_ycBg, R.color.white)//背景颜色
        setBackgroundResource(bg)
        //左侧图片
        val imgLeftBg = a.getResourceId(R.styleable.YcSingleLayout_ycImgLeftBg, R.drawable.yc_img_loading)
        val imgLeftWidth = a.getDimensionPixelSize(R.styleable.YcSingleLayout_ycImgLeftWidth, ycGetDimensionPixelSize(R.dimen.jetpack_img_left))
        val imgLeftHeight = a.getDimensionPixelSize(R.styleable.YcSingleLayout_ycImgLeftHeight, ycGetDimensionPixelSize(R.dimen.jetpack_img_left))
        mIvLeft = ImageView(context)
        mIvLeft.setImageResource(imgLeftBg)
        mIvLeft.scaleType = ImageView.ScaleType.FIT_CENTER
        mIvLeft.adjustViewBounds = true
        mIvLeft.layoutParams = LayoutParams(imgLeftWidth, imgLeftHeight)
        addView(mIvLeft)

        //中间文字
        val textColor = a.getColor(R.styleable.YcSingleLayout_ycTextColor, ycGetColorRes(R.color.common_single_tv_color))
        val text = a.getString(R.styleable.YcSingleLayout_ycText)
        val textSize = a.getDimension(R.styleable.YcSingleLayout_ycTextSize, ycGetDimension(R.dimen.jetpack_single_text_size))
        mTextView = TextView(context)
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        mTextView.setTextColor(textColor)
        mTextView.text = text
        mTextView.setPadding(8.ycDp(), 0, 8.ycDp(), 0)
        mTextView.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.weight = 1.0f //在此处设置weight
        mTextView.layoutParams = params
        addView(mTextView)

        //右侧图片
        val imgRightBg = a.getResourceId(R.styleable.YcSingleLayout_ycImgRightBg, R.drawable.yc_common_next)
        val imgRightWidth = a.getDimensionPixelSize(R.styleable.YcSingleLayout_ycImgRightWidth, ycGetDimensionPixelSize(R.dimen.jetpack_img_right_width))
        val imgRightHeight = a.getDimensionPixelSize(R.styleable.YcSingleLayout_ycImgRightHeight, ycGetDimensionPixelSize(R.dimen.jetpack_img_right_height))
        mIvRight = ImageView(context)
        mIvRight.setImageResource(imgRightBg)
        mIvRight.scaleType = ImageView.ScaleType.FIT_CENTER
        mIvRight.adjustViewBounds = true
        mIvRight.layoutParams = LayoutParams(imgRightWidth, imgRightHeight)
        addView(mIvRight)
        a.recycle()
    }
}