package com.c23ps008.opet.ui.screen.map_nearby_pet

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.screen.permissions_dialog.LocationPermissionTextProvider
import com.c23ps008.opet.ui.screen.permissions_dialog.PermissionsDialogScreen
import com.c23ps008.opet.ui.screen.permissions_dialog.PermissionsViewModel
import com.c23ps008.opet.ui.theme.OPetTheme
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState

object MapNearbyPetDestination : NavigationDestination {
    override val route: String = "map/nearby-pet"
}

@Composable
fun MapNearbyPetScreen(
    modifier: Modifier = Modifier,
    permissionsViewModel: PermissionsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateUp: () -> Unit,
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
                Manifest.permission.ACCESS_COARSE_LOCATION -> LocationPermissionTextProvider()
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
        MapNearbyPetContent(
            modifier = modifier,
            onNavigateUp = onNavigateUp,
        )
    }
}

@SuppressLint("MissingPermission")
@Composable
fun MapNearbyPetContent(modifier: Modifier = Modifier, onNavigateUp: () -> Unit) {
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
            // -6.208499708116554, 106.84122912722904
            latitude = -6.208499708116554
            longitude = 106.84122912722904
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

    Scaffold(
        modifier = modifier,
        topBar = { MapNearbyTopBar(onNavigateUp = onNavigateUp) }) { paddingValues ->
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
            )
        }
    }
}

private fun checkLocationSetting(
    context: Context,
    onDisabled: (IntentSenderRequest) -> Unit,
    onEnabled: () -> Unit,
) {
    val locationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
    val client: SettingsClient = LocationServices.getSettingsClient(context)
    val builder: LocationSettingsRequest.Builder =
        LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val gpsSettingTask =
        client.checkLocationSettings(builder.build())
    gpsSettingTask.addOnSuccessListener { onEnabled() }
    gpsSettingTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                onDisabled(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                Toast.makeText(context, "Error: ${sendEx.message}", Toast.LENGTH_SHORT).show()
            }
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
                onClick = onNavigateUp
            ) {
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