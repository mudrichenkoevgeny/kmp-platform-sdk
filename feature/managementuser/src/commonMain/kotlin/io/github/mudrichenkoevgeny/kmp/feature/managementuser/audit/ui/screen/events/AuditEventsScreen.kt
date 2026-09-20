package io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.events

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.OnBottomReached
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.PagingFooter
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.option.ListingOptionsPanel
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.domain.model.event.auditEventMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.ui.screen.events.AuditEventsComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.component.audit.item.AuditItem
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_logs_title
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditEventsScreen(component: AuditEventsComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.audit_logs_title),
                        modifier = Modifier.testTag(AuditEventsTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(AuditEventsTestTags.BACK_BUTTON)
                    )
                },
                actions = {
                    IconButton(
                        onClick = component::onToggleFilterPanel,
                        modifier = Modifier.testTag(AuditEventsTestTags.FILTER_BUTTON)
                    ) {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_filter),
                            contentDescription = null,
                            modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                        )
                    }
                    IconButton(
                        onClick = component::onRefresh,
                        modifier = Modifier.testTag(AuditEventsTestTags.REFRESH_BUTTON)
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
                is AuditEventsScreenState.Loading -> FullscreenLoading()
                is AuditEventsScreenState.Content -> {
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
                is AuditEventsScreenState.Error -> {
                    CoreErrorText(
                        text = currentState.error.toLocalizedMessage(),
                        modifier = Modifier.testTag(AuditEventsTestTags.GLOBAL_ERROR_TEXT)
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: AuditEventsScreenState.Content,
    component: AuditEventsComponent,
    onEventClick: (AuditEventId) -> Unit,
    onLoadNextPage: () -> Unit
) {
    val listState = rememberLazyListState()

    listState.OnBottomReached {
        onLoadNextPage()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = state.isFilterPanelExpanded) {
            ListingOptionsPanel(
                config = getAuditEventsListingConfig(),
                sortState = state.sortState,
                filterStates = state.filterStates,
                onSortChanged = component::onSortChanged,
                onFilterChanged = component::onFilterChanged,
                onApplyClick = component::onApplyFilters,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CoreTheme.dimens.paddingMedium)
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .testTag(AuditEventsTestTags.EVENT_LIST),
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
    }
}

@InternalApi
internal class AuditEventsPreviewProvider : PreviewParameterProvider<AuditEventsScreenState> {
    private val items: List<Pair<String, AuditEventsScreenState>> = listOf(
        "Content" to AuditEventsScreenState.Content(
            paging = PaginationState(
                items = listOf(auditEventMock(), auditEventMock())
            )
        ),
        "Error" to AuditEventsScreenState.Error(error = CommonError.Unknown()),
        "Loading" to AuditEventsScreenState.Loading
    )

    override val values: Sequence<AuditEventsScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun AuditEventsScreenPreviewContent(state: AuditEventsScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        AuditEventsScreen(
            component = AuditEventsComponentMock(initialState = state)
        )
    }
}

@InternalApi
private val defaultAuditEventsPreviewState = AuditEventsScreenState.Content(
    paging = PaginationState(items = listOf(auditEventMock(), auditEventMock()))
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(AuditEventsPreviewProvider::class) state: AuditEventsScreenState
) {
    ScreenPreviewContainer {
        AuditEventsScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        AuditEventsScreenPreviewContent(state = defaultAuditEventsPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        AuditEventsScreenPreviewContent(state = defaultAuditEventsPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        AuditEventsScreenPreviewContent(state = defaultAuditEventsPreviewState)
    }
}

object AuditEventsTestTags {
    const val TITLE = "AuditEvents_Title"
    const val BACK_BUTTON = "AuditEvents_BackButton"
    const val FILTER_BUTTON = "AuditEvents_FilterButton"
    const val REFRESH_BUTTON = "AuditEvents_RefreshButton"
    const val GLOBAL_ERROR_TEXT = "AuditEvents_GlobalErrorText"
    const val EVENT_LIST = "AuditEvents_List"
    const val AUDIT_ITEM_PREFIX = "AuditEvents_Item_"
}
