package com.c23ps008.opet.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c23ps008.opet.data.remote.response.PetAdoptionItem
import com.c23ps008.opet.data.repository.LocalDataStoreRepository
import com.c23ps008.opet.data.repository.PetRepository
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.utils.createErrorResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    private val petRepository: PetRepository,
    private val localDataStoreRepository: LocalDataStoreRepository,
) : ViewModel() {
    private val _petAdoptionState: MutableStateFlow<UiState<List<PetAdoptionItem?>>> =
        MutableStateFlow(UiState.Loading)
    val petAdoptionState: StateFlow<UiState<List<PetAdoptionItem?>>> = _petAdoptionState

    fun getAllPetAdoption() {
        viewModelScope.launch {
            try {
                val token = localDataStoreRepository.getToken().firstOrNull()
                val response = petRepository.getAllPetAdoption(token.toString())
                _petAdoptionState.value = UiState.Success(response.data?.rows.orEmpty().take(4))
            } catch (e: HttpException) {
                val errorResponse = createErrorResponse(e)
                _petAdoptionState.value = UiState.Error(errorResponse.message.toString())
            } catch (e: Exception) {
                _petAdoptionState.value = UiState.Error(e.message.toString())
            }
        }
    }
}