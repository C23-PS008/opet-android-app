package com.c23ps008.opet.ui.screen.search_breed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.c23ps008.opet.R
import com.c23ps008.opet.data.PetBreedData
import com.c23ps008.opet.data.remote.response.PetAdoptionItem
import com.c23ps008.opet.ui.components.PetCard
import com.c23ps008.opet.ui.components.PetCardState
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.theme.OPetTheme
import com.c23ps008.opet.utils.getCityName

object SearchBreedDestination : NavigationDestination {
    override val route: String = "search-breed"
}

@Composable
fun SearchBreedScreen(
    viewModel: SearchBreedViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateUp: () -> Unit,
    navigateToDetail: (String) -> Unit,
    navigateToSearchBreedCamera: () -> Unit,
) {
    val lazyPetAdoptionList = viewModel.pagingSource.collectAsLazyPagingItems()
    SearchBreedContent(
        onNavigateUp = onNavigateUp,
        lazyResult = lazyPetAdoptionList,
        onSearchSubmit = {
            viewModel.updateSearchQuery(it)
        },
        navigateToDetail = navigateToDetail,
        navigateToSearchBreedCamera = navigateToSearchBreedCamera
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBreedContent(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    navigateToDetail: (String) -> Unit,
    navigateToSearchBreedCamera: () -> Unit,
    lazyResult: LazyPagingItems<PetAdoptionItem>?,
    onSearchSubmit: (String) -> Unit,
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    val petBreed by remember {
        mutableStateOf(PetBreedData.getAll())
    }

    val filteredBreed = petBreed.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    val context = LocalContext.current

    Box(modifier.fillMaxSize()) {
        Box(
            Modifier
                .semantics {
                    isContainer = true
                }
                .zIndex(1f)
                .fillMaxSize()) {
            SearchBar(
                modifier = Modifier.align(Alignment.TopCenter),
                windowInsets = WindowInsets(left = 16.dp, right = 16.dp),
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = {
                    active = false
                    onSearchSubmit(searchQuery)
                },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text(text = "Search By Breed") },
                leadingIcon = {
                    IconButton(onClick = { onNavigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(
                                id = R.string.menu_back
                            )
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { navigateToSearchBreedCamera() }) {
                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = stringResource(
                                R.string.search_with_image
                            )
                        )
                    }
                }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(filteredBreed) {
                        DropdownMenuItem(
                            text = { Text(text = it) },
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                searchQuery = it
                                active = false
                                onSearchSubmit(searchQuery)
                            }
                        )
                    }
                }
            }
        }
        if (lazyResult?.loadState?.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else
            if (lazyResult?.itemCount == 0 && lazyResult.loadState.append !is LoadState.Loading) {
                Text(
                    text = "Pet not found!",
                    modifier = Modifier
                        .padding(top = 72.dp)
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyVerticalGrid(
                    modifier = Modifier,
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(
                        bottom = 16.dp,
                        top = 72.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                ) {
                    if (lazyResult != null) {
                        items(count = lazyResult.itemCount, key = lazyResult.itemKey()) { index ->
                            val data = lazyResult[index]
                            val cardState =
                                PetCardState(
                                    data?.petId.toString(),
                                    data?.photoUrl.toString(),
                                    data?.breed.toString(),
                                    data?.name.toString(),
                                    getCityName(context, data?.lat as Double, data.lon as Double)
                                )
                            PetCard(
                                data = cardState,
                                onClick = { navigateToDetail(data.petId.toString()) })
                        }
                    }
                    if (lazyResult?.loadState?.append == LoadState.Loading) {
                        item(span = { GridItemSpan(2) }) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(
                                        Alignment.CenterHorizontally
                                    )
                            )
                        }
                    }
                }
            }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SearchBreedContentPreview() {
    OPetTheme {
        SearchBreedContent(
            onNavigateUp = {},
            onSearchSubmit = {},
            lazyResult = null,
            navigateToDetail = {},
            navigateToSearchBreedCamera = {})
    }
}

