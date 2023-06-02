package com.ymdwiseguy.composesnackbars.ui.snackbar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.ymdwiseguy.composesnackbars.ui.domain.SnackbarSeverity
import com.ymdwiseguy.composesnackbars.ui.domain.SnackbarSeverity.ERROR
import com.ymdwiseguy.composesnackbars.ui.domain.SnackbarSeverity.INFO
import com.ymdwiseguy.composesnackbars.ui.domain.SnackbarSeverityProvider
import com.ymdwiseguy.composesnackbars.ui.theme.ComposeSnackbarsTheme
import com.ymdwiseguy.composesnackbars.ui.theme.colors
import com.ymdwiseguy.composesnackbars.ui.theme.dimensions
import com.ymdwiseguy.composesnackbars.ui.theme.shapes

@Composable
fun CustomSnackbar(
    message: String,
    severity: SnackbarSeverity = INFO,
) {

    val color = when (severity) {
        INFO -> colors.onSurface
        ERROR -> colors.error
    }

    val icon = when(severity){
        INFO -> Icons.Outlined.Info
        ERROR -> Icons.Rounded.Warning
    }

    ElevatedCard(
        modifier = Modifier
            .padding(dimensions.gapS)
            .border(1.dp, color, shapes.small),
        shape = shapes.small
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(dimensions.gapL),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.padding(end = dimensions.gapL)
            )
            Text(text = message, color = color)
        }
    }
}

@Preview(widthDp = 360, heightDp = 120)
@Composable
private fun CustomSnackbarPreview(
    @PreviewParameter(SnackbarSeverityProvider::class) severity: SnackbarSeverity
) {
    ComposeSnackbarsTheme {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CustomSnackbar(
                    message = "This is a snackbar with the severity level \"${severity.name}\"",
                    severity = severity
                )
            }
        }
    }
}