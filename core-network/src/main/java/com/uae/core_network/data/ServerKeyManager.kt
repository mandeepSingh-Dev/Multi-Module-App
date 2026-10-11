package com.uae.core_network.data

import com.app.core_common.datastore.DatastoreManager
import com.app.core_common.datastore.DatastoreConstants
import com.uae.core_network.data.service.ServerKeyService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class ServerKeyManager (
    private val apiService: ServerKeyService,
    private val dataStoreManager: DatastoreManager
) {

    suspend fun getServerPublicKey(): String {
         return dataStoreManager
             .read(DatastoreConstants.SERVER_PUBLIC_KEY, "").firstOrNull()?.takeIf { it.isNotEmpty() }
             ?: refreshServerPublicKey()
    }

    suspend fun refreshServerPublicKey(): String {
        val response = apiService.getServerPublicKey()
        val newKey = response?.body()?.data
        dataStoreManager.write(DatastoreConstants.SERVER_PUBLIC_KEY, newKey ?: "")

        return newKey ?: ""
    }
}