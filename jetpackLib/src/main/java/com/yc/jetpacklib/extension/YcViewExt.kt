package com.yc.jetpacklib.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.yc.jetpacklib.R
import com.yc.jetpacklib.image.GlideApp
import com.yc.jetpacklib.utils.YcColorUtil
import com.yc.jetpacklib.utils.YcSoftInputUtil
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

/**
 * 点击非EditText位置，隐藏输入法
 */
fun Activity.touchOutsideHideSoftInput(event: MotionEvent) {
    YcSoftInputUtil.clickNoEditHideSoftInput(currentFocus, event)
}

/**
 * 隐藏输入法
 */
fun View.hideSoftInput() {
    YcSoftInputUtil.hideSoftInput(context, windowToken)
}

/**
 * 隐藏输入法
 */
fun Activity.hideSoftInput() {
    window.peekDecorView()?.hideSoftInput()
}

/**
 * 隐藏输入法
 */
fun Fragment.hideSoftInput() {
    view?.hideSoftInput()
}

@ColorInt
fun Fragment.ycGetColorRes(resId: Int): Int {
    return resources.getColor(resId)
}

@ColorInt
fun Activity.ycGetColorRes(resId: Int): Int {
    return resources.getColor(resId)
}

@ColorInt
fun Context.ycGetColorRes(resId: Int): Int {
    return resources.getColor(resId)
}

fun View.ycGetFont(resId: Int): Typeface? {
    return ResourcesCompat.getFont(context, resId)
}

fun Context.ycGetFont(resId: Int): Typeface? {
    return ResourcesCompat.getFont(this, resId)
}

@ColorInt
fun View.ycGetColorRes(resId: Int): Int {
    return resources.getColor(resId)
}

