package com.c23ps008.opet.ui.screen.get_started

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.theme.OPetTheme

object GetStartedDestination : NavigationDestination {
    override val route: String = "get-started"

}

@Composable
fun GetStartedScreen(modifier: Modifier = Modifier, navigateToLogin: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.get_started_picture_with_title),
            contentDescription = stringResource(
                R.string.get_started_picture
            ),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.padding(bottom = 34.dp))
        Button(modifier = Modifier.fillMaxWidth(), onClick = { navigateToLogin() }) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.get_started),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GetStartedScreenPreview() {
    OPetTheme {
        GetStartedScreen(navigateToLogin = {})
    }
}