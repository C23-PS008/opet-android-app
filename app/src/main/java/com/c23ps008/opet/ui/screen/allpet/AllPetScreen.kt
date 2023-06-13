package com.c23ps008.opet.ui.screen.allpet

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c23ps008.opet.R
import com.c23ps008.opet.data.FakeDataSource
import com.c23ps008.opet.ui.components.PetCard
import com.c23ps008.opet.ui.components.PetCardState
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.theme.OPetTheme

object AllPetDestination : NavigationDestination {
    override val route: String = "pets"
}

@Composable
fun AllPetScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    navigateToDetail: (String) -> Unit,
) {
    AllPetScreenContent(modifier = modifier, onNavigateUp = onNavigateUp, navigateToDetail = navigateToDetail)
}

@Composable
fun AllPetScreenContent(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    navigateToDetail: (String) -> Unit,
) {
    val fakeData = FakeDataSource.dummyHomePetResources
    Scaffold(modifier = modifier, topBar = { AllPetTopBar(onNavigateUp = onNavigateUp) }) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp, top = 12.dp)
        ) {
            items(fakeData, key = { it.id }) {
                val cardState =
                    PetCardState(it.id, it.pet_image, it.pet_breed, it.name, it.pet_address)
                PetCard(data = cardState, onClick = { navigateToDetail(it.id) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllPetTopBar(modifier: Modifier = Modifier, onNavigateUp: () -> Unit) {
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
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.menu_search)
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.menu_more)
                    )
                }
            }
        )
        Spacer(modifier = Modifier.padding(bottom = 12.dp))
        PetFilter()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetFilter(modifier: Modifier = Modifier) {
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
            FilterChip(selected = false, onClick = { /*TODO*/ }, leadingIcon = {
                Icon(
                    painter = filterList[it].icon,
                    contentDescription = filterList[it].value
                )
            }, label = { Text(text = filterList[it].label) })
        }
    }
}

data class FilterChipStat(val icon: Painter, val label: String, val value: String)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AllPetScreenContentPreview() {
    OPetTheme {
        AllPetScreenContent(onNavigateUp = {}, navigateToDetail = {})
    }
}