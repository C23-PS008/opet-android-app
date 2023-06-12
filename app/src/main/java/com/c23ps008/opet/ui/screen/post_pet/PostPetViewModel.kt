package com.c23ps008.opet.ui.screen.post_pet

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.c23ps008.opet.data.formdata.PostPetFormData
import com.c23ps008.opet.data.formdata.UploadPetResponse
import com.c23ps008.opet.data.repository.LocalDataStoreRepository
import com.c23ps008.opet.data.repository.PostPetRepository
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.utils.createErrorResponse
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException

class PostPetViewModel(
    savedStateHandle: SavedStateHandle,
    private val postPetRepository: PostPetRepository,
    private val localDataStoreRepository: LocalDataStoreRepository,
) : ViewModel() {
    val imageUri: String = checkNotNull(savedStateHandle[PostPetDestination.imageUriArg])

    suspend fun uploadPet(postPetFormData: PostPetFormData): UiState<UploadPetResponse> = try {
        val token = localDataStoreRepository.getToken().firstOrNull()
        Log.d("TESTS", "uploadPet: $token")
        val response = postPetRepository.uploadPet(token.toString(), postPetFormData)
        UiState.Success(response)
    } catch (e: HttpException) {
        val errorResponse = createErrorResponse(e)
        UiState.Error(errorResponse.message.toString())
    } catch (e: Exception) {
        UiState.Error(e.message.toString())
    }
}