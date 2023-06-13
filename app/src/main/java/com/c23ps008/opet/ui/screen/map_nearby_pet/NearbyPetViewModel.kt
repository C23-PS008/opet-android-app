package com.c23ps008.opet.ui.screen.map_nearby_pet

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

class NearbyPetViewModel(
    private val petRepository: PetRepository,
    private val localDataStoreRepository: LocalDataStoreRepository,
) : ViewModel() {
    private val _mapDataState: MutableStateFlow<UiState<List<PetAdoptionItem>>> =
        MutableStateFlow(UiState.Loading)
    val mapDataState: StateFlow<UiState<List<PetAdoptionItem>>> get() = _mapDataState

    fun getListPet() {
        viewModelScope.launch {
            try {
                val token = localDataStoreRepository.getToken().firstOrNull()
                val response =
                    petRepository.getAllPetAdoption(token = token.toString(), page = 0, size = 20)
                _mapDataState.value = UiState.Success(response.data?.rows.orEmpty())
            } catch (e: HttpException) {
                val errorResponse = createErrorResponse(e)
                _mapDataState.value = UiState.Error(errorResponse.message.toString())
            } catch (e: Exception) {
                _mapDataState.value = UiState.Error(e.message.toString())
            }
        }
    }
}