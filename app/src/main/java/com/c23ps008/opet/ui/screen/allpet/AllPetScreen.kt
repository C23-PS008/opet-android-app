package com.c23ps008.opet.ui.screen.allpet

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.c23ps008.opet.ui.theme.OPetTheme
import com.c23ps008.opet.utils.getCityName

object AllPetDestination : NavigationDestination {
    override val route: String = "pets"
}

@Composable
fun AllPetScreen(
    modifier: Modifier = Modifier,
    viewModel: AllPetViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateUp: () -> Unit,
    navigateToDetail: (String) -> Unit,
    navigateToSearchBreed: () -> Unit,
) {
    val petType = viewModel.petType.collectAsState(initial = "all")
    val lazyPetAdoptionList = viewModel.pagingSource.collectAsLazyPagingItems()

    AllPetScreenContent(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        navigateToDetail = navigateToDetail,
        lazyData = lazyPetAdoptionList,
        petType = petType.value,
        onPetTypeChange = {
            viewModel.updatePetType(it)
        },
        navigateToSearchBreed = navigateToSearchBreed
    )
}

@Composable
fun AllPetScreenContent(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    navigateToDetail: (String) -> Unit,
    lazyData: LazyPagingItems<PetAdoptionItem>?,
    petType: String = "all",
    onPetTypeChange: (String) -> Unit,
    navigateToSearchBreed: () -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        topBar = {
            AllPetTopBar(
                onNavigateUp = onNavigateUp,
                petType = petType,
                onPetTypeChange = onPetTypeChange,
                navigateToSearchBreed = navigateToSearchBreed
            )
        }) { paddingValues ->
        Box(modifier = modifier.fillMaxSize().padding(paddingValues)) {
            if (lazyData?.loadState?.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else
                if (lazyData?.itemCount == 0 && lazyData.loadState.append !is LoadState.Loading) {
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
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                    ) {
                        if (lazyData != null) {
                            items(count = lazyData.itemCount, key = lazyData.itemKey()) { index ->
                                val data = lazyData[index]
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
                        if (lazyData?.loadState?.append == LoadState.Loading) {
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
fun AllPetTopBar(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    petType: String,
    onPetTypeChange: (String) -> Unit,
    navigateToSearchBreed: () -> Unit,
) {
    Column(modifier = modifier) {
        TopAppBar(title = { Text(text = "Pets for Adoption") }, navigationIcon = {
            IconButton(
                onClick = onNavigateUp
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.menu_back)
                )
            }
        },
            actions = {
                IconButton(onClick = { navigateToSearchBreed() }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.menu_search)
                    )
                }
            }
        )
        Spacer(modifier = Modifier.padding(bottom = 12.dp))
        PetFilter(petType = petType, onPetTypeChange = onPetTypeChange)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetFilter(modifier: Modifier = Modifier, petType: String, onPetTypeChange: (String) -> Unit) {
    val filterList = listOf(
        FilterChipStat(
            icon = painterResource(id = R.drawable.paw_prints),
            label = "All",
            value = "all"
        ),
        FilterChipStat(
            icon = painterResource(id = R.drawable.cat_face),
            label = "Cat",
            value = "cat"
        ),
        FilterChipStat(
            icon = painterResource(id = R.drawable.dog_face),
            label = "Dog",
            value = "dog"
        )
    )

    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(filterList.size) {
            FilterChip(
                selected = petType == filterList[it].value,
                onClick = { onPetTypeChange(filterList[it].value) },
                leadingIcon = {
                    Icon(
                        painter = filterList[it].icon,
                        contentDescription = filterList[it].value
                    )
                },
                label = { Text(text = filterList[it].label) })
        }
    }
}

data class FilterChipStat(val icon: Painter, val label: String, val value: String)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AllPetScreenContentPreview() {
    OPetTheme {
        AllPetScreenContent(
            onNavigateUp = {},
            navigateToDetail = {},
            lazyData = null,
            onPetTypeChange = {},
            navigateToSearchBreed = {}
        )
    }
}