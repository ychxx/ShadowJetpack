package com.yc.jetpacklib.vue3

import android.webkit.WebChromeClient
import androidx.annotation.IntDef
import com.yc.jetpacklib.extension.ycIsNotEmpty

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
     * 见证取样自定义相机
     */
    const val IMG_CAMERA_WITNESS_SAMPLE = 21

    /**
     * 视频选择自定义1
     */
    const val VIDEO_SELECT_1 = 31

    /**
     * 录像自定义1
     */
    const val VIDEO_CAMERA_1 = 41

    /**
     * 音频选择
     */
    const val AUDIO_SELECT = 51

    /**
     * 语音识别
     */
    const val SPEECH_RECOGNITION = 50

    /**
     * 跳转到身份证扫描-正面
     */
    const val ID_CARD_SCAN_FRONT = 60

    /**
     * 跳转到身份证扫描-反面
     */
    const val ID_CARD_SCAN_BACK = 61

    /**
     * 跳转到人脸识别
     */
    const val FACE_SCAN = 70


    /**
     * 跳转到裂缝测量
     */
    const val MEASURE_CRACK = 80


    /**
     * 文件选择
     */
    const val FILE_SELECT = 100

    /**
     * 未知
     */
    const val ERROR = -1

    @IntDef(
        IMG_SELECT,
        IMG_CAMERA,
        VIDEO_SELECT,
        VIDEO_CAMERA,
        IMG_SELECT_1,
        IMG_CAMERA_WITNESS_SAMPLE,//见证取样自定义相机
        VIDEO_SELECT_1,
        VIDEO_CAMERA_1,
        SPEECH_RECOGNITION,
        ID_CARD_SCAN_FRONT,
        ID_CARD_SCAN_BACK,
        FACE_SCAN,
        MEASURE_CRACK,
        FILE_SELECT,
        AUDIO_SELECT,
        ERROR
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class SelectorType {}

    val VUE_ACCEPT = listOf(
        "image/*",
        "video/*",
        "audio/*",
        "*/*",
        "*/*,image/*",
        "video/*,audio/*",
        "*/*,image/*,audio/*",
        "image/*,video/*",
        "*/*,audio/*",
        "*/*,video/*",
        "*/*,image/*,video/*",
        "*/*,audio/*,video/*",
        "*/*,image/*,audio/*,video/*",
        "image/*,audio/*",
        "image/*,audio/*,video/*"
    )
    val ANDROID_ACCEPT = listOf(
        IMG_SELECT,
        VIDEO_SELECT,
        AUDIO_SELECT,
        FILE_SELECT,
        IMG_CAMERA,
        VIDEO_CAMERA,
        IMG_SELECT_1,
        IMG_CAMERA_WITNESS_SAMPLE,
        VIDEO_SELECT_1,
        VIDEO_CAMERA_1,
        SPEECH_RECOGNITION,
        ID_CARD_SCAN_FRONT,
        ID_CARD_SCAN_BACK,
        FACE_SCAN,
        MEASURE_CRACK,
    )

    @SelectorType
    @JvmStatic
    fun acceptVueToAndroid(fileChooserParams: WebChromeClient.FileChooserParams?): Int {
        var accept = ""
        fileChooserParams?.acceptTypes?.forEach {
            accept += if (accept.ycIsNotEmpty()) {
                ",${it}"
            } else {
                it
            }
        }
        VUE_ACCEPT.forEachIndexed { index: Int, item ->
            if (accept == item && ANDROID_ACCEPT.size > index) {
                return ANDROID_ACCEPT[index]
            }
        }
        return ERROR
    }
}