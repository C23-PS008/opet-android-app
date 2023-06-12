package com.c23ps008.opet.data.remote.retrofit

import com.c23ps008.opet.data.formdata.LoginFormData
import com.c23ps008.opet.data.formdata.ProfileFormData
import com.c23ps008.opet.data.formdata.RegisterFormData
import com.c23ps008.opet.data.formdata.UploadPetResponse
import com.c23ps008.opet.data.remote.response.LoginResponse
import com.c23ps008.opet.data.remote.response.RegisterResponse
import com.c23ps008.opet.data.remote.response.UpdateProfileResponse
import com.c23ps008.opet.data.remote.response.UserMeResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiBackendService {
    @POST("login")
    suspend fun login(@Body body: LoginFormData): LoginResponse

    @POST("register")
    suspend fun register(@Body body: RegisterFormData): RegisterResponse

    @GET("users/me")
    suspend fun getProfile(@Header("Authorization") authorization: String): UserMeResponse

    @PUT("users/edit")
    suspend fun updateProfile(
        @Header("Authorization") authorization: String,
        @Body body: ProfileFormData,
    ): UpdateProfileResponse

    @Multipart
    @POST("mypets")
    suspend fun uploadPet(
        @Header("Authorization") authorization: String,
        @Part("name") name: RequestBody,
        @Part("petCategory") petCategory: RequestBody,
        @Part("breed") breed: RequestBody,
        @Part("characters") characters: RequestBody,
        @Part("age") age: RequestBody,
        @Part("size") size: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("about") about: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part image: MultipartBody.Part,
    ): UploadPetResponse
}