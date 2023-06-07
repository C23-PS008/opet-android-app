package com.c23ps008.opet

import android.app.Application
import com.c23ps008.opet.data.AppContainer
import com.c23ps008.opet.data.AppDataContainer
import com.c23ps008.opet.data.remote.retrofit.ApiConfig

class OPetApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        val apiConfig = ApiConfig
        container = AppDataContainer(this, apiConfig)
    }
}