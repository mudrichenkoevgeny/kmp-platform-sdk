package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.ic_back
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewComponentSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import org.jetbrains.compose.resources.painterResource

/**
 * Standard back navigation icon button for top app bars.
 *
 * @param onClick Callback invoked when the button is clicked.
 * @param modifier [Modifier] applied to the icon button.
 * @param enabled Controls the enabled state of the button.
 * @param icon [Painter] displayed inside the button. Defaults to [Res.drawable.ic_back].
 * @param contentDescription Optional accessibility content description for the icon.
 */
@Composable
public fun CoreBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Painter = painterResource(Res.drawable.ic_back),
    contentDescription: String? = null
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
        )
    }
}

@InternalApi
@PreviewComponentSizes
@Composable
private fun CoreBackButtonComponentSizePreview() {
    CoreTheme {
        Surface {
            CoreBackButton(onClick = {})
        }
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun CoreBackButtonThemePreview() {
    CoreTheme {
        Surface {
            CoreBackButton(onClick = {})
        }
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun CoreBackButtonFontScalePreview() {
    CoreTheme {
        Surface {
            CoreBackButton(onClick = {})
        }
    }
}
