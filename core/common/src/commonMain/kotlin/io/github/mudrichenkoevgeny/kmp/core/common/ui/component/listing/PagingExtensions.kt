package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * Monitors the scroll position and triggers [onBottomReached] when the user scrolls near the end.
 *
 * @param buffer How many items before the end to trigger the callback.
 * @param onBottomReached Callback to load the next page.
 */
@Composable
fun LazyListState.OnBottomReached(
    buffer: Int = 3,
    onBottomReached: () -> Unit
) {
    val shouldLoadMore = remember(buffer) {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false

            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - buffer
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                onBottomReached()
            }
    }
}
