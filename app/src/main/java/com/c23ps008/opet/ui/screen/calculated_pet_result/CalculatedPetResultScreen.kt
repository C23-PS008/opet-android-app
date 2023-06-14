package com.c23ps008.opet.ui.screen.calculated_pet_result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.c23ps008.opet.R
import com.c23ps008.opet.data.remote.response.PetAdoptionItem
import com.c23ps008.opet.ui.components.PetCard
import com.c23ps008.opet.ui.components.PetCardState
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.utils.getCityName

object CalculatedPetResultDestination : NavigationDestination {
    override val route: String = "calculated-result"
    const val petBreed = "petBreed"
    val routeWithArgs = "${route}/{$petBreed}"
}

@Composable
fun CalculatedPetResultScreen(
    viewModel: CalculatedPetResultViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateUp: () -> Unit, navigateToDetail: (String) -> Unit,
) {
    val lazyResult = viewModel.pagingSource.collectAsLazyPagingItems()
    CalculatedPetResultContent(
        onNavigateUp = onNavigateUp,
        navigateToDetail = navigateToDetail,
        petBreed = viewModel.petBreed,
        lazyResult = lazyResult
    )
}

@Composable
fun CalculatedPetResultContent(
    onNavigateUp: () -> Unit,
    navigateToDetail: (String) -> Unit,
    petBreed: String,
    lazyResult: LazyPagingItems<PetAdoptionItem>?,
) {
    val context = LocalContext.current

    Scaffold(topBar = {
        CalculatedTopBar(
            onNavigateUp = onNavigateUp,
            breed = petBreed
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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
                            items(
                                count = lazyResult.itemCount,
                                key = lazyResult.itemKey()
                            ) { index ->
                                val data = lazyResult[index]
                                val cardState =
                                    PetCardState(
                                        data?.petId.toString(),
                                        data?.photoUrl.toString(),
                                        data?.breed.toString(),
                                        data?.name.toString(),
                                        getCityName(
                                            context,
                                            data?.lat as Double,
                                            data.lon as Double
                                        )
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatedTopBar(modifier: Modifier = Modifier, onNavigateUp: () -> Unit, breed: String = "") {
    TopAppBar(modifier = modifier, title = { Text(text = "Result for $breed") }, navigationIcon = {
        IconButton(onClick = { onNavigateUp() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.menu_back)
            )
        }
    })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalculatedPetResultContentPreview() {
    CalculatedPetResultContent(
        onNavigateUp = {},
        navigateToDetail = {},
        lazyResult = null,
        petBreed = "Shiba Inu"
    )
}