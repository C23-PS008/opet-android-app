package com.c23ps008.opet.ui.screen.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.c23ps008.opet.ui.components.NameTextField
import com.c23ps008.opet.ui.components.PasswordTextField
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.theme.OPetTheme

object RegisterDestination : NavigationDestination {
    override val route: String = "register"
}

@Composable
fun RegisterScreen(modifier: Modifier = Modifier, navigateToLogin: () -> Unit, onNavigateUp: () -> Unit) {
    RegisterContent(modifier = modifier, navigateToLogin = navigateToLogin, onNavigateUp = onNavigateUp)
}

@Composable
fun RegisterContent(modifier: Modifier = Modifier, navigateToLogin: () -> Unit, onNavigateUp: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AuthHeader(height = 161.dp) {
            IconButton(modifier = Modifier.padding(top = 36.dp), onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.menu_back)
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            AuthTitle(title = "Create Account")
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
                    RegisterInputForm()
                    Button(modifier = Modifier.fillMaxWidth(), onClick = { navigateToLogin() }) {
                        Text(text = stringResource(R.string.register))
                    }
                }
                AuthFooter(
                    label = stringResource(R.string.already_have_an_account),
                    navLabel = stringResource(R.string.login),
                    onNavigate = onNavigateUp
                )
            }
        }
    }
}

@Composable
fun RegisterInputForm(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        NameTextField(label = "Name")
        EmailTextField(label = "Email")
        PasswordTextField(label = "Password")
        PasswordTextField(label = "Confirm Password")
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegisterContentPreview() {
    OPetTheme {
        RegisterContent(navigateToLogin = {}, onNavigateUp = {})
    }
}