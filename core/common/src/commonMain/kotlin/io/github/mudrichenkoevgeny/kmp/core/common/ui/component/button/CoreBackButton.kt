package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme

/**
 * Standard back navigation icon button for top app bars.
 *
 * @param onClick Callback invoked when the button is clicked.
 * @param modifier [Modifier] applied to the icon button.
 * @param enabled Controls the enabled state of the button.
 * @param icon [ImageVector] displayed inside the button. Defaults to [Icons.AutoMirrored.Filled.ArrowBack].
 * @param contentDescription Optional accessibility content description for the icon.
 */
@Composable
fun CoreBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    contentDescription: String? = null
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun CoreBackButtonComponentSizePreview() {
    CoreTheme {
        Surface {
            CoreBackButton(onClick = {})
        }
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun CoreBackButtonThemePreview() {
    CoreTheme {
        Surface {
            CoreBackButton(onClick = {})
        }
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun CoreBackButtonFontScalePreview() {
    CoreTheme {
        Surface {
            CoreBackButton(onClick = {})
        }
    }
}
