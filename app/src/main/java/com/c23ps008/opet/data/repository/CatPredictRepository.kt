package com.c23ps008.opet.data.repository

import com.c23ps008.opet.data.formdata.CatPredictFormData
import com.c23ps008.opet.data.remote.retrofit.ApiCatPredict

class CatPredictRepository(private val apiCatPredict: ApiCatPredict) {
    suspend fun predict(catPredictFormData: CatPredictFormData) =
        apiCatPredict.predict(catPredictFormData)
}