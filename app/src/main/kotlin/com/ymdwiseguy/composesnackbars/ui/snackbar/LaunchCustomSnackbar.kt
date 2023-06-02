package com.ymdwiseguy.composesnackbars.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ymdwiseguy.composesnackbars.ui.domain.SnackbarSeverity

@Composable
fun LaunchCustomSnackbar(
    key: Any?,
    snackbarHostState: SnackbarHostState,
    message: String,
    severity: SnackbarSeverity,
) {

    LaunchedEffect(key){
        snackbarHostState.showSnackbar(
            visuals = CustomSnackbarVisuals(
                actionLabel = null,
                duration = SnackbarDuration.Short,
                message = message,
                withDismissAction = false,
                severity = severity,
            )
        )
    }

}