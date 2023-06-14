package com.c23ps008.opet.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.c23ps008.opet.R
import com.c23ps008.opet.data.remote.response.PetAdoptionItem
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.ui.components.CustomGridLayout
import com.c23ps008.opet.ui.components.FindYourMatchCard
import com.c23ps008.opet.ui.components.OPetNavigationBar
import com.c23ps008.opet.ui.components.PetCard
import com.c23ps008.opet.ui.components.PetCardState
import com.c23ps008.opet.ui.components.SectionHeader
import com.c23ps008.opet.ui.components.SectionOverviewNoData
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.theme.OPetTheme
import com.c23ps008.opet.utils.getCityName

object HomeDestination : NavigationDestination {
    override val route: String = "home"
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToProfile: () -> Unit,
    navigateToMapNearby: () -> Unit,
    navigateToDetail: (String) -> Unit,
    navigateToViewAllPet: () -> Unit,
    navigateToPostPet: () -> Unit,
    navigateToMyPost: () -> Unit,
    navigateToFindMatchDog: () -> Unit,
    navigateToFindMatchCat: () -> Unit,
    navigateToSearchBreed: () -> Unit,
) {
    val petsForAdoptionState = viewModel.petAdoptionState.collectAsState(initial = UiState.Loading)
    val petsForAdoptionData = petsForAdoptionState.value

    HomeContent(
        modifier = modifier,
        navigateToProfile = navigateToProfile,
        navigateToMapNearby = navigateToMapNearby,
        navigateToDetail = navigateToDetail,
        navigateToViewAllPet = navigateToViewAllPet,
        navigateToPostPet = navigateToPostPet,
        navigateToMyPost = navigateToMyPost,
        navigateToFindMatchDog = navigateToFindMatchDog,
        navigateToFindMatchCat = navigateToFindMatchCat,
        navigateToSearchBreed = navigateToSearchBreed,
        petsForAdoptionData = petsForAdoptionData,
        getAllPetAdoption = { viewModel.getAllPetAdoption() },
    )
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    navigateToProfile: () -> Unit,
    navigateToMapNearby: () -> Unit,
    navigateToDetail: (String) -> Unit,
    navigateToViewAllPet: () -> Unit,
    navigateToPostPet: () -> Unit,
    navigateToMyPost: () -> Unit,
    navigateToFindMatchDog: () -> Unit,
    navigateToFindMatchCat: () -> Unit,
    navigateToSearchBreed: () -> Unit,
    petsForAdoptionData: UiState<List<PetAdoptionItem?>>,
    getAllPetAdoption: () -> Unit,
) {
    val context = LocalContext.current
    Scaffold(bottomBar = {
        OPetNavigationBar(
            currentRoute = HomeDestination.route,
            navigateToMyPost = navigateToMyPost,
            navigateToPostPet = navigateToPostPet
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
                            HomeSearchBar(onClick = navigateToSearchBreed)
                        }
                    }
                    HomeExploreMenu(navigateToMapNearby = navigateToMapNearby)
                }
                SectionHeader(label = "Find Your Match", onViewAll = {}, showAllNav = false)
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(200.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FindYourMatchCard(
                        modifier = Modifier.weight(1f),
                        backgroundPainter = painterResource(id = R.drawable.find_your_match_dog),
                        label = stringResource(
                            id = R.string.dog
                        ),
                        onClick = {navigateToFindMatchDog() }
                    )
                    FindYourMatchCard(
                        modifier = Modifier.weight(1f),
                        backgroundPainter = painterResource(id = R.drawable.find_your_match_cat),
                        label = stringResource(
                            id = R.string.cat
                        ),
                        onClick = { navigateToFindMatchCat() }
                    )
                }
                SectionHeader(
                    label = stringResource(R.string.pets_for_adoption),
                    onViewAll = navigateToViewAllPet
                )
                when (petsForAdoptionData) {
                    is UiState.Error -> {
                        Toast.makeText(context, petsForAdoptionData.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is UiState.Loading -> {
                        getAllPetAdoption()
                        Box(modifier = Modifier.fillMaxWidth()) {
                            CircularProgressIndicator(modifier.align(Alignment.Center))
                        }
                    }

                    is UiState.Success -> {
                        val listOfData = petsForAdoptionData.data
                        if (listOfData.isEmpty()) {
                            SectionOverviewNoData(message = stringResource(R.string.no_pets_for_adoption_found))
                        } else {
                            CustomGridLayout(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                data = listOfData,
                                columns = 2,
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                val cardData =
                                    PetCardState(
                                        it?.petId.toString(),
                                        it?.photoUrl.toString(),
                                        it?.breed.toString(),
                                        it?.name.toString(),
                                        getCityName(context, it?.lat as Double, it.lon as Double)
                                    )
                                PetCard(
                                    data = cardData,
                                    modifier = Modifier.weight(1f),
                                    onClick = { navigateToDetail(it.petId.toString()) })
                            }
                        }
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
            navigateToMapNearby = {},
            navigateToDetail = {},
            navigateToViewAllPet = {},
            navigateToPostPet = {},
            navigateToMyPost = {},
            navigateToFindMatchDog = {},
            navigateToFindMatchCat = {},
            navigateToSearchBreed = {},
            petsForAdoptionData = UiState.Loading,
            getAllPetAdoption = {}
        )
    }
}