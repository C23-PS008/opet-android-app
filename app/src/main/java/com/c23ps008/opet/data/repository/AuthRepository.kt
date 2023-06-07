package com.c23ps008.opet.data.repository

import com.c23ps008.opet.data.formdata.LoginFormData
import com.c23ps008.opet.data.formdata.RegisterFormData
import com.c23ps008.opet.data.remote.retrofit.ApiBackendService

class AuthRepository(private val apiService: ApiBackendService) {
    suspend fun login(loginFormData: LoginFormData) = apiService.login(loginFormData)

    suspend fun register(registerFormData: RegisterFormData) = apiService.register(registerFormData)
}