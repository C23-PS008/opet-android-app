package com.c23ps008.opet.ui.screen.post_pet

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.c23ps008.opet.R
import com.c23ps008.opet.data.PetBreedData
import com.c23ps008.opet.data.formdata.PostPetFormData
import com.c23ps008.opet.ui.components.OPetSegmentedButton
import com.c23ps008.opet.ui.components.SegmentedItem
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.screen.permissions_dialog.LocationPermissionTextProvider
import com.c23ps008.opet.ui.screen.permissions_dialog.PermissionsDialogScreen
import com.c23ps008.opet.ui.screen.permissions_dialog.PermissionsViewModel
import com.c23ps008.opet.ui.theme.OPetTheme
import com.c23ps008.opet.utils.checkLocationSetting
import com.c23ps008.opet.utils.getAddressName
import com.google.android.gms.location.LocationServices
import java.io.File

object PostPetDestination : NavigationDestination {
    override val route: String = "post-pet"
    const val imageUriArg = "imageUri"
    val routeWithArgs = "$route/{$imageUriArg}"
}

@Composable
fun PostPetScreen(
    viewModel: PostPetViewModel = viewModel(factory = AppViewModelProvider.Factory),
    permissionsViewModel: PermissionsViewModel = viewModel(factory = AppViewModelProvider.Factory),
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

    if(permissionGranted) {
        PostPetContent(
            imageUri = viewModel.imageUri
        )
    }

}

@Composable
fun PostPetContent(
    modifier: Modifier = Modifier,
    imageUri: String,
) {
    var formData by remember { mutableStateOf(PostPetFormData(image = File(imageUri))) }
    Scaffold(modifier = modifier, bottomBar = { BottomAction() }) { paddingValues ->
        PostPetEntry(
            modifier = Modifier.padding(paddingValues),
            imageUri = imageUri,
            formData = formData,
            onFormDataChange = { formData = it }
        )
    }
}

@Composable
@SuppressLint("MissingPermission")
fun PostPetEntry(
    modifier: Modifier = Modifier,
    imageUri: String,
    formData: PostPetFormData,
    onFormDataChange: (PostPetFormData) -> Unit,
) {
    val context = LocalContext.current

    val genderItems =
        listOf(
            SegmentedItem(label = "Unknown", value = "Unknown"),
            SegmentedItem(label = "Male", value = "Male"),
            SegmentedItem(label = "Female", value = "Female")
        )

    val catAgeList = listOf(
        SegmentedItem(label = "Kitten", value = "Kitten"),
        SegmentedItem(label = "Young", value = "Young"),
        SegmentedItem(label = "Adult", value = "Adult"),
    )

    val dogAgeList = listOf(
        SegmentedItem(label = "Puppy", value = "Puppy"),
        SegmentedItem(label = "Young", value = "Young"),
        SegmentedItem(label = "Adult", value = "Adult"),
    )

    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK)
                Log.d("appDebug", "Accepted")
            else {
                Log.d("appDebug", "Denied")
            }
        })

    val petCategoryLabel = listOf("", "Cat", "Dog")
    var petBreedsOptions by remember { mutableStateOf(listOf<String>()) }

    var ageIndex by remember { mutableStateOf(0) }

    val locationFusedClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    LaunchedEffect(formData.petCategory) {
        formData.apply {
            breed = ""
        }
        onFormDataChange(formData)
        if (formData.petCategory != null) {
            when (petCategoryLabel[formData.petCategory as Int]) {
                "Cat" -> petBreedsOptions = PetBreedData.getCatBreed()
                "Dog" -> petBreedsOptions = PetBreedData.getDogBreed()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.padding(bottom = 16.dp)) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                painter = rememberAsyncImagePainter(model = Uri.parse(imageUri)),
                contentDescription = "null",
                contentScale = ContentScale.Crop
            )
            FilledTonalIconButton(onClick = { /*TODO*/ }, modifier = Modifier.padding(16.dp)) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.menu_back)
                )
            }
        }
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SelectInputPetType(
                petType = petCategoryLabel[if (formData.petCategory == null) 0 else formData.petCategory as Int],
                onValueChange = {
                    when (it) {
                        "Cat" -> {
                            onFormDataChange(formData.copy(petCategory = 1))
                        }

                        "Dog" -> {
                            onFormDataChange(formData.copy(petCategory = 2))
                        }
                    }
                })
            if (formData.petCategory != null) {
                Spacer(modifier = Modifier.padding(bottom = 28.dp))
                FormSectionHeader(text = stringResource(id = R.string.lets_describe_the_pet))
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    InputPetName(
                        label = stringResource(
                            R.string.pet_name,
                            if (formData.petCategory == null) stringResource(id = R.string.pet) else petCategoryLabel[formData.petCategory as Int]
                        ),
                        value = formData.name,
                        onValueChange = {
                            onFormDataChange(formData.copy(name = it))
                        }
                    )
                    SelectInputPetBreed(
                        label = stringResource(
                            id = R.string.select_pet_breed,
                            if (formData.petCategory == null) stringResource(id = R.string.pet) else petCategoryLabel[formData.petCategory as Int]
                        ),
                        value = formData.breed,
                        onValueChange = {
                            onFormDataChange(formData.copy(breed = it))
                        },
                        options = petBreedsOptions
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        FormNormalLabel(text = stringResource(R.string.gender))
                        OPetSegmentedButton(items = genderItems, onOptionSelected = {
                            onFormDataChange(formData.copy(gender = genderItems[it].value))
                        })
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        FormNormalLabel(text = stringResource(R.string.age))
                        OPetSegmentedButton(
                            items = if (petCategoryLabel[if (formData.petCategory != null) formData.petCategory as Int else 0] == "Cat") catAgeList else dogAgeList,
                            onOptionSelected = {
                                ageIndex = it
                                if (formData.petCategory != null) {
                                    if (petCategoryLabel[formData.petCategory as Int] == "Cat") {
                                        onFormDataChange(formData.copy(age = catAgeList[ageIndex].value))
                                    } else {
                                        onFormDataChange(formData.copy(age = dogAgeList[ageIndex].value))
                                    }
                                }
                            })
                    }
                    InputPetAbout(value = formData.about, onValueChange = {
                        onFormDataChange(formData.copy(about = it))
                    })
                }
                Spacer(modifier = Modifier.padding(bottom = 28.dp))
                // FormSectionHeader(
                //     text = stringResource(R.string.contact_me),
                // )
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // InputContactName(value = contactName, onValueChange = { contactName = it })
                    // InputPhoneNumber(value = phoneNumber, onValueChange = { phoneNumber = it })
                    InputLocation(value = if(formData.lat != null && formData.lon != null) getAddressName(context, formData.lat as Double, formData.lon as Double) else "",
                        onClick = {
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
                                            onFormDataChange(
                                                formData.copy(
                                                    lat = location.latitude,
                                                    lon = location.longitude
                                                )
                                            )
                                        }
                                    }
                                })
                        })
                }
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

@Preview
@Composable
fun PostPetContentPreview() {
    OPetTheme {
        PostPetContent(imageUri = "")
    }
}