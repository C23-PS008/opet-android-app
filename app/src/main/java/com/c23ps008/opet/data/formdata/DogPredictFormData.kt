package com.c23ps008.opet.data.formdata

data class DogPredictFormData(
	val trainability_category: List<String?>,
	val energy_level_category: List<String?>,
	val shedding_category: List<String?>,
	val grooming_frequency_category: List<String?>,
	val temperament_category: List<String?>,
	val weight_category: List<String?>,
	val height_category: List<String?>,
	val demeanor_category: List<String?>
)

