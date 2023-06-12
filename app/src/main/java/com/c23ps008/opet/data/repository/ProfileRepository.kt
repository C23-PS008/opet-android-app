package com.c23ps008.opet.data.repository

import com.c23ps008.opet.data.formdata.ProfileFormData
import com.c23ps008.opet.data.remote.retrofit.ApiBackendService

class ProfileRepository(private val apiService: ApiBackendService) {
    suspend fun getProfile(token: String) = apiService.getProfile(authorization = token)

    suspend fun updateProfile(token: String, profileFormData: ProfileFormData) = apiService.updateProfile(authorization = token, body = profileFormData)
}