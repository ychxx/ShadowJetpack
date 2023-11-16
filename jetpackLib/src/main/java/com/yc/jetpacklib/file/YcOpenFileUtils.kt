package com.yc.jetpacklib.file

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.yc.jetpacklib.init.YcJetpack
import java.io.File
import java.util.*

/**
 *
 */
object YcOpenFileUtils {
    //这是在网上找到的一些常见的文件后缀名及其对应的MIME类型，使用中有新的后缀可以加入进去
    var MIMES: HashMap<String?, String?> = object : HashMap<String?, String?>() {
        init {
            put(".3gp", "video/3gpp")
            put(".asf", "video/x-ms-asf")
            put(".avi", "video/x-msvideo")
            put(".m4u", "video/vnd.mpegurl")
            put(".m4v", "video/x-m4v")
            put(".mov", "video/quicktime")
            put(".mp4", "video/mp4")
            put(".mpe", "video/mpeg")
            put(".mpeg", "video/mpeg")
            put(".mpg", "video/mpeg")
            put(".mpg4", "video/mp4")
            put(".wav", "audio/x-wav")
            put(".wma", "audio/x-ms-wma")
            put(".wmv", "audio/x-ms-wmv")
            put(".m3u", "audio/x-mpegurl")
            put(".rmvb", "audio/x-pn-realaudio")
            put(".mp2", "audio/x-mpeg")
            put(".mp3", "audio/x-mpeg")
            put(".m4a", "audio/mp4a-latm")
            put(".m4b", "audio/mp4a-latm")
            put(".m4p", "audio/mp4a-latm")
            put(".mpga", "audio/mpeg")
            put(".ogg", "audio/ogg")
            put(".bmp", "image/bmp")
            put(".gif", "image/gif")
            put(".jpeg", "image/jpeg")
            put(".jpg", "image/jpeg")
            put(".png", "image/png")
            put(".prop", "text/plain")
            put(".c", "text/plain")
            put(".rc", "text/plain")
            put(".conf", "text/plain")
            put(".cpp", "text/plain")
            put(".h", "text/plain")
            put(".java", "text/plain")
            put(".htm", "text/html")
            put(".html", "text/html")
            put(".log", "text/plain")
            put(".sh", "text/plain")
            put(".xml", "text/plain")
            put(".txt", "text/plain")
            put(".apk", "application/vnd.android.package-archive")
            put(".mpc", "application/vnd.mpohun.certificate")
            put(".msg", "application/vnd.ms-outlook")
            put(".pps", "application/vnd.ms-powerpoint")
            put(".ppt", "application/vnd.ms-powerpoint")
            put(".wps", "application/vnd.ms-works")
            put(".bin", "application/octet-stream")
            put(".class", "application/octet-stream")
            put(".exe", "application/octet-stream")
            put(".gtar", "application/x-gtar")
            put(".gz", "application/x-gzip")
            put(".js", "application/x-javascript")
            put(".tar", "application/x-tar")
            put(".tgz", "application/x-compressed")
            put(".rar", "application/x-rar-compressed")
            put(".z", "application/x-compress")
            put(".rtf", "application/rtf")
            put(".pdf", "application/pdf")
            put(".zip", "application/zip")
            put(".doc", "application/msword")
            put(".jar", "application/java-archive")
            put("", "*/*") //所有文件
        }
    }

    fun open(context: Context, path: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW //动作，会根据不同的数据类型打开相应的Activity
        intent.addCategory(Intent.CATEGORY_DEFAULT) //意图，系统默认
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) //标签，创建一个新的任务栈存放Activity，在最顶部展示
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //标签，授予目录临时共享权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //Android7.0之后
            intent.setDataAndType(
                FileProvider.getUriForFile(
                    context, YcJetpack.mInstance.mApplication.packageName + ".fileprovider",
                    File(path)
                ),
                getFileType(path)
            )
        } else {
            intent.setDataAndType(Uri.fromFile(File(path)), getFileType(path))
        }
        context.startActivity(intent)
    }

    /**
     * @param path 文件路径
     * @return type 文件类型
     */
    fun getFileType(path: String): String {
        //默认类型
        var type = "*/*"
        //获取后缀名前的分隔符"."在path中的位置。
        val index = path.lastIndexOf(".")
        //防止路径不存在"."出现异常
        if (index < 0) {
            return type
        }
        //获取文件的后缀名
        val fileType = path.substring(index).toLowerCase(Locale.getDefault())
        MIMES[fileType]?.apply {
            type = this
        }
        return type
    }
}