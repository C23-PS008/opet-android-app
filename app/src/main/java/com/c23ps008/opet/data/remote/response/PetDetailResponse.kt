package com.c23ps008.opet.data.remote.response

import com.google.gson.annotations.SerializedName

data class PetDetailResponse(

	@field:SerializedName("data")
	val data: PetDetailData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PetDetailData(

	@field:SerializedName("petId")
	val petId: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("about")
	val about: String? = null,

	@field:SerializedName("lon")
	val lon: Any? = null,

	@field:SerializedName("breed")
	val breed: String? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("characters")
	val characters: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("size")
	val size: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("petCategory")
	val petCategory: String? = null,

	@field:SerializedName("pet_category")
	val pet_category: PetCategory? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("age")
	val age: String? = null,

	@field:SerializedName("lat")
	val lat: Any? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
