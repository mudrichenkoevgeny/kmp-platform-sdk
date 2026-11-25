package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewComponentSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes

/**
 * Standardized screen title text component for top app bars and header layouts.
 *
 * @param text The title text to display.
 * @param modifier [Modifier] applied to the text.
 * @param style [TextStyle] applied to the text. Defaults to [MaterialTheme.typography].
 * @param maxLines Maximum number of lines for the title text. Defaults to 1.
 * @param overflow [TextOverflow] strategy when text exceeds available space.
 */
@Composable
public fun CoreScreenTitleText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    Text(
        text = text,
        modifier = modifier,
        style = style,
        maxLines = maxLines,
        overflow = overflow
    )
}

@InternalApi
@PreviewComponentSizes
@Composable
private fun CoreScreenTitleTextComponentSizePreview() {
    ComponentPreviewContainer {
        CoreScreenTitleText(
            text = LocalCommonStrings.current.ui.status
        )
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun CoreScreenTitleTextThemePreview() {
    ComponentPreviewContainer {
        CoreScreenTitleText(
            text = LocalCommonStrings.current.ui.status
        )
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun CoreScreenTitleTextFontScalePreview() {
    ComponentPreviewContainer {
        CoreScreenTitleText(
            text = LocalCommonStrings.current.ui.status
        )
    }
}
