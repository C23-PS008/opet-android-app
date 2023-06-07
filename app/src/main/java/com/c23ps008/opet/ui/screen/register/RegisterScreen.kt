package com.c23ps008.opet.ui.screen.register

import android.widget.Toast
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.c23ps008.opet.R
import com.c23ps008.opet.data.formdata.RegisterFormData
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.ui.components.AuthFooter
import com.c23ps008.opet.ui.components.AuthHeader
import com.c23ps008.opet.ui.components.AuthTitle
import com.c23ps008.opet.ui.components.EmailTextField
import com.c23ps008.opet.ui.components.NameTextField
import com.c23ps008.opet.ui.components.PasswordTextField
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.theme.OPetTheme
import kotlinx.coroutines.launch

object RegisterDestination : NavigationDestination {
    override val route: String = "register"
}

@Composable
fun RegisterScreen(
    navigateToLogin: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    RegisterContent(
        onNavigateUp = onNavigateUp,
        onRegisterClick = { registerFormData, setLoading ->
            setLoading(true)
            coroutineScope.launch {
                when (val result = viewModel.register(registerFormData)) {
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
                        navigateToLogin()
                    }
                }
            }
        }
    )
}

@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    onRegisterClick: (formData: RegisterFormData, setLoading: (Boolean) -> Unit) -> Unit,
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

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
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        NameTextField(
                            label = "Name",
                            value = name,
                            onValueChange = { name = it }, imeAction = ImeAction.Next
                        )
                        EmailTextField(
                            label = "Email",
                            value = email,
                            onValueChange = { email = it }, imeAction = ImeAction.Next
                        )
                        PasswordTextField(
                            label = "Password",
                            value = password,
                            onValueChange = { password = it }, imeAction = ImeAction.Next
                        )
                        PasswordTextField(
                            label = "Confirm Password",
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            isError = confirmPassword != password, imeAction = ImeAction.Done
                        )
                    }
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        onRegisterClick(
                            RegisterFormData(name, email, password)
                        ) { loading -> isLoading = loading }
                    }, enabled = !isLoading) {
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegisterContentPreview() {
    OPetTheme {
        RegisterContent(onNavigateUp = {}, onRegisterClick = { _, _ -> run {} })
    }
}