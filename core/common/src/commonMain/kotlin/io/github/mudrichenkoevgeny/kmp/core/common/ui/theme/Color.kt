package io.github.mudrichenkoevgeny.kmp.core.common.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.tokens.GeneratedDesignTokens

/**
 * Primary accent color for light theme.
 */
public val PrimaryLight: Color = GeneratedDesignTokens.Colors.Light.primary

/**
 * Color used for content drawn on top of [PrimaryLight].
 */
public val OnPrimaryLight: Color = GeneratedDesignTokens.Colors.Light.onPrimary

/**
 * Container color for primary elements in light theme.
 */
public val PrimaryContainerLight: Color = GeneratedDesignTokens.Colors.Light.primaryContainer

/**
 * Color used for content drawn on top of [PrimaryContainerLight].
 */
public val OnPrimaryContainerLight: Color = GeneratedDesignTokens.Colors.Light.onPrimaryContainer

/**
 * Background color for light theme.
 */
public val BackgroundLight: Color = GeneratedDesignTokens.Colors.Light.background

/**
 * Color used for content drawn on top of [BackgroundLight].
 */
public val OnBackgroundLight: Color = GeneratedDesignTokens.Colors.Light.onBackground

/**
 * Surface color for light theme.
 */
public val SurfaceLight: Color = GeneratedDesignTokens.Colors.Light.surface

/**
 * Color used for content drawn on top of [SurfaceLight].
 */
public val OnSurfaceLight: Color = GeneratedDesignTokens.Colors.Light.onSurface

/**
 * Error color for light theme.
 */
public val ErrorLight: Color = GeneratedDesignTokens.Colors.Light.error

/**
 * Primary accent color for dark theme.
 */
public val PrimaryDark: Color = GeneratedDesignTokens.Colors.Dark.primary

/**
 * Color used for content drawn on top of [PrimaryDark].
 */
public val OnPrimaryDark: Color = GeneratedDesignTokens.Colors.Dark.onPrimary

/**
 * Container color for primary elements in dark theme.
 */
public val PrimaryContainerDark: Color = GeneratedDesignTokens.Colors.Dark.primaryContainer

/**
 * Color used for content drawn on top of [PrimaryContainerDark].
 */
public val OnPrimaryContainerDark: Color = GeneratedDesignTokens.Colors.Dark.onPrimaryContainer

/**
 * Background color for dark theme.
 */
public val BackgroundDark: Color = GeneratedDesignTokens.Colors.Dark.background

/**
 * Color used for content drawn on top of [BackgroundDark].
 */
public val OnBackgroundDark: Color = GeneratedDesignTokens.Colors.Dark.onBackground

/**
 * Surface color for dark theme.
 */
public val SurfaceDark: Color = GeneratedDesignTokens.Colors.Dark.surface

/**
 * Color used for content drawn on top of [SurfaceDark].
 */
public val OnSurfaceDark: Color = GeneratedDesignTokens.Colors.Dark.onSurface

/**
 * Error color for dark theme.
 */
public val ErrorDark: Color = GeneratedDesignTokens.Colors.Dark.error

/**
 * Default light color scheme configured with neutral charcoal accent and custom background colors.
 */
public val CoreLightColorScheme: ColorScheme = lightColorScheme(
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
public val CoreDarkColorScheme: ColorScheme = darkColorScheme(
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
