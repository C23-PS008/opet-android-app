package com.c23ps008.opet.data.repository

import com.c23ps008.opet.data.formdata.DogPredictFormData
import com.c23ps008.opet.data.remote.retrofit.ApiDogPredict

class DogPredictRepository(private val apiDogPredict: ApiDogPredict) {
    suspend fun predict(dogPredictFormData: DogPredictFormData) =
        apiDogPredict.predict(dogPredictFormData)
}