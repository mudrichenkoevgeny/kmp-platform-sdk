package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_message
import org.jetbrains.compose.resources.stringResource

/**
 * Custom SDK outlined text field component styled with [CoreTheme] defaults.
 *
 * @param value Current text value inside the field.
 * @param onValueChange Callback invoked when text changes.
 * @param modifier [Modifier] applied to the text field.
 * @param label Optional composable label displayed above or inside the field.
 * @param placeholder Optional composable placeholder displayed when field is empty.
 * @param leadingIcon Optional leading icon composable.
 * @param trailingIcon Optional trailing icon composable.
 * @param isError Controls error state formatting.
 * @param visualTransformation Controls visual transformation of input text.
 * @param keyboardOptions Options configuring IME keyboard type and actions.
 * @param keyboardActions Actions triggered by IME keyboard buttons.
 * @param singleLine Controls single line input mode. Defaults to true.
 * @param shape Shape of the text field border container. Defaults to [CoreTheme.shapes].
 */
@Composable
fun CoreOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    shape: Shape = CoreTheme.shapes.small
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        shape = shape
    )
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun CoreOutlinedTextFieldComponentSizePreview() {
    CoreTheme {
        Surface {
            CoreOutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(stringResource(Res.string.ui_common_message)) }
            )
        }
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun CoreOutlinedTextFieldThemePreview() {
    CoreTheme {
        Surface {
            CoreOutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(stringResource(Res.string.ui_common_message)) }
            )
        }
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun CoreOutlinedTextFieldFontScalePreview() {
    CoreTheme {
        Surface {
            CoreOutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(stringResource(Res.string.ui_common_message)) }
            )
        }
    }
}
