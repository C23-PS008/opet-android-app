package com.c23ps008.opet.ui.screen.login

import androidx.lifecycle.ViewModel
import com.c23ps008.opet.data.formdata.LoginFormData
import com.c23ps008.opet.data.remote.response.LoginResponse
import com.c23ps008.opet.data.repository.AuthRepository
import com.c23ps008.opet.data.repository.LocalDataStoreRepository
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.utils.createErrorResponse
import retrofit2.HttpException

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val localDataStoreRepository: LocalDataStoreRepository,
): ViewModel() {
    suspend fun login(loginFormData: LoginFormData): UiState<LoginResponse> = try {
        val response = authRepository.login(loginFormData)
        localDataStoreRepository.saveToken(response.data?.token.toString())
        UiState.Success(response)
    } catch (e: HttpException) {
        val errorResponse = createErrorResponse(e)
        UiState.Error(errorResponse.message.toString())
    } catch (e: Exception) {
        UiState.Error(e.message.toString())
    }
}