package com.c23ps008.opet.data.formdata

import java.io.File

data class PostPetFormData(
    var name: String = "",
    var petCategory: Int? = null,
    var breed: String = "",
    var characters: String = "Unknown",
    var age: String = "",
    var size: String = "Unknown",
    var gender: String = "",
    var about: String = "",
    var lat: Double? = null,
    var lon: Double? = null,
    var image: File,
)