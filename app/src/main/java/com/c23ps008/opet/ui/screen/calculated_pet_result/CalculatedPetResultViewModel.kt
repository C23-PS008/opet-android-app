package com.c23ps008.opet.ui.screen.calculated_pet_result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.c23ps008.opet.data.PetAdoptionListDataSource
import com.c23ps008.opet.data.repository.LocalDataStoreRepository
import com.c23ps008.opet.data.repository.PetRepository

class CalculatedPetResultViewModel(
    savedStateHandle: SavedStateHandle,
    private val petRepository: PetRepository,
    private val localDataStoreRepository: LocalDataStoreRepository,
) : ViewModel() {
    val petBreed: String = checkNotNull(savedStateHandle[CalculatedPetResultDestination.petBreed])

    val pagingSource =
        Pager(PagingConfig(pageSize = 20)) {
            PetAdoptionListDataSource(petRepository, localDataStoreRepository, petBreed = petBreed)
        }.flow.cachedIn(viewModelScope)

}