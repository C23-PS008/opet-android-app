package com.c23ps008.opet.ui.screen.post_pet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.components.OPetSegmentedButton
import com.c23ps008.opet.ui.components.SegmentedItem

@Composable
fun DogPostEntry(modifier: Modifier = Modifier, defaultPetBreed: String, imageUri: String) {
    val genderItems =
        listOf(
            SegmentedItem(label = "Unknown", value = "Unknown"),
            SegmentedItem(label = "Male", value = "Male"),
            SegmentedItem(label = "Female", value = "Female")
        )
    val ageList =
        listOf(
            SegmentedItem(label = "Puppy", value = "Kitten"),
            SegmentedItem(label = "Young", value = "Young"),
            SegmentedItem(label = "Adult", value = "Adult"),
        )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.padding(bottom = 16.dp)) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                painter = painterResource(id = R.drawable.anjing),
                contentDescription = "null",
                contentScale = ContentScale.Crop
            )
            FilledTonalIconButton(onClick = { /*TODO*/ }, modifier = Modifier.padding(16.dp)) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.menu_back)
                )
            }
        }
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            FormSectionHeader(text = stringResource(id = R.string.lets_describe_the_pet))
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                InputPetName(label = stringResource(R.string.dog_name))
                SelectInputPetBreed(label = stringResource(R.string.select_dog_breed))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FormNormalLabel(text = stringResource(R.string.gender))
                    OPetSegmentedButton(items = genderItems, onOptionSelected = {})
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FormNormalLabel(text = stringResource(R.string.age))
                    OPetSegmentedButton(items = ageList, onOptionSelected = {})
                }
                InputPetAbout()
            }
            Spacer(modifier = Modifier.padding(bottom = 28.dp))
            FormSectionHeader(
                text = stringResource(R.string.contact_me),
            )
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                InputContactName()
                InputPhoneNumber()
                InputLocation()
            }
        }
    }
}