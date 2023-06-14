package com.c23ps008.opet.ui.screen.home

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.c23ps008.opet.R

@Composable
fun HomeSearchBar(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val source = remember {
        MutableInteractionSource()
    }
    if (source.collectIsPressedAsState().value) {
        onClick()
    }
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(), value = "", onValueChange = {},
        leadingIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(
                        R.string.search
                    )
                )
            }
        },
        readOnly = true,
        placeholder = { Text(text = "Search by Breed") },
        interactionSource = source,
        shape = RoundedCornerShape(100f),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White
        )
    )
}