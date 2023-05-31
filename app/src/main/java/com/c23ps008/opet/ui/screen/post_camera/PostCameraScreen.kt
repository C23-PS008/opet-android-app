package com.c23ps008.opet.ui.screen.post_camera

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
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
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
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.screen.permissions_dialog.CameraPermissionTextProvider
import com.c23ps008.opet.ui.screen.permissions_dialog.PermissionsDialogScreen
import com.c23ps008.opet.ui.screen.permissions_dialog.PermissionsViewModel
import com.c23ps008.opet.ui.theme.OPetTheme

object PostCameraDestination : NavigationDestination {
    override val route: String = "take-picture-for-post"
}

@Composable
fun PostCameraScreen(
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
            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
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

    TakeCameraLayout(onNavigateUp = onNavigateUp, onCaptureImage = onCaptureImage) {
        CameraPreview(imageCapture = imageCapture)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TakeCameraLayout(
    modifier: Modifier = Modifier,
    onCaptureImage: () -> Unit,
    onNavigateUp: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Box(modifier = Modifier.align(Alignment.Center)) {
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
            })
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
            modifier = Modifier.fillMaxWidth().aspectRatio(1f).padding(16.dp).align(Alignment.Center),
            painter = painterResource(id = R.drawable.__1_ratio_helper),
            contentDescription = null
        )

    }
}

@Composable
private fun CameraPreview(modifier: Modifier = Modifier, imageCapture: ImageCapture?) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProvideFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    Box(
        modifier
            .fillMaxSize()
            .aspectRatio(1f)
    ) {
        AndroidView(
            factory = { ctx ->
                val preview = PreviewView(ctx).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                    layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
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
            modifier = modifier
        )
    }
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

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
fun TakeCameraPreview() {
    OPetTheme {
        TakeCameraLayout(
            onNavigateUp = {},
            content = {},
            onCaptureImage = {}
        )
    }
}
