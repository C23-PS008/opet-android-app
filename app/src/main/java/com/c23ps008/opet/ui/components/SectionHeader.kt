package com.c23ps008.opet.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.c23ps008.opet.R

@Composable
fun SectionHeader(modifier: Modifier = Modifier, label: String, showAllNav: Boolean = true, onViewAll: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 30.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            if(showAllNav) {
                Text(
                    modifier = Modifier.clickable { onViewAll() },
                    text = stringResource(R.string.view_all),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}