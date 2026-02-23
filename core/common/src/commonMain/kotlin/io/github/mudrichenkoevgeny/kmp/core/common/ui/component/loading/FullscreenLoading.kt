package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import kotlinx.coroutines.delay

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
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(Dimens.progressIndicatorSizeLarge),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = Dimens.progressIndicatorStrokeWidth
            )
        }
    }
}

object FullscreenLoadingConfig {
    const val DELAY_MILLIS = 250L
}