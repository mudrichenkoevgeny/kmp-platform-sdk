package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewComponentSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.generated.resources.ic_warning
import org.jetbrains.compose.resources.painterResource
import io.github.mudrichenkoevgeny.kmp.core.generated.resources.Res as CommonRes

/**
 * Placeholder component when a paginated list returns no items.
 *
 * @param modifier Optional [Modifier].
 * @param text Message to display.
 */
@Composable
public fun ListingEmptyState(
    modifier: Modifier = Modifier,
    text: String = LocalCommonStrings.current.ui.emptyList
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(CoreTheme.dimens.paddingLarge),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
        ) {
            Icon(
                painter = painterResource(CommonRes.drawable.ic_warning),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(CoreTheme.dimens.iconButtonSize)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@InternalApi
@PreviewComponentSizes
@Composable
private fun ListingEmptyStateComponentSizePreview() {
    ComponentPreviewContainer {
        ListingEmptyState()
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ListingEmptyStateThemePreview() {
    ComponentPreviewContainer {
        ListingEmptyState()
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun ListingEmptyStateFontScalePreview() {
    ComponentPreviewContainer {
        ListingEmptyState()
    }
}
