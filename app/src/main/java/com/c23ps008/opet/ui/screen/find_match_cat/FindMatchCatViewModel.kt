package com.c23ps008.opet.ui.screen.find_match_cat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c23ps008.opet.data.formdata.CatPredictFormData
import com.c23ps008.opet.data.remote.response.CatPredictResponse
import com.c23ps008.opet.data.repository.CatPredictRepository
import com.c23ps008.opet.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FindMatchCatViewModel(private val catPredictRepository: CatPredictRepository): ViewModel() {
    private val _dataState: MutableStateFlow<UiState<CatPredictResponse>> =
        MutableStateFlow(UiState.Loading)
    val dataState: StateFlow<UiState<CatPredictResponse>> get() = _dataState

    fun predict(formData: CatPredictFormData) {
        viewModelScope.launch {
            try {
                val response = catPredictRepository.predict(formData)
                _dataState.value = UiState.Success(response)
            } catch (e: Exception) {
                _dataState.value = UiState.Error(e.message.toString())
            }
        }
    }
}