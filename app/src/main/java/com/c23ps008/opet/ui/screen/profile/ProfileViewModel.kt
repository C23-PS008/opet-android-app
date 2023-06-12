package com.c23ps008.opet.ui.screen.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c23ps008.opet.data.formdata.ProfileFormData
import com.c23ps008.opet.data.remote.response.UpdateProfileResponse
import com.c23ps008.opet.data.remote.response.UserMeResponse
import com.c23ps008.opet.data.repository.LocalDataStoreRepository
import com.c23ps008.opet.data.repository.ProfileRepository
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.utils.createErrorResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileViewModel(
    private val localDataStoreRepository: LocalDataStoreRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _profileState: MutableStateFlow<UiState<UserMeResponse>> =
        MutableStateFlow(UiState.Loading)
    val profileState: StateFlow<UiState<UserMeResponse>> get() = _profileState

    private val _isAuthorized: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isAuthorized: StateFlow<Boolean> get() = _isAuthorized

    suspend fun logout() {
        localDataStoreRepository.deleteToken()
    }

    fun getProfile() {
        viewModelScope.launch {
            try {
                _profileState.value = UiState.Loading
                localDataStoreRepository.getToken().collect { token ->
                    if (token != null) {
                        val response = profileRepository.getProfile(token)
                        _profileState.value = UiState.Success(response)
                    } else {
                        _isAuthorized.value = false
                    }
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    _isAuthorized.value = false
                }
                val errorResponse = createErrorResponse(e)
                _profileState.value = UiState.Error(errorResponse.message.toString())
            } catch (e: Exception) {
                _profileState.value = UiState.Error(e.message.toString())
            }
        }
    }

    suspend fun updateProfile(profileFormData: ProfileFormData): UiState<UpdateProfileResponse> =
        try {
            val token = localDataStoreRepository.getToken().firstOrNull()
            val response = profileRepository.updateProfile(token.toString(), profileFormData)
            UiState.Success(response)
        } catch (e: HttpException) {
            val errorResponse = createErrorResponse(e)
            UiState.Error(errorResponse.toString())
        } catch (e: Exception) {
            Log.d("TESTS", e.message.toString())
            UiState.Error(e.message.toString())
        }
}