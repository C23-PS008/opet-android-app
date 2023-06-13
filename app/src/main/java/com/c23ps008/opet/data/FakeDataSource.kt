package com.c23ps008.opet.data

object FakeDataSource {
    val dummyHomePetResources = listOf(
        PetData("1", "John", "Husky", "Bali", "dog", ""),
        PetData("2", "Kat The Cat", "Pure", "Bandung", "cat", ""),
        PetData("3", "Kitten", "Pure", "Bali", "cat", ""),
        PetData("4", "Alex The Dog", "Husky", "Jakarta", "dog", ""),
    )
}

data class PetData(
    val id: String,
    val name: String,
    val pet_breed: String,
    val pet_address: String,
    val pet_type: String,
    val pet_image: String,
)