package com.c23ps008.opet.ui.screen.pet_detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.c23ps008.opet.R
import com.c23ps008.opet.data.remote.response.PetDetailData
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.theme.OPetTheme
import com.c23ps008.opet.utils.getAddressName
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

object PetDetailDestination : NavigationDestination {
    override val route: String = "pet-detail"
    const val petIdArg = "petId"
    val routeWithArgs = "$route/{$petIdArg}"
}

@Composable
fun PetDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: PetDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateUp: () -> Unit,
) {
    LocalContext.current

    viewModel.detailState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> {
                PetDetailErrorContent(message = uiState.message, onNavigateUp = onNavigateUp)
            }

            is UiState.Loading -> {
                viewModel.getDetail()
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is UiState.Success -> {
                PetDetailContent(
                    modifier = modifier,
                    onNavigateUp = onNavigateUp,
                    petDetailData = uiState.data.data
                )
            }
        }
    }
}

@Composable
fun PetDetailContent(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    petDetailData: PetDetailData?,
) {
    val context = LocalContext.current
    Scaffold(
        modifier = modifier,
        floatingActionButton = { BottomAction(petDetailData = petDetailData) },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop,
                    painter = rememberAsyncImagePainter(model = petDetailData?.photoUrl),
                    contentDescription = stringResource(
                        R.string.pet_for_adoption_image
                    )
                )
                FilledTonalIconButton(
                    onClick = onNavigateUp, modifier = Modifier
                        .padding(16.dp)
                        .align(
                            Alignment.TopStart
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.menu_back)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(bottom = 12.dp))
            Column {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = petDetailData?.name.toString(),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = petDetailData?.breed.toString(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "${petDetailData?.age} • ${petDetailData?.gender}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = petDetailData?.about.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Location",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    if (petDetailData?.lat != null) {
                        Text(
                            text = getAddressName(
                                context,
                                petDetailData.lat as Double,
                                petDetailData.lon as Double
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                if (petDetailData?.lat != null) {
                    PetDetailMapLocation(
                        lat = petDetailData.lat as Double,
                        lon = petDetailData.lon as Double,
                        petName = petDetailData.name.toString()
                    )
                }
            }
        }
    }
}

@Composable
fun PetDetailMapLocation(modifier: Modifier = Modifier, lat: Double, lon: Double, petName: String) {
    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(Unit) {
        val cameraPosition = CameraPosition.fromLatLngZoom(LatLng(lat, lon), 10f)
        cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000)
    }

    val openGoogleMap = {
        val uri = Uri.parse("geo:$lat,$lon?q=$lat,$lon($petName)")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if(mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        }
    }
    GoogleMap(
        modifier = modifier
            .fillMaxSize()
            .height(240.dp),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            tiltGesturesEnabled = false,
            zoomGesturesEnabled = false,
            zoomControlsEnabled = false,
            scrollGesturesEnabled = false,
            scrollGesturesEnabledDuringRotateOrZoom = false
        ),
        onMapClick = {
            openGoogleMap()
        }
    ) {
        Marker(state = MarkerState(LatLng(lat, lon)))
    }
}

@Composable
fun BottomAction(modifier: Modifier = Modifier,  petDetailData: PetDetailData?) {
    ElevatedCard(
        modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(12.dp)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Contact",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = petDetailData?.user?.name.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                FilledIconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = "")
                }
                FilledIconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "")
                }
            }
        }
    }
}

@Composable
fun PetDetailErrorContent(
    modifier: Modifier = Modifier,
    message: String,
    onNavigateUp: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = stringResource(R.string.error),
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.padding(bottom = 48.dp))
            Button(onClick = { onNavigateUp() }) {
                Text(text = "Go Back", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PetNotFoundContentPreview() {
    OPetTheme {
        PetDetailErrorContent(message = "Error Message | Not Found", onNavigateUp = {})
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PetDetailContentPreview() {
    OPetTheme {
        PetDetailContent(onNavigateUp = {}, petDetailData = null)
    }
}