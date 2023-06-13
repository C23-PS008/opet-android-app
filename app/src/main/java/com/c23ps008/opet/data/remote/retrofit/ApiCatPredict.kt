package com.c23ps008.opet.data.remote.retrofit

import com.c23ps008.opet.data.formdata.CatPredictFormData
import com.c23ps008.opet.data.remote.response.CatPredictResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiCatPredict {
    @POST("predict")
    suspend fun predict(@Body body: CatPredictFormData): CatPredictResponse
}