package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.container

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.scrollbar.CoreVerticalScrollbar
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewComponentSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme

/**
 * Core container for screen body content supporting vertical scrolling.
 *
 * @param modifier [Modifier] applied to the scrollable container.
 * @param paddingValues Inner padding applied around the column content. Defaults to [CoreTheme.dimens].
 * @param verticalArrangement Vertical arrangement for children inside the column. Defaults to spaced by [CoreTheme.dimens].
 * @param horizontalAlignment Horizontal alignment for children inside the column. Defaults to [Alignment.CenterHorizontally].
 * @param scrollState [ScrollState] managing vertical scroll position.
 * @param content Composable scope content rendered inside the scrollable column.
 */
@Composable
public fun CoreScrollableScreenContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(CoreTheme.dimens.paddingLarge),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    scrollState: ScrollState = rememberScrollState(),
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = CoreTheme.dimens.maxContentWidth)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(paddingValues),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content
        )

        CoreVerticalScrollbar(
            scrollState = scrollState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        )
    }
}

@InternalApi
@PreviewComponentSizes
@Composable
private fun CoreScrollableScreenContentComponentSizePreview() {
    ComponentPreviewContainer {
        CoreScrollableScreenContent {
            Text(LocalCommonStrings.current.ui.action)
        }
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun CoreScrollableScreenContentThemePreview() {
    ComponentPreviewContainer {
        CoreScrollableScreenContent {
            Text(LocalCommonStrings.current.ui.action)
        }
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun CoreScrollableScreenContentFontScalePreview() {
    ComponentPreviewContainer {
        CoreScrollableScreenContent {
            Text(LocalCommonStrings.current.ui.action)
        }
    }
}
