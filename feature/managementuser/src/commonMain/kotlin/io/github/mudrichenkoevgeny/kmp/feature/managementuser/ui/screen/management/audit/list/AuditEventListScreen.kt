package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.ListingEmptyState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.ListingHeaderBar
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.OnBottomReach
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.PagingFooter
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.option.ListingOptionsPanel
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.scrollbar.CoreLazyColumnScrollbar
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewScreenSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.generated.resources.ic_filter
import io.github.mudrichenkoevgeny.kmp.core.generated.resources.ic_refresh
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.audit.list.AuditEventListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.component.audit.item.AuditItem
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.audit.domain.model.event.createTestAuditEventSummaryList
import org.jetbrains.compose.resources.painterResource
import io.github.mudrichenkoevgeny.kmp.core.generated.resources.Res as CommonRes

/**
 * Screen displaying a paginated list of audit log events with filtering and sorting options.
 *
 * @param component Decompose controller for managing audit event list state and navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun AuditEventListScreen(
    component: AuditEventListComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.subscribeAsState()
    val userUi = LocalUserStrings.current.ui

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = userUi.auditLogsTitle,
                        modifier = Modifier.testTag(AuditEventListTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(AuditEventListTestTags.BACK_BUTTON)
                    )
                },
                actions = {
                    IconButton(
                        onClick = component::onToggleFilterPanel,
                        modifier = Modifier.testTag(AuditEventListTestTags.FILTER_BUTTON)
                    ) {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_filter),
                            contentDescription = null,
                            modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                        )
                    }
                    IconButton(
                        onClick = component::onRefresh,
                        modifier = Modifier.testTag(AuditEventListTestTags.REFRESH_BUTTON)
                    ) {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_refresh),
                            contentDescription = null,
                            modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = state) {
                is AuditEventListScreenState.Loading -> FullscreenLoading()
                is AuditEventListScreenState.Content -> {
                    if (currentState.paging.isInitialLoading && currentState.paging.items.isEmpty()) {
                        FullscreenLoading()
                    } else {
                        Content(
                            state = currentState,
                            component = component,
                            onEventClick = component::onEventClick,
                            onLoadNextPage = component::onLoadNextPage
                        )
                    }
                }

                is AuditEventListScreenState.Error -> {
                    CoreErrorText(
                        text = currentState.error.toLocalizedMessage(),
                        modifier = Modifier.testTag(AuditEventListTestTags.GLOBAL_ERROR_TEXT)
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: AuditEventListScreenState.Content,
    component: AuditEventListComponent,
    onEventClick: (AuditEventId) -> Unit,
    onLoadNextPage: () -> Unit
) {
    val listState = rememberLazyListState()

    listState.OnBottomReach {
        onLoadNextPage()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = state.isFilterPanelExpanded) {
            ListingOptionsPanel(
                config = getAuditEventListingOptionsConfig(),
                sortState = state.sortState,
                filterStates = state.filterStates,
                onSortChange = { sortState -> component.onSortChanged(sortState) },
                onFilterChange = { key, filterState -> component.onFilterChanged(key, filterState) },
                onApplyClick = component::onApplyFilters,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CoreTheme.dimens.paddingMedium)
            )
        }

        if (state.paging.isEmpty && !state.paging.isInitialLoading && state.paging.error == null) {
            ListingEmptyState(
                modifier = Modifier.weight(1f)
            )
        } else {
            ListingHeaderBar(
                state = state.paging,
                lazyListState = listState
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(AuditEventListTestTags.EVENT_LIST),
                    contentPadding = PaddingValues(CoreTheme.dimens.paddingMedium),
                    verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
                ) {
                    items(state.paging.items, key = { it.id.value }) { event ->
                        AuditItem(
                            event = event,
                            onClick = { onEventClick(event.id) }
                        )
                    }

                    item {
                        PagingFooter(
                            state = state.paging,
                            onRetry = onLoadNextPage
                        )
                    }
                }

                CoreLazyColumnScrollbar(
                    lazyListState = listState,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@InternalApi
internal class AuditEventListPreviewProvider : PreviewParameterProvider<AuditEventListScreenState> {
    private val items: List<Pair<String, AuditEventListScreenState>> = listOf(
        "Content" to AuditEventListScreenState.Content(
            paging = PaginationState(
                items = createTestAuditEventSummaryList(count = 2)
            )
        ),
        "Error" to AuditEventListScreenState.Error(error = CommonError.Unknown()),
        "Loading" to AuditEventListScreenState.Loading
    )

    override val values: Sequence<AuditEventListScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun AuditEventListScreenPreviewContent(state: AuditEventListScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        AuditEventListScreen(
            component = AuditEventListComponentMock(initialState = state)
        )
    }
}

@InternalApi
private val defaultAuditEventListPreviewState = AuditEventListScreenState.Content(
    paging = PaginationState(items = createTestAuditEventSummaryList(count = 2))
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(AuditEventListPreviewProvider::class) state: AuditEventListScreenState
) {
    ScreenPreviewContainer {
        AuditEventListScreenPreviewContent(state = state)
    }
}

@InternalApi
@PreviewScreenSizes
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        AuditEventListScreenPreviewContent(state = defaultAuditEventListPreviewState)
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        AuditEventListScreenPreviewContent(state = defaultAuditEventListPreviewState)
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        AuditEventListScreenPreviewContent(state = defaultAuditEventListPreviewState)
    }
}

public object AuditEventListTestTags {
    public const val TITLE: String = "AuditEventList_Title"
    public const val BACK_BUTTON: String = "AuditEventList_BackButton"
    public const val FILTER_BUTTON: String = "AuditEventList_FilterButton"
    public const val REFRESH_BUTTON: String = "AuditEventList_RefreshButton"
    public const val GLOBAL_ERROR_TEXT: String = "AuditEventList_GlobalErrorText"
    public const val EVENT_LIST: String = "AuditEventList_List"
    public const val AUDIT_ITEM_PREFIX: String = "AuditEventList_Item_"
}
