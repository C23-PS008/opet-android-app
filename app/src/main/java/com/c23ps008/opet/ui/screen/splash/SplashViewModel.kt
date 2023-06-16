package com.c23ps008.opet.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c23ps008.opet.data.repository.LocalDataStoreRepository
import com.c23ps008.opet.data.repository.ProfileRepository
import com.c23ps008.opet.ui.common.AuthState
import com.c23ps008.opet.utils.createErrorResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SplashViewModel(
    private val localDataStoreRepository: LocalDataStoreRepository,
    private val profileRepository: ProfileRepository,
) :
    ViewModel() {
    private val _isAuthorized: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    val isAuthorized: StateFlow<AuthState> get() = _isAuthorized

    fun authMe() {
        viewModelScope.launch {
            try {
                val token = localDataStoreRepository.getToken().firstOrNull()
                if (token == null) _isAuthorized.value = AuthState.Unauthorized
                else {
                    profileRepository.getProfile(token.toString())
                    _isAuthorized.value = AuthState.Authorized
                }

            } catch (e: HttpException) {
                if (e.code() == 401) {
                    localDataStoreRepository.deleteToken()
                    _isAuthorized.value = AuthState.Unauthorized
                } else {
                    val errorResponse = createErrorResponse(e)
                    _isAuthorized.value = AuthState.Error(errorResponse.message.toString())
                }
            } catch (e: Exception) {
                _isAuthorized.value = AuthState.Error(e.message.toString())
            }
        }
    }
}