package com.c23ps008.opet.ui.screen.post_pet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.c23ps008.opet.R

@Composable
fun FormSectionHeader(text: String) {
    Text(
        modifier = Modifier.padding(bottom = 16.dp),
        text = text,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun FormNormalLabel(modifier: Modifier = Modifier, text: String) {
    Text(modifier = modifier, text = text, style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun InputPetName(modifier: Modifier = Modifier, label: String) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = "",
        label = { Text(text = label) },
        onValueChange = {},
        maxLines = 1
    )
}

@Composable
fun SelectInputPetBreed(modifier: Modifier = Modifier, label: String) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = "",
        onValueChange = {},
        maxLines = 1,
        trailingIcon = {
            IconButton(
                onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.ArrowRight, contentDescription = null)
            }
        },
        label = { Text(text = label) })
}

@Composable
fun InputPetAbout(modifier: Modifier = Modifier) {
    OutlinedTextField(modifier = modifier.fillMaxWidth(),
        value = "",
        onValueChange = {},
        label = {
            Text(
                text = stringResource(
                    R.string.about
                )
            )
        },
        minLines = 3
    )
}

@Composable
fun InputContactName(modifier: Modifier = Modifier) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = "",
        label = { Text(text = stringResource(R.string.contact_name_required)) },
        onValueChange = {},
        maxLines = 1
    )
}

@Composable
fun InputPhoneNumber(modifier: Modifier = Modifier) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = "",
        label = { Text(text = "Phone Number *") },
        onValueChange = {},
        maxLines = 1
    )
}

@Composable
fun InputLocation(modifier: Modifier = Modifier) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = "",
        label = { Text(text = "Location *") },
        onValueChange = {},
        maxLines = 1,
        trailingIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = stringResource(
                        R.string.add_location
                    )
                )
            }
        }
    )
}