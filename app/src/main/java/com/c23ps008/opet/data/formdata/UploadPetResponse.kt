package com.c23ps008.opet.data.formdata

import com.google.gson.annotations.SerializedName

data class UploadPetResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
