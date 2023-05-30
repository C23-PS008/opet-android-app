package com.c23ps008.opet.ui.screen

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.c23ps008.opet.ui.screen.permissions_dialog.PermissionsViewModel
import com.c23ps008.opet.ui.screen.post_camera.ConfirmImageViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            PermissionsViewModel()
        }
        initializer {
            ConfirmImageViewModel(this.createSavedStateHandle())
        }
    }
}