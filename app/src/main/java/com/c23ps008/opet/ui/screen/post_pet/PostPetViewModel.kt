package com.c23ps008.opet.ui.screen.post_pet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.c23ps008.opet.data.formdata.PostPetFormData
import com.c23ps008.opet.ui.common.UiState

class PostPetViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val imageUri: String = checkNotNull(savedStateHandle[PostPetDestination.imageUriArg])
}