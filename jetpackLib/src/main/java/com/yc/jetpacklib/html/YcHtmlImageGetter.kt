package com.yc.jetpacklib.html

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.text.Html.ImageGetter
import android.widget.TextView
import com.yc.jetpacklib.image.YcImgUtils

/**
 * 用于解决TextView展示Html时图片不显示问题
 */
class YcHtmlImageGetter(private val textView: TextView, private val mContext: Context) : ImageGetter {

    override fun getDrawable(imgUrl: String): Drawable {
        val drawable = LevelListDrawable()
        YcImgUtils.loadNetImg(mContext, imgUrl) { resource ->
            drawable.addLevel(1, 1, BitmapDrawable(mContext.resources, resource))
            drawable.setBounds(0, 0, resource.width, resource.height)
            drawable.level = 1
            textView.invalidate()
            textView.text = textView.text
        }
        return drawable
    }
}
