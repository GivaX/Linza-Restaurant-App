package com.example.linza_apps.ui.theme
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.linza_apps.ui.theme.backgroundDark
import com.example.linza_apps.ui.theme.backgroundDarkHighContrast
import com.example.linza_apps.ui.theme.backgroundDarkMediumContrast
import com.example.linza_apps.ui.theme.backgroundLight
import com.example.linza_apps.ui.theme.backgroundLightHighContrast
import com.example.linza_apps.ui.theme.backgroundLightMediumContrast
import com.example.linza_apps.ui.theme.errorContainerDark
import com.example.linza_apps.ui.theme.errorContainerDarkHighContrast
import com.example.linza_apps.ui.theme.errorContainerDarkMediumContrast
import com.example.linza_apps.ui.theme.errorContainerLight
import com.example.linza_apps.ui.theme.errorContainerLightHighContrast
import com.example.linza_apps.ui.theme.errorContainerLightMediumContrast
import com.example.linza_apps.ui.theme.errorDark
import com.example.linza_apps.ui.theme.errorDarkHighContrast
import com.example.linza_apps.ui.theme.errorDarkMediumContrast
import com.example.linza_apps.ui.theme.errorLight
import com.example.linza_apps.ui.theme.errorLightHighContrast
import com.example.linza_apps.ui.theme.errorLightMediumContrast
import com.example.linza_apps.ui.theme.inverseOnSurfaceDark
import com.example.linza_apps.ui.theme.inverseOnSurfaceDarkHighContrast
import com.example.linza_apps.ui.theme.inverseOnSurfaceDarkMediumContrast
import com.example.linza_apps.ui.theme.inverseOnSurfaceLight
import com.example.linza_apps.ui.theme.inverseOnSurfaceLightHighContrast
import com.example.linza_apps.ui.theme.inverseOnSurfaceLightMediumContrast
import com.example.linza_apps.ui.theme.inversePrimaryDark
import com.example.linza_apps.ui.theme.inversePrimaryDarkHighContrast
import com.example.linza_apps.ui.theme.inversePrimaryDarkMediumContrast
import com.example.linza_apps.ui.theme.inversePrimaryLight
import com.example.linza_apps.ui.theme.inversePrimaryLightHighContrast
import com.example.linza_apps.ui.theme.inversePrimaryLightMediumContrast
import com.example.linza_apps.ui.theme.inverseSurfaceDark
import com.example.linza_apps.ui.theme.inverseSurfaceDarkHighContrast
import com.example.linza_apps.ui.theme.inverseSurfaceDarkMediumContrast
import com.example.linza_apps.ui.theme.inverseSurfaceLight
import com.example.linza_apps.ui.theme.inverseSurfaceLightHighContrast
import com.example.linza_apps.ui.theme.inverseSurfaceLightMediumContrast
import com.example.linza_apps.ui.theme.onBackgroundDark
import com.example.linza_apps.ui.theme.onBackgroundDarkHighContrast
import com.example.linza_apps.ui.theme.onBackgroundDarkMediumContrast
import com.example.linza_apps.ui.theme.onBackgroundLight
import com.example.linza_apps.ui.theme.onBackgroundLightHighContrast
import com.example.linza_apps.ui.theme.onBackgroundLightMediumContrast
import com.example.linza_apps.ui.theme.onErrorContainerDark
import com.example.linza_apps.ui.theme.onErrorContainerDarkHighContrast
import com.example.linza_apps.ui.theme.onErrorContainerDarkMediumContrast
import com.example.linza_apps.ui.theme.onErrorContainerLight
import com.example.linza_apps.ui.theme.onErrorContainerLightHighContrast
import com.example.linza_apps.ui.theme.onErrorContainerLightMediumContrast
import com.example.linza_apps.ui.theme.onErrorDark
import com.example.linza_apps.ui.theme.onErrorDarkHighContrast
import com.example.linza_apps.ui.theme.onErrorDarkMediumContrast
import com.example.linza_apps.ui.theme.onErrorLight
import com.example.linza_apps.ui.theme.onErrorLightHighContrast
import com.example.linza_apps.ui.theme.onErrorLightMediumContrast
import com.example.linza_apps.ui.theme.onPrimaryContainerDark
import com.example.linza_apps.ui.theme.onPrimaryContainerDarkHighContrast
import com.example.linza_apps.ui.theme.onPrimaryContainerDarkMediumContrast
import com.example.linza_apps.ui.theme.onPrimaryContainerLight
import com.example.linza_apps.ui.theme.onPrimaryContainerLightHighContrast
import com.example.linza_apps.ui.theme.onPrimaryContainerLightMediumContrast
import com.example.linza_apps.ui.theme.onPrimaryDark
import com.example.linza_apps.ui.theme.onPrimaryDarkHighContrast
import com.example.linza_apps.ui.theme.onPrimaryDarkMediumContrast
import com.example.linza_apps.ui.theme.onPrimaryLight
import com.example.linza_apps.ui.theme.onPrimaryLightHighContrast
import com.example.linza_apps.ui.theme.onPrimaryLightMediumContrast
import com.example.linza_apps.ui.theme.onSecondaryContainerDark
import com.example.linza_apps.ui.theme.onSecondaryContainerDarkHighContrast
import com.example.linza_apps.ui.theme.onSecondaryContainerDarkMediumContrast
import com.example.linza_apps.ui.theme.onSecondaryContainerLight
import com.example.linza_apps.ui.theme.onSecondaryContainerLightHighContrast
import com.example.linza_apps.ui.theme.onSecondaryContainerLightMediumContrast
import com.example.linza_apps.ui.theme.onSecondaryDark
import com.example.linza_apps.ui.theme.onSecondaryDarkHighContrast
import com.example.linza_apps.ui.theme.onSecondaryDarkMediumContrast
import com.example.linza_apps.ui.theme.onSecondaryLight
import com.example.linza_apps.ui.theme.onSecondaryLightHighContrast
import com.example.linza_apps.ui.theme.onSecondaryLightMediumContrast
import com.example.linza_apps.ui.theme.onSurfaceDark
import com.example.linza_apps.ui.theme.onSurfaceDarkHighContrast
import com.example.linza_apps.ui.theme.onSurfaceDarkMediumContrast
import com.example.linza_apps.ui.theme.onSurfaceLight
import com.example.linza_apps.ui.theme.onSurfaceLightHighContrast
import com.example.linza_apps.ui.theme.onSurfaceLightMediumContrast
import com.example.linza_apps.ui.theme.onSurfaceVariantDark
import com.example.linza_apps.ui.theme.onSurfaceVariantDarkHighContrast
import com.example.linza_apps.ui.theme.onSurfaceVariantDarkMediumContrast
import com.example.linza_apps.ui.theme.onSurfaceVariantLight
import com.example.linza_apps.ui.theme.onSurfaceVariantLightHighContrast
import com.example.linza_apps.ui.theme.onSurfaceVariantLightMediumContrast
import com.example.linza_apps.ui.theme.onTertiaryContainerDark
import com.example.linza_apps.ui.theme.onTertiaryContainerDarkHighContrast
import com.example.linza_apps.ui.theme.onTertiaryContainerDarkMediumContrast
import com.example.linza_apps.ui.theme.onTertiaryContainerLight
import com.example.linza_apps.ui.theme.onTertiaryContainerLightHighContrast
import com.example.linza_apps.ui.theme.onTertiaryContainerLightMediumContrast
import com.example.linza_apps.ui.theme.onTertiaryDark
import com.example.linza_apps.ui.theme.onTertiaryDarkHighContrast
import com.example.linza_apps.ui.theme.onTertiaryDarkMediumContrast
import com.example.linza_apps.ui.theme.onTertiaryLight
import com.example.linza_apps.ui.theme.onTertiaryLightHighContrast
import com.example.linza_apps.ui.theme.onTertiaryLightMediumContrast
import com.example.linza_apps.ui.theme.outlineDark
import com.example.linza_apps.ui.theme.outlineDarkHighContrast
import com.example.linza_apps.ui.theme.outlineDarkMediumContrast
import com.example.linza_apps.ui.theme.outlineLight
import com.example.linza_apps.ui.theme.outlineLightHighContrast
import com.example.linza_apps.ui.theme.outlineLightMediumContrast
import com.example.linza_apps.ui.theme.outlineVariantDark
import com.example.linza_apps.ui.theme.outlineVariantDarkHighContrast
import com.example.linza_apps.ui.theme.outlineVariantDarkMediumContrast
import com.example.linza_apps.ui.theme.outlineVariantLight
import com.example.linza_apps.ui.theme.outlineVariantLightHighContrast
import com.example.linza_apps.ui.theme.outlineVariantLightMediumContrast
import com.example.linza_apps.ui.theme.primaryContainerDark
import com.example.linza_apps.ui.theme.primaryContainerDarkHighContrast
import com.example.linza_apps.ui.theme.primaryContainerDarkMediumContrast
import com.example.linza_apps.ui.theme.primaryContainerLight
import com.example.linza_apps.ui.theme.primaryContainerLightHighContrast
import com.example.linza_apps.ui.theme.primaryContainerLightMediumContrast
import com.example.linza_apps.ui.theme.primaryDark
import com.example.linza_apps.ui.theme.primaryDarkHighContrast
import com.example.linza_apps.ui.theme.primaryDarkMediumContrast
import com.example.linza_apps.ui.theme.primaryLight
import com.example.linza_apps.ui.theme.primaryLightHighContrast
import com.example.linza_apps.ui.theme.primaryLightMediumContrast
import com.example.linza_apps.ui.theme.scrimDark
import com.example.linza_apps.ui.theme.scrimDarkHighContrast
import com.example.linza_apps.ui.theme.scrimDarkMediumContrast
import com.example.linza_apps.ui.theme.scrimLight
import com.example.linza_apps.ui.theme.scrimLightHighContrast
import com.example.linza_apps.ui.theme.scrimLightMediumContrast
import com.example.linza_apps.ui.theme.secondaryContainerDark
import com.example.linza_apps.ui.theme.secondaryContainerDarkHighContrast
import com.example.linza_apps.ui.theme.secondaryContainerDarkMediumContrast
import com.example.linza_apps.ui.theme.secondaryContainerLight
import com.example.linza_apps.ui.theme.secondaryContainerLightHighContrast
import com.example.linza_apps.ui.theme.secondaryContainerLightMediumContrast
import com.example.linza_apps.ui.theme.secondaryDark
import com.example.linza_apps.ui.theme.secondaryDarkHighContrast
import com.example.linza_apps.ui.theme.secondaryDarkMediumContrast
import com.example.linza_apps.ui.theme.secondaryLight
import com.example.linza_apps.ui.theme.secondaryLightHighContrast
import com.example.linza_apps.ui.theme.secondaryLightMediumContrast
import com.example.linza_apps.ui.theme.surfaceBrightDark
import com.example.linza_apps.ui.theme.surfaceBrightDarkHighContrast
import com.example.linza_apps.ui.theme.surfaceBrightDarkMediumContrast
import com.example.linza_apps.ui.theme.surfaceBrightLight
import com.example.linza_apps.ui.theme.surfaceBrightLightHighContrast
import com.example.linza_apps.ui.theme.surfaceBrightLightMediumContrast
import com.example.linza_apps.ui.theme.surfaceContainerDark
import com.example.linza_apps.ui.theme.surfaceContainerDarkHighContrast
import com.example.linza_apps.ui.theme.surfaceContainerDarkMediumContrast
import com.example.linza_apps.ui.theme.surfaceContainerHighDark
import com.example.linza_apps.ui.theme.surfaceContainerHighDarkHighContrast
import com.example.linza_apps.ui.theme.surfaceContainerHighDarkMediumContrast
import com.example.linza_apps.ui.theme.surfaceContainerHighLight
import com.example.linza_apps.ui.theme.surfaceContainerHighLightHighContrast
import com.example.linza_apps.ui.theme.surfaceContainerHighLightMediumContrast
import com.example.linza_apps.ui.theme.surfaceContainerHighestDark
import com.example.linza_apps.ui.theme.surfaceContainerHighestDarkHighContrast
import com.example.linza_apps.ui.theme.surfaceContainerHighestDarkMediumContrast
import com.example.linza_apps.ui.theme.surfaceContainerHighestLight
import com.example.linza_apps.ui.theme.surfaceContainerHighestLightHighContrast
import com.example.linza_apps.ui.theme.surfaceContainerHighestLightMediumContrast
import com.example.linza_apps.ui.theme.surfaceContainerLight
import com.example.linza_apps.ui.theme.surfaceContainerLightHighContrast
import com.example.linza_apps.ui.theme.surfaceContainerLightMediumContrast
import com.example.linza_apps.ui.theme.surfaceContainerLowDark
import com.example.linza_apps.ui.theme.surfaceContainerLowDarkHighContrast
import com.example.linza_apps.ui.theme.surfaceContainerLowDarkMediumContrast
import com.example.linza_apps.ui.theme.surfaceContainerLowLight
import com.example.linza_apps.ui.theme.surfaceContainerLowLightHighContrast
import com.example.linza_apps.ui.theme.surfaceContainerLowLightMediumContrast
import com.example.linza_apps.ui.theme.surfaceContainerLowestDark
import com.example.linza_apps.ui.theme.surfaceContainerLowestDarkHighContrast
import com.example.linza_apps.ui.theme.surfaceContainerLowestDarkMediumContrast
import com.example.linza_apps.ui.theme.surfaceContainerLowestLight
import com.example.linza_apps.ui.theme.surfaceContainerLowestLightHighContrast
import com.example.linza_apps.ui.theme.surfaceContainerLowestLightMediumContrast
import com.example.linza_apps.ui.theme.surfaceDark
import com.example.linza_apps.ui.theme.surfaceDarkHighContrast
import com.example.linza_apps.ui.theme.surfaceDarkMediumContrast
import com.example.linza_apps.ui.theme.surfaceDimDark
import com.example.linza_apps.ui.theme.surfaceDimDarkHighContrast
import com.example.linza_apps.ui.theme.surfaceDimDarkMediumContrast
import com.example.linza_apps.ui.theme.surfaceDimLight
import com.example.linza_apps.ui.theme.surfaceDimLightHighContrast
import com.example.linza_apps.ui.theme.surfaceDimLightMediumContrast
import com.example.linza_apps.ui.theme.surfaceLight
import com.example.linza_apps.ui.theme.surfaceLightHighContrast
import com.example.linza_apps.ui.theme.surfaceLightMediumContrast
import com.example.linza_apps.ui.theme.surfaceVariantDark
import com.example.linza_apps.ui.theme.surfaceVariantDarkHighContrast
import com.example.linza_apps.ui.theme.surfaceVariantDarkMediumContrast
import com.example.linza_apps.ui.theme.surfaceVariantLight
import com.example.linza_apps.ui.theme.surfaceVariantLightHighContrast
import com.example.linza_apps.ui.theme.surfaceVariantLightMediumContrast
import com.example.linza_apps.ui.theme.tertiaryContainerDark
import com.example.linza_apps.ui.theme.tertiaryContainerDarkHighContrast
import com.example.linza_apps.ui.theme.tertiaryContainerDarkMediumContrast
import com.example.linza_apps.ui.theme.tertiaryContainerLight
import com.example.linza_apps.ui.theme.tertiaryContainerLightHighContrast
import com.example.linza_apps.ui.theme.tertiaryContainerLightMediumContrast
import com.example.linza_apps.ui.theme.tertiaryDark
import com.example.linza_apps.ui.theme.tertiaryDarkHighContrast
import com.example.linza_apps.ui.theme.tertiaryDarkMediumContrast
import com.example.linza_apps.ui.theme.tertiaryLight
import com.example.linza_apps.ui.theme.tertiaryLightHighContrast
import com.example.linza_apps.ui.theme.tertiaryLightMediumContrast

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {
  val colorScheme = when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
          val context = LocalContext.current
          if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      
      darkTheme -> darkScheme
      else -> lightScheme
  }
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = colorScheme.primary.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    content = content
  )
}

