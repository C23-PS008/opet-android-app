package com.c23ps008.opet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.c23ps008.opet.R

@Composable
fun NameTextField(
    label: String,
    modifier: Modifier = Modifier,
    value: String,
    supportingText: String? = null,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    imeAction: ImeAction
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label
            )
        },
        isError = isError,
        supportingText = { if (supportingText != null) Text(text = supportingText) },
        leadingIcon = { Icon(Icons.Outlined.Person, null) }, maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = imeAction)
    )
}

@Composable
fun PhoneTextField(
    label: String, modifier: Modifier = Modifier, value: String,
    onValueChange: (String) -> Unit,
    supportingText: String? = null,
    isError: Boolean = false,
    imeAction: ImeAction
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label
            )
        },
        isError = isError,
        supportingText = { if (supportingText != null) Text(text = supportingText) },
        leadingIcon = { Icon(Icons.Outlined.Phone, null) }, maxLines = 1,
        placeholder = { Text(text = stringResource(R.string.example_62xx)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = imeAction)
    )
}

@Composable
fun EmailTextField(
    label: String, modifier: Modifier = Modifier, value: String, supportingText: String? = null,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    imeAction: ImeAction
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label
            )
        },
        isError = isError,
        supportingText = { if (supportingText != null) Text(text = supportingText) },
        leadingIcon = { Icon(Icons.Outlined.Email, null) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction),
        maxLines = 1
    )
}

@Composable
fun PasswordTextField(
    label: String, modifier: Modifier = Modifier, value: String,
    onValueChange: (String) -> Unit,
    supportingText: String? = null,
    isError: Boolean = false,
    imeAction: ImeAction
) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label
            )
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    modifier = modifier,
                    imageVector = if (!passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                    contentDescription = null
                )
            }
        },
        isError = isError,
        supportingText = { if (supportingText != null) Text(text = supportingText) },
        leadingIcon = { Icon(Icons.Outlined.Key, null) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        maxLines = 1
    )
}