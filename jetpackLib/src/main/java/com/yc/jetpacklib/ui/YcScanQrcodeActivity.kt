package com.yc.jetpacklib.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar

import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.yc.jetpacklib.R
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.databinding.YcScanQrcodeActivityBinding
import com.yc.jetpacklib.extension.ycCreateResultLauncher
import com.yc.jetpacklib.utils.showErrorDialog
import com.yc.jetpacklib.widget.dialog.YcCommonDialog
import java.io.IOException

/**
 * SimpleDes:
 * Creator: Sindi
 * Date: 2021-07-29
 * UseDes:
 */
open class YcScanQrcodeActivity : YcBaseActivityPlus<YcScanQrcodeActivityBinding>(YcScanQrcodeActivityBinding::inflate) {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var remoteView: RemoteView? = null
    var mScreenWidth = 0
    var mScreenHeight = 0
    val SCAN_FRAME_SIZE = 240//scan_view_finder 的宽度和高度都是 240 dp。
    private val img = intArrayOf(R.drawable.yc_ic_scan_light_on, R.drawable.yc_ic_scan_light_off)
    private val dialog by lazy {
        YcCommonDialog(this, this)
    }

    companion object {
        const val SCAN_RESULT = "scanResult"//扫描得结果
        const val SCAN_AREA_BG = "scanAreaBg"//扫描得结果
        private const val REQUEST_PERMISSION_CAMERA = 1000
        private const val REQUEST_PERMISSION_PHOTO = 1001

        fun newInstance(activity: Activity, launcher: ActivityResultLauncher<Intent>, scanAreaBg: Int? = R.drawable.yc_scan_code_frame_bg) {
            val intent = Intent(activity, YcScanQrcodeActivity::class.java)
            intent.putExtra(SCAN_AREA_BG, scanAreaBg)
            launcher.launch(intent)
        }
    }

//    override fun initImmersionBar() {
//        ImmersionBar.with(this)
//            .statusBarColor(R.color.transparent)
//            .statusBarDarkFont(false)
//            .hideBar(BarHide.FLAG_SHOW_BAR)
//            .fitsSystemWindows(false)
//            .init()
//    }

    override fun YcScanQrcodeActivityBinding.initView() {
        ivScanArea.setBackgroundResource(intent.getIntExtra(SCAN_AREA_BG, R.drawable.yc_scan_code_frame_bg))
        resultLauncher = ycCreateResultLauncher {
            if (it.data != null) {
                try {
                    var bitmap = MediaStore.Images.Media.getBitmap(this@YcScanQrcodeActivity.contentResolver, it.data!!.data)
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)//API23  压缩位图,防止OOM
                        bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true)

                    val hmsScans = ScanUtil.decodeWithBitmap(
                        this@YcScanQrcodeActivity, bitmap,
                        HmsScanAnalyzerOptions.Creator().setPhotoMode(true).create()
                    )
                    if (hmsScans != null && hmsScans.isNotEmpty() && hmsScans[0] != null && !TextUtils.isEmpty(hmsScans[0]!!.getOriginalValue())) {
                        val intent = Intent()
                        intent.putExtra(SCAN_RESULT, hmsScans[0].showResult)
                        setResult(RESULT_OK, intent)
                        finish()
                    } else {
                        AlertDialog.Builder(this@YcScanQrcodeActivity).setTitle("提示").setMessage("此图片无法识别")
                            .setPositiveButton("确定", null).show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        val dm = resources.displayMetrics//1、获取屏幕密度以计算取景器的矩形。
        val density = dm.density
        //2. 获取屏幕尺寸
        mScreenWidth = resources.displayMetrics.widthPixels
        mScreenHeight = resources.displayMetrics.heightPixels

        val scanFrameSize = (SCAN_FRAME_SIZE * density).toInt()

        //3.计算取景器的矩形，它位于布局的中间。
        //设置扫描区域。 （可选。矩形可以为空。如果没有指定设置，它将位于布局的中间。）
        val rect = Rect()
        rect.left = mScreenWidth / 2 - scanFrameSize / 2
        rect.right = mScreenWidth / 2 + scanFrameSize / 2
        rect.top = mScreenHeight / 2 - scanFrameSize / 2
        rect.bottom = mScreenHeight / 2 + scanFrameSize / 2

        //初始化 RemoteView 实例，并设置扫描结果的回调。
        remoteView =
            RemoteView.Builder().setContext(this@YcScanQrcodeActivity).setBoundingBox(rect).setFormat(HmsScan.ALL_SCAN_TYPE).build()

        // 返回直接扫描结果
        remoteView?.setOnResultCallback { hmsScans ->
            if (hmsScans != null && hmsScans.isNotEmpty() && hmsScans[0] != null && !TextUtils.isEmpty(hmsScans[0].getOriginalValue())) {
                val intent = Intent()
                intent.putExtra(SCAN_RESULT, hmsScans[0].showResult)
                setResult(RESULT_OK, intent)
                finish()
            }
        }

        // 将自定义视图加载到活动中
        remoteView?.onCreate(Bundle())//TODO
        val params = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        rim.addView(remoteView, params)

        ivBack.setOnClickListener { finish() }
        ivCameraPicture.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_PHOTO)
            } else {
                resultLauncher.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    ).setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                )
            }
        }

        ivFlush.apply {
            setOnClickListener {
                if (remoteView?.lightStatus == true) {
                    remoteView?.switchLight()
                    setImageResource(img[1])
                } else {
                    remoteView?.switchLight()
                    setImageResource(img[0])
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showErrorDialog(dialog, "请在系统设置中为App开启摄像头权限后重试")  // 未获得Camera权限
            }
        } else if (grantResults.isNotEmpty() && requestCode == REQUEST_PERMISSION_PHOTO) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showErrorDialog(dialog, "请在系统设置中为App中开启文件权限后重试")
            } else {
                resultLauncher.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    ).setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        remoteView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        remoteView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        remoteView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        remoteView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        remoteView?.onDestroy()
    }
}