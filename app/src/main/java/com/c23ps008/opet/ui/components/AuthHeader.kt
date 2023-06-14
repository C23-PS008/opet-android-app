package com.c23ps008.opet.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.c23ps008.opet.R

@Composable
fun AuthHeader(height: Dp, modifier: Modifier = Modifier, content: @Composable (() -> Unit)? = null) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Image(
            painter = painterResource(id = R.drawable.auth_surface),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (content != null) {
            content()
        }
    }
}