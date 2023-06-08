package com.c23ps008.opet.ui.screen.profile

import androidx.lifecycle.ViewModel
import com.c23ps008.opet.data.repository.LocalDataStoreRepository

class ProfileViewModel(private val localDataStoreRepository: LocalDataStoreRepository): ViewModel() {
    suspend fun logout() {
        localDataStoreRepository.deleteToken()
    }
}