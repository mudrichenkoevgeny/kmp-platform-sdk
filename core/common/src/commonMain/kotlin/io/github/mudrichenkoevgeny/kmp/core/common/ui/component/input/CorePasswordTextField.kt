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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
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
 * @param label Optional composable label.
 * @param placeholder Optional composable placeholder.
 * @param isError Controls error state.
 * @param toggleModifier [Modifier] applied to the visibility toggle icon button.
 */
@Composable
fun CorePasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    toggleModifier: Modifier = Modifier
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
@ComponentSizePreviews
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
@ThemePreviews
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
@FontScalePreviews
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
