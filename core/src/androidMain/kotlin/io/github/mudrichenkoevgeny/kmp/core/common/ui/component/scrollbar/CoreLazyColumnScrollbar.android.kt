package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.scrollbar

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Android implementation for rendering a scrollbar for a lazy column.
 *
 * @param lazyListState State object to bound and control lazy list scroll.
 * @param modifier Optional modifier for layout configuration.
 */
@Composable
public actual fun CoreLazyColumnScrollbar(
    lazyListState: LazyListState,
    modifier: Modifier
): Unit = Unit
