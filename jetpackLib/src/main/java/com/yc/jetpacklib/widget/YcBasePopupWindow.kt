package com.yc.jetpacklib.widget

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.LayoutRes
import com.yc.jetpacklib.R

abstract class YcBasePopupWindow(
    protected var context: Context
) : PopupWindow(context), View.OnClickListener {
    /**
     * 最上边的背景视图
     */
    private val vBgBasePicker: View

    /**
     * 内容viewgroup
     */
    private val llBaseContentPicker: LinearLayout

    /**
     * 初始化布局
     *
     * @return
     */
    protected abstract fun bindLayout(): Int

    /**
     * 初始化view
     *
     * @param parentView
     */
    protected abstract fun initView(parentView: View?)

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 初始化监听
     */
    protected abstract fun initListener()

    /**
     * 为了适配7.0系统以上显示问题(显示在控件的底部)
     *
     * @param anchor
     */
    override fun showAsDropDown(anchor: View) {
        if (Build.VERSION.SDK_INT >= 24) {
            val rect = Rect()
            anchor.getGlobalVisibleRect(rect)
            val h = anchor.resources.displayMetrics.heightPixels - rect.bottom
            height = h
        }
        super.showAsDropDown(anchor)
        if (isNeedBgHalfTrans) {
            backgroundAlpha(0.5f)
        }
    }

    /**
     * 展示在屏幕的底部
     *
     * @param layoutid rootview
     */
    fun showAtLocation(@LayoutRes layoutid: Int) {
        showAtLocation(LayoutInflater.from(context).inflate(layoutid, null), Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
        if (isNeedBgHalfTrans) {
            backgroundAlpha(0.5f)
        }
    }

    /**
     * 最上边视图的点击事件的监听
     *
     * @param v
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.v_bg_base_picker -> dismiss()
        }
    }

    /**
     * 是否设置背景半透明
     */
    private val isNeedBackgroundHalfTransition = false
    private var isNeedBgHalfTrans = false
    private fun setBackgroundHalfTransition(isNeed: Boolean) {
        isNeedBgHalfTrans = isNeed
        if (isNeedBgHalfTrans) {
            this.setOnDismissListener { backgroundAlpha(1f) }
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    private fun backgroundAlpha(bgAlpha: Float) {
        val lp = (context as Activity).window.attributes
        lp.alpha = bgAlpha //0.0-1.0
        (context as Activity).window.attributes = lp
    }

    init {
        val parentView = View.inflate(context, R.layout.yc_widget_popupwindow_base_picker, null)
        vBgBasePicker = parentView.findViewById(R.id.v_bg_base_picker)
        llBaseContentPicker = parentView.findViewById<View>(R.id.ll_base_content_picker) as LinearLayout
        /***
         * 添加布局到界面中
         */
        llBaseContentPicker.addView(View.inflate(context, bindLayout(), null))
        contentView = parentView
        //设置PopupWindow弹出窗体的宽
        this.width = ViewGroup.LayoutParams.MATCH_PARENT
        //设置PopupWindow弹出窗体的高
        this.height = ViewGroup.LayoutParams.WRAP_CONTENT
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        this.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        this.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        isFocusable = true //设置获取焦点
        isTouchable = true //设置可以触摸
        isOutsideTouchable = true //设置外边可以点击
        val dw = ColorDrawable(0xffffff)
        setBackgroundDrawable(dw)
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.animationStyle = R.style.YcBottomDialogWindowAnim
        initView(parentView)
        initData()
        initListener()
        vBgBasePicker.setOnClickListener(this)
        //是否需要屏幕半透明
        setBackgroundHalfTransition(isNeedBackgroundHalfTransition)
    }
}