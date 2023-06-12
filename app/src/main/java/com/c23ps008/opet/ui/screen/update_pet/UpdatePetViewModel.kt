package com.c23ps008.opet.ui.screen.update_pet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c23ps008.opet.data.formdata.UpdatePetFormData
import com.c23ps008.opet.data.remote.response.PetDetailResponse
import com.c23ps008.opet.data.remote.response.UpdateMyPetResponse
import com.c23ps008.opet.data.repository.LocalDataStoreRepository
import com.c23ps008.opet.data.repository.PetRepository
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.utils.createErrorResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UpdatePetViewModel(
    savedStateHandle: SavedStateHandle,
    private val petRepository: PetRepository,
    private val localDataStoreRepository: LocalDataStoreRepository,
) : ViewModel() {
    private val petId: String = checkNotNull(savedStateHandle[UpdatePetDestination.petIdArg])

    private val _updateState: MutableStateFlow<UiState<PetDetailResponse>> =
        MutableStateFlow(UiState.Loading)
    val updateState: StateFlow<UiState<PetDetailResponse>> get() = _updateState

    fun getPetDetail() {
        viewModelScope.launch {
            try {
                val token = localDataStoreRepository.getToken().firstOrNull()
                val response = petRepository.getDetail(token.toString(), petId)
                _updateState.value = UiState.Success(response)
            } catch (e: HttpException) {
                val errorResponse = createErrorResponse(e)
                _updateState.value = UiState.Error(errorResponse.message.toString())
            } catch (e: Exception) {
                _updateState.value = UiState.Error(e.message.toString())
            }
        }
    }

    suspend fun updatePet(updatePetFormData: UpdatePetFormData): UiState<UpdateMyPetResponse> =
        try {
            val token = localDataStoreRepository.getToken().firstOrNull()
            val response = petRepository.updatePet(token.toString(), petId, updatePetFormData)
            UiState.Success(response)
        } catch (e: HttpException) {
            val errorResponse = createErrorResponse(e)
            UiState.Error(errorResponse.message.toString())
        } catch (e: Exception) {
            UiState.Error(e.message.toString())
        }
}