package com.yc.jetpacklib.vue3

import android.webkit.WebChromeClient
import androidx.annotation.IntDef

/**
 * Creator: yc
 * Date: 2022/10/13 16:49
 * UseDes:
 */
object YcVueSelectorUtil {
    /**
     * 图片选择
     */
    const val IMG_SELECT = 10

    /**
     * 拍照
     */
    const val IMG_CAMERA = 20

    /**
     * 视频选择
     */
    const val VIDEO_SELECT = 30

    /**
     * 录像
     */
    const val VIDEO_CAMERA = 40

    /**
     * 图片选择自定义1
     */
    const val IMG_SELECT_1 = 11

    /**
     * 拍照自定义1
     */
    const val IMG_CAMERA_1 = 21

    /**
     * 视频选择自定义1
     */
    const val VIDEO_SELECT_1 = 31

    /**
     * 录像自定义1
     */
    const val VIDEO_CAMERA_1 = 41

    /**
     * 语音识别
     */
    const val SPEECH_RECOGNITION = 50

    /**
     * 未知
     */
    const val ERROR = -1

    @IntDef(IMG_SELECT, IMG_CAMERA, VIDEO_SELECT, VIDEO_CAMERA, IMG_SELECT_1, IMG_CAMERA_1, VIDEO_SELECT_1, VIDEO_CAMERA_1, SPEECH_RECOGNITION, ERROR)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class SelectorType {}

    val VUE_ACCEPT = listOf(
        "application/alto-costmap+json",
        "application/alto-costmapfilter+json",
        "application/alto-directory+json",
        "application/alto-endpointprop+json",
        "application/alto-endpointpropparams+json",
        "application/alto-endpointcost+json",
        "application/alto-endpointcostparams+json",
        "application/alto-error+json",
        "application/alto-networkmapfilter+json",
        "application/alto-networkmap+json"
    )
    val ANDROID_ACCEPT =
        listOf(IMG_SELECT, IMG_CAMERA, VIDEO_SELECT, VIDEO_CAMERA, IMG_SELECT_1, IMG_CAMERA_1, VIDEO_SELECT_1, VIDEO_CAMERA_1, SPEECH_RECOGNITION, ERROR)

    @SelectorType
    @JvmStatic
    fun acceptVueToAndroid(fileChooserParams: WebChromeClient.FileChooserParams?): Int {
        var accept = ""
        fileChooserParams?.acceptTypes?.forEach {
            accept += it
        }
        VUE_ACCEPT.forEachIndexed { index: Int, item ->
            if (accept.contains(item)) {
                return if (index < ANDROID_ACCEPT.size) {
                    ANDROID_ACCEPT[index]
                } else {
                    ERROR
                }
            }
        }
        return ERROR
    }
}