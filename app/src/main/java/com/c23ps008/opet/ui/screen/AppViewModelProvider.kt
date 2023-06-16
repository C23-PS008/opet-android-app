package com.c23ps008.opet.ui.screen

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.c23ps008.opet.OPetApplication
import com.c23ps008.opet.ui.screen.allpet.AllPetViewModel
import com.c23ps008.opet.ui.screen.calculated_pet_result.CalculatedPetResultViewModel
import com.c23ps008.opet.ui.screen.find_match_cat.FindMatchCatViewModel
import com.c23ps008.opet.ui.screen.find_match_dog.FindMatchDogViewModel
import com.c23ps008.opet.ui.screen.home.HomeViewModel
import com.c23ps008.opet.ui.screen.login.LoginViewModel
import com.c23ps008.opet.ui.screen.map_nearby_pet.NearbyPetViewModel
import com.c23ps008.opet.ui.screen.my_post.MyPostViewModel
import com.c23ps008.opet.ui.screen.permissions_dialog.PermissionsViewModel
import com.c23ps008.opet.ui.screen.pet_detail.PetDetailViewModel
import com.c23ps008.opet.ui.screen.post_camera.ConfirmImageViewModel
import com.c23ps008.opet.ui.screen.post_pet.PostPetViewModel
import com.c23ps008.opet.ui.screen.profile.ProfileViewModel
import com.c23ps008.opet.ui.screen.register.RegisterViewModel
import com.c23ps008.opet.ui.screen.search_breed.ProcessingImageViewModel
import com.c23ps008.opet.ui.screen.search_breed.SearchBreedConfirmImageViewModel
import com.c23ps008.opet.ui.screen.search_breed.SearchBreedViewModel
import com.c23ps008.opet.ui.screen.splash.SplashViewModel
import com.c23ps008.opet.ui.screen.update_pet.UpdatePetViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SplashViewModel(
                opetApplication().container.localDataStoreRepository,
                opetApplication().container.profileRepository
            )
        }
        initializer {
            RegisterViewModel(opetApplication().container.authRepository)
        }
        initializer {
            LoginViewModel(
                opetApplication().container.authRepository,
                opetApplication().container.localDataStoreRepository
            )
        }
        initializer {
            ProfileViewModel(
                opetApplication().container.localDataStoreRepository,
                opetApplication().container.profileRepository
            )
        }
        initializer {
            PermissionsViewModel()
        }
        initializer {
            ConfirmImageViewModel(this.createSavedStateHandle())
        }
        initializer {
            PostPetViewModel(
                this.createSavedStateHandle(),
                opetApplication().container.postPetRepository,
                opetApplication().container.localDataStoreRepository
            )
        }
        initializer {
            MyPostViewModel(
                opetApplication().container.myPetsRepository,
                opetApplication().container.localDataStoreRepository
            )
        }
        initializer {
            PetDetailViewModel(
                this.createSavedStateHandle(),
                opetApplication().container.petRepository,
                opetApplication().container.localDataStoreRepository
            )
        }
        initializer {
            UpdatePetViewModel(
                this.createSavedStateHandle(),
                opetApplication().container.petRepository,
                opetApplication().container.localDataStoreRepository
            )
        }
        initializer {
            HomeViewModel(
                opetApplication().container.petRepository,
                opetApplication().container.localDataStoreRepository
            )
        }
        initializer {
            AllPetViewModel(
                opetApplication().container.petRepository,
                opetApplication().container.localDataStoreRepository
            )
        }
        initializer {
            NearbyPetViewModel(
                opetApplication().container.petRepository,
                opetApplication().container.localDataStoreRepository
            )
        }
        initializer {
            FindMatchDogViewModel(
                opetApplication().container.dogPredictRepository
            )
        }
        initializer {
            FindMatchCatViewModel(
                opetApplication().container.catPredictRepository
            )
        }
        initializer {
            SearchBreedViewModel(
                opetApplication().container.petRepository,
                opetApplication().container.localDataStoreRepository
            )
        }
        initializer {
            SearchBreedConfirmImageViewModel(
                this.createSavedStateHandle()
            )
        }
        initializer {
            ProcessingImageViewModel(this.createSavedStateHandle())
        }
        initializer {
            CalculatedPetResultViewModel(
                this.createSavedStateHandle(),
                opetApplication().container.petRepository,
                opetApplication().container.localDataStoreRepository
            )
        }
    }
}

fun CreationExtras.opetApplication(): OPetApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as OPetApplication)