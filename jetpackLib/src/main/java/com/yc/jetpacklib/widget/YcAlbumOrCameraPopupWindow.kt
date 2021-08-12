package com.yc.jetpacklib.widget

import android.content.Context
import android.view.View
import android.widget.Button
import com.yc.jetpacklib.R

class YcAlbumOrCameraPopupWindow : YcBasePopupWindow {
    private var btnFirst: Button? = null
    private var btnSecond: Button? = null
    private var btnThird: Button? = null
    private var viewThird: View? = null
    private var btnCancel: Button? = null
    private var onCameraOrAlbumSelectListener: OnCameraOrAlbumSelectListener? = null
    private var onThirdVisibilityListener: OnThirdVisibilityListener? = null

    constructor(context: Context?, onCameraOrAlbumSelectListener: OnCameraOrAlbumSelectListener?) : super(context!!) {
        this.onCameraOrAlbumSelectListener = onCameraOrAlbumSelectListener
    }

    constructor(context: Context?, OnThirdVisibilityListener: OnThirdVisibilityListener?) : super(context!!) {
        onThirdVisibilityListener = OnThirdVisibilityListener
        btnThird!!.visibility = View.VISIBLE
        viewThird!!.visibility = View.VISIBLE
    }

    val isNeedBackgroundHalfTransition: Boolean
        get() = true

    override fun bindLayout(): Int {
        return R.layout.yc_widget_popupwindow_album_or_camera
    }

    override fun initView(parentView: View?) {
        btnFirst = parentView!!.findViewById<View>(R.id.btnFirst) as Button
        btnSecond = parentView.findViewById<View>(R.id.btnSecond) as Button
        btnCancel = parentView.findViewById<View>(R.id.btnCancel) as Button
        btnThird = parentView.findViewById<View>(R.id.btnThird) as Button
        viewThird = parentView.findViewById(R.id.viewThird) as View
    }

    override fun initData() {}
    override fun initListener() {
        btnFirst!!.setOnClickListener {
            dismiss()
            if (onCameraOrAlbumSelectListener != null) {
                onCameraOrAlbumSelectListener!!.OnSelectFirst()
            }
            if (onThirdVisibilityListener != null) {
                onThirdVisibilityListener!!.OnSelectFirst()
            }
        }
        btnSecond!!.setOnClickListener {
            dismiss()
            if (onCameraOrAlbumSelectListener != null) {
                onCameraOrAlbumSelectListener!!.OnSelectSecond()
            }
            if (onThirdVisibilityListener != null) {
                onThirdVisibilityListener!!.OnSelectSecond()
            }
        }
        btnThird!!.setOnClickListener {
            dismiss()
            if (onThirdVisibilityListener != null) {
                onThirdVisibilityListener!!.OnSelectThird()
            }
        }
        btnCancel!!.setOnClickListener {
            dismiss()
            if (onCameraOrAlbumSelectListener != null) {
                onCameraOrAlbumSelectListener!!.OnSelectCancel()
            }
            if (onThirdVisibilityListener != null) {
                onThirdVisibilityListener!!.OnSelectCancel()
            }
        }
    }

    fun setBtnFirstText(text: String?) {
        btnFirst!!.text = text
    }

    fun setBtnSecondText(text: String?) {
        btnSecond!!.text = text
    }

    fun setBtnThirdText(text: String?) {
        btnThird!!.text = text
    }

    fun setBtnCancelText(text: String?) {
        btnCancel!!.text = text
    }

    interface OnCameraOrAlbumSelectListener {
        fun OnSelectFirst()
        fun OnSelectSecond()
        fun OnSelectCancel()
    }

    interface OnThirdVisibilityListener {
        fun OnSelectFirst()
        fun OnSelectSecond()
        fun OnSelectThird()
        fun OnSelectCancel()
    }
}