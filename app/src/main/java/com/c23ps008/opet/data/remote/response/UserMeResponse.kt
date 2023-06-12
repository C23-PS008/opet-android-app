package com.c23ps008.opet.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserMeResponse(

	@field:SerializedName("data")
	val data: UserMeResponseData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class UserMeResponseData(

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
