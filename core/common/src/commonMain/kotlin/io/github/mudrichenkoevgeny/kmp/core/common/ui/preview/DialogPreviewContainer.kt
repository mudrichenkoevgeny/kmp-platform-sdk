package io.github.mudrichenkoevgeny.kmp.core.common.ui.preview

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme

/**
 * Container for rendering dialog and bottom sheet Compose Previews with standardized theme and surface styling.
 *
 * @param modifier Modifier to be applied to the dialog container surface.
 * @param content The composable content to render inside the dialog surface.
 */
@Composable
fun DialogPreviewContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    CoreTheme {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(CoreTheme.dimens.roundedCornerShape),
            color = MaterialTheme.colorScheme.surface
        ) {
            content()
        }
    }
}
