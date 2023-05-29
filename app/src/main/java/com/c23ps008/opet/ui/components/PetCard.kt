package com.c23ps008.opet.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.c23ps008.opet.data.FakeDataSource
import com.c23ps008.opet.ui.theme.OPetTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetCard(data: PetCardState, modifier: Modifier = Modifier, onClick: () -> Unit) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .padding(bottom = 8.dp),
                painter = painterResource(id = data.image),
                contentDescription = stringResource(R.string.pet_image),
                contentScale = ContentScale.Crop
            )
            Text(
                text = data.breed,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = data.name,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = data.location,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun PetCardPreview() {
    OPetTheme {
        val fakeData = FakeDataSource.dummyHomePetResources[0]
        val petCardState = PetCardState(
            fakeData.id,
            fakeData.pet_image,
            fakeData.pet_breed,
            fakeData.name,
            fakeData.pet_address
        )
        PetCard(data = petCardState, modifier = Modifier.width(200.dp), onClick = {})
    }
}

data class PetCardState(
    val id: Int,
    val image: Int,
    val breed: String,
    val name: String,
    val location: String,
)