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
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.theme.OPetTheme

object PostPetDestination : NavigationDestination {
    override val route: String = "post-pet"
}

@Composable
fun PostPetScreen() {
    PostPetContent()
}

@Composable
fun PostPetContent(modifier: Modifier = Modifier) {
    val petType: PetTypeState = PetTypeState.Dog
    Scaffold(bottomBar = { BottomAction() }) { paddingValues ->
        when(petType) {
            is PetTypeState.Cat -> {
                CatPostEntry(
                    modifier
                        .padding(paddingValues)
                )
            }
            is PetTypeState.Dog -> {
                DogPostEntry(
                    modifier.padding(paddingValues)
                )
            }
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

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PostPetScreenPreview() {
    OPetTheme {
        PostPetContent()
    }
}