package com.c23ps008.opet.ui.screen.allpet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.c23ps008.opet.data.PetAdoptionListDataSource
import com.c23ps008.opet.data.repository.LocalDataStoreRepository
import com.c23ps008.opet.data.repository.PetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest

class AllPetViewModel(
    private val petRepository: PetRepository,
    private val localDataStoreRepository: LocalDataStoreRepository,
) : ViewModel() {
    private val _petType = MutableStateFlow("all")
    val petType : StateFlow<String> = _petType

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingSource = petType.flatMapLatest { type ->
        Pager(PagingConfig(pageSize = 20)) {
            PetAdoptionListDataSource(petRepository, localDataStoreRepository, type)
        }.flow
    }.cachedIn(viewModelScope)

    fun updatePetType(type: String) {
        _petType.value = type
    }

    // fun getPetsAdoption(): PagingSource<Int, PetAdoptionItem> {
    //     return object : PagingSource<Int, PetAdoptionItem>() {
    //         override fun getRefreshKey(state: PagingState<Int, PetAdoptionItem>): Int? {
    //             return state.anchorPosition?.let {
    //                 state.closestPageToPosition(it)?.prevKey?.plus(1)
    //                     ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
    //             }
    //         }
    //
    //         override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PetAdoptionItem> {
    //             return try {
    //                 val pageNumber = params.key ?: 0
    //                 val token = localDataStoreRepository.getToken().firstOrNull()
    //                 val response = petRepository.getAllPetAdoption(
    //                     token = token.toString(),
    //                     type = "all",
    //                     page = pageNumber,
    //                     size = 10
    //                 )
    //                 val prevKey = if (pageNumber > 0) pageNumber - 1 else null
    //                 val nextKey =
    //                     if (response.data?.rows?.isEmpty() == true) pageNumber + 1 else null
    //
    //                 LoadResult.Page(
    //                     data = response.data?.rows.orEmpty(),
    //                     prevKey = prevKey,
    //                     nextKey = nextKey
    //                 )
    //             } catch (e: HttpException) {
    //                 val errorResponse = createErrorResponse(e)
    //                 LoadResult.Error(Error(errorResponse.message))
    //             } catch (e: Exception) {
    //                 LoadResult.Error(e)
    //             }
    //         }
    //     }
    // }
}