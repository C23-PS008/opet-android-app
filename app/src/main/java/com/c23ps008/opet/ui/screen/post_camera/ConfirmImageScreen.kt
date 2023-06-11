package com.c23ps008.opet.ui.screen.post_camera

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider

object ConfirmImageDestination : NavigationDestination {
    override val route: String = "confirm-image-for-post"
    const val imgUriArg = "imgUri"
    val routeWithArgs = "$route/{$imgUriArg}"
}

@Composable
fun ConfirmImageScreen(
    modifier: Modifier = Modifier,
    viewModel: ConfirmImageViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateUp: () -> Unit,
    navigateToProcessingImage: (String) -> Unit
) {
    ConfirmImageLayout(modifier = modifier, imgUri = viewModel.imageUri, onNavigateUp = onNavigateUp, navigateToProcessingImage = navigateToProcessingImage)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmImageLayout(modifier: Modifier = Modifier, imgUri: String, onNavigateUp: () -> Unit, navigateToProcessingImage: (String) -> Unit) {
    Log.d("TEST", "imgUri: $imgUri")
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        CenterAlignedTopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.White
            ),
            windowInsets = WindowInsets(16.dp, 4.dp, 16.dp, 4.dp),
            title = { Text(text = "Confirm Image") },
            navigationIcon = {
                FilledTonalIconButton(
                    onClick = onNavigateUp
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.menu_back)
                    )
                }
            }
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .align(Alignment.Center),
            painter = rememberAsyncImagePainter(model = Uri.parse(imgUri)),
            contentDescription = "Picture",
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomStart), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledTonalButton(onClick = onNavigateUp, modifier = Modifier.weight(1f)) {
                Text(text = "Cancel")
            }
            Button(onClick = { navigateToProcessingImage(imgUri) }, modifier = Modifier.weight(1f)) {
                Text(text = "Continue")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConfirmImageLayoutPreview() {
    ConfirmImageLayout(imgUri = "null", onNavigateUp = {}, navigateToProcessingImage = {})
}