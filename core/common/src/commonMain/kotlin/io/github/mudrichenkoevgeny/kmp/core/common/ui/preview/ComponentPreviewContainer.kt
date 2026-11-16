package io.github.mudrichenkoevgeny.kmp.core.common.ui.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme

/**
 * Container for rendering reusable UI component Compose Previews with standardized theme and surface styling.
 *
 * @param modifier Modifier to be applied to the component container surface.
 * @param content The composable content to render inside the preview surface.
 */
@Composable
public fun ComponentPreviewContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    CoreTheme {
        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.background
        ) {
            Box(contentAlignment = Alignment.Center) {
                content()
            }
        }
    }
}
