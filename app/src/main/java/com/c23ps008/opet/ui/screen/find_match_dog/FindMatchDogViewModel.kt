package com.c23ps008.opet.ui.screen.find_match_dog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c23ps008.opet.data.formdata.DogPredictFormData
import com.c23ps008.opet.data.remote.response.DogPredictResponse
import com.c23ps008.opet.data.repository.DogPredictRepository
import com.c23ps008.opet.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FindMatchDogViewModel(private val dogPredictRepository: DogPredictRepository) : ViewModel() {
    private val _dataState: MutableStateFlow<UiState<DogPredictResponse>> =
        MutableStateFlow(UiState.Loading)
    val dataState: StateFlow<UiState<DogPredictResponse>> get() = _dataState

    fun predict(formData: DogPredictFormData) {
        viewModelScope.launch {
            try {
                val response = dogPredictRepository.predict(formData)
                _dataState.value = UiState.Success(response)
            } catch (e: Exception) {
                _dataState.value = UiState.Error(e.message.toString())
            }
        }
    }
}