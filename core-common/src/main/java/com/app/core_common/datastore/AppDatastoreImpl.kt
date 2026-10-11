package com.app.core_common.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDatastoreImpl @Inject constructor(@ApplicationContext val context : Context) :
    DatastoreManager {

    private val Context.datastore by preferencesDataStore(name = DatastoreConstants.UAE_ASSIST_PREFERENCE)


    override suspend fun <T> write(key: Preferences.Key<T>, value: T): T? {
        val pref = context.datastore.edit {
            it[key] = value
        }
        return pref[key]
    }

    override suspend fun <T> read(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return context.datastore.data
            .catch { exception ->
                if(exception is IOException){
                    emit(emptyPreferences())
                }else{
                    throw exception
                }
            }.map {
                it[key] ?: defaultValue
            }
    }

    override suspend fun clearAllDataStore(): Boolean {
        return  context.datastore.edit {
            it.clear()
        }.asMap().isEmpty()
    }

    override suspend fun <T> clearDataOfKey(key: Preferences.Key<T>): Boolean? {
        return context.datastore.edit {
            it.remove(key)
        }.asMap().containsKey(key)
    }
}