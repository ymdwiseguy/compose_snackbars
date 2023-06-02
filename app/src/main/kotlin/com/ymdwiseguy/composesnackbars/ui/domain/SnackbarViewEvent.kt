package com.ymdwiseguy.composesnackbars.ui.domain

import java.util.UUID

data class SnackbarViewEvent(
    val message: String,
    val severity: SnackbarSeverity = SnackbarSeverity.INFO,
    val eventId: UUID = UUID.randomUUID(),
)