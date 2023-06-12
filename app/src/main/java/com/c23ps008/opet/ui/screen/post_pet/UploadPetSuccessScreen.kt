package com.c23ps008.opet.ui.screen.post_pet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.theme.OPetTheme

object UploadPetSuccessDestination: NavigationDestination {
    override val route: String = "upload-pet-success"
}

@Composable
fun UploadPetSuccessScreen(navigateToMyPost: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(160.dp),
                imageVector = Icons.Default.CheckCircle,
                contentDescription = stringResource(R.string.success),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
            Text(
                text = "Post Pet Successfully",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
            )
            Spacer(modifier = Modifier.padding(bottom = 48.dp))
            Button(onClick = { navigateToMyPost() }) {
                Text(text = stringResource(R.string.go_to_my_posts), style = MaterialTheme.typography.titleMedium)
            }
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun UploadPetSuccessScreenPreview() {
    OPetTheme {
        UploadPetSuccessScreen(navigateToMyPost = {})
    }
}