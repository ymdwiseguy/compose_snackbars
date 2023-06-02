package com.ymdwiseguy.composesnackbars.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview(showBackground = true, widthDp = 520)
@Composable
private fun ColorsScreenPreview() {
    ComposeSnackbarsTheme {
        ColorsScreen()
    }
}

@Composable
private fun ColorsScreen() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensions.gapM),
        modifier = Modifier
            .fillMaxSize()
            .padding(all = dimensions.gapM)
    ) {
        item { Text("Colors", style = typography.titleLarge) }

        item {
            PrimaryColorRow(
                color = colors.primary,
                onColor = colors.onPrimary,
                container = colors.primaryContainer,
                onContainer = colors.onPrimaryContainer,
                name = "Primary"
            )
        }

        item {
            PrimaryColorRow(
                color = colors.secondary,
                onColor = colors.onSecondary,
                container = colors.secondaryContainer,
                onContainer = colors.onSecondaryContainer,
                name = "Secondary"
            )
        }

        item {
            SingleColorRow(
                firstColor = colors.background,
                firstTextColor = colors.onBackground,
                firstColorName = "Background",
                secondColor = colors.onBackground,
                secondTextColor = colors.background,
                secondColorName = "On Background",
            )
        }
        item {
            SingleColorRow(
                firstColor = colors.surface,
                firstTextColor = colors.onSurface,
                firstColorName = "Surface",
                secondColor = colors.onSurface,
                secondTextColor = colors.surface,
                secondColorName = "On Surface"
            )
        }
        item {
            SingleColorRow(
                firstColor = colors.surfaceVariant,
                firstTextColor = colors.onSurfaceVariant,
                firstColorName = "Surface Variant",
                secondColor = colors.onSurfaceVariant,
                secondTextColor = colors.surfaceVariant,
                secondColorName = "On Surface Variant"
            )
        }

        item {
            SingleColorRow(
                firstColor = colors.outline,
                firstTextColor = Color.White,
                firstColorName = "Outline",
                secondColor = colors.outlineVariant,
                secondTextColor = Color.Black,
                secondColorName = "Outline Variant"
            )
        }

        item {
            PrimaryColorRow(
                color = colors.error,
                onColor = colors.onError,
                container = colors.errorContainer,
                onContainer = colors.onErrorContainer,
                name = "Error"
            )
        }
    }
}

@Composable
private fun PrimaryColorRow(
    color: Color,
    onColor: Color,
    container: Color,
    onContainer: Color,
    name: String,
) {
    SingleColorRow(
        firstColor = color,
        firstTextColor = onColor,
        firstColorName = name,
        secondColor = onColor,
        secondTextColor = color,
        secondColorName = "On $name",
    )
    SingleColorRow(
        firstColor = container,
        firstTextColor = onContainer,
        firstColorName = "$name Container",
        secondColor = onContainer,
        secondTextColor = container,
        secondColorName = "on $name Container",
    )
}

@Composable
private fun SingleColorRow(
    firstColor: Color,
    firstTextColor: Color,
    firstColorName: String,
    secondColor: Color,
    secondTextColor: Color,
    secondColorName: String,
) {
    Row {
        ColorBox(
            Modifier.weight(1f),
            textColor = firstTextColor,
            backgroundColor = firstColor,
            label = firstColorName,
        )
        ColorBox(
            Modifier.weight(1f),
            textColor = secondTextColor,
            backgroundColor = secondColor,
            label = secondColorName,
        )
    }
}

@Composable
private fun ColorBox(
    modifier: Modifier = Modifier,
    textColor: Color,
    backgroundColor: Color,
    label: String
) {
    Column(
        modifier
            .background(backgroundColor)
            .requiredHeight(100.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(Modifier.padding(all = dimensions.gapS)) {
            Text(
                label,
                color = textColor,
                style = typography.labelSmall
            )
        }
    }
}