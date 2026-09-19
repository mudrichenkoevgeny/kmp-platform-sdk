package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.container

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_action
import org.jetbrains.compose.resources.stringResource

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
fun CoreScrollableScreenContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(CoreTheme.dimens.paddingLarge),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    scrollState: ScrollState = rememberScrollState(),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(paddingValues),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun CoreScrollableScreenContentComponentSizePreview() {
    CoreTheme {
        Surface {
            CoreScrollableScreenContent {
                Text(stringResource(Res.string.ui_common_action))
            }
        }
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun CoreScrollableScreenContentThemePreview() {
    CoreTheme {
        Surface {
            CoreScrollableScreenContent {
                Text(stringResource(Res.string.ui_common_action))
            }
        }
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun CoreScrollableScreenContentFontScalePreview() {
    CoreTheme {
        Surface {
            CoreScrollableScreenContent {
                Text(stringResource(Res.string.ui_common_action))
            }
        }
    }
}
