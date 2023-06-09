package com.c23ps008.opet.data.remote.retrofit

import com.c23ps008.opet.data.formdata.LoginFormData
import com.c23ps008.opet.data.formdata.ProfileFormData
import com.c23ps008.opet.data.formdata.RegisterFormData
import com.c23ps008.opet.data.formdata.UploadPetResponse
import com.c23ps008.opet.data.remote.response.DeleteMyPetsResponse
import com.c23ps008.opet.data.remote.response.GetAllPetAdoptionResponse
import com.c23ps008.opet.data.remote.response.LoginResponse
import com.c23ps008.opet.data.remote.response.MyPetsResponse
import com.c23ps008.opet.data.remote.response.PetDetailResponse
import com.c23ps008.opet.data.remote.response.RegisterResponse
import com.c23ps008.opet.data.remote.response.UpdateMyPetResponse
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

    @GET("mypets")
    suspend fun getMyPets(@Header("Authorization") authorization: String): MyPetsResponse

    @DELETE("mypets/{petId}")
    suspend fun deleteMyPet(
        @Header("Authorization") authorization: String,
        @Path("petId") petId: String,
    ): DeleteMyPetsResponse

    @Multipart
    @PUT("mypets/{petId}")
    suspend fun updateMyPet(
        @Header("Authorization") authorization: String,
        @Path("petId") petId: String,
        @Part("name") name: RequestBody,
        @Part("breed") breed: RequestBody,
        @Part("characters") characters: RequestBody,
        @Part("age") age: RequestBody,
        @Part("size") size: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("about") about: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part("lat") lat: RequestBody,
    ): UpdateMyPetResponse

    @GET("pets/{petId}")
    suspend fun getPet(
        @Header("Authorization") authorization: String,
        @Path("petId") petId: String,
    ): PetDetailResponse

    @GET("pets")
    suspend fun getAllPetAdoption(
        @Header("Authorization") authorization: String,
        @Query("type") type: String = "all",
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("breed") breed: String?,
    ): GetAllPetAdoptionResponse
}