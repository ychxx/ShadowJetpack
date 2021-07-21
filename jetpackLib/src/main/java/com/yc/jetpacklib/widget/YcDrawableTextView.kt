package com.yc.jetpacklib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.Dimension
import androidx.annotation.IntDef
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.yc.jetpacklib.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.*

/**
 * SimpleDes:
 * Creator:
 * Date: 2021-7-19
 * UseDes: TextView的Drawable可修改
 *
 * 1、继承[AppCompatTextView]
 * 2、可用于单独修改Drawable的宽度和高度
 * 3、文字在中心显示
 * 4、设置圆角（需要SDK_INT > 21）
 */
@Suppress("UNCHECKED_CAST")
class YcDrawableTextView : AppCompatTextView {

    @IntDef(POSITION.START, POSITION.TOP, POSITION.END, POSITION.BOTTOM)
    @Retention(RetentionPolicy.SOURCE)
    annotation class POSITION {
        companion object {
            const val START = 0
            const val TOP = 1
            const val END = 2
            const val BOTTOM = 3
        }
    }

    val drawables: Array<Drawable?> = arrayOf(null, null, null, null)
    private val mDrawablesBounds = arrayOfNulls<Rect>(4)
    protected var textWidth = 0f
        private set
    var textHeight = 0f
        private set
    private var firstLayout = false
    protected var canvasTransX = 0
        private set
    protected var canvasTransY = 0
        private set
    private var isCenterHorizontal //Gravity是否水平居中
            = false
    private var isCenterVertical //Gravity是否垂直居中
            = false
    var isEnableCenterDrawables //drawable跟随文本居中
            = false
        private set
    var isEnableTextInCenter //默认情况下文字与图片共同居中，开启后文字在最中间，图片紧挨
            = false
        private set

