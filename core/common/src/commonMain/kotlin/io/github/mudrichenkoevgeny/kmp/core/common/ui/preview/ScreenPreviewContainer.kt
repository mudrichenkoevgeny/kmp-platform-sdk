package io.github.mudrichenkoevgeny.kmp.core.common.ui.preview

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme

/**
 * Wrapper for Compose Previews that determines layout form factor based on canvas dimensions.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param content The composable content to render, receiving a boolean flag
 * indicating whether the canvas represents a mobile viewport.
 */
@Composable
fun ScreenPreviewContainer(
    modifier: Modifier = Modifier,
    content: @Composable (isMobile: Boolean) -> Unit
) {
    CoreTheme {
        BoxWithConstraints(modifier = modifier) {
            val isDesktopPreview = maxWidth == ScreenSizePreviewSpecs.WIDTH_DESKTOP.dp &&
                    maxHeight == ScreenSizePreviewSpecs.HEIGHT_DESKTOP.dp
            val isMobile = !isDesktopPreview

            content(isMobile)
        }
    }
}