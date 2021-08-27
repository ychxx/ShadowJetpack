package com.yc.jetpacklib.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.TypefaceCompat
import com.yc.jetpacklib.R
import com.yc.jetpacklib.extension.ycGetColorRes
import com.yc.jetpacklib.extension.ycIsNotEmpty
import com.yc.jetpacklib.extension.ycToNoEmpty
import kotlin.math.PI
import kotlin.math.atan2

/**
 *  圆角环形
 */
class YcRingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    companion object {
        /**
         * 由于画环形的函数0度是在时钟的3点位置，所以要加一个-90度，让它从12点位置为开始
         */
        const val CORRECT_ANGLE = -90
    }

    @ColorInt
    private var mBgColor: Int

    /**
     * 环的颜色1
     */
    @ColorInt
    private var mRingColor1: Int

    /**
     * 环的颜色2
     */
    @ColorInt
    private var mRingColor2: Int

    /**
     * 内圆半径
     */
    private var mRadiusInner: Int

    /**
     * 外圆半径
     */
    private var mRadiusOuter: Int

    /**
     * 百分比进度
     */
    private var mProgress: Float

    private val mPaintRing: Paint
    private val mPaintText: Paint
    private var mText: String
    private var mText2: String
    private var mTextSize: Float

    /**
     * 文字颜色
     */
    @ColorInt
    private var mTextColor: Int

    /**
     * 文字颜色2
     */
    @ColorInt
    private var mTextColor2: Int

    init {
        // 硬件加速不支持，图层混合。
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        val a = context.obtainStyledAttributes(attrs, R.styleable.YcRingView)
        mBgColor = a.getColor(R.styleable.YcRingView_ycBg, ycGetColorRes(R.color.transparent))
        mRingColor1 = a.getColor(R.styleable.YcRingView_ycRingColor1, ycGetColorRes(R.color.jetpack_ring_1))
        mRingColor2 = a.getColor(R.styleable.YcRingView_ycRingColor2, ycGetColorRes(R.color.jetpack_ring_2))
        mRadiusInner = a.getDimensionPixelSize(R.styleable.YcRingView_ycRadiusInner, 30)
        mRadiusOuter = a.getDimensionPixelSize(R.styleable.YcRingView_ycRadiusOuter, 60)
        mProgress = a.getFloat(R.styleable.YcRingView_ycProgress, 0.5f)
        mPaintRing = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintRing.style = Paint.Style.STROKE
        mPaintRing.isAntiAlias = true
        mPaintRing.strokeCap = Paint.Cap.ROUND//半圆形

        mText = a.getString(R.styleable.YcRingView_ycText).ycToNoEmpty("50")
        mText2 = a.getString(R.styleable.YcRingView_ycText2).ycToNoEmpty("100")
        mTextSize = a.getDimension(R.styleable.YcRingView_ycTextSize, 22f)
        mTextColor = a.getColor(R.styleable.YcRingView_ycTextColor, ycGetColorRes(R.color.jetpack_ring_1))
        mTextColor2 = a.getColor(R.styleable.YcRingView_ycTextColor2, ycGetColorRes(R.color.jetpack_ring_2))
        mPaintText = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintText.style = Paint.Style.FILL
        mPaintText.isAntiAlias = true
        mPaintText.textSize = mTextSize
        mPaintText.typeface = ResourcesCompat.getFont(context, R.font.dinmittelschriftstd)
        a.recycle()
    }

    fun setTypeface(tf: Typeface) {
        if (mPaintText.typeface !== tf) {
            mPaintText.typeface = tf
            requestLayout()
            invalidate()
        }
    }

    /**
     * 设置进度（0%~100%）
     */
    fun setProgress(progress: Float) {
        mProgress = progress
        invalidate()
    }

    override fun draw(canvas: Canvas?) {
        canvas?.apply {
            this.drawColor(mBgColor)
            val centerX = width / 2f
            val centerY = height / 2f
            val ringWidth = (mRadiusOuter - mRadiusInner).toFloat()
            val ringWidthHalf = ringWidth / 2f
            val rectF = RectF(
                centerX - mRadiusInner - ringWidthHalf,
                centerY - mRadiusInner - ringWidthHalf,
                centerX + mRadiusInner + ringWidthHalf,
                centerY + mRadiusInner + ringWidthHalf
            )
            mPaintRing.strokeWidth = ringWidth
            when {
                mProgress >= 1 -> {
                    mPaintRing.color = mRingColor2
                    drawArc(rectF, 360f, 360f, false, mPaintRing)
                }
                mProgress <= 0 -> {
                    mPaintRing.color = mRingColor1
                    drawArc(rectF, 360f, 360f, false, mPaintRing)
                }
                else -> {
                    //mPaint.strokeCap = Paint.Cap.ROUND 是在原有线的结尾和开头再加一个半圆，所以需要减去多余
                    val out = (atan2(ringWidthHalf.toDouble(), (ringWidthHalf + mRadiusInner).toDouble()) * 180f / PI).toFloat()
                    //画环形1
                    mPaintRing.color = mRingColor1
                    drawArc(rectF, 360f, 360f, false, mPaintRing)
                    //画环形2
                    mPaintRing.color = mRingColor2
                    drawArc(rectF, (1 - mProgress) * 360 + CORRECT_ANGLE + out, mProgress * 360 - out * 2, false, mPaintRing)
                }
            }
            if (mText.ycIsNotEmpty() && mText2.ycIsNotEmpty()) {
                val textRectSum = Rect()
                val textSum = mText + mText2
                mPaintText.getTextBounds(textSum, 0, textSum.length, textRectSum)
                val textWidthSum = textRectSum.width()
                val textHeightSum = textRectSum.height()
                val startX = (width - textWidthSum) / 2f
                val startY = (height + textHeightSum) / 2f
                mPaintText.color = mTextColor
                drawText(mText, startX, startY, mPaintText)
                if (mText2.ycIsNotEmpty()) {
                    mPaintText.color = mTextColor2
                    val textWidth = mPaintText.measureText(mText)
                    drawText(mText2, startX + textWidth, startY, mPaintText)
                }
            }

//            val tempPaint = Paint()
//            tempPaint.strokeWidth = 5f
//            tempPaint.style = Paint.Style.STROKE
//            tempPaint.color = ycGetColorRes(R.color.every_lib_blue)
//            drawLine(0f, height / 2f, width.toFloat(), height / 2f, tempPaint)
//            drawLine(width / 2f, 0f, width / 2f, height.toFloat(), tempPaint)
        }
        super.draw(canvas)
    }

    fun setText(textFront: String, textAfter: String) {
        mText = textFront
        mText2 = textAfter
        invalidate()
    }
}