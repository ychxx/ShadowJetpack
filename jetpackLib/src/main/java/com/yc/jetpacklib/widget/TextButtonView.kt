package com.yc.jetpacklib.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import com.yc.jetpacklib.R
import com.yc.jetpacklib.extension.ycToNoEmpty
import com.yc.jetpacklib.utils.YcResources
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 *
 */
class TextButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {
    private lateinit var mTextView: YcDefaultTextView
    private var mImageView: ImageView? = null

    @ColorInt
    private var mTextColor1 = 0

    @ColorInt
    private var mTextColor2 = 0

    //是否只显示单行
    private var isSingleLine = false
    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.YcTextButtonView)
        val defaultColor = a.getColor(
            R.styleable.YcTextButtonView_TextButtonViewDefaultTextColor, YcResources.getColorRes(
                R.color.every_lib_black_333A40
            )
        )
        var defaultText = a.getString(R.styleable.YcTextButtonView_TextButtonViewDefaultText)
        var text = a.getString(R.styleable.YcTextButtonView_TextButtonViewText)
        if (TextUtils.isEmpty(defaultText)) {
            defaultText = ""
        }
        if (TextUtils.isEmpty(text)) {
            text = ""
        }
        mTextColor1 = a.getColor(
            R.styleable.YcTextButtonView_TextButtonViewTextColor, YcResources.getColorRes(
                R.color.every_lib_black_333A40
            )
        )
        mTextColor2 = a.getColor(
            R.styleable.YcTextButtonView_TextButtonViewTextColor2, YcResources.getColorRes(
                R.color.every_lib_blue
            )
        )
        val maxLength = a.getInt(
            R.styleable.YcTextButtonView_TextButtonViewTextMaxLength,
            Int.MAX_VALUE - 1
        )
        val textSize = a.getDimension(R.styleable.YcTextButtonView_TextButtonViewTextSize, 18f)
        isSingleLine = a.getBoolean(R.styleable.YcTextButtonView_TextButtonViewSingleLine, false)
        val imageViewResource = a.getResourceId(
            R.styleable.YcTextButtonView_TextButtonViewNextImage,
            -1
        )
        a.recycle()
        mTextView = YcDefaultTextView(getContext())
        mTextView.setTextDefaultColor(defaultColor)
        mTextView.setDefaultText(defaultText)
        mTextView.setTextMaxLength(maxLength)
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        mTextView.setTextColorNew(mTextColor1)
        mTextView.setText(text)
        mTextView.setPadding(0, 0, 0, 0)
        mTextView.setGravity(Gravity.END or Gravity.CENTER_VERTICAL)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.weight = 1.0f //在此处设置weight
        mTextView.setLayoutParams(params)
        addView(mTextView)
        mImageView = ImageView(getContext())
        if (imageViewResource == -1) {
            mImageView!!.setImageResource(R.drawable.yc_icon_arrow_right_2)
        } else {
            mImageView!!.setImageResource(imageViewResource)
        }
        mImageView!!.scaleType = ImageView.ScaleType.FIT_CENTER
        mImageView!!.adjustViewBounds = true
        mImageView!!.setPadding(resources.getDimensionPixelOffset(R.dimen.margin_10dp), 0, 0, 0)
        val imageSize = resources.getDimensionPixelOffset(R.dimen.every_lib_icon_next_height)
        mImageView!!.layoutParams = ViewGroup.LayoutParams(
            imageSize + resources.getDimensionPixelOffset(
                R.dimen.margin_10dp
            ), imageSize
        )
        addView(mImageView)
        setBackgroundResource(R.color.transparent)
        gravity = Gravity.CENTER_VERTICAL
        mTextView.setSingleLine(isSingleLine)
        if (isSingleLine) {
            mTextView.setEllipsize(TextUtils.TruncateAt.END)
        }
    }

    fun setText(text: CharSequence?, @StyleIndex styleIndex: Int) {
        setStyleIndex(styleIndex)
        setText(text)
    }

    fun setText(text: CharSequence?) {
        mTextView.text = text.toString().ycToNoEmpty("")
    }


    fun setStyleIndex(@StyleIndex styleIndex: Int) {
        when (styleIndex) {
            StyleIndex.STYLE_1 -> mTextView.setTextColorNew(mTextColor1)
            StyleIndex.STYLE_2 -> mTextView.setTextColorNew(mTextColor2)
        }
    }


    fun getText(): String {
        return mTextView.text.toString().ycToNoEmpty("")
    }

    val isEmpty: Boolean
        get() = mTextView.isEmpty
    val textView: YcDefaultTextView
        get() = mTextView

    /**
     * 隐藏右侧图标，并且设置成不能点击
     *
     * @param enabled 能否点击
     */
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (enabled) {
            mImageView!!.visibility = VISIBLE
        } else {
            mImageView!!.visibility = GONE
        }
    }

    /**
     * 恢复默认值
     */
    fun recoveryDefault() {
        mTextView.recoveryDefault()
    }

    @IntDef(StyleIndex.STYLE_1, StyleIndex.STYLE_2)
    @Retention(RetentionPolicy.SOURCE)
    annotation class StyleIndex {
        companion object {
            const val STYLE_1 = 0
            const val STYLE_2 = 1
        }
    }

    init {
        init(context, attrs)
    }
}