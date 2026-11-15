package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.scrollbar

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * WasmJs implementation of vertical scrollbar for standard scrollable containers.
 *
 * @param scrollState State object to be observed for scrolling position.
 * @param modifier Modifier to be applied to the scrollbar layout.
 */
@Composable
actual fun CoreVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState),
        modifier = modifier
    )
}
