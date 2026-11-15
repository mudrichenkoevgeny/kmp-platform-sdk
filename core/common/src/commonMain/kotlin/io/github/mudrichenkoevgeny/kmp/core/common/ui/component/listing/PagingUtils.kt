package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * Triggers [onBottomReach] when the scroll position of [LazyListState] reaches
 * within [buffer] items from the bottom.
 *
 * @param buffer The number of items from the end of the list that triggers the callback.
 * @param onBottomReach Callback invoked when the list is scrolled near the bottom.
 */
@Composable
public fun LazyListState.OnBottomReach(
    buffer: Int = 3,
    onBottomReach: () -> Unit
) {
    val currentOnBottomReach by rememberUpdatedState(onBottomReach)
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
                currentOnBottomReach()
            }
    }
}
