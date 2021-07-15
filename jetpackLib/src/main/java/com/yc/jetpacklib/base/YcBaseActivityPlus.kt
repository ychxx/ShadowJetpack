package com.yc.jetpacklib.base

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

/**
 * Creator: yc
 * Date: 2021/6/9 16:47
 * UseDes:
 */
abstract class YcBaseActivityPlus<VB : ViewBinding>(createVB: ((LayoutInflater) -> VB)? = null) : YcBaseActivity<VB>(createVB) {

    override fun initView() {
        mViewBinding.initView()
    }

    abstract fun VB.initView()
//    protected fun startResultYc(intent: Intent, success: ((result: Instrumentation.ActivityResult) -> Unit)? = null) {
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            success?.let { it1 ->
//                if (it.resultCode != Activity.RESULT_OK) { //安装apk失败
//                    it1(it)
//                }
//            }
//        }.launch(intent)
//    }
}
