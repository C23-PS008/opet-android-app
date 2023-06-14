package com.c23ps008.opet.ui.screen.find_match_cat

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.c23ps008.opet.R
import com.c23ps008.opet.data.formdata.CatPredictFormData
import com.c23ps008.opet.data.remote.response.CatPredictResponse
import com.c23ps008.opet.ui.common.UiState
import com.c23ps008.opet.ui.components.ErrorContent
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.screen.AppViewModelProvider
import com.c23ps008.opet.ui.theme.OPetTheme

object FindMatchCatDestination : NavigationDestination {
    override val route: String = "find-match-cat"
}

@Composable
fun FindMatchCatScreen(
    viewModel: FindMatchCatViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateUp: () -> Unit,
    navigateToCalculatedResult: (String) -> Unit,
) {
    val dataState = viewModel.dataState.collectAsState().value
    var isCalculated by rememberSaveable {
        mutableStateOf(false)
    }
    if (isCalculated) {
        FindMatchCatCalculatedContent(onNavigateUp = onNavigateUp, dataState = dataState, navigateToCalculatedResult)
    } else {
        FindMatchCatContent(onNavigateUp = onNavigateUp, onCalculateClick = { data ->
            val catPredictFormData = CatPredictFormData(
                data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7],
            )
            viewModel.predict(catPredictFormData)
            isCalculated = true
        })

    }
}

@Composable
fun FindMatchCatCalculatedContent(
    onNavigateUp: () -> Unit,
    dataState: UiState<CatPredictResponse>,
    navigateToCalculatedResult: (String) -> Unit,
) {
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
                    val result = dataState.data.recommendations
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
                                                painter = rememberAsyncImagePainter(
                                                    model = it?.breedImage
                                                ),
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
                                                Spacer(modifier = Modifier.padding(bottom = 16.dp))
                                                Box(modifier = Modifier.fillMaxWidth()) {
                                                    Row(
                                                        modifier = Modifier.align(Alignment.BottomEnd),
                                                        horizontalArrangement = Arrangement.spacedBy(
                                                            8.dp
                                                        )
                                                    ) {
                                                        Button(onClick = { navigateToCalculatedResult(it?.breed.toString()) }) {
                                                            Text(text = "Find Cat")
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

@Composable
fun FindMatchCatContent(onNavigateUp: () -> Unit, onCalculateClick: (List<Int?>) -> Unit) {
    var questionIndex by remember { mutableStateOf(0) }
    val answersState =
        remember { mutableStateListOf<Int?>(null, null, null, null, null, null, null, null) }

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
                    text = findMatchCatQuestions[questionIndex].question,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.padding(bottom = 8.dp))
                Column {
                    findMatchCatQuestions[questionIndex].options.forEach { answer ->
                        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = answersState[questionIndex] == answer.value,
                                onClick = {
                                    answersState[questionIndex] = answer.value
                                })
                            Text(modifier = Modifier.clickable {
                                answersState[questionIndex] = answer.value
                            }, text = answer.label, style = MaterialTheme.typography.bodyLarge)
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
        title = { Text(text = stringResource(id = R.string.find_your_match_cat)) },
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
        FindMatchCatContent(onNavigateUp = {}, onCalculateClick = {})
    }
}