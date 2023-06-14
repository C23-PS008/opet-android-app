package com.c23ps008.opet.ui.screen.find_match_dog

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.c23ps008.opet.R
import com.c23ps008.opet.data.formdata.DogPredictFormData
import com.c23ps008.opet.data.remote.response.DogPredictResponse
import com.c23ps008.opet.data.remote.response.TopBreedsItem
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.ui.components.ErrorContent
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.theme.OPetTheme
import kotlinx.coroutines.launch

object FindMatchDogDestination : NavigationDestination {
    override val route: String = "find-match-dog"
}

@Composable
fun FindMatchDogScreen(
    viewModel: FindMatchDogViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateUp: () -> Unit,
    navigateToCalculatedResult: (String) -> Unit,
) {
    val dataState = viewModel.dataState.collectAsState().value
    var isCalculated by rememberSaveable {
        mutableStateOf(false)
    }
    if (isCalculated) {
        FindMatchDogCalculatedContent(dataState = dataState, onNavigateUp = onNavigateUp, navigateToCalculatedResult = navigateToCalculatedResult)
    } else {
        FindMatchDogContent(onNavigateUp = onNavigateUp, onCalculateClick = { data ->
            val dogPredictFormData = DogPredictFormData(
                listOf(data[0]), listOf(data[1]), listOf(data[2]),
                listOf(data[3]), listOf(data[4]), listOf(data[5]),
                listOf(data[6]), listOf(data[7]),
            )
            viewModel.predict(dogPredictFormData)
            isCalculated = true
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindMatchDogCalculatedContent(
    onNavigateUp: () -> Unit,
    dataState: UiState<DogPredictResponse>,
    navigateToCalculatedResult: (String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var selectedPet by remember { mutableStateOf(TopBreedsItem()) }

    DetailBottomSheet(petData = selectedPet, scaffoldState = scaffoldState) {
        Scaffold(topBar = { FindMatchTopBar(onNavigateUp = onNavigateUp) }) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (dataState) {
                    is UiState.Error -> {
                        ErrorContent(
                            modifier = Modifier.align(Alignment.Center),
                            message = dataState.message,
                            onNavigateUp = onNavigateUp
                        )
                    }

                    is UiState.Loading -> {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.padding(bottom = 16.dp))
                            Text(text = "Calculating...", textAlign = TextAlign.Center)
                        }
                    }

                    is UiState.Success -> {
                        val result = dataState.data.predictions?.get(0)?.topBreeds
                        if (result.isNullOrEmpty()) {
                            ErrorContent(
                                modifier = Modifier.align(Alignment.Center),
                                message = stringResource(R.string.result_not_found),
                                onNavigateUp = onNavigateUp
                            )
                        } else {
                            Column {
                                Spacer(modifier = Modifier.padding(bottom = 8.dp))
                                LazyColumn(
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(result, key = { it?.breed.toString() }) {
                                        OutlinedCard {
                                            Column {
                                                Image(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .aspectRatio(1.5f),
                                                    painter = rememberAsyncImagePainter(model = it?.breedImage),
                                                    contentDescription = null,
                                                    contentScale = ContentScale.Crop
                                                )
                                                Column(modifier = Modifier.padding(16.dp)) {
                                                    Text(
                                                        text = stringResource(R.string.breed),
                                                        style = MaterialTheme.typography.labelMedium
                                                    )
                                                    Text(
                                                        text = it?.breed.toString(),
                                                        style = MaterialTheme.typography.bodyLarge
                                                    )
                                                    Spacer(modifier = Modifier.padding(bottom = 8.dp))
                                                    Text(
                                                        text = it?.description.toString(),
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        overflow = TextOverflow.Ellipsis,
                                                        maxLines = 3
                                                    )
                                                    Spacer(modifier = Modifier.padding(bottom = 16.dp))
                                                    Box(modifier = Modifier.fillMaxWidth()) {
                                                        Row(
                                                            modifier = Modifier.align(Alignment.BottomEnd),
                                                            horizontalArrangement = Arrangement.spacedBy(
                                                                8.dp
                                                            )
                                                        ) {
                                                            OutlinedButton(onClick = {
                                                                if (it != null) {
                                                                    selectedPet = it
                                                                    coroutineScope.launch {
                                                                        scaffoldState.bottomSheetState.expand()
                                                                    }
                                                                }
                                                            }) {
                                                                Text(text = "Learn More")
                                                            }
                                                            Button(onClick = { navigateToCalculatedResult(it?.breed.toString()) }) {
                                                                Text(text = "Find Dog")
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
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
fun DetailBottomSheet(
    modifier: Modifier = Modifier,
    petData: TopBreedsItem,
    scaffoldState: BottomSheetScaffoldState,
    content: @Composable () -> Unit,
) {
    BottomSheetScaffold(
        modifier = modifier,
        sheetPeekHeight = 0.dp,
        scaffoldState = scaffoldState,
        sheetContainerColor = Color.White,
        sheetContent = {
            Column {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f),
                    painter = rememberAsyncImagePainter(model = petData.breedImage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.breed),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = petData.breed.toString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.padding(bottom = 8.dp))
                    Text(
                        text = petData.description.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    ) {
        content()
    }
}

@Composable
fun FindMatchDogContent(onNavigateUp: () -> Unit, onCalculateClick: (List<String?>) -> Unit) {
    var questionIndex by remember { mutableStateOf(0) }
    val answersState =
        remember { mutableStateListOf<String?>(null, null, null, null, null, null, null, null) }

    LaunchedEffect(questionIndex) {
        Log.d("LOADING", "$questionIndex")
    }

    Scaffold(
        topBar = { FindMatchTopBar(onNavigateUp = onNavigateUp) },
        bottomBar = {
            FindMatchBottomBar(
                currentIndex = questionIndex + 1,
                questionsSize = answersState.size,
                isCalculate = questionIndex + 1 == answersState.size && answersState[questionIndex] != null,
                isBack = questionIndex > 0,
                isContinue = questionIndex + 1 < answersState.size && answersState[questionIndex] != null,
                onBack = { questionIndex -= 1 },
                onContinue = { questionIndex += 1 },
                onCalculateClick = {
                    onCalculateClick(answersState.toList())
                }
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = findMatchDogQuestions[questionIndex].question,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.padding(bottom = 8.dp))
                Column {
                    findMatchDogQuestions[questionIndex].options.forEach { answer ->
                        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = answersState[questionIndex] == answer,
                                onClick = {
                                    answersState[questionIndex] = answer
                                })
                            Text(modifier = Modifier.clickable {
                                answersState[questionIndex] = answer
                            }, text = answer, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindMatchTopBar(modifier: Modifier = Modifier, onNavigateUp: () -> Unit) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(text = stringResource(id = R.string.find_your_match_dog)) },
        navigationIcon = {
            IconButton(onClick = { onNavigateUp() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.menu_back)
                )
            }
        })
}

@Composable
fun FindMatchBottomBar(
    modifier: Modifier = Modifier,
    currentIndex: Int,
    questionsSize: Int,
    isCalculate: Boolean,
    isBack: Boolean,
    isContinue: Boolean,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    onCalculateClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "$currentIndex of $questionsSize Questions",
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = currentIndex.toFloat() / questionsSize.toFloat()
        )
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { onBack() },
                modifier = Modifier.weight(1f),
                enabled = isBack
            ) {
                Text(text = stringResource(R.string.back))
            }
            if (isCalculate) {
                Button(
                    onClick = { onCalculateClick() },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = stringResource(R.string.calculate))
                }
            } else {
                Button(
                    onClick = { onContinue() },
                    modifier = Modifier.weight(1f),
                    enabled = isContinue
                ) {
                    Text(text = stringResource(R.string.continue_label))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FindMatchDogContentPreview() {
    OPetTheme {
        FindMatchDogContent(onNavigateUp = {}, onCalculateClick = {})
    }
}