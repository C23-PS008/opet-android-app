package com.c23ps008.opet.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c23ps008.opet.R

@Composable
fun FindYourMatchCard(
    modifier: Modifier = Modifier,
    backgroundPainter: Painter,
    label: String,
    onClick: () -> Unit,
) {
    ElevatedCard(modifier = modifier
        .fillMaxSize()
        .clickable { onClick() }) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = backgroundPainter,
                contentDescription = "find your match, $label",
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            ) {
                Text(

                    text = label,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(offset = Offset(0f, 1f), blurRadius = 2f)
                    )
                )

                Text(
                    modifier = Modifier.sizeIn(maxWidth = 100.dp),
                    text = "Take quiz to find your match",
                    style = MaterialTheme.typography.labelSmall.copy(
                        shadow = Shadow(offset = Offset(0f, 1f), blurRadius = 2f)
                    ),
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FindYourMatchCardPreview() {
    FindYourMatchCard(
        backgroundPainter = painterResource(id = R.drawable.find_your_match_dog),
        onClick = {}, label = stringResource(R.string.dog)
    )
}