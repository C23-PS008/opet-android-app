package com.c23ps008.opet.ui.screen

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.c23ps008.opet.OPetApplication
import com.c23ps008.opet.ui.screen.permissions_dialog.PermissionsViewModel
import com.c23ps008.opet.ui.screen.post_camera.ConfirmImageViewModel
import com.c23ps008.opet.ui.screen.register.RegisterViewModel
import com.c23ps008.opet.ui.screen.splash.SplashViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SplashViewModel(opetApplication().container.localDataStoreRepository)
        }
        initializer {
            RegisterViewModel(opetApplication().container.authRepository)
        }
        initializer {
            PermissionsViewModel()
        }
        initializer {
            ConfirmImageViewModel(this.createSavedStateHandle())
        }
    }
}

fun CreationExtras.opetApplication(): OPetApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as OPetApplication)