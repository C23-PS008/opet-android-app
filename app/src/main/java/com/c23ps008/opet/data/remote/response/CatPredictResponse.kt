package com.c23ps008.opet.data.remote.response

import com.google.gson.annotations.SerializedName

data class CatPredictResponse(

	@field:SerializedName("recommendations")
	val recommendations: List<RecommendationsItem?>? = null
)

data class RecommendationsItem(

	@field:SerializedName("other_pets_friendly")
	val otherPetsFriendly: Int? = null,

	@field:SerializedName("children_friendly")
	val childrenFriendly: Int? = null,

	@field:SerializedName("family_friendly")
	val familyFriendly: Int? = null,

	@field:SerializedName("origin")
	val origin: String? = null,

	@field:SerializedName("length")
	val length: String? = null,

	@field:SerializedName("max_life_expectancy")
	val maxLifeExpectancy: Any? = null,

	@field:SerializedName("min_life_expectancy")
	val minLifeExpectancy: Any? = null,

	@field:SerializedName("shedding")
	val shedding: Int? = null,

	@field:SerializedName("breed_image")
	val breedImage: String? = null,

	@field:SerializedName("breed")
	val breed: String? = null,

	@field:SerializedName("playfulness")
	val playfulness: Int? = null,

	@field:SerializedName("intelligence")
	val intelligence: Int? = null,

	@field:SerializedName("general_health")
	val generalHealth: Int? = null,

	@field:SerializedName("max_weight")
	val maxWeight: Any? = null,

	@field:SerializedName("grooming")
	val grooming: Int? = null,

	@field:SerializedName("min_weight")
	val minWeight: Any? = null,

	@field:SerializedName("similarity_score")
	val similarityScore: Any? = null
)
