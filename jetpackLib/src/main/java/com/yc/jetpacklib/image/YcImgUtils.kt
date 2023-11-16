package com.yc.jetpacklib.image

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.TextUtils
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.file.YcFileUtils
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


/**
 * Creator: yc
 * Date: 2021/7/15 14:22
 * UseDes:图片工具类（非ImageView加载图片）
 */
object YcImgUtils {
    /**
     * 加载网络图片(返回Bitmap)
     */
    @JvmStatic
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
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
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

    /**
     * 复制到系统相册里
     */
    fun copyToMediaPicture(context: Context, filePath: String, isImg: Boolean): Boolean {
        if (TextUtils.isEmpty(filePath)) return false
        val file = File(filePath)
        //判断android Q  (10 ) 版本
        return if (Build.VERSION.SDK_INT >= 29) {
            if (!file.exists()) {
                false
            } else {
                try {
                    if (isImg) {
                        MediaStore.Images.Media.insertImage(context.contentResolver, file.absolutePath, file.name, null)
                    } else {
                        val values = ContentValues()
                        values.put(MediaStore.Video.Media.DATA, file.absolutePath)
                        values.put(MediaStore.Video.Media.DISPLAY_NAME, file.name)
                        values.put(MediaStore.Video.Media.MIME_TYPE, "video/*")
                        values.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                        values.put(MediaStore.Video.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000)
                        val resolver = context.contentResolver
                        val uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
                    }
                    true
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    false
                }
            }
        } else { //老方法
            if (isImg) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
                values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, System.currentTimeMillis().toString() + "")
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.absolutePath)))
            } else {
                val localContentResolver = context.contentResolver
                val localContentValues = getVideoContentValues(File(filePath), System.currentTimeMillis())
                val localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues)
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri))
            }
            true
        }
    }


    fun getVideoContentValues(paramFile: File, paramLong: Long): ContentValues {
        val localContentValues = ContentValues()
        localContentValues.put(MediaStore.Video.Media.TITLE, paramFile.name)
        localContentValues.put(MediaStore.Video.Media.DISPLAY_NAME, paramFile.name)
        localContentValues.put(MediaStore.Video.Media.MIME_TYPE, "video/*")
        localContentValues.put(MediaStore.Video.Media.DATE_TAKEN, java.lang.Long.valueOf(paramLong))
        localContentValues.put(MediaStore.Video.Media.DATE_MODIFIED, java.lang.Long.valueOf(paramLong))
        localContentValues.put(MediaStore.Video.Media.DATE_ADDED, java.lang.Long.valueOf(paramLong))
        localContentValues.put(MediaStore.Video.Media.DATA, paramFile.absolutePath)
        localContentValues.put(MediaStore.Video.Media.SIZE, java.lang.Long.valueOf(paramFile.length()))
        return localContentValues
    }
}