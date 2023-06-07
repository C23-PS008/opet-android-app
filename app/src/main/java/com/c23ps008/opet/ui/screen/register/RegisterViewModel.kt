package com.c23ps008.opet.ui.screen.register

import androidx.lifecycle.ViewModel
import com.c23ps008.opet.data.formdata.RegisterFormData
import com.c23ps008.opet.data.remote.response.RegisterResponse
import com.c23ps008.opet.data.repository.AuthRepository
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.utils.createErrorResponse
import retrofit2.HttpException

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    suspend fun register(registerFormData: RegisterFormData): UiState<RegisterResponse> = try {
        val response = authRepository.register(registerFormData)
        UiState.Success(response)
    } catch (e: HttpException) {
        val errorResponse = createErrorResponse(e)
        UiState.Error(errorResponse.message.toString())
    } catch (e: Exception) {
        UiState.Error(e.message.toString())
    }
}