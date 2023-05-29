package com.c23ps008.opet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun<T> CustomGridLayout(
    data: List<T>,
    columns: Int,
    horizontalArrangement: Arrangement.Horizontal,
    verticalArrangement: Arrangement.Vertical,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement
    ) {
        for (i in data.indices step columns) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = horizontalArrangement) {
                for (j in i until (i + columns).coerceAtMost(data.size)) {
                    content(data[j])
                }
            }
        }
    }
}