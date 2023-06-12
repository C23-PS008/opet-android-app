package com.c23ps008.opet.data.formdata

data class UpdatePetFormData(
    var name: String? = null,
    var petCategory: Int? = null,
    var breed: String? = null,
    var characters: String? = "Unknown",
    var age: String? = null,
    var size: String? = "Small",
    var gender: String? = null,
    var about: String? = null,
    var lat: Double? = null,
    var lon: Double? = null,
)
