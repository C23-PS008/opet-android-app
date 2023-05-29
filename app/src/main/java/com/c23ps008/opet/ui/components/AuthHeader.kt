package com.c23ps008.opet.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AuthHeader(height: Dp, modifier: Modifier = Modifier, content: @Composable (() -> Unit)? = null) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .customWaveBackground(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.inversePrimary,
            )
            .height(height)
            .padding(horizontal = 16.dp)
    ) {
        if (content != null) {
            content()
        }
    }
}