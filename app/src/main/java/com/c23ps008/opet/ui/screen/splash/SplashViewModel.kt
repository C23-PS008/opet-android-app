package com.c23ps008.opet.ui.screen.splash

import androidx.lifecycle.ViewModel
import com.c23ps008.opet.data.repository.LocalDataStoreRepository

class SplashViewModel(private val localDataStoreRepository: LocalDataStoreRepository) :
    ViewModel() {

    fun checkToken() = localDataStoreRepository.getToken()
}