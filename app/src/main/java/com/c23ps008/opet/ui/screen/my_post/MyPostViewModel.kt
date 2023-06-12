package com.c23ps008.opet.ui.screen.my_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c23ps008.opet.data.remote.response.DeleteMyPetsResponse
import com.c23ps008.opet.data.remote.response.MyPetsDataItem
import com.c23ps008.opet.data.repository.LocalDataStoreRepository
import com.c23ps008.opet.data.repository.MyPetsRepository
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.utils.createErrorResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MyPostViewModel(
    private val myPetsRepository: MyPetsRepository,
    private val localDataStoreRepository: LocalDataStoreRepository,
): ViewModel() {
    private val _myPostsState: MutableStateFlow<UiState<List<MyPetsDataItem?>>> =
        MutableStateFlow(UiState.Loading)
    val myPostsState: StateFlow<UiState<List<MyPetsDataItem?>>> get() = _myPostsState

    fun getMyPosts() {
        viewModelScope.launch {
            try {
                val token = localDataStoreRepository.getToken().firstOrNull()
                val response = myPetsRepository.getMyPets(token.toString())
                _myPostsState.value = UiState.Success(response.data.orEmpty())
            } catch (e: HttpException) {
                val errorResponse = createErrorResponse(e)
                _myPostsState.value = UiState.Error(errorResponse.message.toString())
            } catch (e: Exception) {
                _myPostsState.value = UiState.Error(e.message.toString())
            }
        }
    }

    suspend fun deletePost(id: String) : UiState<DeleteMyPetsResponse> = try {
        val token = localDataStoreRepository.getToken().firstOrNull()
        val response = myPetsRepository.deletePet(token.toString(), id)
        UiState.Success(response)
    } catch (e: HttpException) {
        val errorResponse = createErrorResponse(e)
        UiState.Error(errorResponse.message.toString())
    } catch (e: Exception) {
        UiState.Error(e.message.toString())
    }
}