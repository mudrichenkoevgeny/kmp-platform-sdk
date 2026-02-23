package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens

@Composable
fun BoxScope.FullscreenOverlayLoading(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
) {
    Surface(
        color = backgroundColor,
        modifier = modifier
            .matchParentSize()
            .pointerInput(Unit) {
                detectTapGestures { }
            }
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.size(Dimens.progressIndicatorSizeLarge),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = Dimens.progressIndicatorStrokeWidth
            )
        }
    }
}