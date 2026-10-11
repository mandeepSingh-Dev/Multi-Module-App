package com.app.core_common.datastore

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow


interface DatastoreManager{

    /** Add/Set key value in datastore and return true if add/set successfully */
    suspend fun <T> write(key : Preferences.Key<T>,value : T) : T?


    suspend fun <T> read(key: Preferences.Key<T>, defaultValue : T) : Flow<T>

    /**Clear datastore and return true if clear successfully */
    suspend fun clearAllDataStore() : Boolean?

    /**Remove key from datastore and return true if removed successfully */
    suspend fun <T> clearDataOfKey(key : Preferences.Key<T>) : Boolean?
}