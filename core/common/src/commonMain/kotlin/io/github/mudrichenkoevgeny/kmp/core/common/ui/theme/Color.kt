package io.github.mudrichenkoevgeny.kmp.core.common.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Primary accent color for light theme.
 */
val PrimaryLight: Color = Color(0xFF2C3137)

/**
 * Color used for content drawn on top of [PrimaryLight].
 */
val OnPrimaryLight: Color = Color(0xFFFFFFFF)

/**
 * Container color for primary elements in light theme.
 */
val PrimaryContainerLight: Color = Color(0xFFE2E6ED)

/**
 * Color used for content drawn on top of [PrimaryContainerLight].
 */
val OnPrimaryContainerLight: Color = Color(0xFF161C22)

/**
 * Background color for light theme.
 */
val BackgroundLight: Color = Color(0xFFE5E4E4)

/**
 * Color used for content drawn on top of [BackgroundLight].
 */
val OnBackgroundLight: Color = Color(0xFF191C1A)

/**
 * Surface color for light theme.
 */
val SurfaceLight: Color = Color(0xFFE5E4E4)

/**
 * Color used for content drawn on top of [SurfaceLight].
 */
val OnSurfaceLight: Color = Color(0xFF191C1A)

/**
 * Primary accent color for dark theme.
 */
val PrimaryDark: Color = Color(0xFFC4CBD4)

/**
 * Color used for content drawn on top of [PrimaryDark].
 */
val OnPrimaryDark: Color = Color(0xFF2C3137)

/**
 * Container color for primary elements in dark theme.
 */
val PrimaryContainerDark: Color = Color(0xFF424952)

/**
 * Color used for content drawn on top of [PrimaryContainerDark].
 */
val OnPrimaryContainerDark: Color = Color(0xFFE2E6ED)

/**
 * Background color for dark theme.
 */
val BackgroundDark: Color = Color(0xFF121412)

/**
 * Color used for content drawn on top of [BackgroundDark].
 */
val OnBackgroundDark: Color = Color(0xFFE2E3DC)

/**
 * Surface color for dark theme.
 */
val SurfaceDark: Color = Color(0xFF121412)

/**
 * Color used for content drawn on top of [SurfaceDark].
 */
val OnSurfaceDark: Color = Color(0xFFE2E3DC)

/**
 * Default light color scheme configured with neutral charcoal accent and custom background colors.
 */
val CoreLightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight
)

/**
 * Default dark color scheme configured with neutral charcoal accent and custom background colors.
 */
val CoreDarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark
)
