package com.yc.jetpacklib.extension

import androidx.datastore.preferences.core.Preferences
import com.yc.jetpacklib.data.datastore.YcDataStore.ycDataStore
import com.yc.jetpacklib.init.YcJetpack
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Creator: yc
 * Date: 2022/1/10 11:12
 * UseDes:
 */
suspend inline fun <T> Preferences.Key<T>.get(defaultValue: T): T {
    return YcJetpack.mInstance.mApplication.ycDataStore.data.map {
        it[this] ?: defaultValue
    }.first()
}

suspend inline fun Preferences.Key<Int>.get(defaultValue: Int = -1): Int {
    return YcJetpack.mInstance.mApplication.ycDataStore.data.map {
        it[this] ?: defaultValue
    }.first()
}

suspend inline fun Preferences.Key<Long>.get(defaultValue: Long = -1L): Long {
    return YcJetpack.mInstance.mApplication.ycDataStore.data.map {
        it[this] ?: defaultValue
    }.first()
}

suspend inline fun Preferences.Key<Double>.get(defaultValue: Double = -1.0): Double {
    return YcJetpack.mInstance.mApplication.ycDataStore.data.map {
        it[this] ?: defaultValue
    }.first()
}

suspend inline fun Preferences.Key<Float>.get(defaultValue: Float = -1f): Float {
    return YcJetpack.mInstance.mApplication.ycDataStore.data.map {
        it[this] ?: defaultValue
    }.first()
}

suspend inline fun Preferences.Key<String>.get(defaultValue: String = ""): String {
    return YcJetpack.mInstance.mApplication.ycDataStore.data.map {
        it[this] ?: defaultValue
    }.first()
}

suspend inline fun Preferences.Key<Boolean>.get(defaultValue: Boolean = false): Boolean {
    return YcJetpack.mInstance.mApplication.ycDataStore.data.map {
        it[this] ?: defaultValue
    }.first()
}