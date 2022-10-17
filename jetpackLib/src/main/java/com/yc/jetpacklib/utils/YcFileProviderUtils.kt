package com.yc.jetpacklib.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import java.io.*
import java.util.*

/**
 * Creator: yc
 * Date: 2022/8/17 19:56
 * UseDes:
 */
object YcFileProviderUtils {

    /**
     * 根据Uri获取文件绝对路径，解决Android4.4以上版本Uri转换 兼容Android 10
     *
     * @param context
     * @param imageUri
     * @param isAddUUID 是否需要给沙盒的文件名添加UUID，默认不需要，开发时候按需配置，如果有选择同名文件的隐患，最好配置为true
     */
    fun getFileAbsolutePath(context: Context, imageUri: Uri, isAddUUID: Boolean = false): String? {
        if (Build.VERSION.SDK_INT <= 29 && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                val docId: String = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":")
                val type = split[0]
                if ("primary" == type) {
                    return "${Environment.getExternalStorageDirectory()}/${split[1]}"
                }
            } else if (isDownloadsDocument(imageUri)) {
                val id = DocumentsContract.getDocumentId(imageUri)
                val contentUri: Uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    id.toLong()
                )
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":")
                val type = split[0]
                var contentUri: Uri? = null
                when (type) {
                    "image" -> {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                }
                val selection = "${MediaStore.Images.Media._ID}=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if (Build.VERSION.SDK_INT >= 29) {
            return uriToFileApiQ(context, imageUri, isAddUUID)
        } else if ("content" == imageUri.scheme) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri)) {
                return imageUri.lastPathSegment
            }
            return getDataColumn(context, imageUri, null, null)
        }
        // File
        else if ("file" == imageUri.scheme) {
            return imageUri.path
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column: String = MediaStore.Images.Media.DATA
        val projection = arrayOf(column)
        if (uri != null) {
            try {
                cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val index: Int = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index);
                }
            } finally {
                cursor?.close()
            }
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * Android 10 以上适配 另一种写法
     * @param context
     * @param uri
     * @return
     */
    @SuppressLint("Range")
    fun getFileFromContentUri(context: Context, uri: Uri): String? {
        val filePath: String?
        val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME)
        val contentResolver: ContentResolver = context.contentResolver
        val cursor: Cursor? = contentResolver.query(uri, filePathColumn, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            try {
                filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]))
                return filePath
            } catch (e: Exception) {
            } finally {
                cursor.close()
            }
        }
        return ""
    }

    /**
     * Android 10 以上适配
     * @param context
     * @param uri
     * @return
     */
    @SuppressLint("Range")
    private fun uriToFileApiQ(context: Context, uri: Uri, isAddUUID: Boolean): String? {
        var file: File? = null
        //android10以上转换
        if (uri.scheme.equals(ContentResolver.SCHEME_FILE)) {
            file = File(uri.path!!)
        } else if (uri.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            val contentResolver: ContentResolver = context.contentResolver
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val displayName: String = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    )
                    try {
                        //是否需要添加UUID,默认不需要,开发时候按需配置
                        val fileName = if (isAddUUID) {
                            "${get22UUID()}-$displayName"
                        } else {
                            displayName
                        }
                        contentResolver.openInputStream(uri).use { inputS ->
                            if (inputS != null) {
                                val cache = File(context.externalCacheDir?.absolutePath, fileName)
                                FileOutputStream(cache).use { outS ->
                                    //定义一个数组 用于保存 一次性读取的多个数据
                                    val buffer = ByteArray(1024)
                                    var len = 0
                                    //从输入流里面获取数据 写入到输出流
                                    while (true) {
                                        //read()  一个字节一个字节的读取
                                        len = inputS.read(buffer)
                                        //判断是否读取完毕 -> 返回值为-1
                                        if (len == -1) {
                                            break
                                        }
                                        //write() 一个字节一个字节的写入
                                        outS.write(buffer)
                                    }
                                }
                                file = cache
                            } else {
                                file = null
                            }

                        }
                    } catch (e: IOException) {
                        e.printStackTrace();
                    } finally {
                        cursor.close()
                    }
                }
            }

        }
        return file?.absolutePath
    }


    /**
     * path转uri
     * @param context
     * @param filePath
     * @return
     */
    fun toUri(context: Context, filePath: String): Uri {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.applicationInfo.packageName + ".fileprovider", File(filePath))
        }
        return Uri.fromFile(File(filePath))
    }

    private val DIGITS64 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

    /**
     * 获取22位长度的UUID
     */
    fun get22UUID(): String {
        val u = UUID.randomUUID()
        return toIDString(u.mostSignificantBits) + toIDString(u.leastSignificantBits)
    }

    private fun toIDString(l: Long): String {
        var l = l
        val buf = "00000000000".toCharArray() // 限定11位长度
        var length = 11
        val least = 61L // 0x0000003FL
        do {
            buf[--length] = DIGITS64[(l and least).toInt()] // l & least取低6位
            /* 无符号的移位只有右移，没有左移
             * 使用“>>>”进行移位
             * 为什么没有无符号的左移呢，知道原理的说一下哈
             */l = l ushr 6
        } while (l != 0L)
        return String(buf)
    }
}