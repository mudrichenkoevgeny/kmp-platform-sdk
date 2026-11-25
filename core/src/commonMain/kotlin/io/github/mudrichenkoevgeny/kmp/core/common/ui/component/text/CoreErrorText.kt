package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewComponentSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes

/**
 * Standardized error message text component styled with error color palette and optional animated visibility.
 *
 * @param text The error message to display. If null or blank, rendering is skipped.
 * @param modifier [Modifier] applied to the text.
 * @param visible Controls whether the error text is visible. Defaults to true if text is non-blank.
 * @param animated Whether to animate appearance and disappearance using [AnimatedVisibility].
 * @param color Text color. Defaults to [MaterialTheme] error color palette.
 * @param style Text style. Defaults to [MaterialTheme] typography bodySmall style.
 * @param textAlign Text alignment. Defaults to [TextAlign.Start].
 */
@Composable
public fun CoreErrorText(
    text: String?,
    modifier: Modifier = Modifier,
    visible: Boolean = !text.isNullOrBlank(),
    animated: Boolean = false,
    color: Color = MaterialTheme.colorScheme.error,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    textAlign: TextAlign = TextAlign.Start
) {
    if (animated) {
        AnimatedVisibility(
            visible = visible && !text.isNullOrBlank(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            text?.let { nonNullText ->
                Text(
                    text = nonNullText,
                    modifier = modifier,
                    color = color,
                    style = style,
                    textAlign = textAlign
                )
            }
        }
    } else {
        if (visible && !text.isNullOrBlank()) {
            Text(
                text = text,
                modifier = modifier,
                color = color,
                style = style,
                textAlign = textAlign
            )
        }
    }
}

@InternalApi
@PreviewComponentSizes
@Composable
private fun CoreErrorTextComponentSizePreview() {
    ComponentPreviewContainer {
        CoreErrorText(
            text = LocalCommonStrings.current.errors.unknown
        )
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun CoreErrorTextThemePreview() {
    ComponentPreviewContainer {
        CoreErrorText(
            text = LocalCommonStrings.current.errors.unknown
        )
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun CoreErrorTextFontScalePreview() {
    ComponentPreviewContainer {
        CoreErrorText(
            text = LocalCommonStrings.current.errors.unknown
        )
    }
}
