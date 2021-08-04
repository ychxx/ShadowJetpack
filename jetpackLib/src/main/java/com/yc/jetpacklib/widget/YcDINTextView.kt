package com.yc.jetpacklib.widget

import android.content.Context
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.yc.jetpacklib.R

/**
 * 处理DIN字体
 */
class YcDINTextView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(
    context!!, attrs, defStyleAttr
) {
    private fun init() {
        val typeface = ResourcesCompat.getFont(context, R.font.dinmittelschriftstd)
        setTypeface(typeface)
        //        onRefreshPadding(getText().toString());
    }

    override fun setText(text: CharSequence, type: BufferType) {
//        onRefreshPadding(text.toString());
        super.setText(text, type)
    } //

    //    private boolean mIsHasPadding = false;//是否添加了间距
    //
    //    private void onRefreshPadding(String s) {
    //        try {
    //            if (s.charAt(0) == '1') {
    //                if (!mIsHasPadding) {
    //                    int left = getPaddingLeft();
    //                    int right = getPaddingRight();
    //                    int top = getPaddingTop();
    //                    int bottom = getPaddingBottom();
    //                    setPadding(left, top, right + YcUI.dpToPx(10 ), bottom);
    //                    mIsHasPadding = true;
    //                }
    //            } else {
    //                if (mIsHasPadding) {
    //                    int left = getPaddingLeft();
    //                    int right = getPaddingRight();
    //                    int top = getPaddingTop();
    //                    int bottom = getPaddingBottom();
    //                    setPadding(left, top, right - YcUI.dpToPx(5), bottom);
    //                    mIsHasPadding = true;
    //                }
    //            }
    //
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }
    init {
        init()
    }
}