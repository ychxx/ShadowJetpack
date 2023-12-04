package com.yc.shadowjetpack

import android.os.Environment
import androidx.core.graphics.drawable.toBitmap
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.extension.showToast
import com.yc.jetpacklib.file.YcFileUtils
import com.yc.jetpacklib.image.YcImgUtils
import com.yc.jetpacklib.image.YcImgUtils.copyToMediaPicture
import com.yc.jetpacklib.image.YcImgUtils.postUpdate
import com.yc.jetpacklib.permission.XXPermissionUtil
import com.yc.jetpacklib.permission.YcXXPermissionHelper
import com.yc.jetpacklib.utils.YcResources
import com.yc.shadowjetpack.databinding.TestStorageActivityBinding
import java.io.File

/**
 * 测试存储
 */
class TestStorageActivity : YcBaseActivityPlus<TestStorageActivityBinding>(TestStorageActivityBinding::inflate) {
    private val mYcPermissionHelper by lazy { YcXXPermissionHelper(this) }


    override fun TestStorageActivityBinding.initView() {

        val permission: MutableList<String> = mutableListOf()
        permission.addAll(XXPermissionUtil.STORAGE)
        mYcPermissionHelper.addPermission(*permission.toTypedArray())
        mYcPermissionHelper.mSuccessCall = {
            showToast("权限申请成功")
        }
        requestPermission.setOnClickListener {
            mYcPermissionHelper.start()
        }
        create1.setOnClickListener {
            val filePath = YcFileUtils.getExternalPath() + File.separator + "test1.jpeg"
            YcFileUtils.createFile(filePath)
            YcImgUtils.bitmapToFile(YcResources.getDrawable(R.drawable.test).toBitmap(), filePath)
            postUpdate(this@TestStorageActivity, filePath)
        }
        create2.setOnClickListener {
            val filePath = YcFileUtils.getInternalPath() + File.separator + "test2.jpeg"
            YcFileUtils.createFile(filePath)
            YcImgUtils.bitmapToFile(YcResources.getDrawable(R.drawable.test).toBitmap(), filePath)
            postUpdate(this@TestStorageActivity, filePath)
        }
        create3.setOnClickListener {
            val filePath = YcFileUtils.getExternalPath(Environment.DIRECTORY_PICTURES) + File.separator + "test3.jpeg"
            YcFileUtils.createFile(filePath)
            YcImgUtils.bitmapToFile(YcResources.getDrawable(R.drawable.test).toBitmap(), filePath)
            postUpdate(this@TestStorageActivity, filePath)
        }
    }
}