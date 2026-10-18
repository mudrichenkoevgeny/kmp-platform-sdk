package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme

/**
 * Standardized error message text component styled with error color palette.
 *
 * @param text The error message to display.
 * @param modifier [Modifier] applied to the text.
 * @param color Text color. Defaults to [MaterialTheme.colorScheme].
 * @param style Text style. Defaults to [MaterialTheme.typography].
 * @param textAlign Text alignment. Defaults to [TextAlign.Start].
 */
@Composable
fun CoreErrorText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.error,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        textAlign = textAlign
    )
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun CoreErrorTextComponentSizePreview() {
    CoreTheme {
        Surface {
            CoreErrorText(
                text = LocalCommonStrings.current.errors.unknown
            )
        }
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun CoreErrorTextThemePreview() {
    CoreTheme {
        Surface {
            CoreErrorText(
                text = LocalCommonStrings.current.errors.unknown
            )
        }
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun CoreErrorTextFontScalePreview() {
    CoreTheme {
        Surface {
            CoreErrorText(
                text = LocalCommonStrings.current.errors.unknown
            )
        }
    }
}
