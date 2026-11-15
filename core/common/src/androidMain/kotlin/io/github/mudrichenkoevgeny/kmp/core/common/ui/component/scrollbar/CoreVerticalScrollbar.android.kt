package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.scrollbar

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Android implementation for rendering a vertical scrollbar.
 *
 * @param scrollState State object to bound and control vertical scroll.
 * @param modifier Optional modifier for layout configuration.
 */
@Composable
public actual fun CoreVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
): Unit = Unit
