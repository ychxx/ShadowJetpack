/*
 * Copyright 2020 Shreyas Patil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yc.jetpacklib.widget.dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.yc.jetpacklib.R

/**
 * 加载框
 */
class YcLoadingDialog constructor(context: Context, lifecycleOwner: LifecycleOwner, theme: Int = R.style.YcLoadingDialogStyle) :
    Dialog(context, theme) {
    init {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                Log.e("CommonDialog", "onDestroy: ")
                if (this@YcLoadingDialog.isShowing) {
                    this@YcLoadingDialog.cancel()
                }
            }
        })
        setContentView(R.layout.yc_widget_dialog_loading)
        //设置对话框位置大小
        window?.apply {
            setGravity(Gravity.CENTER)
            setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            val lp = attributes
            attributes = lp //此处暂未设置偏移量
        }
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
}