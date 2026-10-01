package io.github.mudrichenkoevgeny.kmp.core.common.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.tokens.GeneratedDesignTokens

/**
 * Primary accent color for light theme.
 */
val PrimaryLight: Color = GeneratedDesignTokens.Colors.Light.primary

/**
 * Color used for content drawn on top of [PrimaryLight].
 */
val OnPrimaryLight: Color = GeneratedDesignTokens.Colors.Light.onPrimary

/**
 * Container color for primary elements in light theme.
 */
val PrimaryContainerLight: Color = GeneratedDesignTokens.Colors.Light.primaryContainer

/**
 * Color used for content drawn on top of [PrimaryContainerLight].
 */
val OnPrimaryContainerLight: Color = GeneratedDesignTokens.Colors.Light.onPrimaryContainer

/**
 * Background color for light theme.
 */
val BackgroundLight: Color = GeneratedDesignTokens.Colors.Light.background

/**
 * Color used for content drawn on top of [BackgroundLight].
 */
val OnBackgroundLight: Color = GeneratedDesignTokens.Colors.Light.onBackground

/**
 * Surface color for light theme.
 */
val SurfaceLight: Color = GeneratedDesignTokens.Colors.Light.surface

/**
 * Color used for content drawn on top of [SurfaceLight].
 */
val OnSurfaceLight: Color = GeneratedDesignTokens.Colors.Light.onSurface

/**
 * Error color for light theme.
 */
val ErrorLight: Color = GeneratedDesignTokens.Colors.Light.error

/**
 * Primary accent color for dark theme.
 */
val PrimaryDark: Color = GeneratedDesignTokens.Colors.Dark.primary

/**
 * Color used for content drawn on top of [PrimaryDark].
 */
val OnPrimaryDark: Color = GeneratedDesignTokens.Colors.Dark.onPrimary

/**
 * Container color for primary elements in dark theme.
 */
val PrimaryContainerDark: Color = GeneratedDesignTokens.Colors.Dark.primaryContainer

/**
 * Color used for content drawn on top of [PrimaryContainerDark].
 */
val OnPrimaryContainerDark: Color = GeneratedDesignTokens.Colors.Dark.onPrimaryContainer

/**
 * Background color for dark theme.
 */
val BackgroundDark: Color = GeneratedDesignTokens.Colors.Dark.background

/**
 * Color used for content drawn on top of [BackgroundDark].
 */
val OnBackgroundDark: Color = GeneratedDesignTokens.Colors.Dark.onBackground

/**
 * Surface color for dark theme.
 */
val SurfaceDark: Color = GeneratedDesignTokens.Colors.Dark.surface

/**
 * Color used for content drawn on top of [SurfaceDark].
 */
val OnSurfaceDark: Color = GeneratedDesignTokens.Colors.Dark.onSurface

/**
 * Error color for dark theme.
 */
val ErrorDark: Color = GeneratedDesignTokens.Colors.Dark.error

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
    onSurface = OnSurfaceLight,
    error = ErrorLight
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
    onSurface = OnSurfaceDark,
    error = ErrorDark
)
