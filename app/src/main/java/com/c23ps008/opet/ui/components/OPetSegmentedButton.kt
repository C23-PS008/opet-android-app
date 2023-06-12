package com.c23ps008.opet.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OPetSegmentedButton(
    modifier: Modifier = Modifier,
    items: List<SegmentedItem>,
    onOptionSelected: (Int) -> Unit,
    selectedOption: Int,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        repeat(items.size) { itemIndex ->
            val shape: Shape = when (itemIndex) {
                0 -> RoundedCornerShape(
                    topStart = 100f,
                    bottomStart = 100f
                )

                items.size - 1 -> RoundedCornerShape(
                    topEnd = 100f,
                    bottomEnd = 100f
                )

                else -> RectangleShape
            }
            OutlinedButton(
                onClick = {
                    onOptionSelected(itemIndex)
                },
                modifier = Modifier
                    .weight(1f),
                shape = shape,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selectedOption == itemIndex) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                    contentColor = if (selectedOption == itemIndex) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                )
            ) {
                if (items[itemIndex].icon != null) {
                    items[itemIndex].icon?.let { iconSource ->
                        Icon(
                            imageVector = if (selectedOption == itemIndex) Icons.Outlined.Check else iconSource,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    text = items[itemIndex].label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OPetSegmentedButtonPreview() {
    val segmentedItem =
        listOf(
            SegmentedItem(Icons.Outlined.QuestionMark, "Unknown", "Unknown"),
            SegmentedItem(Icons.Outlined.Male, "Male", "Male"),
            SegmentedItem(Icons.Outlined.Female, "Female", "Female")
        )
    OPetSegmentedButton(items = segmentedItem, onOptionSelected = {}, selectedOption = 0)
}

data class SegmentedItem(
    val icon: ImageVector? = null,
    val label: String,
    val value: String,
)