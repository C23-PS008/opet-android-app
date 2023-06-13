package com.c23ps008.opet.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetAllPetAdoptionResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PetAdoptionItem(

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

	@field:SerializedName("pet_category")
	val petCategory: PetCategory? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("age")
	val age: String? = null,

	@field:SerializedName("lat")
	val lat: Any? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class Data(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("rows")
	val rows: List<PetAdoptionItem>
)
