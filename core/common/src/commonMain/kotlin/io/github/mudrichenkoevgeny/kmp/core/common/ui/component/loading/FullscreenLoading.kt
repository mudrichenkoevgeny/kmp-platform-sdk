package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import kotlinx.coroutines.delay

/**
 * Fullscreen loading indicator.
 *
 * When [delayMillis] is greater than zero, the indicator is shown only after the delay.
 *
 * @param modifier Layout modifier.
 * @param delayMillis Delay before the indicator becomes visible.
 */
@Composable
fun FullscreenLoading(
    modifier: Modifier = Modifier,
    delayMillis: Long = FullscreenLoadingConfig.DELAY_MILLIS
) {
    var isVisible by remember { mutableStateOf(delayMillis <= 0L) }

    LaunchedEffect(Unit) {
        if (delayMillis > 0L) {
            delay(delayMillis)
            isVisible = true
        }
    }

    if (isVisible) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(CoreTheme.dimens.progressIndicatorSizeLarge),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = CoreTheme.dimens.progressIndicatorStrokeWidth
            )
        }
    }
}

/**
 * Configuration defaults for the loading screen.
 */
object FullscreenLoadingConfig {
    /**
     * Default delay to prevent flickering for fast operations.
     */
    const val DELAY_MILLIS = 250L
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun FullscreenLoadingComponentSizePreview() {
    CoreTheme {
        Surface {
            FullscreenLoading(delayMillis = 0L)
        }
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun FullscreenLoadingThemePreview() {
    CoreTheme {
        Surface {
            FullscreenLoading(delayMillis = 0L)
        }
    }
}
