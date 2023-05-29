package com.c23ps008.opet.ui.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.components.AuthFooter
import com.c23ps008.opet.ui.components.AuthHeader
import com.c23ps008.opet.ui.components.AuthTitle
import com.c23ps008.opet.ui.components.EmailTextField
import com.c23ps008.opet.ui.components.PasswordTextField
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.theme.OPetTheme

object LoginDestination : NavigationDestination {
    override val route: String = "login"
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToRegister: () -> Unit,
    navigateToHome: () -> Unit,
) {
    LoginContent(
        modifier = modifier,
        navigateToRegister = navigateToRegister,
        navigateToHome = navigateToHome
    )
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    navigateToRegister: () -> Unit,
    navigateToHome: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        AuthHeader(height = 161.dp)
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            AuthTitle(title = "Login", label = "Please Login to continue")
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    LoginInputForm()
                    Button(modifier = Modifier.fillMaxWidth(), onClick = navigateToHome) {
                        Text(text = stringResource(R.string.login))
                    }
                }
                AuthFooter(
                    label = stringResource(R.string.don_t_have_an_account),
                    navLabel = stringResource(
                        R.string.register
                    ),
                    onNavigate = navigateToRegister
                )
            }
        }
    }
}

@Composable
fun LoginInputForm(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        EmailTextField(label = "Email")
        PasswordTextField(label = "Password")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginContentPreview() {
    OPetTheme {
        LoginContent(navigateToRegister = {}, navigateToHome = {})
    }
}