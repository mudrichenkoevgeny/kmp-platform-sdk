package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.ic_hide
import io.github.mudrichenkoevgeny.kmp.core.common.ic_show
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewComponentSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import org.jetbrains.compose.resources.painterResource

/**
 * Specialized password input text field with toggleable password visibility.
 *
 * @param value Current password text.
 * @param onValueChange Callback invoked when text changes.
 * @param isPasswordVisible Whether password characters are visible.
 * @param onTogglePasswordVisibility Callback invoked when toggle icon is clicked.
 * @param modifier [Modifier] applied to the text field.
 * @param toggleModifier [Modifier] applied to the visibility toggle icon button.
 * @param enabled Controls whether the text field is enabled.
 * @param isError Controls error state.
 * @param label Optional composable label.
 * @param placeholder Optional composable placeholder.
 */
@Composable
public fun CorePasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    modifier: Modifier = Modifier,
    toggleModifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null
) {
    CoreOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        isError = isError,
        trailingIcon = {
            IconButton(
                onClick = onTogglePasswordVisibility,
                enabled = enabled,
                modifier = toggleModifier
            ) {
                Icon(
                    painter = painterResource(
                        if (isPasswordVisible) {
                            Res.drawable.ic_hide
                        } else {
                            Res.drawable.ic_show
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                )
            }
        }
    )
}

@InternalApi
@PreviewComponentSizes
@Composable
private fun CorePasswordTextFieldComponentSizePreview() {
    CoreTheme {
        Surface {
            CorePasswordTextField(
                value = "secret123",
                onValueChange = {},
                isPasswordVisible = false,
                onTogglePasswordVisibility = {}
            )
        }
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun CorePasswordTextFieldThemePreview() {
    CoreTheme {
        Surface {
            CorePasswordTextField(
                value = "secret123",
                onValueChange = {},
                isPasswordVisible = false,
                onTogglePasswordVisibility = {}
            )
        }
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun CorePasswordTextFieldFontScalePreview() {
    CoreTheme {
        Surface {
            CorePasswordTextField(
                value = "secret123",
                onValueChange = {},
                isPasswordVisible = false,
                onTogglePasswordVisibility = {}
            )
        }
    }
}
