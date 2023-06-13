package com.c23ps008.opet.data.remote.retrofit

import com.c23ps008.opet.data.formdata.DogPredictFormData
import com.c23ps008.opet.data.remote.response.DogPredictResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiDogPredict {
    @POST("predict")
    suspend fun predict(@Body body: DogPredictFormData): DogPredictResponse
}