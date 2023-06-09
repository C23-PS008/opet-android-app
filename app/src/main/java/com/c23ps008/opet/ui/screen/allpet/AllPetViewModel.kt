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
}