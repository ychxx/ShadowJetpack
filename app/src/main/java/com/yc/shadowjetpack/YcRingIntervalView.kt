package com.yc.shadowjetpack

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.yc.jetpacklib.R
import com.yc.jetpacklib.extension.ycGetColorRes
import com.yc.jetpacklib.extension.ycLogE
import kotlin.math.cos
import kotlin.math.sin

/**
 * Creator: yc
 * Date: 2021/10/20 17:41
 * UseDes:
 */
class YcRingIntervalView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    companion object {
        /**
         * 由于画环形的函数0度是在时钟的3点位置，所以要加一个-90度，让它从12点位置为开始
         */
        const val CORRECT_ANGLE = -90
    }

    /**
     * 百分比进度
     */
    private var mProgress: Float

    /**
     * 内圆半径
     */
    private var mRadiusInner: Int

    /**
     * 外圆半径
     */
    private var mRadiusOuter: Int

    @ColorInt
    private var mBgColor: Int

//    /**
//     * 环的颜色背景
//     */
//    @ColorInt
//    private var mRingColorBg: Int
//
//    /**
//     * 环的颜色
//     */
//    @ColorInt
//    private var mRingColor: Int

    private val mPaintRing: Paint

    /**
     * 圆心点距离上方边界
     */
    private var mCircleCenterY: Int = 0
    private var mCircleCenterX: Int = 0
    private val mPath = Path()

    init {
        // 硬件加速不支持，图层混合。
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        val a = context.obtainStyledAttributes(attrs, R.styleable.YcRingIntervalView)
        mBgColor = a.getColor(R.styleable.YcRingIntervalView_ycBg, ycGetColorRes(R.color.transparent))
//        mRingColorBg = a.getColor(R.styleable.YcRingIntervalView_ycRingColor1, ycGetColorRes(R.color.jetpack_ring_1))
//        mRingColor = a.getColor(R.styleable.YcRingIntervalView_ycRingColor2, ycGetColorRes(R.color.jetpack_ring_2))
        mRadiusInner = a.getDimensionPixelSize(R.styleable.YcRingIntervalView_ycRadiusInner, 30)
        mRadiusOuter = a.getDimensionPixelSize(R.styleable.YcRingIntervalView_ycRadiusOuter, 60)
        mProgress = a.getFloat(R.styleable.YcRingIntervalView_ycProgress, 0.5f)
        mPaintRing = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintRing.style = Paint.Style.FILL
        mPaintRing.isAntiAlias = true
        mPaintRing.color = ycGetColorRes(R.color.black)
        a.recycle()
    }

    private val mRadiusWidth = 20f
    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            mCircleCenterX = width / 2
            mCircleCenterY = height / 2
            this.translate(mCircleCenterX.toFloat(), mCircleCenterY.toFloat())

            mRadiusInner = width / 4
            val tempPaint = Paint()
            tempPaint.strokeWidth = 2f
            tempPaint.style = Paint.Style.STROKE
            tempPaint.color = Color.RED
            drawLine(0f, -height / 2f, 0f, height / 2f, tempPaint)
            drawLine(-width / 2f, 0f, width / 2f, 0f, tempPaint)
            drawCircle(0f, 0f, mRadiusInner.toFloat(), tempPaint)

            val tempPaint2 = Paint()
            tempPaint2.strokeWidth = 2f
            tempPaint2.style = Paint.Style.STROKE
            tempPaint2.color = Color.BLUE
            val startAngle: Float = (2 * Math.PI / 8f * 3).toFloat()
            val startX = mRadiusInner * cos(startAngle)
            val startY = mRadiusInner * sin(startAngle)
            ycLogE("--startX:$startX startY:$startY")
            this.drawText("测试", startX, startY, mPaintRing)
            val path = Path()

            path.rQuadTo(0f, -mRadiusWidth, mRadiusWidth, -mRadiusWidth)
            path.rQuadTo(mRadiusWidth, 0f, mRadiusWidth, mRadiusWidth)
//            path.arcTo()
//            path.addArc()
//            val matrix = Matrix()
//            matrix.setRotate(125f)
//            matrix.postTranslate(startX, startY)
//            path.transform(matrix)
            drawPath(path, tempPaint2)

            val startAngle2: Float = (2 * Math.PI / 8f * 4).toFloat()
            val startX2 = mRadiusInner * cos(startAngle2)
            val startY2 = mRadiusInner * sin(startAngle2)
            this.drawText("测试2", startX2, startY2, mPaintRing)

            val startAngle3: Float = (2 * Math.PI / 8f * 5).toFloat()
            val startX3 = mRadiusInner * cos(startAngle3)
            val startY3 = mRadiusInner * sin(startAngle3)
            this.drawText("测试3", startX3, startY3, mPaintRing)
//            mPath.moveTo(startX, startY)


        }
//        super.draw(canvas)
    }
}