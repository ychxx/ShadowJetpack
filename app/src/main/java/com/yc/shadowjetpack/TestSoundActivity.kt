package com.yc.shadowjetpack

import android.Manifest
import android.media.MediaRecorder
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.extension.showToast
import com.yc.jetpacklib.file.YcFileUtils
import com.yc.jetpacklib.permission.YcPermissionHelper
import com.yc.shadowjetpack.databinding.TestSoundActivityBinding
import java.io.File


class TestSoundActivity : YcBaseActivityPlus<TestSoundActivityBinding>(TestSoundActivityBinding::inflate) {
    private var mMediaRecorder: MediaRecorder? = null
    private val mYcPermissionHelper by lazy { YcPermissionHelper(this) }
    override fun TestSoundActivityBinding.initView() {
        btnTestPermission.setOnClickListener {
            mYcPermissionHelper.mPermission =
                listOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray()
            mYcPermissionHelper.start()
        }
        btnTestConfig1.setOnClickListener {
            if (mMediaRecorder != null) {
                recorderRelease()
            }
            mMediaRecorder = MediaRecorder()
            mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mMediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
            mMediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mMediaRecorder?.setOutputFile(YcFileUtils.getSDPath(this@TestSoundActivity) + File.separator + "recorder_format_nb_encoder_nb.amr")
        }
//        btnTestConfig2.setOnClickListener {
//            if (mMediaRecorder != null) {
//                recorderRelease()
//            }
//            mMediaRecorder = MediaRecorder()
//            mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
//            mMediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
//            mMediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
//            mMediaRecorder?.setOutputFile(YcFileUtils.getSDPath(this@TestSoundActivity) + File.separator + "recorder_format_nb_encoder_wb.amr")
//
//        }
//        btnTestConfig3.setOnClickListener {
//            if (mMediaRecorder != null) {
//                recorderRelease()
//            }
//            mMediaRecorder = MediaRecorder()
//            mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
//            mMediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB)
//            mMediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//            mMediaRecorder?.setOutputFile(YcFileUtils.getSDPath(this@TestSoundActivity) + File.separator + "recorder_format_wb_encoder_nb.amr")
//        }
        btnTestConfig4.setOnClickListener {
            if (mMediaRecorder != null) {
                recorderRelease()
            }
            mMediaRecorder = MediaRecorder()
            mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mMediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB)
            mMediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
            mMediaRecorder?.setOutputFile(YcFileUtils.getSDPath(this@TestSoundActivity) + File.separator + "recorder_format_wb_encoder_wb.amr")
        }

        btnTestStart.setOnClickListener {
            soundStart()
        }
        btnTestEnd.setOnClickListener {
            soundEnd()
        }
        mYcPermissionHelper.register()
    }

    private fun soundStart() {
        try {
            if (mMediaRecorder == null) {
                showToast("未点配置按钮")
            } else {
                mMediaRecorder?.prepare()
                mMediaRecorder?.start()
                showToast("开始录音")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("开始录音失败")
        }
    }

    private fun soundEnd() {
        try {
            if (mMediaRecorder == null) {
                showToast("请先开始")
            } else {
                mMediaRecorder?.stop()
                recorderRelease()
                showToast("录音完成")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("录音完成失败")
        }
    }

    private fun recorderRelease() {
        mMediaRecorder?.reset()
        mMediaRecorder?.release()
        mMediaRecorder = null
    }
}