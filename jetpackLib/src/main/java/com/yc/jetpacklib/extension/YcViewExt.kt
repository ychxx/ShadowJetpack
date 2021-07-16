package com.yc.jetpacklib.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.yc.jetpacklib.image.GlideApp
import java.io.File

/**
 * Creator: yc
 * Date: 2021/2/2 21:03
 * UseDes:
 */
/**
 * 创建跳转回调的launcher（必须在onCreate生命周期里创建）
 */
inline fun AppCompatActivity.ycCreateResultLauncher(crossinline success: ((result: ActivityResult) -> Unit)): ActivityResultLauncher<Intent> =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            success(it)
        }
    }

/**
 * 创建跳转回调的launcher（必须在onCreate生命周期里创建）
 */
inline fun AppCompatActivity.ycCreateResultLauncher(
    crossinline success: ((result: ActivityResult) -> Unit),
    crossinline fail: ((result: ActivityResult) -> Unit)
): ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    if (it.resultCode == Activity.RESULT_OK) {
        success(it)
    } else {
        fail(it)
    }
}

/**
 * 创建跳转回调的launcher（必须在onCreate生命周期里创建）
 */
inline fun Fragment.ycCreateResultLauncher(crossinline success: ((result: ActivityResult) -> Unit)): ActivityResultLauncher<Intent> =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            success(it)
        }
    }

/**
 * 创建跳转回调的launcher（必须在onCreate生命周期里创建）
 */
inline fun Fragment.ycCreateResultLauncher(
    crossinline success: ((result: ActivityResult) -> Unit),
    crossinline fail: ((result: ActivityResult) -> Unit)
): ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    if (it.resultCode == Activity.RESULT_OK) {
        success(it)
    } else {
        fail(it)
    }
}

fun Activity.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    if (this.isFinishing) {
        return
    } else {
        (this as Context).showToast(msg, duration)
    }
}

fun FragmentActivity.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    if (this.isFinishing) {
        return
    } else {
        (this as Context).showToast(msg, duration)
    }
}

fun Fragment.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    if (this.isDetached) {
        return
    }
    requireContext().showToast(msg, duration)
}

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun RecyclerView.ycInitLinearLayoutManage(orientation: Int = RecyclerView.VERTICAL) {
    this.layoutManager = LinearLayoutManager(this.context, orientation, false)
}

fun TextView.ycSetTextColorRes(@ColorRes textColorRes: Int) {
    this.setTextColor(this.context.resources.getColor(textColorRes))
}

fun Button.ycSetTextColorRes(@ColorRes textColorRes: Int) {
    this.setTextColor(this.context.resources.getColor(textColorRes))
}

/**
 * 加载网络图片
 */
fun ImageView.ycLoadImageNet(imageNet: String?) {
    GlideApp.with(this.context)
        .asBitmap()
        .load(imageNet)
        .into(this)
}

fun ImageView.ycLoadImageNetCircle(imageNet: String?) {
    GlideApp.with(this.context)
        .asBitmap()
        .load(imageNet)
        .circleCrop()
        .into(this)
}

/**
 * 加载网络图片（用时间来区分地址相同，图片内容不相同情况）
 */
fun ImageView.ycLoadImageNet(imgNetUrl: String?, imageUpdateTime: String) {
    GlideApp.with(this.context)
        .applyDefaultRequestOptions(RequestOptions().signature(ObjectKey(imageUpdateTime)))
        .asBitmap()
        .load(imgNetUrl)
        .into(this)
}

/**
 * 加载网络图片
 */
fun ImageView.ycLoadImageNetCircle(imgNetUrl: String?, imageUpdateTime: String) {
    GlideApp.with(this.context)
        .applyDefaultRequestOptions(RequestOptions().signature(ObjectKey(imageUpdateTime)))
        .asBitmap()
        .load(imgNetUrl)
        .circleCrop()
        .into(this)
}

fun ImageView.ycLoadImageFile(imageFile: File) {
    GlideApp.with(this.context)
        .asBitmap()
        .load(imageFile)
        .into(this)
}

fun ImageView.ycLoadImageFileCircle(imageFile: File) {
    GlideApp.with(this.context)
        .asBitmap()
        .load(imageFile)
        .circleCrop()
        .into(this)
}

fun ImageView.ycLoadImageRes(@DrawableRes imgRes: Int) {
    GlideApp.with(this.context)
        .asBitmap()
        .load(imgRes)
        .into(this)
}

fun ImageView.ycLoadImageResCircle(@DrawableRes imgRes: Int) {
    GlideApp.with(this.context)
        .asBitmap()
        .load(imgRes)
        .circleCrop()
        .into(this)
}