@SuppressLint("UseCompatLoadingForDrawables")
fun View.ycGetDrawable(resId: Int): Drawable {
    return resources.getDrawable(resId)
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Fragment.ycGetDrawable(resId: Int): Drawable {
    return resources.getDrawable(resId)
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Activity.ycGetDrawable(resId: Int): Drawable {
    return resources.getDrawable(resId)
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Context.ycGetDrawable(resId: Int): Drawable {
    return resources.getDrawable(resId)
}

fun View.ycGetString(resId: Int): String {
    return resources.getString(resId)
}

fun Fragment.ycGetString(resId: Int): String {
    return resources.getString(resId)
}

fun Activity.ycGetString(resId: Int): String {
    return resources.getString(resId)
}

fun Context.ycGetString(resId: Int): String {
    return resources.getString(resId)
}

fun View.ycGetStringArray(resId: Int): Array<String> {
    return resources.getStringArray(resId)
}

fun View.ycGetStringList(resId: Int): List<String> {
    return mutableListOf(*resources.getStringArray(resId))
}

fun Context.ycDpToPx(dp: Float): Int {
    return (resources.displayMetrics.density * dp + 0.5f).toInt()
}

fun View.ycDpToPx(dp: Float): Int {
    return (resources.displayMetrics.density * dp + 0.5f).toInt()
}

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

/**
 * 等同于getDimension()得到值进行四舍五入
 */
fun View.ycGetDimensionPixelSize(resId: Int): Int {
    return resources.getDimensionPixelSize(resId)
}

fun Context.ycGetDimensionPixelSize(resId: Int): Int {
    return resources.getDimensionPixelSize(resId)
}

fun View.ycGetDimension(resId: Int): Float {
    return resources.getDimension(resId)
}

fun Context.ycGetDimension(resId: Int): Float {
    return resources.getDimension(resId)
}

fun TextView.ycSetTextSizeRes(@DimenRes idRes: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(idRes))
}

fun TextView.ycSetTextSize(textSize: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
}

fun TextView.ycSetTextColorRes(@ColorRes textColorRes: Int) {
    this.setTextColor(context.resources.getColor(textColorRes))
}

fun Button.ycSetTextColorRes(@ColorRes textColorRes: Int) {
    this.setTextColor(context.resources.getColor(textColorRes))
}

fun View.ycBgRgb(red: Int, green: Int, blue: Int) {
    setBackgroundColor(YcColorUtil.rgb(red, green, blue))
}

fun TextView.ycTextColorRgb(red: Int, green: Int, blue: Int) {
    setTextColor(YcColorUtil.rgb(red, green, blue))
}

fun View.ycBgArgb(alpha: Int, red: Int, green: Int, blue: Int) {
    setBackgroundColor(YcColorUtil.argb(alpha, red, green, blue))
}

fun ImageView.ycSetColorFilter(@ColorRes colorRes: Int) {
    this.setColorFilter(context.resources.getColor(colorRes))
}

fun RecyclerView.ycInitLinearLayoutManage(orientation: Int = RecyclerView.VERTICAL) {
    this.layoutManager = LinearLayoutManager(context, orientation, false)
}

/**
 * 加载网络图片
 */
fun ImageView.ycLoadImageNet(imageNet: String?) {
    GlideApp.with(context)
        .asBitmap()
        .load(imageNet)
        .into(this)
}

fun ImageView.ycLoadImageNetCircle(imageNet: String?) {
    GlideApp.with(context)
        .asBitmap()
        .load(imageNet)
        .circleCrop()
        .into(this)
}

/**
 * 加载圆角图片(自定义弧度)
 */
fun ImageView.ycLoadImageNetCircle(imageNet: String?, roundingRadius: Int, placeholderImg: Int = R.drawable.yc_ic_scan_code_pic) {
    GlideApp.with(context)
        .asBitmap()
        .load(imageNet)
        .transform(CenterCrop(), RoundedCorners(roundingRadius))
        .placeholder(placeholderImg)
        .into(this)
}

/**
 * 加载网络图片（用时间来区分地址相同，图片内容不相同情况）
 */
fun ImageView.ycLoadImageNet(imgNetUrl: String?, imageUpdateTime: String) {
    GlideApp.with(context)
        .applyDefaultRequestOptions(RequestOptions().signature(ObjectKey(imageUpdateTime)))
        .asBitmap()
        .load(imgNetUrl)
        .into(this)
}

/**
 * 加载网络图片（圆）
 */
fun ImageView.ycLoadImageNetCircle(imgNetUrl: String?, imageUpdateTime: String) {
    GlideApp.with(context)
        .applyDefaultRequestOptions(RequestOptions().signature(ObjectKey(imageUpdateTime)))
        .asBitmap()
        .load(imgNetUrl)
        .circleCrop()
        .into(this)
}

fun ImageView.ycLoadImagePath(imageFilePath: String) {
    this.ycLoadImageFile(File(imageFilePath))
}

fun ImageView.ycLoadImagePathCircle(imageFilePath: String) {
    this.ycLoadImageFileCircle(File(imageFilePath))
}

fun ImageView.ycLoadImagePathCircle(imageFilePath: String, roundingRadius: Int) {
    this.ycLoadImageFileCircle(File(imageFilePath), roundingRadius)
}

/**
 * 加载本地图片
 */
fun ImageView.ycLoadImageFile(imageFile: File) {
    GlideApp.with(context)
        .load(imageFile)
        .into(this)
}

fun ImageView.ycLoadImageFileCircle(imageFile: File) {
    GlideApp.with(context)
        .load(imageFile)
        .circleCrop()
        .into(this)
}

fun ImageView.ycLoadImageFileCircle(imageFile: File, roundingRadius: Int) {
    GlideApp.with(context)
        .load(imageFile)
        .transform(CenterCrop(), RoundedCorners(roundingRadius))//px
        .into(this)
}

/**
 * 加载资源图片
 */
fun ImageView.ycLoadImageRes(@DrawableRes imgRes: Int) {
    GlideApp.with(context)
        .load(imgRes)
        .into(this)
}


/**
 * 加载资源图片圆角
 */
fun ImageView.ycLoadImageResCircle(@DrawableRes imgRes: Int) {
    GlideApp.with(context)
        .load(imgRes)
        .circleCrop()
        .into(this)
}

/**
 * 加载资源图片圆角图片(自定义弧度)
 * roundingRadius：px
 */
fun ImageView.ycLoadImageResCircle(@DrawableRes imgRes: Int, roundingRadius: Int) {
    GlideApp.with(context)
        .load(imgRes)
        .transform(CenterCrop(), RoundedCorners(roundingRadius))
        .into(this)
}


