package com.c23ps008.opet.ui.screen.post_camera

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ConfirmImageViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val imageUri: String = checkNotNull(savedStateHandle[ConfirmImageDestination.imgUriArg])
}