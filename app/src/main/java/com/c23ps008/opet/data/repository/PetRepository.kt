package com.c23ps008.opet.data.repository

import com.c23ps008.opet.data.formdata.UpdatePetFormData
import com.c23ps008.opet.data.remote.response.UpdateMyPetResponse
import com.c23ps008.opet.data.remote.retrofit.ApiBackendService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class PetRepository(private val apiService: ApiBackendService) {
    suspend fun getDetail(token: String, petId: String) = apiService.getPet(token, petId)
    suspend fun updatePet(
        token: String,
        petId: String,
        updatePetFormData: UpdatePetFormData,
    ): UpdateMyPetResponse {
        val name = updatePetFormData.name.toString().toRequestBody("text/plain".toMediaType())
        val breed = updatePetFormData.breed.toString().toRequestBody("text/plain".toMediaType())
        val characters =
            updatePetFormData.characters.toString().toRequestBody("text/plain".toMediaType())
        val age = updatePetFormData.age.toString().toRequestBody("text/plain".toMediaType())
        val size = updatePetFormData.size.toString().toRequestBody("text/plain".toMediaType())
        val gender = updatePetFormData.gender.toString().toRequestBody("text/plain".toMediaType())
        val about = updatePetFormData.about.toString().toRequestBody("text/plain".toMediaType())
        val lat = updatePetFormData.lat.toString().toRequestBody("text/plain".toMediaType())
        val lon = updatePetFormData.lon.toString().toRequestBody("text/plain".toMediaType())
        return apiService.updateMyPet(
            token,
            petId,
            name,
            breed,
            characters,
            age,
            size,
            gender,
            about,
            lon,
            lat,
        )
    }
}