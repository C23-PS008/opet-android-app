package com.c23ps008.opet.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.theme.OPetTheme
import kotlinx.coroutines.delay

object SplashDestination : NavigationDestination {
    override val route: String = "splash-screen"
}

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToGetStarted: () -> Unit,
) {
    val isAuth = false

    LaunchedEffect(key1 = true) {
        delay(2500L)
        if (isAuth) {
            navigateToHome()
        } else {
            navigateToGetStarted()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.width(200.dp),
            painter = painterResource(R.drawable.opet_logo_white),
            contentDescription = stringResource(
                id = R.string.app_name
            )
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SplashScreenPreview() {
    OPetTheme {
        SplashScreen(navigateToHome = {}, navigateToGetStarted = {})
    }
}