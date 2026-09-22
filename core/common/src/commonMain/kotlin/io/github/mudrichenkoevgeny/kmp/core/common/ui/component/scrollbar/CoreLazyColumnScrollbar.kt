package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.scrollbar

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Vertical scrollbar for lazy lists.
 */
@Composable
expect fun CoreLazyColumnScrollbar(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
)
