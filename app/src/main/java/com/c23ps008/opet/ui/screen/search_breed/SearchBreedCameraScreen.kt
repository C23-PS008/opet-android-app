package com.c23ps008.opet.ui.screen.search_breed

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.CameraEnhance
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.screen.permissions_dialog.CameraPermissionTextProvider
import com.c23ps008.opet.ui.screen.permissions_dialog.PermissionsDialogScreen
import com.c23ps008.opet.ui.screen.permissions_dialog.PermissionsViewModel

object SearchBreedCameraDestination: NavigationDestination {
    override val route: String = "search-breed-camera"
}

@Composable
fun SearchBreedCameraScreen(
    onCaptureSuccess: (String) -> Unit,
    onNavigateUp: () -> Unit,
    permissionViewModel: PermissionsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val permissionsToRequest =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val permissionDialogQueue = permissionViewModel.visiblePermissionDialogQueue

    val requestPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                permissionViewModel.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
            }
        }
    )
    val context = LocalContext.current

    val imageCapture by remember {
        mutableStateOf(
            ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build()
        )
    }

    LaunchedEffect(Unit) {
        requestPermission.launch(permissionsToRequest)
    }


    permissionDialogQueue.reversed().forEach { permission ->
        PermissionsDialogScreen(
            permissionTextProvider = when (permission) {
                Manifest.permission.CAMERA -> CameraPermissionTextProvider()
                else -> return@forEach
            },
            isPermanentlyDeclined = !ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                permission
            ),
            onDismiss = {
                permissionViewModel.dismissDialog()
                onNavigateUp()
            },
            onOkClick = {
                permissionViewModel.dismissDialog()
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

    val onCaptureImage: () -> Unit = {
        val timestamp = System.currentTimeMillis()
        val imageFileName = "OPet_IMG_$timestamp.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/OPet")
            }
        }

        val imageCaptureOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        imageCapture.takePicture(
            imageCaptureOptions, ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri
                    Log.d("TEST", "Photo capture succeeded: $savedUri")
                    onCaptureSuccess(savedUri.toString())
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        context,
                        "Image capture failed ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("TEST", "Image capture failed: ${exception.message}", exception)
                }

            }
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                val selectedImgUri = result.data?.data
                Log.d("TESTS", "selectedImg: $selectedImgUri")
                onCaptureSuccess(selectedImgUri.toString())
            }
        })

    val pickImageFromGallery = {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose an image")
        galleryLauncher.launch(chooser)
    }

    TakeCameraLayout(
        onNavigateUp = onNavigateUp,
        onCaptureImage = onCaptureImage,
        pickImageFromGallery = pickImageFromGallery
    ) {
        CameraPreview(imageCapture = imageCapture)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TakeCameraLayout(
    modifier: Modifier = Modifier,
    onCaptureImage: () -> Unit,
    pickImageFromGallery: () -> Unit,
    onNavigateUp: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            content()
        }
        CenterAlignedTopAppBar(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            windowInsets = WindowInsets(16.dp, 4.dp, 16.dp, 4.dp),
            title = { Text(text = "Take Image", color = Color.White) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
            navigationIcon = {
                FilledTonalIconButton(
                    onClick = onNavigateUp
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.menu_back)
                    )
                }
            },
            actions = {
                FilledTonalIconButton(onClick = { pickImageFromGallery() }) {
                    Icon(
                        imageVector = Icons.Outlined.AddPhotoAlternate,
                        contentDescription = stringResource(
                            R.string.add_from_gallery
                        )
                    )
                }
            }
        )
        FilledIconButton(
            onClick = onCaptureImage, modifier = Modifier
                .size(92.dp)
                .align(
                    Alignment.BottomCenter
                )
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.CameraEnhance,
                contentDescription = stringResource(R.string.take_image)
            )
        }
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp)
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.__1_ratio_helper),
            contentDescription = null
        )

    }
}

@Composable
private fun CameraPreview(imageCapture: ImageCapture?) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProvideFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f),
        factory = { ctx ->
            val preview = PreviewView(ctx).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
            val executor = ContextCompat.getMainExecutor(ctx)
            cameraProvideFuture.addListener({
                val cameraProvider = cameraProvideFuture.get()
                bindPreview(
                    lifecycleOwner,
                    preview,
                    imageCapture,
                    cameraProvider,
                )
            }, executor)
            preview
        },
    )
}

private fun bindPreview(
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    imageCapture: ImageCapture?,
    cameraProvider: ProcessCameraProvider,
) {
    val preview = Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3).build().also {
        it.setSurfaceProvider(previewView.surfaceProvider)
    }

    try {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            imageCapture
        )
    } catch (e: Exception) {
        Log.e("POSTCAMERA", "Camera x binding failed.", e)
    }
}