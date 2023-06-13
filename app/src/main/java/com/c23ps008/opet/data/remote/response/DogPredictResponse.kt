package com.c23ps008.opet.data.remote.response

import com.google.gson.annotations.SerializedName

data class DogPredictResponse(

	@field:SerializedName("predictions")
	val predictions: List<PredictionsItem?>? = null
)

data class TopBreedsItem(

	@field:SerializedName("demeanor_category")
	val demeanorCategory: String? = null,

	@field:SerializedName("demeanor_value")
	val demeanorValue: Any? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("max_height")
	val maxHeight: Any? = null,

	@field:SerializedName("breed_image")
	val breedImage: String? = null,

	@field:SerializedName("min_expectancy")
	val minExpectancy: Any? = null,

	@field:SerializedName("shedding_value")
	val sheddingValue: Any? = null,

	@field:SerializedName("popularity")
	val popularity: Int? = null,

	@field:SerializedName("expectancy")
	val expectancy: Any? = null,

	@field:SerializedName("trainability_category")
	val trainabilityCategory: String? = null,

	@field:SerializedName("energy_level_value")
	val energyLevelValue: Any? = null,

	@field:SerializedName("min_weight")
	val minWeight: Any? = null,

	@field:SerializedName("group")
	val group: String? = null,

	@field:SerializedName("height")
	val height: Any? = null,

	@field:SerializedName("trainability_value")
	val trainabilityValue: Any? = null,

	@field:SerializedName("shedding_category")
	val sheddingCategory: String? = null,

	@field:SerializedName("height_category")
	val heightCategory: String? = null,

	@field:SerializedName("temperament_category")
	val temperamentCategory: String? = null,

	@field:SerializedName("max_expectancy")
	val maxExpectancy: Any? = null,

	@field:SerializedName("weight_category")
	val weightCategory: String? = null,

	@field:SerializedName("weight")
	val weight: Any? = null,

	@field:SerializedName("grooming_frequency_category")
	val groomingFrequencyCategory: String? = null,

	@field:SerializedName("breed")
	val breed: String? = null,

	@field:SerializedName("max_weight")
	val maxWeight: Any? = null,

	@field:SerializedName("min_height")
	val minHeight: Any? = null,

	@field:SerializedName("grooming_frequency_value")
	val groomingFrequencyValue: Any? = null,

	@field:SerializedName("temperament")
	val temperament: String? = null,

	@field:SerializedName("expectancy_category")
	val expectancyCategory: String? = null,

	@field:SerializedName("energy_level_category")
	val energyLevelCategory: String? = null
)

data class PredictionsItem(

	@field:SerializedName("label")
	val label: String? = null,

	@field:SerializedName("top_breeds")
	val topBreeds: List<TopBreedsItem?>? = null
)
