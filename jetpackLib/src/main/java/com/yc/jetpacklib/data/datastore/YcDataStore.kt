package com.yc.jetpacklib.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.yc.jetpacklib.init.YcJetpack
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 *  PreferencesDataStore    先用
 *  ProtoDataStore          以后在换
 */
object YcDataStore {

    @JvmStatic
    val Context.ycDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    @JvmStatic
    suspend inline fun <reified T : Any> save(key: Preferences.Key<T>, value: T) {
        YcJetpack.mInstance.mApplication.ycDataStore.edit {
            it[key] = value
        }
    }

    @JvmStatic
    suspend inline fun saveMore(crossinline transform: suspend (MutablePreferences) -> Unit) {
        YcJetpack.mInstance.mApplication.ycDataStore.edit {
            transform(it)
        }
    }

    @JvmStatic
    suspend inline fun <reified T : Any> get(key: Preferences.Key<T>, crossinline block: (T?) -> Unit) {
        YcJetpack.mInstance.mApplication.ycDataStore.data.map {
            block(it[key])
        }.first()
    }

    @JvmStatic
    suspend inline fun getMore(crossinline block: (Preferences) -> Unit) {
        YcJetpack.mInstance.mApplication.ycDataStore.data.map {
            block(it)
            emptyPreferences()
        }.first()
    }
}

