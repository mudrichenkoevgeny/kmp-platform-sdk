package io.github.mudrichenkoevgeny.kmp.core.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

/**
 * The core theme for the KMP Platform SDK.
 * Wrap screens or app roots with this composable to apply SDK styling.
 *
 * @param darkTheme Whether dark theme should be applied. Defaults to system setting.
 * @param colorScheme Color palette for the theme.
 * @param typography Typography styles for the theme. Defaults to [CoreTypography].
 * @param shapes Shapes for component corners. Defaults to [CoreShapes].
 * @param dimens Layout dimensions and paddings. Defaults to default [CoreDimens].
 * @param content The composable UI tree to render inside this theme.
 */
@Composable
public fun CoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme = if (darkTheme) CoreDarkColorScheme else CoreLightColorScheme,
    typography: Typography = CoreTypography,
    shapes: Shapes = CoreShapes,
    dimens: CoreDimens = CoreDimens(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalCoreDimens provides dimens
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}

/**
 * Main theme object to access theme properties across SDK components.
 */
public object CoreTheme {
    /**
     * Color scheme for the current theme.
     */
    public val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    /**
     * Typography settings for the current theme.
     */
    public val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    /**
     * Shape definitions for the current theme.
     */
    public val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    /**
     * Dimension settings for the current theme.
     */
    public val dimens: CoreDimens
        @Composable
        @ReadOnlyComposable
        get() = LocalCoreDimens.current
}
