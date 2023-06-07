package com.c23ps008.opet.data

import android.content.Context
import com.c23ps008.opet.data.local.LocalDataStore
import com.c23ps008.opet.data.remote.retrofit.ApiConfig
import com.c23ps008.opet.data.repository.AuthRepository
import com.c23ps008.opet.data.repository.LocalDataStoreRepository

interface AppContainer {
    val authRepository: AuthRepository
    val localDataStoreRepository: LocalDataStoreRepository
}

class AppDataContainer(private val context: Context, private val apiConfig: ApiConfig) :
    AppContainer {
    override val authRepository: AuthRepository by lazy {
        AuthRepository(apiConfig.getApiBackendService())
    }

    override val localDataStoreRepository: LocalDataStoreRepository by lazy {
        LocalDataStoreRepository(LocalDataStore(context))
    }
}