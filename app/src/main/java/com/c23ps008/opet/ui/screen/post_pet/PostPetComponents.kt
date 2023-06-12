package com.c23ps008.opet.ui.screen.post_pet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
fun InputPetName(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        label = { Text(text = label) },
        onValueChange = onValueChange,
        maxLines = 1
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectInputPetType(
    modifier: Modifier = Modifier,
    petType: String,
    onValueChange: (String) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val options = listOf("Cat", "Dog")
    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it }) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = petType,
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text(
                    text = stringResource(
                        id = R.string.select_pet_type
                    )
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            })
        if (isExpanded) {
            Dialog(onDismissRequest = { isExpanded = false }) {
                Surface(
                    shadowElevation = 4.dp,
                    tonalElevation = 4.dp,
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        itemsIndexed(options) { index, option ->
                            DropdownMenuItem(text = { Text(text = option) }, onClick = {
                                onValueChange(option)
                                isExpanded = false
                            })
                            if (index < options.lastIndex) {
                                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectInputPetTypePreview() {
    SelectInputPetType(petType = "", onValueChange = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectInputPetBreed(
    modifier: Modifier = Modifier,
    label: String,
    options: List<String>,
    value: String,
    onValueChange: (String) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val filteredOptions = options.filter {
        it.contains(searchQuery, ignoreCase = true)
    }


    ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = it }) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = value,
            onValueChange = {},
            readOnly = true,
            maxLines = 1,
            placeholder = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            })

        if (isExpanded) {
            Dialog(onDismissRequest = { isExpanded = false }) {
                Surface(
                    modifier = Modifier.sizeIn(maxHeight = 640.dp),
                    shadowElevation = 4.dp,
                    tonalElevation = 4.dp,
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column() {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text(text = stringResource(R.string.search_breed)) }
                        )
                        Spacer(modifier = Modifier.padding(bottom = 8.dp))
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            itemsIndexed(filteredOptions) { index, option ->
                                DropdownMenuItem(text = { Text(text = option) }, onClick = {
                                    onValueChange(option)
                                    isExpanded = false
                                })
                                if (index < filteredOptions.lastIndex) {
                                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectInputPetBreedPreview() {
    SelectInputPetBreed(
        label = stringResource(id = R.string.select_cat_breed),
        options = listOf("Chihuahua"),
        value = "",
        onValueChange = {})
}

@Composable
fun InputPetAbout(modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
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
fun InputContactName(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        label = { Text(text = stringResource(R.string.contact_name_required)) },
        onValueChange = onValueChange,
        maxLines = 1
    )
}

@Composable
fun InputPhoneNumber(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        label = { Text(text = "Phone Number *") },
        onValueChange = onValueChange,
        maxLines = 1
    )
}

@Composable
fun InputLocation(modifier: Modifier = Modifier, onClick: () -> Unit, value: String) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        value = value,
        placeholder = { Text(text = "Location *") },
        onValueChange = {},
        maxLines = 1,
        singleLine = true,
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { onClick() }) {
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

@Preview(showBackground = true)
@Composable
fun InputLocationPreview() {
    InputLocation(onClick = {}, value = "")
}