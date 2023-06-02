package com.ymdwiseguy.composesnackbars.ui.domain

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

enum class SnackbarSeverity{
    INFO, ERROR
}

class SnackbarSeverityProvider: PreviewParameterProvider<SnackbarSeverity> {
    override val values: Sequence<SnackbarSeverity>
        get() = SnackbarSeverity.values().asSequence()
}