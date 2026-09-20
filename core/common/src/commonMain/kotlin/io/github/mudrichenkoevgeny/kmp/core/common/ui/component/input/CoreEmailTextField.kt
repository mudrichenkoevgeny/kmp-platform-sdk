package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_email
import org.jetbrains.compose.resources.stringResource

/**
 * Specialized email input text field configured with email keyboard options.
 *
 * @param value Current email string.
 * @param onValueChange Callback invoked when text changes.
 * @param modifier [Modifier] applied to the text field.
 * @param label Optional composable label. Defaults to email localized string.
 * @param placeholder Optional composable placeholder. Defaults to email localized string.
 * @param isError Controls error state.
 */
@Composable
fun CoreEmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = { Text(stringResource(Res.string.ui_common_email)) },
    placeholder: @Composable (() -> Unit)? = { Text(stringResource(Res.string.ui_common_email)) },
    isError: Boolean = false
) {
    CoreOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true
    )
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun CoreEmailTextFieldComponentSizePreview() {
    CoreTheme {
        Surface {
            CoreEmailTextField(
                value = "user@example.com",
                onValueChange = {}
            )
        }
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun CoreEmailTextFieldThemePreview() {
    CoreTheme {
        Surface {
            CoreEmailTextField(
                value = "user@example.com",
                onValueChange = {}
            )
        }
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun CoreEmailTextFieldFontScalePreview() {
    CoreTheme {
        Surface {
            CoreEmailTextField(
                value = "user@example.com",
                onValueChange = {}
            )
        }
    }
}
