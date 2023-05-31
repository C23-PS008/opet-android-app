package com.c23ps008.opet.ui.screen.map_nearby_pet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.theme.OPetTheme
import com.google.maps.android.compose.GoogleMap

object MapNearbyPetDestination : NavigationDestination {
    override val route: String = "map/nearby-pet"
}

@Composable
fun MapNearbyPetScreen(modifier: Modifier = Modifier, onNavigateUp: () -> Unit) {
    MapNearbyPetContent(modifier = modifier, onNavigateUp = onNavigateUp)
}

@Composable
fun MapNearbyPetContent(modifier: Modifier = Modifier, onNavigateUp: () -> Unit) {
    var isMapLoaded by remember { mutableStateOf(false) }

    Scaffold(modifier = modifier, topBar = { MapNearbyTopBar(onNavigateUp = onNavigateUp) }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Map
            GoogleMap(modifier = Modifier.fillMaxSize(), onMapLoaded = { isMapLoaded = true })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapNearbyTopBar(modifier: Modifier = Modifier, onNavigateUp: () -> Unit) {
    CenterAlignedTopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = { Text(text = "Nearby Pets") },
        navigationIcon = {
            IconButton(
                onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.menu_back)
                )
            }
        })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MapNearbyPetContentPreview() {
    OPetTheme {
        MapNearbyPetContent(onNavigateUp = {})
    }
}