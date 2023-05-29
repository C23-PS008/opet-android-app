package com.c23ps008.opet.ui.screen.pet_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.theme.OPetTheme

object PetDetailDestination: NavigationDestination {
    override val route: String = "pet-detail"
    const val petIdArg = "petId"
    val routeWithArgs = "$route/{$petIdArg}"
}

@Composable
fun PetDetailScreen(modifier: Modifier = Modifier, onNavigateUp: () -> Unit) {
    PetDetailContent(modifier = modifier, onNavigateUp = onNavigateUp)
}

@Composable
fun PetDetailContent(modifier: Modifier = Modifier, onNavigateUp: () -> Unit) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = { BottomAction() },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.anjing),
                    contentDescription = stringResource(
                        R.string.pet_for_adoption_image
                    )
                )
                FilledTonalIconButton(
                    onClick = onNavigateUp, modifier = Modifier
                        .padding(16.dp)
                        .align(
                            Alignment.TopStart
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.menu_back)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(bottom = 12.dp))
            Column() {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Pet Name",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Pet Breed",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Young • Female",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(text = "About the pet", style = MaterialTheme.typography.bodyMedium)
                }
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Location",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "blok E6 nomor 413Pengadegan Timur I No.2-9, RT.12/RW.5, Rawajati, Kec. Pancoran, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12750",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun BottomAction(modifier: Modifier = Modifier, onCall: () -> Unit = {}) {
    ElevatedCard(
        modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(12.dp)),
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Nama",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(text = "6281xxxxxxx", maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium)
            }
            Button(onClick = onCall) {
                Text(text = "Call")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PetDetailContentPreview() {
    OPetTheme {
        PetDetailContent(onNavigateUp = {})
    }
}