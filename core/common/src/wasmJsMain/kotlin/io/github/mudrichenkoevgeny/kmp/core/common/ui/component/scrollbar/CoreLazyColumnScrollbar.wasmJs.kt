package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.scrollbar

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * WasmJs implementation of vertical scrollbar for lazy list columns.
 *
 * @param lazyListState State object to be observed for scrolling position.
 * @param modifier Modifier to be applied to the scrollbar layout.
 */
@Composable
actual fun CoreLazyColumnScrollbar(
    lazyListState: LazyListState,
    modifier: Modifier
) {
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(lazyListState),
        modifier = modifier
    )
}
