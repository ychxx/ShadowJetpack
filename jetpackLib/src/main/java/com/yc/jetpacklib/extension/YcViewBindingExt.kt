//package com.yc.jetpacklib.extension
//
//import android.app.Activity
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.LifecycleOwner
//import androidx.viewbinding.ViewBinding
//import com.yc.jetpacklib.R
//import com.yc.jetpacklib.test.A
//import kotlin.properties.ReadOnlyProperty
//import kotlin.reflect.KProperty
//
///**
// * Creator: yc
// * Date: 2021/6/10 11:19
// * UseDes:思路来源pengxr作者
// */
//inline fun <A : AppCompatActivity, V : ViewBinding> ycViewBindingAc(
//    crossinline viewBinding: (LayoutInflater) -> V,
//    crossinline layoutInflaterProvider: (A) -> LayoutInflater = {
//        LayoutInflater.from(it)
//    }
//) = YcViewBindingProperty<A, V> {
//    viewBinding(layoutInflaterProvider(it))
//}
////inline fun <A : AppCompatActivity, V : ViewBinding> ycViewBindingAc(
////    crossinline viewBinding: (LayoutInflater) -> V,
////    crossinline layoutInflaterProvider: (A) -> LayoutInflater = {
////        LayoutInflater.from(it)
////    }
////) = YcViewBindingProperty<A, V> {
////    viewBinding(layoutInflaterProvider(it))
////}
//class YcViewBindingProperty<A : AppCompatActivity, V : ViewBinding>(private val viewBinder: (A) -> V) {
//    private var mViewBinding: V? = null
//    operator fun getValue(thisRef: A, property: KProperty<*>): V {
//        mViewBinding?.let { return it }
//        val mViewBinding = viewBinder(thisRef)
//        thisRef.lifecycle
//        return mViewBinding
//    }
//}