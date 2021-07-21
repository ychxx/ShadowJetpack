package com.yc.jetpacklib.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.file.YcFileUtils
import java.io.*

/**
 * Creator: yc
 * Date: 2021/7/15 14:22
 * UseDes:图片工具类（非ImageView加载图片）
 */
object YcImgUtils {
    /**
     * 加载圆角图片
     */
    @JvmStatic
    fun loadRoundedCornerImgFromDrawableId(imageView: ImageView, drawableId: Int) {
        GlideApp.with(imageView.context)
            .load(drawableId)
            .transform(CenterCrop(), RoundedCorners(14))
            .into(imageView)
    }
    /**
     * 加载圆形图片
     */
    @JvmStatic
    fun loadCircleImgFromUrl(
        context: Context,
        imageView: ImageView,
        imgUrl: String,
        placeHolder: Drawable?
    ) {
        GlideApp.with(context)
            .load(imgUrl)
            .placeholder(placeHolder)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(imageView)
    }

    /**
     * 加载网络图片(返回Bitmap)
     */
    @JvmStatic
    fun loadNetImg(context: Context, imgUrl: String?, callBack: (Bitmap) -> Unit) {
        GlideApp.with(context)
            .asBitmap()
            .load(imgUrl)
            .into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    Handler(Looper.getMainLooper()).post {
                        callBack.invoke(resource)
                    }
                }
            })
    }

    /**
     * 加载网络图片(返回Bitmap)
     */
    @JvmStatic
    fun loadNetImg(
        context: Context,
        imgUrl: String?,
        width: Int,
        height: Int,
        callBack: (Bitmap) -> Unit
    ) {
        GlideApp.with(context)
            .asBitmap()
            .load(imgUrl)
            .into(object : SimpleTarget<Bitmap?>(width, height) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    Handler(Looper.getMainLooper()).post {
                        callBack.invoke(resource)
                    }
                }
            })
    }

    /**
     * 获取到jpeg文件
     * @param imgFilePath String
     * @param imgPathSave String
     * @return File?
     */
    fun getJpegFile(imgFilePath: String, imgPathSave: String): File? {
        when (getImgMimeType(imgFilePath)) {
            "image/png" -> {
                return try {
                    val file: File? = YcFileUtils.createFile(imgPathSave)
                    val bos = BufferedOutputStream(FileOutputStream(file))
                    val bitmap = BitmapFactory.decodeStream(FileInputStream(imgFilePath))
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                    bos.flush()
                    bos.close()
                    file
                } catch (e: IOException) {
                    e.printStackTrace()
                    ycLogE("图片png转成jpg失败!")
                    null
                }
            }
            "image/jpeg" -> {
                return File(imgFilePath)
            }
            else -> {
                ycLogE("图片格式不是jpeg或png")
                return null
            }
        }
    }

    fun bitmapToFile(
        bitmap: Bitmap,
        imgPathSave: String,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): File? {
        return try {
            val file: File? = YcFileUtils.createFile(imgPathSave)
            val bos = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(format, 100, bos)
            bos.flush()
            bos.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            ycLogE("图片保存失败!")
            null
        }
    }

    fun getImgMimeType(imgFilePath: String?): String {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imgFilePath, options)
        return options.outMimeType
    }

    fun imgPathToBitmap(imgPath: String): Bitmap? {
        return try {
            BitmapFactory.decodeStream(FileInputStream(imgPath))
        } catch (e: Exception) {
            ycLogE("path转换Bitmap失败")
            e.printStackTrace()
            null
        }
    }
}