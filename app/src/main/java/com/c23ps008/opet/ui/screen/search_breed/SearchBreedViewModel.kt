package com.c23ps008.opet.ui.screen.search_breed

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

class SearchBreedViewModel(
    private val petRepository: PetRepository,
    private val localDataStoreRepository: LocalDataStoreRepository,
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val searchQuery : StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingSource = searchQuery.flatMapLatest { query ->
        Pager(PagingConfig(pageSize = 20)) {
            PetAdoptionListDataSource(petRepository, localDataStoreRepository, petBreed = query)
        }.flow
    }.cachedIn(viewModelScope)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}