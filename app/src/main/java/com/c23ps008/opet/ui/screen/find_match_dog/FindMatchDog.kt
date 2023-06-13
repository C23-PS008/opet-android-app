package com.c23ps008.opet.ui.screen.find_match_dog

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.navigation.NavigationDestination
import com.c23ps008.opet.ui.theme.OPetTheme

object FindMatchDogDestination : NavigationDestination {
    override val route: String = "find-match-dog"
}

@Composable
fun FindMatchDogScreen(onNavigateUp: () -> Unit) {
    FindMatchDogContent(onNavigateUp = onNavigateUp)
}

@Composable
fun FindMatchDogContent(onNavigateUp: () -> Unit) {
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
                isBack = questionIndex > 0,
                isContinue = questionIndex + 1 < answersState.size && answersState[questionIndex] != null,
                onBack = { questionIndex -= 1 },
                onContinue = { questionIndex += 1 }
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
    isBack: Boolean,
    isContinue: Boolean,
    onBack: () -> Unit,
    onContinue: () -> Unit,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FindMatchDogContentPreview() {
    OPetTheme {
        FindMatchDogContent(onNavigateUp = {})
    }
}