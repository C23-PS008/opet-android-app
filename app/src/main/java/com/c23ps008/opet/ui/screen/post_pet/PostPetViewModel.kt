package com.c23ps008.opet.ui.screen.post_pet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class PostPetViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val petType: String = checkNotNull(savedStateHandle[PostPetDestination.petTypeArg])
    val petBreed: String = checkNotNull(savedStateHandle[PostPetDestination.petBreedArg])
    val imageUri: String = checkNotNull(savedStateHandle[PostPetDestination.imageUriArg])
}