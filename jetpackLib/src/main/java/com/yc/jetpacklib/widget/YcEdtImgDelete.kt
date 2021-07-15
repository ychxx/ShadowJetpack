package com.yc.jetpacklib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import com.yc.jetpacklib.R

/**
 * Creator: yc
 * Date: 2021/6/21 21:04
 * UseDes:
 */
@SuppressLint("CustomViewStyleable")
class YcEdtImgDelete @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    YcEdtImg(context, attrs, defStyleAttr) {
    /**
     * 密码是否可见
     */
    protected var mIvWidgetRight1Password: Boolean = false
    override fun initView() {
        mIvWidgetRight1.setOnClickListener {
            //否则隐藏密码
            if (mIvWidgetRight1Password) {
                mEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                mEdt.setSelection(mEdt.text.toString().length)
                mIvWidgetRight1.setImageResource(R.drawable.yc_icon_eye_gone)
                mIvWidgetRight1Password = false
            } else {
                mEdt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                mEdt.setSelection(mEdt.text.toString().length)
                mIvWidgetRight1.setImageResource(R.drawable.yc_icon_eye_visible)
                mIvWidgetRight1Password = true
            }
        }
        mIvWidgetRight2.setOnClickListener {
            mEdt.setText("")
            mEdt.requestFocus()
        }
    }
}