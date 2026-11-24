package io.github.mudrichenkoevgeny.kmp.core.common.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.tokens.PlatformDesignTokens

/**
 * Primary accent color for light theme.
 */
public val PrimaryLight: Color = PlatformDesignTokens.Colors.Light.primary

/**
 * Color used for content drawn on top of [PrimaryLight].
 */
public val OnPrimaryLight: Color = PlatformDesignTokens.Colors.Light.onPrimary

/**
 * Container color for primary elements in light theme.
 */
public val PrimaryContainerLight: Color = PlatformDesignTokens.Colors.Light.primaryContainer

/**
 * Color used for content drawn on top of [PrimaryContainerLight].
 */
public val OnPrimaryContainerLight: Color = PlatformDesignTokens.Colors.Light.onPrimaryContainer

/**
 * Background color for light theme.
 */
public val BackgroundLight: Color = PlatformDesignTokens.Colors.Light.background

/**
 * Color used for content drawn on top of [BackgroundLight].
 */
public val OnBackgroundLight: Color = PlatformDesignTokens.Colors.Light.onBackground

/**
 * Surface color for light theme.
 */
public val SurfaceLight: Color = PlatformDesignTokens.Colors.Light.surface

/**
 * Color used for content drawn on top of [SurfaceLight].
 */
public val OnSurfaceLight: Color = PlatformDesignTokens.Colors.Light.onSurface

/**
 * Error color for light theme.
 */
public val ErrorLight: Color = PlatformDesignTokens.Colors.Light.error

/**
 * Primary accent color for dark theme.
 */
public val PrimaryDark: Color = PlatformDesignTokens.Colors.Dark.primary

/**
 * Color used for content drawn on top of [PrimaryDark].
 */
public val OnPrimaryDark: Color = PlatformDesignTokens.Colors.Dark.onPrimary

/**
 * Container color for primary elements in dark theme.
 */
public val PrimaryContainerDark: Color = PlatformDesignTokens.Colors.Dark.primaryContainer

/**
 * Color used for content drawn on top of [PrimaryContainerDark].
 */
public val OnPrimaryContainerDark: Color = PlatformDesignTokens.Colors.Dark.onPrimaryContainer

/**
 * Background color for dark theme.
 */
public val BackgroundDark: Color = PlatformDesignTokens.Colors.Dark.background

/**
 * Color used for content drawn on top of [BackgroundDark].
 */
public val OnBackgroundDark: Color = PlatformDesignTokens.Colors.Dark.onBackground

/**
 * Surface color for dark theme.
 */
public val SurfaceDark: Color = PlatformDesignTokens.Colors.Dark.surface

/**
 * Color used for content drawn on top of [SurfaceDark].
 */
public val OnSurfaceDark: Color = PlatformDesignTokens.Colors.Dark.onSurface

/**
 * Error color for dark theme.
 */
public val ErrorDark: Color = PlatformDesignTokens.Colors.Dark.error

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
