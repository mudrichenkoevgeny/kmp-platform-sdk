package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_status
import org.jetbrains.compose.resources.stringResource

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
fun CoreScreenTitleText(
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
@ComponentSizePreviews
@Composable
private fun CoreScreenTitleTextComponentSizePreview() {
    CoreTheme {
        Surface {
            CoreScreenTitleText(
                text = stringResource(Res.string.ui_common_status)
            )
        }
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun CoreScreenTitleTextThemePreview() {
    CoreTheme {
        Surface {
            CoreScreenTitleText(
                text = stringResource(Res.string.ui_common_status)
            )
        }
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun CoreScreenTitleTextFontScalePreview() {
    CoreTheme {
        Surface {
            CoreScreenTitleText(
                text = stringResource(Res.string.ui_common_status)
            )
        }
    }
}
