package com.yc.jetpacklib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.yc.jetpacklib.R
import com.yc.jetpacklib.extension.ycGetColorRes
import kotlin.math.PI
import kotlin.math.atan2

/**
 * 由于画环形的函数0度是在时钟的3点位置，所以要加一个-90度，让它从12点位置为开始
 */
const val CORRECT_ANGLE = -90

/**
 *  圆角环形
 */
class YcRingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatTextView(context, attrs, defStyleAttr) {
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

    private val mPaint: Paint

    init {
        // 硬件加速不支持，图层混合。
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.STROKE
        mPaint.isAntiAlias = true
        mPaint.strokeCap = Paint.Cap.ROUND//半圆形
        val a = context.obtainStyledAttributes(attrs, R.styleable.YcRingView)
        mBgColor = a.getColor(R.styleable.YcRingView_ycBg, ycGetColorRes(R.color.transparent))
        mRingColor1 = a.getColor(R.styleable.YcRingView_ycRingColor1, ycGetColorRes(R.color.jetpack_ring_1))
        mRingColor2 = a.getColor(R.styleable.YcRingView_ycRingColor2, ycGetColorRes(R.color.jetpack_ring_2))
        mRadiusInner = a.getDimensionPixelSize(R.styleable.YcRingView_ycRadiusInner, 30)
        mRadiusOuter = a.getDimensionPixelSize(R.styleable.YcRingView_ycRadiusOuter, 60)
        mProgress = a.getFloat(R.styleable.YcRingView_ycProgress, 0f)
        gravity = Gravity.CENTER
        a.recycle()
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
            mPaint.strokeWidth = ringWidth
            when {
                mProgress >= 1 -> {
                    mPaint.color = mRingColor2
                    drawArc(rectF, 360f, 360f, false, mPaint)
                }
                mProgress <= 0 -> {
                    mPaint.color = mRingColor1
                    drawArc(rectF, 360f, 360f, false, mPaint)
                }
                else -> {
                    //mPaint.strokeCap = Paint.Cap.ROUND 是在原有线的结尾和开头再加一个半圆，所以需要减去多余
                    val out = (atan2(ringWidthHalf.toDouble(), (ringWidthHalf + mRadiusInner).toDouble()) * 180f / PI).toFloat()
                    //画环形1
                    mPaint.color = mRingColor1
                    drawArc(rectF, 360f, 360f, false, mPaint)
                    //画环形2
                    mPaint.color = mRingColor2
                    drawArc(rectF, (1 - mProgress) * 360 + CORRECT_ANGLE + out, mProgress * 360 - out * 2, false, mPaint)
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

}