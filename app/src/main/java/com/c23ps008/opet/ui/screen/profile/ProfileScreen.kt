package com.c23ps008.opet.ui.screen.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.c23ps008.opet.data.formdata.ProfileFormData
import com.c23ps008.opet.data.remote.response.UserMeResponseData
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.ui.components.EmailTextField
import com.c23ps008.opet.ui.components.NameTextField
import com.c23ps008.opet.ui.components.PhoneTextField
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.theme.OPetTheme
import kotlinx.coroutines.launch

object ProfileDestination : NavigationDestination {
    override val route: String = "my-profile"
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    navigateToGetStarted: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val isAuthorized = viewModel.isAuthorized.collectAsState()
    LaunchedEffect(Unit) {
        if (!isAuthorized.value) {
            coroutineScope.launch {
                viewModel.logout()
                navigateToGetStarted()
            }
        }
    }
    viewModel.profileState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> {
                Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
            }

            is UiState.Loading -> {
                viewModel.getProfile()
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is UiState.Success -> {
                ProfileContent(
                    modifier = modifier,
                    onNavigateUp = onNavigateUp,
                    onLogout = {
                        coroutineScope.launch {
                            viewModel.logout()
                            navigateToGetStarted()
                        }
                    },
                    data = uiState.data.data as UserMeResponseData,
                    onSaveClick = { profileFormData, setLoading ->
                        if (!profileFormData.email.isNullOrEmpty() && !profileFormData.name.isNullOrEmpty()) {
                            coroutineScope.launch {
                                setLoading(true)
                                when (val result = viewModel.updateProfile(profileFormData)) {
                                    is UiState.Error -> {
                                        setLoading(false)
                                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                    is UiState.Loading -> {}
                                    is UiState.Success -> {
                                        setLoading(false)
                                        Toast.makeText(context, result.data.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    onLogout: () -> Unit,
    data: UserMeResponseData,
    onSaveClick: (ProfileFormData, (Boolean) -> Unit) -> Unit,
) {

    var profileFormData by remember {
        mutableStateOf(
            ProfileFormData(
                name = data.name,
                email = data.email,
                phoneNumber = data.phoneNumber
            )
        )
    }

    var isLoading by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        ProfileTopBar(
            onNavigateUp = onNavigateUp,
            onLogout = onLogout
        )
    }) { paddingValues ->
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .padding(top = 30.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NameTextField(
                        label = stringResource(R.string.name),
                        value = profileFormData.name.orEmpty(),
                        onValueChange = { profileFormData = profileFormData.copy(name = it) },
                        imeAction = ImeAction.Done
                    )
                    PhoneTextField(
                        label = stringResource(R.string.phone_number),
                        value = profileFormData.phoneNumber.orEmpty(),
                        onValueChange = {
                            profileFormData = profileFormData.copy(phoneNumber = it)
                        },
                        imeAction = ImeAction.Done
                    )
                    EmailTextField(
                        label = stringResource(R.string.email),
                        value = profileFormData.email.orEmpty(),
                        onValueChange = { profileFormData = profileFormData.copy(email = it) },
                        imeAction = ImeAction.Done
                    )
                }
                Button(modifier = Modifier.fillMaxWidth(), enabled = !isLoading, onClick = {
                    onSaveClick(profileFormData) { loading ->
                        isLoading = loading
                    }
                }) {
                    Text(text = stringResource(R.string.save))
                }
            }
            if(isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    onLogout: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(text = "My Profile") },
        navigationIcon = {
            IconButton(
                onClick = onNavigateUp
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.menu_back)
                )
            }
        },
        actions = {
            IconButton(onClick = { onLogout() }) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = stringResource(R.string.logout_menu)
                )
            }
        })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileContentPreview() {
    OPetTheme {
        ProfileContent(
            onNavigateUp = {},
            onLogout = {},
            data = UserMeResponseData(),
            onSaveClick = { _, _ -> })
    }
}
