package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme

/**
 * Specialized numeric confirmation code text field configured with number keyboard options.
 *
 * @param value Current code value.
 * @param onValueChange Callback invoked when text changes.
 * @param modifier [Modifier] applied to text field.
 * @param label Optional composable label.
 * @param placeholder Optional composable placeholder.
 * @param isError Controls error state.
 */
@Composable
fun CoreCodeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    isError: Boolean = false
) {
    CoreOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun CoreCodeTextFieldComponentSizePreview() {
    CoreTheme {
        Surface {
            CoreCodeTextField(
                value = "123456",
                onValueChange = {}
            )
        }
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun CoreCodeTextFieldThemePreview() {
    CoreTheme {
        Surface {
            CoreCodeTextField(
                value = "123456",
                onValueChange = {}
            )
        }
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun CoreCodeTextFieldFontScalePreview() {
    CoreTheme {
        Surface {
            CoreCodeTextField(
                value = "123456",
                onValueChange = {}
            )
        }
    }
}
