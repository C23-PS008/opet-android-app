package com.c23ps008.opet.data.repository

import com.c23ps008.opet.data.remote.retrofit.ApiBackendService

class MyPetsRepository(private val apiService: ApiBackendService) {
    suspend fun getMyPets(token: String) = apiService.getMyPets(token)

    suspend fun deletePet(token: String, petId: String) = apiService.deleteMyPet(token, petId)
}