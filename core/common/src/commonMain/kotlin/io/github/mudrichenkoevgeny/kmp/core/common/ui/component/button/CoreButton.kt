package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewComponentSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme

/**
 * Custom SDK button component accepting a text string.
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
public fun CoreButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = CoreTheme.shapes.small,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = remember { MutableInteractionSource() }
) {
    CoreButton(
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
 * Custom SDK button component with composable content slot.
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
public fun CoreButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = CoreTheme.shapes.small,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    Button(
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
@PreviewComponentSizes
@Composable
private fun CoreButtonComponentSizePreview() {
    ComponentPreviewContainer {
        CoreButton(
            text = LocalCommonStrings.current.ui.apply,
            onClick = {}
        )
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun CoreButtonThemePreview() {
    ComponentPreviewContainer {
        CoreButton(
            text = LocalCommonStrings.current.ui.apply,
            onClick = {}
        )
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun CoreButtonFontScalePreview() {
    ComponentPreviewContainer {
        CoreButton(
            text = LocalCommonStrings.current.ui.apply,
            onClick = {}
        )
    }
}
