package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewComponentSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme

/**
 * Standard body text component styled with [MaterialTheme.typography].
 *
 * @param text Text string to display.
 * @param modifier [Modifier] applied to the text.
 * @param color Color of the text. Defaults to [Color.Unspecified].
 * @param style [TextStyle] applied to the text. Defaults to [MaterialTheme.typography].
 * @param textAlign Alignment of the text.
 * @param maxLines Maximum lines.
 * @param overflow Overflow strategy.
 */
@Composable
public fun CoreBodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

/**
 * Small text component styled with [MaterialTheme.typography].
 *
 * @param text Text string to display.
 * @param modifier [Modifier] applied to the text.
 * @param color Color of the text. Defaults to [Color.Unspecified].
 * @param style [TextStyle] applied to the text. Defaults to [MaterialTheme.typography].
 * @param textAlign Alignment of the text.
 * @param maxLines Maximum lines.
 * @param overflow Overflow strategy.
 */
@Composable
public fun CoreSmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

/**
 * Section title text component styled with [MaterialTheme.typography].
 *
 * @param text Text string to display.
 * @param modifier [Modifier] applied to the text.
 * @param color Color of the text. Defaults to [Color.Unspecified].
 * @param style [TextStyle] applied to the text. Defaults to [MaterialTheme.typography].
 * @param textAlign Alignment of the text.
 * @param maxLines Maximum lines.
 * @param overflow Overflow strategy.
 */
@Composable
public fun CoreTitleText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@InternalApi
@PreviewComponentSizes
@Composable
private fun CoreBodyTextComponentSizePreview() {
    CoreTheme {
        Surface {
            CoreBodyText(text = LocalCommonStrings.current.ui.message)
        }
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun CoreBodyTextThemePreview() {
    CoreTheme {
        Surface {
            CoreBodyText(text = LocalCommonStrings.current.ui.message)
        }
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun CoreBodyTextFontScalePreview() {
    CoreTheme {
        Surface {
            CoreBodyText(text = LocalCommonStrings.current.ui.message)
        }
    }
}
