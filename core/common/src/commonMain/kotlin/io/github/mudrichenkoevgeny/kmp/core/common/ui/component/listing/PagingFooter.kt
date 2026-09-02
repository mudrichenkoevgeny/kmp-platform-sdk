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
import androidx.compose.ui.unit.dp
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.retry
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

/**
 * A combined footer for paginated lists.
 * Shows a loading indicator or an error with a retry button based on [state].
 *
 * @param state Current [PaginationState] of the list.
 * @param onRetry Callback invoked when the retry button is clicked (usually triggers [state.toNextPageLoading]).
 */
@Composable
fun PagingFooter(
    state: PaginationState<*>,
    onRetry: () -> Unit
) {
    if (state.items.isEmpty()) return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.paddingMedium),
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
 */
@Composable
fun PagingLoadingFooter() {
    CircularProgressIndicator(
        modifier = Modifier.size(24.dp),
        strokeWidth = 2.dp,
        color = MaterialTheme.colorScheme.primary
    )
}

/**
 * Error message and retry button for the bottom of the list.
 */
@Composable
fun PagingErrorFooter(
    error: io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError,
    onRetry: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = error.toLocalizedMessage(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        if (error.isRetryable) {
            Spacer(Modifier.height(Dimens.paddingSmall))
            TextButton(onClick = onRetry) {
                Text(text = stringResource(Res.string.retry))
            }
        }
    }
}
