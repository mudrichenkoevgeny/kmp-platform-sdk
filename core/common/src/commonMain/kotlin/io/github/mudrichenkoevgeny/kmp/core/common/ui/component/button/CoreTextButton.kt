package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_action
import org.jetbrains.compose.resources.stringResource

/**
 * Custom SDK text button component accepting a text string.
 *
 * @param text Text displayed inside the button.
 * @param onClick Callback invoked when this button is clicked.
 * @param modifier [Modifier] to be applied to the button.
 * @param enabled Controls the enabled state of this button.
 * @param shape Shape of the button container. Defaults to [CoreTheme.shapes].
 * @param colors Colors used for the button container and content.
 * @param elevation Elevation for the button container.
 * @param border Optional border stroke drawn around the button.
 * @param contentPadding Padding applied inside the button container.
 * @param interactionSource Stream of interactions for observing button state changes.
 */
@Composable
fun CoreTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = CoreTheme.shapes.small,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    interactionSource: MutableInteractionSource? = remember { MutableInteractionSource() }
) {
    CoreTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    ) {
        Text(text = text)
    }
}

/**
 * Custom SDK text button component with composable content slot.
 *
 * @param onClick Callback invoked when this button is clicked.
 * @param modifier [Modifier] to be applied to the button.
 * @param enabled Controls the enabled state of this button.
 * @param shape Shape of the button container. Defaults to [CoreTheme.shapes].
 * @param colors Colors used for the button container and content.
 * @param elevation Elevation for the button container.
 * @param border Optional border stroke drawn around the button.
 * @param contentPadding Padding applied inside the button container.
 * @param interactionSource Stream of interactions for observing button state changes.
 * @param content Composable content displayed inside the button.
 */
@Composable
fun CoreTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = CoreTheme.shapes.small,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    interactionSource: MutableInteractionSource? = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.widthIn(max = CoreTheme.dimens.maxButtonWidth).fillMaxWidth(),
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun CoreTextButtonComponentSizePreview() {
    CoreTheme {
        Surface {
            CoreTextButton(
                text = stringResource(Res.string.ui_common_action),
                onClick = {}
            )
        }
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun CoreTextButtonThemePreview() {
    CoreTheme {
        Surface {
            CoreTextButton(
                text = stringResource(Res.string.ui_common_action),
                onClick = {}
            )
        }
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun CoreTextButtonFontScalePreview() {
    CoreTheme {
        Surface {
            CoreTextButton(
                text = stringResource(Res.string.ui_common_action),
                onClick = {}
            )
        }
    }
}
