package com.c23ps008.opet.ui.screen.search_breed

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.theme.OPetTheme
import com.c23ps008.opet.utils.classifyPetImage
import com.c23ps008.opet.utils.cropOneToOneRatio
import kotlinx.coroutines.delay

object ProcessingImageDestination : NavigationDestination {
    override val route: String = "processing-image"
    const val imgUriArg = "imgUri"
    val routeWithArgs = "$route/{$imgUriArg}"
}

@Composable
fun ProcessingImageScreen(
    viewModel: ProcessingImageViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToCalculatedResult: (String) -> Unit,
) {
    val context = LocalContext.current

    var isProcessing by remember { mutableStateOf(true) }
    var resultLabel by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val imageUri = Uri.parse(viewModel.imageUri)
        val bitmap = cropOneToOneRatio(context, imageUri)
        if (bitmap != null) {
            resultLabel = classifyPetImage(context, bitmap, 299)
            isProcessing = false
            delay(2000)
            if (resultLabel != null) {
                Toast.makeText(context, resultLabel, Toast.LENGTH_SHORT).show()
                navigateToCalculatedResult(resultLabel.toString())
            }
        }
    }

    ProcessingImageContent()
}

@Composable
fun ProcessingImageContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.please_wait),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.padding(top = 24.dp)
            )
            Text(
                text = stringResource(R.string.checking_your_image),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
                )
            )
            CircularProgressIndicator(modifier = Modifier.padding(top = 36.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProcessingImageContentPreview() {
    OPetTheme {
        ProcessingImageContent()
    }
}