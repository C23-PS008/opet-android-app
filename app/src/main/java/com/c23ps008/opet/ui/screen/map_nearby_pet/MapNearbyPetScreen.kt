package com.c23ps008.opet.ui.screen.map_nearby_pet

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.c23ps008.opet.R
import com.c23ps008.opet.data.remote.response.PetAdoptionItem
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.screen.permissions_dialog.LocationPermissionTextProvider
import com.c23ps008.opet.ui.screen.permissions_dialog.PermissionsDialogScreen
import com.c23ps008.opet.ui.screen.permissions_dialog.PermissionsViewModel
import com.c23ps008.opet.ui.theme.OPetTheme
import com.c23ps008.opet.utils.checkLocationSetting
import com.c23ps008.opet.utils.loadIcon
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

object MapNearbyPetDestination : NavigationDestination {
    override val route: String = "map/nearby-pet"
}

@Composable
fun MapNearbyPetScreen(
    modifier: Modifier = Modifier,
    viewModel: NearbyPetViewModel = viewModel(factory = AppViewModelProvider.Factory),
    permissionsViewModel: PermissionsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateUp: () -> Unit,
    navigateToPetDetail: (String) -> Unit,
) {
    val context = LocalContext.current

    val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var permissionGranted by remember {
        mutableStateOf(false)
    }

    val permissionDialogQueue = permissionsViewModel.visiblePermissionDialogQueue

    val requestPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                permissionsViewModel.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
                permissionGranted = perms[permission] == true
            }
        })

    LaunchedEffect(Unit) {
        if (!permissionGranted) {
            requestPermission.launch(permissionsToRequest)
        }
    }

    permissionDialogQueue.reversed().forEach { permission ->
        PermissionsDialogScreen(
            permissionTextProvider = when (permission) {
                Manifest.permission.ACCESS_FINE_LOCATION -> LocationPermissionTextProvider()
                else -> return@forEach
            },
            isPermanentlyDeclined = !ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                permission
            ),
            onDismiss = {
                permissionsViewModel.dismissDialog()
                onNavigateUp()
            },
            onOkClick = {
                permissionsViewModel.dismissDialog()
                requestPermission.launch(permissionsToRequest)
            },
            onGoToAppSettingsClick = {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
                context.startActivity(intent)
            },
        )
    }

    if (permissionGranted) {
        val mapDataState = viewModel.mapDataState.collectAsState(initial = UiState.Loading).value
        MapNearbyPetContent(
            modifier = modifier,
            onNavigateUp = onNavigateUp,
            mapDataState = mapDataState,
            getListPet = { viewModel.getListPet() },
            navigateToPetDetail = { navigateToPetDetail(it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun MapNearbyPetContent(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    mapDataState: UiState<List<PetAdoptionItem>>,
    getListPet: () -> Unit,
    navigateToPetDetail: (String) -> Unit,
) {
    var isLoading by remember { mutableStateOf(false) }
    var listOfNearbyPet by remember {
        mutableStateOf(listOf(PetAdoptionItem()))
    }
    var selectedPet by remember { mutableStateOf(0) }
    val jakartaPos = LatLng(-6.208499708116554, 106.84122912722904)
    val context = LocalContext.current
    var isMapLoaded by remember { mutableStateOf(false) }

    val mapProperties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = true))
    }

    val locationFusedClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var userLocation by remember {
        mutableStateOf(Location("NearbyPetProvider").apply {
            latitude = jakartaPos.latitude
            longitude = jakartaPos.longitude
        })
    }

    val cameraPositionState = rememberCameraPositionState()

    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { activityResult ->
            if (activityResult.resultCode == RESULT_OK)
                Log.d("appDebug", "Accepted")
            else {
                Log.d("appDebug", "Denied")
            }
        })

    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        locationFusedClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                userLocation = location
            }
        }
    }

    LaunchedEffect(userLocation) {
        val cameraPosition = CameraPosition.fromLatLngZoom(
            LatLng(
                userLocation.latitude,
                userLocation.longitude,
            ),
            12f
        )
        cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000)
    }

    when (mapDataState) {
        is UiState.Error -> {
            isLoading = false
            Toast.makeText(context, mapDataState.message, Toast.LENGTH_SHORT).show()
        }

        is UiState.Loading -> {
            isLoading = true
            getListPet()
        }

        is UiState.Success -> {
            isLoading = false
            listOfNearbyPet = mapDataState.data
        }
    }

    DetailBottomSheet(
        petData = listOfNearbyPet[selectedPet],
        scaffoldState = scaffoldState,
        navigateToPetDetail = navigateToPetDetail
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                MapNearbyTopBar(
                    onNavigateUp = onNavigateUp,
                    isLoading = isLoading
                )
            }) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Map
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    onMapLoaded = { isMapLoaded = true },
                    properties = mapProperties,
                    onMyLocationButtonClick = {
                        checkLocationSetting(
                            context,
                            onDisabled = { intentSenderRequest ->
                                settingResultRequest.launch(
                                    intentSenderRequest
                                )
                            },
                            onEnabled = {
                                locationFusedClient.lastLocation.addOnSuccessListener { location ->
                                    if (location != null) {
                                        userLocation = location
                                    }
                                }
                            })
                        return@GoogleMap true
                    },
                    cameraPositionState = cameraPositionState
                ) {
                    listOfNearbyPet.forEachIndexed { index, pet ->
                        if (pet.lat != null && pet.lon != null) {
                            MapMarker(petData = pet, onClick = {
                                selectedPet = index
                                coroutineScope.launch {
                                    scaffoldState.bottomSheetState.expand()
                                }
                                return@MapMarker true
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MapMarker(petData:  PetAdoptionItem, onClick: (Marker) -> Boolean) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current


    var icon by remember {mutableStateOf<BitmapDescriptor?>(null)}
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val width = with(density) {48.dp.toPx()}
            val height = with(density) {48.dp.toPx()}
            icon = loadIcon(context, petData.photoUrl, width.toInt(), height.toInt())
        }
    }
    Marker(
        state = MarkerState(
            position = LatLng(
                petData.lat as Double,
                petData.lon as Double
            )
        ),
        title = petData.name,
        snippet = petData.name,
        onClick = onClick,
        icon = icon
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBottomSheet(
    modifier: Modifier = Modifier,
    petData: PetAdoptionItem,
    scaffoldState: BottomSheetScaffoldState,
    navigateToPetDetail: (String) -> Unit,
    content: @Composable () -> Unit,
) {
    BottomSheetScaffold(
        modifier = modifier,
        sheetPeekHeight = 0.dp,
        scaffoldState = scaffoldState,
        sheetContent = {
            Row(
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .width(100.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    painter = rememberAsyncImagePainter(model = petData.photoUrl),
                    contentDescription = null
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = petData.name.toString(),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = petData.breed.toString(),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                    FilledTonalButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { navigateToPetDetail(petData.petId.toString()) }) {
                        Text(text = "View Detail")
                    }
                }
            }
        }
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapNearbyTopBar(modifier: Modifier = Modifier, onNavigateUp: () -> Unit, isLoading: Boolean) {
    CenterAlignedTopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = { Text(text = "Nearby Pets") },
        navigationIcon = {
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
            if (isLoading) {
                CircularProgressIndicator()
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MapNearbyPetContentPreview() {
    OPetTheme {
        MapNearbyPetContent(
            onNavigateUp = {},
            mapDataState = UiState.Loading,
            getListPet = {},
            navigateToPetDetail = {})
    }
}