package com.ymdwiseguy.composesnackbars.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ymdwiseguy.composesnackbars.ui.theme.ComposeSnackbarsTheme
import java.util.UUID

data class SnackBarViewEvent(
    val message: String,
    val eventId: UUID = UUID.randomUUID(),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnackbarSampleScreen() {

    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarEvents: MutableState<SnackBarViewEvent?> = remember { mutableStateOf(null) }

    fun triggerSnackbar(message: String) {
        snackBarEvents.value = SnackBarViewEvent(message)
    }

    Scaffold(
        topBar = { TopAppBar({ Text("Snackbars") }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            Button(onClick = { triggerSnackbar("my first message") }) {
                Text(text = "Show Snackbar 1")
            }
            Button(onClick = { triggerSnackbar("my second message") }) {
                Text(text = "Show Snackbar 2")
            }
        }
    }

    snackBarEvents.value?.let {
        LaunchedEffect(it.eventId) {
            snackbarHostState.showSnackbar(it.message)
        }
    }
}

@Preview(showBackground = true, heightDp = 560)
@Composable
private fun SnackbarSampleScreenPreview() {
    ComposeSnackbarsTheme {
        SnackbarSampleScreen()
    }
}