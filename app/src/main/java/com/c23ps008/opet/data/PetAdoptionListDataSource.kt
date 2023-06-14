package com.c23ps008.opet.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.c23ps008.opet.data.remote.response.PetAdoptionItem
import com.c23ps008.opet.data.repository.LocalDataStoreRepository
import com.c23ps008.opet.data.repository.PetRepository
import com.c23ps008.opet.utils.createErrorResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException

class PetAdoptionListDataSource(private val petRepository: PetRepository, private val localDataStoreRepository: LocalDataStoreRepository, private val type: String = "all", private val petBreed: String? = null): PagingSource<Int, PetAdoptionItem>() {
    override fun getRefreshKey(state: PagingState<Int, PetAdoptionItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PetAdoptionItem> {
        delay(1000)
        return try {
            val pageNumber = params.key ?: 0
            Log.d("LOADING", "load: $pageNumber")
            val token = localDataStoreRepository.getToken().firstOrNull()
            val response = petRepository.getAllPetAdoption(
                token = token.toString(),
                type = type,
                page = pageNumber,
                size = 10,
                breed = petBreed
            )
            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
            val nextKey =
                if (response.data?.rows?.isEmpty() == true) null else pageNumber + 1

            Log.d("LOADING", "NEXT: $nextKey")
            Log.d("LOADING", "PREV: $prevKey")

            LoadResult.Page(
                data = response.data?.rows.orEmpty(),
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: HttpException) {
            val errorResponse = createErrorResponse(e)
            LoadResult.Error(Error(errorResponse.message))
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}