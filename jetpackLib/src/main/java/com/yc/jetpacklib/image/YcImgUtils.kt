package com.yc.jetpacklib.image

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.yc.jetpacklib.R

/**
 * Creator: yc
 * Date: 2021/7/15 14:22
 * UseDes:用于加载非ImageView
 */
object YcImgUtils {
    /**
     * 加载网络图片(返回Bitmap)
     */
    fun loadNetImg(context: Context, imgUrl: String?, callBack: (Bitmap) -> Unit) {
        GlideApp.with(context)
            .asBitmap()
            .load(imgUrl)
            .into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                    Handler(Looper.getMainLooper()).post {
                        callBack.invoke(resource)
                    }
                }
            })
    }

    /**
     * 加载网络图片(返回Bitmap)
     */
    fun loadNetImg(context: Context, imgUrl: String?, width: Int, height: Int, callBack: (Bitmap) -> Unit) {
        GlideApp.with(context)
            .asBitmap()
            .load(imgUrl)
            .into(object : SimpleTarget<Bitmap?>(width, height) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                    Handler(Looper.getMainLooper()).post {
                        callBack.invoke(resource)
                    }
                }
            })
    }
}