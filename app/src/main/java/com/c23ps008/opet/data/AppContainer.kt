package com.c23ps008.opet.data

import android.content.Context
import com.c23ps008.opet.data.local.LocalDataStore
import com.c23ps008.opet.data.remote.retrofit.ApiConfig
import com.c23ps008.opet.data.repository.AuthRepository
import com.c23ps008.opet.data.repository.CatPredictRepository
import com.c23ps008.opet.data.repository.DogPredictRepository
import com.c23ps008.opet.data.repository.LocalDataStoreRepository
import com.c23ps008.opet.data.repository.MyPetsRepository
import com.c23ps008.opet.data.repository.PetRepository
import com.c23ps008.opet.data.repository.PostPetRepository
import com.c23ps008.opet.data.repository.ProfileRepository

interface AppContainer {
    val authRepository: AuthRepository
    val localDataStoreRepository: LocalDataStoreRepository
    val profileRepository: ProfileRepository
    val postPetRepository: PostPetRepository
    val myPetsRepository: MyPetsRepository
    val petRepository: PetRepository
    val dogPredictRepository: DogPredictRepository
    val catPredictRepository: CatPredictRepository
}

class AppDataContainer(private val context: Context, private val apiConfig: ApiConfig) :
    AppContainer {
    override val authRepository: AuthRepository by lazy {
        AuthRepository(apiConfig.getApiBackendService())
    }

    override val localDataStoreRepository: LocalDataStoreRepository by lazy {
        LocalDataStoreRepository(LocalDataStore(context))
    }

    override val profileRepository: ProfileRepository by lazy {
        ProfileRepository(apiConfig.getApiBackendService())
    }

    override val postPetRepository: PostPetRepository by lazy {
        PostPetRepository(apiConfig.getApiBackendService())
    }

    override val myPetsRepository: MyPetsRepository by lazy {
        MyPetsRepository(apiConfig.getApiBackendService())
    }

    override val petRepository: PetRepository by lazy {
        PetRepository(apiConfig.getApiBackendService())
    }
    override val dogPredictRepository: DogPredictRepository by lazy {
        DogPredictRepository(apiConfig.getApiDogPredict())
    }
    override val catPredictRepository: CatPredictRepository by lazy {
        CatPredictRepository(apiConfig.getApiCatPredict())
    }

}