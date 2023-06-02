package com.ymdwiseguy.composesnackbars.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import com.ymdwiseguy.composesnackbars.ui.domain.SnackbarSeverity

@Composable
fun CustomSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState = hostState) { snackbarData ->
        CustomSnackbar(
            message = snackbarData.visuals.message,
            severity = (snackbarData.visuals as? CustomSnackbarVisuals)?.severity ?: SnackbarSeverity.INFO
        )
    }
}


data class CustomSnackbarVisuals(
    override val actionLabel: String?,
    override val duration: SnackbarDuration,
    override val message: String,
    override val withDismissAction: Boolean,
    val severity: SnackbarSeverity,
) : SnackbarVisuals