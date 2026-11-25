package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewComponentSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme

/**
 * A combined footer for paginated lists.
 * Shows a loading indicator or an error with a retry button based on [state].
 *
 * @param state Current [PaginationState] of the list.
 * @param modifier [Modifier] to be applied to the footer.
 * @param onRetry Callback invoked when the retry button is clicked.
 */
@Composable
public fun PagingFooter(
    state: PaginationState<*>,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {}
) {
    if (state.items.isEmpty()) {
        return
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(CoreTheme.dimens.paddingMedium),
        contentAlignment = Alignment.Center
    ) {
        if (state.isNextPageLoading) {
            PagingLoadingFooter()
        } else if (state.error != null) {
            PagingErrorFooter(
                error = state.error,
                onRetry = onRetry
            )
        }
    }
}

/**
 * Simple loading indicator for the bottom of the list.
 *
 * @param modifier [Modifier] to be applied to the loading indicator.
 */
@Composable
public fun PagingLoadingFooter(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(CoreTheme.dimens.progressIndicatorSizeSmall),
        strokeWidth = CoreTheme.dimens.progressIndicatorStrokeWidthSmall,
        color = MaterialTheme.colorScheme.primary
    )
}

/**
 * Error message and retry button for the bottom of the list.
 *
 * @param error Error to display.
 * @param modifier [Modifier] to be applied to the error footer.
 * @param onRetry Callback invoked when the retry button is clicked.
 */
@Composable
public fun PagingErrorFooter(
    error: AppError,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = error.toLocalizedMessage(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        if (error.isRetryable) {
            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
            TextButton(onClick = onRetry) {
                Text(text = LocalCommonStrings.current.ui.retry)
            }
        }
    }
}

@InternalApi
@PreviewComponentSizes
@Composable
private fun PagingLoadingFooterComponentSizePreview() {
    ComponentPreviewContainer {
        PagingLoadingFooter()
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun PagingLoadingFooterThemePreview() {
    ComponentPreviewContainer {
        PagingLoadingFooter()
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun PagingLoadingFooterFontScalePreview() {
    ComponentPreviewContainer {
        PagingLoadingFooter()
    }
}
