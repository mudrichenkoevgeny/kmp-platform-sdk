package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewComponentSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes

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
public fun CoreCodeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}

@InternalApi
@PreviewComponentSizes
@Composable
private fun CoreCodeTextFieldComponentSizePreview() {
    ComponentPreviewContainer {
        CoreCodeTextField(
            value = "123456",
            onValueChange = {}
        )
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun CoreCodeTextFieldThemePreview() {
    ComponentPreviewContainer {
        CoreCodeTextField(
            value = "123456",
            onValueChange = {}
        )
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun CoreCodeTextFieldFontScalePreview() {
    ComponentPreviewContainer {
        CoreCodeTextField(
            value = "123456",
            onValueChange = {}
        )
    }
}
