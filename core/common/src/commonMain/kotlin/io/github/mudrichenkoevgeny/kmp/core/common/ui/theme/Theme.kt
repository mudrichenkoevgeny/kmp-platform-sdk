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
 * Main theme object to access theme properties.
 */
object CoreTheme {
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val dimens: CoreDimens
        @Composable
        @ReadOnlyComposable
        get() = LocalCoreDimens.current
}

/**
 * The core theme for the KMP Platform SDK.
 * Users can wrap their app or specific screens with this to apply the SDK's styling.
 * They can customize the theme by passing a custom [colorScheme], [typography], [shapes], or [dimens].
 */
@Composable
fun CoreTheme(
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