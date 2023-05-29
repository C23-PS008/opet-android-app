package com.c23ps008.opet.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c23ps008.opet.R
import com.c23ps008.opet.data.FakeDataSource
import com.c23ps008.opet.ui.components.CustomGridLayout
import com.c23ps008.opet.ui.components.OPetNavigationBar
import com.c23ps008.opet.ui.components.PetCard
import com.c23ps008.opet.ui.components.PetCardState
import com.c23ps008.opet.ui.components.SectionHeader
import com.c23ps008.opet.ui.components.SectionOverviewNoData
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.theme.OPetTheme

object HomeDestination : NavigationDestination {
    override val route: String = "home"
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToProfile: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    navigateToViewAllPet: () -> Unit,
    navigateToMyPost: () -> Unit,
) {
    HomeContent(
        modifier = modifier,
        navigateToProfile = navigateToProfile,
        navigateToDetail = navigateToDetail,
        navigateToViewAllPet = navigateToViewAllPet,
        navigateToMyPost = navigateToMyPost,
    )
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    navigateToProfile: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    navigateToViewAllPet: () -> Unit,
    navigateToMyPost: () -> Unit,
) {
    val fakeData = FakeDataSource.dummyHomePetResources
    Scaffold(bottomBar = {
        OPetNavigationBar(
            currentRoute = HomeDestination.route,
            navigateToMyPost = navigateToMyPost
        )
    }) { paddingValues ->
        Column(
            modifier = modifier
                .padding(bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy((-1).dp)
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 20.dp, top = 18.dp)
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.TopCenter),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            HomeHeader(navigateToProfile = navigateToProfile)
                            HomeSearchBar()
                        }
                    }
                    HomeExploreMenu()
                }
                SectionHeader(
                    label = stringResource(R.string.pets_for_adoption),
                    onViewAll = navigateToViewAllPet
                )
                if (fakeData.isEmpty()) {
                    SectionOverviewNoData(message = stringResource(R.string.no_pets_for_adoption_found))
                } else {
                    CustomGridLayout(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        data = fakeData,
                        columns = 2,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val cardData =
                            PetCardState(
                                it.id,
                                it.pet_image,
                                it.pet_breed,
                                it.name,
                                it.pet_address
                            )
                        PetCard(
                            data = cardData,
                            modifier = Modifier.weight(1f),
                            onClick = { navigateToDetail(it.id) })
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeContentPreview() {
    OPetTheme {
        HomeContent(
            navigateToProfile = {},
            navigateToDetail = {},
            navigateToViewAllPet = {},
            navigateToMyPost = {})
    }
}