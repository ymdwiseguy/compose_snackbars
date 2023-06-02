package com.ymdwiseguy.composesnackbars.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ymdwiseguy.composesnackbars.ui.domain.SnackbarSeverity
import com.ymdwiseguy.composesnackbars.ui.domain.SnackbarSeverity.ERROR
import com.ymdwiseguy.composesnackbars.ui.domain.SnackbarSeverity.INFO
import com.ymdwiseguy.composesnackbars.ui.domain.SnackbarViewEvent
import com.ymdwiseguy.composesnackbars.ui.snackbar.CustomSnackbarHost
import com.ymdwiseguy.composesnackbars.ui.snackbar.LaunchCustomSnackbar
import com.ymdwiseguy.composesnackbars.ui.theme.ComposeSnackbarsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnackbarSampleScreen() {

    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarEvents: MutableState<SnackbarViewEvent?> = remember { mutableStateOf(null) }

    fun triggerSnackbar(message: String, severity: SnackbarSeverity) {
        snackBarEvents.value = SnackbarViewEvent(message, severity)
    }

    Scaffold(
        topBar = { TopAppBar({ Text("Snackbars") }) },
        snackbarHost = { CustomSnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            Button(
                onClick = {
                    triggerSnackbar("This is a snackbar with the severity level INFO", INFO)
                }
            ) {
                Text(text = "Show Snackbar 1")
            }
            Button(
                onClick = {
                    triggerSnackbar("This is a snackbar with the severity level ERROR", ERROR)
                }
            ) {
                Text(text = "Show Snackbar 2")
            }
        }
    }

    snackBarEvents.value?.let {
        LaunchCustomSnackbar(
            key = it.eventId,
            snackbarHostState = snackbarHostState,
            message = it.message,
            severity = it.severity,
        )
    }
}

@Preview(showBackground = true, heightDp = 560)
@Composable
private fun SnackbarSampleScreenPreview() {
    ComposeSnackbarsTheme {
        SnackbarSampleScreen()
    }
}