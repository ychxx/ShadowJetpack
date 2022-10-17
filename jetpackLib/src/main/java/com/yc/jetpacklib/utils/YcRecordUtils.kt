package com.yc.jetpacklib.utils

import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.yc.jetpacklib.extension.ycIsEmpty

/**
 * Creator: yc
 * Date: 2022/9/27 16:17
 * UseDes:录音
 */
open class YcRecordUtils(mLifecycleOwner: LifecycleOwner?) {
    private var mMediaRecorder: MediaRecorder? = null
    var mFailCall: ((errorTip: String) -> Unit)? = null
    var mSuccessCall: ((savePath: String) -> Unit)? = null
    var mStartCall: ((data: String) -> Unit)? = null
    private var mSavePath: String = ""

    init {
        mLifecycleOwner?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                recorderRelease()
            }
        })
    }

    open fun initRecord() {
        if (mMediaRecorder != null) {
            recorderRelease()
        }
        mMediaRecorder = MediaRecorder()
        mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mMediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
        mMediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
    }

    open fun recordStart(savePath: String? = null) {
        try {
            if (savePath.ycIsEmpty()) {
                mFailCall?.invoke("录音失败,保存路径为空!")
            } else {
                mSavePath = savePath!!
                if (mMediaRecorder == null) {
                    initRecord()
                }
                mMediaRecorder!!.setOutputFile(savePath)
                mMediaRecorder!!.prepare()
                mMediaRecorder!!.start()
                mStartCall?.invoke("开始录音")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mFailCall?.invoke("开始录音失败")
        }
    }

    open fun recordEnd() {
        try {
            if (mMediaRecorder == null) {
                mFailCall?.invoke("请先开始录音")
            } else {
                mMediaRecorder?.stop()
                recorderRelease()
                mSuccessCall?.invoke(mSavePath)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mFailCall?.invoke("录音完成失败")
        }
    }

    open fun recorderRelease() {
        mMediaRecorder?.reset()
        mMediaRecorder?.release()
        mMediaRecorder = null
    }
}