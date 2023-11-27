package com.yc.jetpacklib.file

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import com.yc.jetpacklib.extension.ycIsEmpty
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.init.YcJetpack
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

/**
 * 文件工具类
 * 处理文件相关
 */
object YcFileUtils {
    //    /**
//     * SD卡根目录地址
//     */
//    @JvmStatic
//    val SD_PATH = Environment.getExternalStorageDirectory().path
//
//
//    /**
//     * 获取sd卡的绝对路径
//     *
//     * @return String 如果sd卡存在，返回sd卡的绝对路径，否则返回null
//     */
//    @JvmStatic
//    fun getSDPath(context: Context?): String? {
//        if (context == null) return null
//        val sdcard = Environment.getExternalStorageState()
//        return if (sdcard == Environment.MEDIA_MOUNTED) {
//            context.getExternalFilesDir(null)?.absolutePath
//        } else {
//            null
//        }
//    }
    /**
     * 获取内部存储路径(其他app会无法找到，系统自带文件夹有的也找不到)
     */
    @JvmStatic
    fun getInternalPath(): String {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Environment.getExternalStorageDirectory().path
        }else{
            YcJetpack.mInstance.mApplication.filesDir.path
        }
    }

    /**
     * 获取外部存储路径
     * @param fileType 文件类型(默认照相机存放位置)
     * DCIM照相机存放   Environment.DIRECTORY_DCIM
     * 图片           Environment.DIRECTORY_PICTURES
     * 视频           Environment.DIRECTORY_MOVIES
     * 下载           Environment.DIRECTORY_DOWNLOADS
     */
    @JvmStatic
    fun getExternalPath(fileType: String = Environment.DIRECTORY_DCIM): String? {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            YcJetpack.mInstance.mApplication.getExternalFilesDir(fileType)?.path
        } else {
            ycLogE("getExternalPath-无权限")
            getInternalPath()
        }
    }

    /**
     * 创建一个文件
     *
     * @param filePath 文件地址（包含后缀）
     * @return 返回是否创建成功
     */
    @JvmStatic
    fun createFile(filePath: String?): File? {
        var newFile: File? = null
        try {
            newFile = File(filePath)
            //判断文件是否不存在，不存在则创建
            if (!newFile.parentFile.exists()) {
                if (!newFile.parentFile.mkdirs()) {
                    Log.e("YcUtils", "文件夹创建失败$filePath")
                }
            }
            if (!newFile.exists()) {
                if (newFile.createNewFile()) {
                    Log.e("YcUtils", "文件创建成功$filePath")
                } else {
                    Log.e("YcUtils", "文件创建失败")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("YcUtils", "文件创建失败异常:" + e.message)
        }
        return newFile
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径（包含后缀）
     * @return 是否删除成功
     */
    @JvmStatic
    fun delFile(filePath: String?): Boolean {
        if (filePath.ycIsEmpty()) {
            return true
        } else {
            try {
                val file = File(filePath)
                if (file.exists()) return file.delete()
            } catch (e: Exception) {
                Log.e("YcUtils", e.message!!)
                return false
            }
            return true
        }
    }

    /**
     * 检查文件是否存在
     *
     * @param fileUrl
     * @return 是否存在
     */
    @JvmStatic
    fun checkFileExists(fileUrl: String?): Boolean {
        return if (fileUrl?.isEmpty() == true) {
            false
        } else {
            val newPath = File(fileUrl)
            newPath.exists()
        }
    }

    /**
     * 获取后缀（根据文件地址）
     *
     * @param filePath 文件地址(含后缀)
     * @return 后缀
     */
    @JvmStatic
    fun filePathToSuffix(filePath: String?): String {
        return if (filePath?.isEmpty() == true) {
            ""
        } else {
            fileNameToSuffix(File(filePath).name)
        }
    }

    /**
     * 获取后缀（根据文件名）
     *
     * @param fileName 文件名(含后缀)
     * @return 后缀
     */
    @JvmStatic
    fun fileNameToSuffix(fileName: String?): String {
        return if (fileName?.isEmpty() == true) {
            return ""
        } else {
            val lastIndexOf = fileName?.lastIndexOf(".")
            if (lastIndexOf == null || lastIndexOf < 1) {
                ""
            } else {
                fileName.substring(1, lastIndexOf)
            }
        }
    }

    /**
     * 将文件转为byte[]
     *
     * @param filePath 文件路径（包含后缀）
     * @return
     */
    @JvmStatic
    fun fileToBytes(filePath: String?): ByteArray? {
        if (filePath?.isEmpty() == true) {
            return null
        } else {
            var s: ByteArray? = null
            try {
                val file = File(filePath)
                val `in` = FileInputStream(file)
                val out = ByteArrayOutputStream()
                val b = ByteArray(1024)
                var n: Int
                while (`in`.read(b).also { n = it } != -1) {
                    out.write(b, 0, n)
                }
                out.close()
                `in`.close()
                s = out.toByteArray()
                out.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return s
        }
    }

    /**
     * 获取指定文件大小
     *
     * @param filePath
     */
    @JvmStatic
    fun getFileSize(filePath: String?): Long {
        var size: Long = 0
        try {
            val file = File(filePath)
            if (file.exists()) {
                val fis = FileInputStream(file)
                size = fis.available().toLong()
            } else {
                Log.e("YcUtils", "获取文件大小失败！原因：文件不存在!")
            }
        } catch (e: Exception) {
            Log.e("YcUtils", "获取文件大小失败！原因：" + e.message)
        }
        return size
    }
}