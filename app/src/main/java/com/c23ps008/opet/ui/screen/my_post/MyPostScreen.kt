package com.c23ps008.opet.ui.screen.my_post

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.components.OPetNavigationBar
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.theme.OPetTheme

object MyPostDestination : NavigationDestination {
    override val route: String = "my-post"
}

@Composable
fun MyPostScreen(modifier: Modifier = Modifier, navigateToHome: () -> Unit, navigateToDetail: (Int) -> Unit) {
    MyPostContent(modifier = modifier, navigateToHome = navigateToHome, navigateToDetail = navigateToDetail)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostContent(modifier: Modifier = Modifier, navigateToHome: () -> Unit, navigateToDetail: (Int) -> Unit) {
    Scaffold(modifier = modifier, bottomBar = {
        OPetNavigationBar(
            currentRoute = MyPostDestination.route,
            navigateToHome = navigateToHome
        )
    }, topBar = { MyPostTopBar() }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(10) {
                item {
                    ElevatedCard(modifier = Modifier.fillMaxWidth(), onClick = {navigateToDetail(2)}) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    modifier = Modifier
                                        .width(80.dp)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop,
                                    painter = painterResource(id = R.drawable.kucing),
                                    contentDescription = stringResource(
                                        R.string.my_pet
                                    )
                                )
                                Column() {
                                    Text(
                                        text = "Pet Name",
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Text(
                                        text = "Pet Breed",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .wrapContentSize(Alignment.TopEnd)
                            ) {
                                var expanded by remember {
                                    mutableStateOf(false)
                                }
                                IconButton(onClick = { expanded = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = stringResource(
                                            R.string.view_action
                                        )
                                    )
                                }
                                ActionDropDown(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false })
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostTopBar() {
    TopAppBar(title = { Text(text = stringResource(id = R.string.my_posts)) })
}

@Composable
fun ActionDropDown(modifier: Modifier = Modifier, expanded: Boolean, onDismissRequest: () -> Unit) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.EditNote,
                    contentDescription = "action edit"
                )
            },
            text = { Text(text = "Edit") },
            onClick = { /*TODO*/ })
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "action delete"
                )
            },
            text = { Text(text = "Delete") },
            onClick = { /*TODO*/ })
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyPostContentPreview() {
    OPetTheme {
        MyPostContent(navigateToHome = {}, navigateToDetail = {})
    }
}