    /**
     * 获取圆角大小，单位是PX
     */
    var radius //四边圆角
            = 0
        private set

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val drawables = compoundDrawablesRelative
        val array = context.obtainStyledAttributes(attrs, R.styleable.YcDrawableTextView)
        isEnableCenterDrawables =
            array.getBoolean(R.styleable.YcDrawableTextView_dtvEnableCenterDrawables, false)
        isEnableTextInCenter =
            array.getBoolean(R.styleable.YcDrawableTextView_dtvEnableTextInCenter, false)
        radius = array.getDimensionPixelSize(R.styleable.YcDrawableTextView_dtvRadius, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (radius > 0) {
                clipToOutline = true
                outlineProvider = RadiusViewOutlineProvider()
            }
        }
        if (drawables[POSITION.START] != null) {
            val startBounds = drawables[POSITION.START]!!.bounds
            startBounds.right = array.getDimensionPixelSize(
                R.styleable.YcDrawableTextView_dtvDrawableStartWidth, drawables[POSITION.START]!!
                    .intrinsicWidth
            )
            startBounds.bottom = array.getDimensionPixelSize(
                R.styleable.YcDrawableTextView_dtvDrawableStartHeight, drawables[POSITION.START]!!
                    .intrinsicHeight
            )
        }
        if (drawables[POSITION.TOP] != null) {
            val topBounds = drawables[POSITION.TOP]!!.bounds
            topBounds.right = array.getDimensionPixelSize(
                R.styleable.YcDrawableTextView_dtvDrawableTopWidth, drawables[POSITION.TOP]!!
                    .intrinsicWidth
            )
            topBounds.bottom = array.getDimensionPixelSize(
                R.styleable.YcDrawableTextView_dtvDrawableTopHeight, drawables[POSITION.TOP]!!
                    .intrinsicHeight
            )
        }
        if (drawables[POSITION.END] != null) {
            val endBounds = drawables[POSITION.END]!!.bounds
            endBounds.right = array.getDimensionPixelSize(
                R.styleable.YcDrawableTextView_dtvDrawableEndWidth, drawables[POSITION.END]!!
                    .intrinsicWidth
            )
            endBounds.bottom = array.getDimensionPixelSize(
                R.styleable.YcDrawableTextView_dtvDrawableEndHeight, drawables[POSITION.END]!!
                    .intrinsicHeight
            )
        }
        if (drawables[POSITION.BOTTOM] != null) {
            val bottomBounds = drawables[POSITION.BOTTOM]!!.bounds
            bottomBounds.right = array.getDimensionPixelSize(
                R.styleable.YcDrawableTextView_dtvDrawableBottomWidth, drawables[POSITION.BOTTOM]!!
                    .intrinsicWidth
            )
            bottomBounds.bottom = array.getDimensionPixelSize(
                R.styleable.YcDrawableTextView_dtvDrawableBottomHeight, drawables[POSITION.BOTTOM]!!
                    .intrinsicHeight
            )
        }
        array.recycle()
        setCompoundDrawables(
            drawables[POSITION.START],
            drawables[POSITION.TOP],
            drawables[POSITION.END],
            drawables[POSITION.BOTTOM]
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (isEnableCenterDrawables) {
            val absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection)
            isCenterHorizontal =
                absoluteGravity and Gravity.HORIZONTAL_GRAVITY_MASK == Gravity.CENTER_HORIZONTAL
            isCenterVertical =
                absoluteGravity and Gravity.VERTICAL_GRAVITY_MASK == Gravity.CENTER_VERTICAL
        }
        if (!firstLayout) {
            onFirstLayout(left, top, right, bottom)
            firstLayout = true
        }
    }

    protected fun onFirstLayout(left: Int, top: Int, right: Int, bottom: Int) {
        measureTextWidth()
        measureTextHeight()
    }

    /**
     * 在绘制前获取图片的Bounds，改变绘制的位置
     */
    override fun onDraw(canvas: Canvas) {
        if (isEnableCenterDrawables && isCenterHorizontal or isCenterVertical) {

            //有文字就才位移画布
            val textNoEmpty = !TextUtils.isEmpty(text)
            //画布的偏移量
            var transX = 0
            var transY = 0
            if (drawables!![POSITION.START] != null && isCenterHorizontal) {
                val bounds = mDrawablesBounds[POSITION.START]
                val offset = calcOffset(POSITION.START).toInt()
                drawables[POSITION.START]!!.setBounds(
                    bounds!!.left + offset, bounds.top,
                    bounds.right + offset, bounds.bottom
                )
                if (isEnableTextInCenter && textNoEmpty) transX -= mDrawablesBounds[POSITION.START]!!
                    .width() + compoundDrawablePadding shr 1
            }
            if (drawables[POSITION.TOP] != null && isCenterVertical) {
                val bounds = mDrawablesBounds[POSITION.TOP]
                val offset = calcOffset(POSITION.TOP).toInt()
                drawables[POSITION.TOP]!!.setBounds(
                    bounds!!.left, bounds.top + offset,
                    bounds.right, bounds.bottom + offset
                )
                if (isEnableTextInCenter && textNoEmpty) transY -= mDrawablesBounds[POSITION.TOP]!!
                    .height() + compoundDrawablePadding shr 1
            }
            if (drawables[POSITION.END] != null && isCenterHorizontal) {
                val bounds = mDrawablesBounds[POSITION.END]
                val offset = (-calcOffset(POSITION.END)).toInt()
                drawables[POSITION.END]!!.setBounds(
                    bounds!!.left + offset, bounds.top,
                    bounds.right + offset, bounds.bottom
                )
                if (isEnableTextInCenter && textNoEmpty) transX += mDrawablesBounds[POSITION.END]!!
                    .width() + compoundDrawablePadding shr 1
            }
            if (drawables[POSITION.BOTTOM] != null && isCenterVertical) {
                val bounds = mDrawablesBounds[POSITION.BOTTOM]
                val offset = (-calcOffset(POSITION.BOTTOM)).toInt()
                drawables[POSITION.BOTTOM]!!.setBounds(
                    bounds!!.left, bounds.top + offset,
                    bounds.right, bounds.bottom + offset
                )
                if (isEnableTextInCenter && textNoEmpty) transY += mDrawablesBounds[POSITION.BOTTOM]!!
                    .height() + compoundDrawablePadding shr 1
            }
            if (isEnableTextInCenter && textNoEmpty) {
                canvas.translate(transX.toFloat(), transY.toFloat())
                canvasTransX = transX
                canvasTransY = transY
            }
        }
        super.onDraw(canvas)
    }

    override fun onDrawForeground(canvas: Canvas) {
        //再次平移回去
        canvas.translate(-canvasTransX.toFloat(), -canvasTransY.toFloat())
        super.onDrawForeground(canvas)
    }

    /**
     * 计算drawable居中还需距离
     * 如果左右两边都有图片，左图片居中则需要加上右侧图片占用的空间[.getCompoundPaddingEnd],其他同理
     *
     * @return 偏移量
     */
    private fun calcOffset(@POSITION position: Int): Float {
        return when (position) {
            POSITION.START, POSITION.END -> (width - (compoundPaddingStart + compoundPaddingEnd + textWidth)) / 2
            POSITION.TOP, POSITION.BOTTOM -> (height - (compoundPaddingTop + compoundPaddingBottom + textHeight)) / 2
            else -> 0f
        }
    }

    /**
     * 测量文字的宽度，通过Paint测量所有文字的长度，
     * 但是这个数据不一定准确，文本还有可能换行，还需要通过[.getLineBounds]来获取文本的最大宽度
     */
    protected fun measureTextWidth() {
        val textBounds = Rect()
        getLineBounds(0, textBounds)
        var text = ""
        if (getText() != null && getText().length > 0) {
            text = getText().toString()
        } else if (hint != null && hint.length > 0) {
            text = hint.toString()
        }
        val width = paint.measureText(text)
        val maxWidth = textBounds.width().toFloat()
        textWidth = if (width <= maxWidth || maxWidth == 0f) width else maxWidth
    }

    /**
     * 获取文本的高度，通过[.getLineHeight]乘文本的行数
     */
    protected fun measureTextHeight() {
        if (text != null && text.length > 0
            || hint != null && hint.length > 0
        ) textHeight = (lineHeight * lineCount).toFloat() else textHeight = 0f
    }

    override fun setText(text: CharSequence, type: BufferType) {
        super.setText(text, type)
        measureTextWidth()
        measureTextHeight()
    }

    /**
     * 设置Drawable，并设置宽高
     * 默认大小为Drawable的[Drawable.getBounds] ,
     * 如果Bounds宽高为0则，取Drawable的内部固定尺寸[Drawable.getIntrinsicHeight]
     *
     * @param position [POSITION]
     * @param drawable Drawable
     * @param width    Px
     * @param height   Px
     */
    fun setDrawable(@POSITION position: Int, drawable: Drawable?, @Px width: Int, @Px height: Int) {
        drawables!![position] = drawable
        if (drawable != null) {
            val bounds = Rect()
            if (width == -1 && height == -1) {
                if (drawable.bounds.width() > 0 && drawable.bounds.height() > 0) {
                    //如果Bounds宽高大于0,则保持默认
                    val origin = drawable.bounds
                    bounds[origin.left, origin.top, origin.right] = origin.bottom
                } else {
                    //否则取Drawable的内部大小
                    bounds[0, 0, drawable.intrinsicWidth] = drawable.intrinsicHeight
                }
            } else {
                bounds.right = width
                bounds.bottom = height
            }
            drawables[position]!!.setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom)
            mDrawablesBounds[position] = bounds
        }
        super.setCompoundDrawables(
            drawables[POSITION.START],
            drawables[POSITION.TOP],
            drawables[POSITION.END],
            drawables[POSITION.BOTTOM]
        )
    }

    override fun setCompoundDrawables(
        left: Drawable?,
        top: Drawable?,
        right: Drawable?,
        bottom: Drawable?
    ) {
        super.setCompoundDrawables(left, top, right, bottom)
        storeDrawables(left, top, right, bottom)
    }

    override fun setCompoundDrawablesRelative(
        start: Drawable?,
        top: Drawable?,
        end: Drawable?,
        bottom: Drawable?
    ) {
        super.setCompoundDrawablesRelative(start, top, end, bottom)
        storeDrawables(start, top, end, bottom)
    }

    private fun storeDrawables(
        start: Drawable?,
        top: Drawable?,
        end: Drawable?,
        bottom: Drawable?
    ) {
        if (drawables != null) {
            if (start != null && start !== drawables[POSITION.START]) {
                mDrawablesBounds[POSITION.START] = start.copyBounds()
            }
            drawables[POSITION.START] = start
            if (top != null && top !== drawables[POSITION.TOP]) {
                mDrawablesBounds[POSITION.TOP] = top.copyBounds()
            }
            drawables[POSITION.TOP] = top
            if (end != null && end !== drawables[POSITION.END]) {
                mDrawablesBounds[POSITION.END] = end.copyBounds()
            }
            drawables[POSITION.END] = end
            if (bottom != null && bottom !== drawables[POSITION.BOTTOM]) {
                mDrawablesBounds[POSITION.BOTTOM] = bottom.copyBounds()
            }
            drawables[POSITION.BOTTOM] = bottom
        }
    }

    protected fun copyDrawables(clearOffset: Boolean): Array<Drawable?> {
        val mDrawables = Arrays.copyOf(drawables, 4)
        //clear offset
        if (clearOffset) clearOffset(*mDrawables as Array<out Drawable>)
        return mDrawables
    }

    private fun clearOffset(vararg drawables: Drawable?) {
        for (drawable in drawables) {
            if (drawable != null) {
                val bounds = drawable.bounds
                bounds.offset(-bounds.left, -bounds.top)
            }
        }
    }

    protected fun dp2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun setDrawableStart(
        drawableStart: Drawable?,
        @Dimension(unit = Dimension.DP) width: Int,
        @Dimension(unit = Dimension.DP) height: Int
    ): YcDrawableTextView {
        setDrawable(POSITION.START, drawableStart, dp2px(width.toFloat()), dp2px(height.toFloat()))
        return this
    }

    fun setDrawableStart(drawableStart: Drawable?): YcDrawableTextView {
        setDrawableStart(drawableStart, -1, -1)
        return this
    }

    fun setDrawableTop(
        drawableTop: Drawable?,
        @Dimension(unit = Dimension.DP) width: Int,
        @Dimension(unit = Dimension.DP) height: Int
    ): YcDrawableTextView {
        setDrawable(POSITION.TOP, drawableTop, dp2px(width.toFloat()), dp2px(height.toFloat()))
        return this
    }

    fun setDrawableTop(drawableTop: Drawable?): YcDrawableTextView {
        setDrawableTop(drawableTop, -1, -1)
        return this
    }

    fun setDrawableEnd(
        drawableEnd: Drawable?,
        @Dimension(unit = Dimension.DP) width: Int,
        @Dimension(unit = Dimension.DP) height: Int
    ): YcDrawableTextView {
        setDrawable(POSITION.END, drawableEnd, dp2px(width.toFloat()), dp2px(height.toFloat()))
        return this
    }

    fun setDrawableEnd(drawableEnd: Drawable?): YcDrawableTextView {
        setDrawableEnd(drawableEnd, -1, -1)
        return this
    }

    fun setDrawableBottom(
        drawableBottom: Drawable?,
        @Dimension(unit = Dimension.DP) width: Int,
        @Dimension(unit = Dimension.DP) height: Int
    ): YcDrawableTextView {
        setDrawable(
            POSITION.BOTTOM,
            drawableBottom,
            dp2px(width.toFloat()),
            dp2px(height.toFloat())
        )
        return this
    }

    fun setDrawableBottom(drawableBottom: Drawable?): YcDrawableTextView {
        setDrawableBottom(drawableBottom, -1, -1)
        return this
    }

    fun setEnableCenterDrawables(enable: Boolean): YcDrawableTextView {
        if (isEnableCenterDrawables) {
            //清除之前的位移
            clearOffset(*drawables)
        }
        isEnableCenterDrawables = enable
        return this
    }

    fun setEnableTextInCenter(enable: Boolean): YcDrawableTextView {
        isEnableTextInCenter = enable
        return this
    }

    /**
     * 设置圆角，单位是PX
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun setRadius(@Px px: Int): YcDrawableTextView {
        radius = px
        if (outlineProvider !is RadiusViewOutlineProvider) {
            outlineProvider = RadiusViewOutlineProvider()
            clipToOutline = true
        } else invalidateOutline()
        return this
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun setRadiusDP(@Dimension(unit = Dimension.DP) dp: Int): YcDrawableTextView {
        return setRadius(dp2px(dp.toFloat()))
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    inner class RadiusViewOutlineProvider : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, width, height, radius.toFloat())
        }
    }
}