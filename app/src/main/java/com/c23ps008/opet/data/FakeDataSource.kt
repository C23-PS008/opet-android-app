package com.c23ps008.opet.data

import com.c23ps008.opet.R

object FakeDataSource {
    val dummyHomePetResources = listOf(
        PetData(1, "John", "Husky", "Bali", "dog", R.drawable.anjing),
        PetData(2, "Kat The Cat", "Pure", "Bandung", "cat", R.drawable.kucing),
        PetData(3, "Kitten", "Pure", "Bali", "cat", R.drawable.kucing),
        PetData(4, "Alex The Dog", "Husky", "Jakarta", "dog", R.drawable.anjing),
    )
}

data class PetData(
    val id: Int,
    val name: String,
    val pet_breed: String,
    val pet_address: String,
    val pet_type: String,
    val pet_image: Int,
)