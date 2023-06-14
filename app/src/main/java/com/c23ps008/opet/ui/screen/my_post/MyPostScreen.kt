package com.c23ps008.opet.ui.screen.my_post

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.c23ps008.opet.R
import com.c23ps008.opet.data.remote.response.MyPetsDataItem
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.ui.components.OPetNavigationBar
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.theme.OPetTheme
import kotlinx.coroutines.launch

object MyPostDestination : NavigationDestination {
    override val route: String = "my-post"
}

@Composable
fun MyPostScreen(
    modifier: Modifier = Modifier,
    viewModel: MyPostViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = AppViewModelProvider.Factory
    ),
    navigateToHome: () -> Unit,
    navigateToPostPet: () -> Unit,
    navigateToDetail: (String) -> Unit,
    navigateToEdit: (String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(modifier = modifier, bottomBar = {
        OPetNavigationBar(
            currentRoute = MyPostDestination.route,
            navigateToHome = navigateToHome,
            navigateToPostPet = navigateToPostPet
        )
    }, topBar = { MyPostTopBar() }) { paddingValues ->
        viewModel.myPostsState.collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Error -> {

                }

                is UiState.Loading -> {
                    viewModel.getMyPosts()
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                is UiState.Success -> {
                    MyPostContent(
                        modifier = Modifier.padding(paddingValues),
                        navigateToDetail = navigateToDetail,
                        myPostsData = uiState.data,
                        navigateToEdit = navigateToEdit,
                        onDeletePost = { petId ->
                            coroutineScope.launch {
                                when (val result = viewModel.deletePost(petId)) {
                                    is UiState.Error -> {
                                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                    is UiState.Loading -> {}
                                    is UiState.Success -> {
                                        viewModel.getMyPosts()
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostContent(
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit,
    myPostsData: List<MyPetsDataItem?>,
    onDeletePost: (String) -> Unit,
    navigateToEdit: (String) -> Unit,
) {
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var selectedIdToDelete by remember { mutableStateOf("") }

    if (showDeleteConfirmDialog) {
        DeleteConfirmDialog(
            onDismissRequest = {
                showDeleteConfirmDialog = false
                selectedIdToDelete = ""
            },
            onDelete = {
                onDeletePost(selectedIdToDelete)
                showDeleteConfirmDialog = false
                selectedIdToDelete = ""
            })
    }

    if (myPostsData.isNotEmpty()) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(myPostsData, key = { it?.petId.toString() }) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navigateToDetail(it?.petId.toString()) }) {
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
                                painter = rememberAsyncImagePainter(model = it?.photoUrl),
                                contentDescription = stringResource(
                                    R.string.my_pet
                                )
                            )
                            Column {
                                Text(
                                    text = it?.name.toString(),
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = it?.breed.toString(),
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
                                onDismissRequest = { expanded = false },
                                onDelete = {
                                    showDeleteConfirmDialog = true
                                    selectedIdToDelete = it?.petId.toString()
                                },
                                navigateToEdit = { navigateToEdit(it?.petId.toString()) }
                            )
                        }
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "You don't have any posted pets"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostTopBar() {
    TopAppBar(title = { Text(text = stringResource(id = R.string.my_posts)) })
}

@Composable
fun ActionDropDown(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
    navigateToEdit: () -> Unit,
) {
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
            onClick = { navigateToEdit() })
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "action delete"
                )
            },
            text = { Text(text = "Delete") },
            onClick = { onDelete() })
    }
}

@Composable
fun DeleteConfirmDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onDelete: () -> Unit,
) {
    var selectedIndex by remember { mutableStateOf(2) }
    val reasonList = listOf(
        "Found a pet owner",
        "Decided to keep",
        "Other"
    )
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier,
            shadowElevation = 4.dp,
            tonalElevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.do_you_want_to_delete_this_post),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                )
                Spacer(modifier = Modifier.padding(bottom = 8.dp))
                Text(text = stringResource(R.string.please_tell_us_the_reason))
                Spacer(modifier = Modifier.padding(bottom = 12.dp))
                Column {
                    reasonList.forEachIndexed { index, text ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedIndex == index,
                                onClick = { selectedIndex = index })
                            Text(
                                text = text,
                                modifier = Modifier.clickable { selectedIndex = index })
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(bottom = 20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalIconButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onDismissRequest() }) {
                        Text(text = "Cancel")
                    }
                    Button(modifier = Modifier.weight(1f), onClick = { onDelete() }) {
                        Text(text = "Delete")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteConfirmDialogPreview() {
    OPetTheme {
        DeleteConfirmDialog(onDelete = {})
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyPostContentPreview() {
    OPetTheme {
        MyPostContent(navigateToDetail = {}, myPostsData = listOf(), onDeletePost = {}, navigateToEdit = {})
    }
}