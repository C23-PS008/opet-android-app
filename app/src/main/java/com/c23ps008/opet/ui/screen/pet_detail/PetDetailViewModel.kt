package com.c23ps008.opet.ui.screen.pet_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c23ps008.opet.data.remote.response.PetDetailResponse
import com.c23ps008.opet.data.repository.LocalDataStoreRepository
import com.c23ps008.opet.data.repository.PetRepository
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.utils.createErrorResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PetDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val petRepository: PetRepository,
    private val localDataStoreRepository: LocalDataStoreRepository,
) : ViewModel() {
    private val petId: String = checkNotNull(savedStateHandle[PetDetailDestination.petIdArg])

    private val _detailState: MutableStateFlow<UiState<PetDetailResponse>> = MutableStateFlow(UiState.Loading)
    val detailState: StateFlow<UiState<PetDetailResponse>> get() = _detailState

    fun getDetail() {
        viewModelScope.launch {
            try {
                val token = localDataStoreRepository.getToken().firstOrNull()
                val response = petRepository.getDetail(token.toString(), petId)
                _detailState.value = UiState.Success(response)
            } catch (e: HttpException) {
                val errorResponse = createErrorResponse(e)
                _detailState.value = UiState.Error(errorResponse.message.toString())
            } catch (e: Exception) {
                _detailState.value = UiState.Error(e.message.toString())
            }
        }
    }
}