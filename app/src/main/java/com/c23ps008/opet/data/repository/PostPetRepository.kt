package com.c23ps008.opet.data.repository

import com.c23ps008.opet.data.formdata.PostPetFormData
import com.c23ps008.opet.data.formdata.UploadPetResponse
import com.c23ps008.opet.data.remote.retrofit.ApiBackendService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class PostPetRepository(private val apiService: ApiBackendService) {
    suspend fun uploadPet(token: String, postPetFormData: PostPetFormData): UploadPetResponse {
        val name = postPetFormData.name.toRequestBody("text/plain".toMediaType())
        val petCategory =
            postPetFormData.petCategory.toString().toRequestBody("text/plain".toMediaType())
        val breed = postPetFormData.breed.toRequestBody("text/plain".toMediaType())
        val characters = postPetFormData.characters.toRequestBody("text/plain".toMediaType())
        val age = postPetFormData.age.toRequestBody("text/plain".toMediaType())
        val size = postPetFormData.size.toRequestBody("text/plain".toMediaType())
        val gender = postPetFormData.gender.toRequestBody("text/plain".toMediaType())
        val about = postPetFormData.about.toRequestBody("text/plain".toMediaType())
        val lat = postPetFormData.lat.toString().toRequestBody("text/plain".toMediaType())
        val lon = postPetFormData.lon.toString().toRequestBody("text/plain".toMediaType())
        val imageFile = postPetFormData.image?.asRequestBody("image/jpeg".toMediaType())
        val image: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", postPetFormData.image?.name, imageFile as RequestBody)
        return apiService.uploadPet(
            token,
            name,
            petCategory,
            breed,
            characters,
            age,
            size,
            gender,
            about,
            lon,
            lat,
            image
        )
    }
}