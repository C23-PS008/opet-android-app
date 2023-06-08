package com.c23ps008.opet.ui.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.c23ps008.opet.R
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
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    ProfileContent(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        onLogout = {
            coroutineScope.launch {
                viewModel.logout()
                navigateToGetStarted()
            }
        }
    )
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    onLogout: () -> Unit,
) {
    Scaffold(topBar = {
        ProfileTopBar(
            onNavigateUp = onNavigateUp,
            onLogout = onLogout
        )
    }) { paddingValues ->
        Column(
            modifier = modifier
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
                    value = "",
                    onValueChange = {},
                    imeAction = ImeAction.Done
                )
                PhoneTextField(
                    label = stringResource(R.string.phone_number),
                    value = "",
                    onValueChange = {},
                    imeAction = ImeAction.Done
                )
                EmailTextField(
                    label = stringResource(R.string.email),
                    value = "",
                    onValueChange = {},
                    imeAction = ImeAction.Done
                )
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                Text(text = stringResource(R.string.save))
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
        ProfileContent(onNavigateUp = {}, onLogout = {})
    }
}
