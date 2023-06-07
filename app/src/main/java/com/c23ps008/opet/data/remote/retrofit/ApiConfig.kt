package com.c23ps008.opet.data.remote.retrofit

import com.c23ps008.opet.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getApiBackendService(): ApiBackendService {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        val apiUrl = BuildConfig.BACKEND_API_ENDPOINT_URL
        val retrofit =
            Retrofit.Builder().baseUrl(apiUrl)
                .client(client).addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit.create(ApiBackendService::class.java)
    }
}