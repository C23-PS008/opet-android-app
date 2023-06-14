package com.c23ps008.opet.ui.screen.search_breed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class SearchBreedConfirmImageViewModel(savedStateHandle: SavedStateHandle): ViewModel() {
    val imageUri: String = checkNotNull(savedStateHandle[SearchBreedConfirmImageDestination.imgUriArg])
}