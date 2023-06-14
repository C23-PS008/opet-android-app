package com.c23ps008.opet.ui.screen.login

import android.widget.Toast
import androidx.lifecycle.viewmodel.compose.viewModel
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c23ps008.opet.R
import com.c23ps008.opet.data.formdata.LoginFormData
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.ui.components.AuthFooter
import com.c23ps008.opet.ui.components.AuthHeader
import com.c23ps008.opet.ui.components.AuthTitle
import com.c23ps008.opet.ui.components.EmailTextField
import com.c23ps008.opet.ui.components.LoadingDialog
import com.c23ps008.opet.ui.components.PasswordTextField
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.theme.OPetTheme
import kotlinx.coroutines.launch

object LoginDestination : NavigationDestination {
    override val route: String = "login"
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToRegister: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    LoginContent(
        modifier = modifier,
        navigateToRegister = navigateToRegister,
        onLoginClick = { loginFormData, setLoading ->
            coroutineScope.launch {
                setLoading(true)
                when (val result = viewModel.login(loginFormData)) {
                    is UiState.Error -> {
                        setLoading(false)
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Loading -> {
                        // Nothing
                    }

                    is UiState.Success -> {
                        setLoading(false)
                        Toast.makeText(context, result.data.message, Toast.LENGTH_SHORT).show()
                        navigateToHome()
                    }
                }
            }
        }
    )
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    navigateToRegister: () -> Unit,
    onLoginClick: (loginFormData: LoginFormData, setLoading: (Boolean) -> Unit) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    if(isLoading) {
        LoadingDialog(onDismiss = {})
    }

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
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EmailTextField(
                            label = "Email",
                            value = email,
                            onValueChange = { email = it },
                            imeAction = ImeAction.Next
                        )
                        PasswordTextField(
                            label = "Password",
                            value = password,
                            onValueChange = { password = it },
                            imeAction = ImeAction.Done
                        )
                    }
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        onLoginClick(LoginFormData(email, password)) { loading ->
                            isLoading = loading
                        }
                    }, enabled = !isLoading) {
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginContentPreview() {
    OPetTheme {
        LoginContent(
            navigateToRegister = {},
            onLoginClick = { _, _ -> run { } })
    }
}