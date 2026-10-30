package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.row

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme

/**
 * Standardized key-value detail row component displaying a descriptor label and value in a row layout.
 *
 * @param label Descriptor title text shown on the start side.
 * @param value Detail text value shown on the end side.
 * @param modifier Optional [Modifier] for positioning and layout.
 * @param onClick Optional callback enabling tap interaction on the row.
 */
@Composable
fun CoreDetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoreBodyText(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        CoreBodyText(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun CoreDetailRowComponentSizePreview() {
    CoreTheme {
        Surface {
            CoreDetailRow(
                label = "Status",
                value = "Active"
            )
        }
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun CoreDetailRowThemePreview() {
    CoreTheme {
        Surface {
            CoreDetailRow(
                label = "Status",
                value = "Active"
            )
        }
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun CoreDetailRowFontScalePreview() {
    CoreTheme {
        Surface {
            CoreDetailRow(
                label = "Status",
                value = "Active"
            )
        }
    }
}
