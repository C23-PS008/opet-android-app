package com.c23ps008.opet.data.remote.retrofit

import com.c23ps008.opet.data.formdata.LoginFormData
import com.c23ps008.opet.data.formdata.RegisterFormData
import com.c23ps008.opet.data.remote.response.LoginResponse
import com.c23ps008.opet.data.remote.response.RegisterResponse
import retrofit2.http.*

interface ApiBackendService {
    @POST("login")
    suspend fun login(@Body body: LoginFormData): LoginResponse

    @POST("register")
    suspend fun register(@Body body: RegisterFormData): RegisterResponse
}