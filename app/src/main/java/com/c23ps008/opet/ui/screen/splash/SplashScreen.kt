package com.c23ps008.opet.ui.screen.splash

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.common.AuthState
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.theme.OPetTheme

object SplashDestination : NavigationDestination {
    override val route: String = "splash-screen"
}

@Composable
fun SplashScreen(
    navigateToHome: () -> Unit,
    navigateToGetStarted: () -> Unit,
    viewModel: SplashViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val context = LocalContext.current

    val isAuthorized = viewModel.isAuthorized.collectAsState(initial = AuthState.Loading).value

    LaunchedEffect(isAuthorized) {
        if (isAuthorized is AuthState.Loading) {
            viewModel.authMe()
        }
        if (isAuthorized is AuthState.Unauthorized) {
            navigateToGetStarted()
        }
        if (isAuthorized is AuthState.Authorized) {
            navigateToHome()
        }
        if (isAuthorized is AuthState.Error) {
            Toast.makeText(context, isAuthorized.message, Toast.LENGTH_SHORT).show()
        }
    }

    SplashScreenContent()
}

@Composable
fun SplashScreenContent(modifier: Modifier = Modifier) {
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
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp),
            color = Color.White
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SplashScreenPreview() {
    OPetTheme {
        SplashScreenContent()
    }
}