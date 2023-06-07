package com.c23ps008.opet.data.repository

import com.c23ps008.opet.data.local.LocalDataStore

class LocalDataStoreRepository(private val localDataStore: LocalDataStore) {
    suspend fun saveToken(token:String) = localDataStore.saveToken(token)

    fun getToken() = localDataStore.getToken()

    suspend fun deleteToken() = localDataStore.deleteToken()
}