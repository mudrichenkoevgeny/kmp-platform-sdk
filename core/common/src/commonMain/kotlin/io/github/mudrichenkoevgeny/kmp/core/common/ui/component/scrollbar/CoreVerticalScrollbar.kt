package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.scrollbar

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Vertical scrollbar for scrollable columns.
 */
@Composable
expect fun CoreVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier = Modifier
)
