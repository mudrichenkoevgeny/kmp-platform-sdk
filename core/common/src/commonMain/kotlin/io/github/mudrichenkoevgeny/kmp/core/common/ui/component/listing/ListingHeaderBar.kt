package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.ListingConstants
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_page_info
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_total_count
import org.jetbrains.compose.resources.stringResource

/**
 * Top bar displaying current page and total count info for a paginated list.
 *
 * @param state Current [PaginationState] of the list.
 * @param lazyListState [LazyListState] of the scrollable list.
 * @param modifier Optional [Modifier].
 */
@Composable
fun ListingHeaderBar(
    state: PaginationState<*>,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    if (state.items.isEmpty()) return

    val totalPages = state.totalPages
    val currentPageState = remember(state.items.size, totalPages) {
        derivedStateOf {
            if (state.items.isEmpty()) {
                0
            } else {
                val pageSize = ListingConstants.DEFAULT_PAGE_SIZE
                val computed = (lazyListState.firstVisibleItemIndex / pageSize) + 1
                if (totalPages > 0) computed.coerceAtMost(totalPages.toInt()) else computed
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = CoreTheme.dimens.paddingMedium,
                vertical = CoreTheme.dimens.paddingSmall
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.ui_common_total_count, state.totalCount),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (totalPages > 0) {
            Text(
                text = stringResource(Res.string.ui_common_page_info, currentPageState.value, totalPages),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@InternalApi
internal class ListingHeaderBarPreviewProvider : PreviewParameterProvider<PaginationState<String>> {
    override val values: Sequence<PaginationState<String>> = sequenceOf(
        PaginationState(
            items = listOf("item1", "item2"),
            pageNumber = 1,
            totalPages = 5,
            totalCount = 100
        ),
        PaginationState(
            items = listOf("item1"),
            pageNumber = 1,
            totalPages = 1,
            totalCount = 1
        )
    )
}

@InternalApi
private val defaultListingHeaderBarPreviewState = PaginationState(
    items = listOf("item1", "item2"),
    pageNumber = 1,
    totalPages = 5,
    totalCount = 100
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun ListingHeaderBarStatesPreview(
    @PreviewParameter(ListingHeaderBarPreviewProvider::class) state: PaginationState<String>
) {
    CoreTheme {
        Surface {
            ListingHeaderBar(
                state = state,
                lazyListState = rememberLazyListState()
            )
        }
    }
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun ListingHeaderBarComponentSizePreview() {
    CoreTheme {
        Surface {
            ListingHeaderBar(
                state = defaultListingHeaderBarPreviewState,
                lazyListState = rememberLazyListState()
            )
        }
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ListingHeaderBarThemePreview() {
    CoreTheme {
        Surface {
            ListingHeaderBar(
                state = defaultListingHeaderBarPreviewState,
                lazyListState = rememberLazyListState()
            )
        }
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun ListingHeaderBarFontScalePreview() {
    CoreTheme {
        Surface {
            ListingHeaderBar(
                state = defaultListingHeaderBarPreviewState,
                lazyListState = rememberLazyListState()
            )
        }
    }
}