package com.c23ps008.opet.utils

import com.google.gson.Gson
import retrofit2.HttpException


data class ErrorResponse(
    val error: Boolean,
    val message: String?
)

fun createErrorResponse(e: HttpException): ErrorResponse {
    val errorJSONString = e.response()?.errorBody()?.string()
    return Gson().fromJson(errorJSONString, ErrorResponse::class.java)
}