package com.c23ps008.opet.ui.screen.post_pet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.theme.OPetTheme

object PostPetDestination : NavigationDestination {
    override val route: String = "post-pet"
    const val petTypeArg = "petType"
    const val petBreedArg = "petBreed"
    const val imageUriArg = "imageUri"
    val routeWithArgs = "$route/{$petTypeArg}/{$petBreedArg}/{$imageUriArg}"
}

@Composable
fun PostPetScreen(viewModel: PostPetViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val petTypeState: PetTypeState = when (viewModel.petType) {
        "cat" -> PetTypeState.Cat
        "dog" -> PetTypeState.Dog
        else -> PetTypeState.Unknown
    }
    PostPetContent(
        petTypeState = petTypeState,
        defaultPetBreed = viewModel.petBreed,
        imageUri = viewModel.imageUri
    )
}

@Composable
fun PostPetContent(
    modifier: Modifier = Modifier,
    petTypeState: PetTypeState,
    defaultPetBreed: String,
    imageUri: String
) {
    
    Scaffold(bottomBar = { BottomAction() }) { paddingValues ->
        when (petTypeState) {
            is PetTypeState.Cat -> {
                CatPostEntry(
                    modifier
                        .padding(paddingValues),
                    defaultPetBreed = defaultPetBreed,
                    imageUri = imageUri
                )
            }

            is PetTypeState.Dog -> {
                DogPostEntry(
                    modifier.padding(paddingValues),
                    defaultPetBreed = defaultPetBreed,
                    imageUri = imageUri
                )
            }

            else -> {}
        }
    }
}

@Composable
fun BottomAction(modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilledTonalButton(modifier = Modifier
            .weight(1f), onClick = { /*TODO*/ }) {
            Text(text = stringResource(R.string.cancel))
        }
        Button(modifier = Modifier.weight(1f), onClick = { /*TODO*/ }) {
            Text(text = stringResource(R.string.post))
        }
    }
}

@Preview
@Composable
fun PostPetContentPreview() {
    OPetTheme {
        PostPetContent(petTypeState = PetTypeState.Cat, defaultPetBreed = "", imageUri = "")
    }
}