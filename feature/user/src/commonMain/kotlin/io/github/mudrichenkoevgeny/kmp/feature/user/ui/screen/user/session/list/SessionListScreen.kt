package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.ic_filter
import io.github.mudrichenkoevgeny.kmp.core.common.ic_refresh
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
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
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.session.SessionListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.session.item.SessionItem
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionSummary
import org.jetbrains.compose.resources.painterResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

/**
 * Universal Screen displaying a paginated list of user active sessions (usable in both client and management contexts).
 *
 * @param component Decompose controller driving state and callbacks.
 * @param modifier Optional [Modifier] for screen positioning.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun SessionListScreen(
    component: SessionListComponent,
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
                        text = userUi.sessions,
                        modifier = Modifier.testTag(SessionListTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(SessionListTestTags.BACK_BUTTON)
                    )
                },
                actions = {
                    if (component.includeFilterPanel) {
                        IconButton(
                            onClick = component::onToggleFilterPanel,
                            modifier = Modifier.testTag(SessionListTestTags.FILTER_BUTTON)
                        ) {
                            Icon(
                                painter = painterResource(CommonRes.drawable.ic_filter),
                                contentDescription = null,
                                modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                            )
                        }
                    }
                    IconButton(
                        onClick = component::onRefresh,
                        modifier = Modifier.testTag(SessionListTestTags.REFRESH_BUTTON)
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
            val currentState = state
            when (currentState) {
                is SessionListScreenState.Loading -> FullscreenLoading()
                is SessionListScreenState.Content -> {
                    if (currentState.paging.isInitialLoading && currentState.paging.items.isEmpty()) {
                        FullscreenLoading()
                    } else {
                        Content(
                            state = currentState,
                            component = component,
                            onRevokeSession = component::onRevokeSessionClick,
                            onRevokeAllOthers = component::onRevokeAllOtherSessionsClick,
                            onLoadNextPage = component::onLoadNextPage
                        )
                    }
                }

                is SessionListScreenState.Error -> {
                    CoreErrorText(
                        text = currentState.error.toLocalizedMessage(),
                        modifier = Modifier.testTag(SessionListTestTags.GLOBAL_ERROR_TEXT)
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: SessionListScreenState.Content,
    component: SessionListComponent,
    onRevokeSession: (UserSessionId) -> Unit,
    onRevokeAllOthers: () -> Unit,
    onLoadNextPage: () -> Unit
) {
    val listState = rememberLazyListState()
    val userUi = LocalUserStrings.current.ui

    listState.OnBottomReach {
        onLoadNextPage()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = CoreTheme.dimens.maxContentWidth)
                .fillMaxSize()
        ) {
            if (component.includeFilterPanel) {
                AnimatedVisibility(visible = state.isFilterPanelExpanded) {
                    ListingOptionsPanel(
                        config = getSessionListingOptionsConfig(includeUserFilter = true),
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
            }

            if (state.paging.isEmpty && !state.paging.isInitialLoading && state.paging.error == null) {
                ListingEmptyState(
                    modifier = Modifier.weight(1f)
                )
            } else {
                if (component.includeFilterPanel) {
                    ListingHeaderBar(
                        state = state.paging,
                        lazyListState = listState
                    )
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .testTag(SessionListTestTags.SESSION_LIST),
                    contentPadding = PaddingValues(CoreTheme.dimens.paddingMedium),
                    verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
                ) {
                    items(state.paging.items, key = { it.id.value }) { session ->
                        val isCurrentSession = state.currentSessionId != null && session.id == state.currentSessionId
                        val canRevoke =
                            component.isRevokeAvailable && (!isCurrentSession || component.includeFilterPanel)

                        SessionItem(
                            session = session,
                            enabled = !state.actionLoading,
                            isCurrentSession = isCurrentSession,
                            onRevokeClick = if (canRevoke) {
                                { onRevokeSession(session.id) }
                            } else {
                                null
                            },
                            onSessionClick = { component.onSessionClick(session) }
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

            if (component.isRevokeAllOthersAvailable && state.paging.items.size > 1) {
                CoreButton(
                    text = userUi.sessionRevokeAllOthers,
                    onClick = onRevokeAllOthers,
                    modifier = Modifier
                        .padding(CoreTheme.dimens.paddingMedium)
                        .fillMaxWidth()
                        .testTag(SessionListTestTags.REVOKE_ALL_OTHERS_BUTTON),
                    enabled = !state.actionLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                )
            }

            ErrorText(
                error = state.actionError,
                testTag = SessionListTestTags.ACTION_ERROR_TEXT
            )
        }

        CoreLazyColumnScrollbar(
            lazyListState = listState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        )
    }
}

@Composable
private fun ErrorText(
    error: AppError?,
    testTag: String
) {
    AnimatedVisibility(
        visible = error != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        error?.let {
            CoreErrorText(
                text = it.toLocalizedMessage(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(CoreTheme.dimens.paddingMedium)
                    .fillMaxWidth()
                    .testTag(testTag)
            )
        }
    }
}

@InternalApi
@Composable
private fun SessionListScreenPreviewContent(state: SessionListScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        SessionListScreen(
            component = SessionListComponentMock(initialState = state)
        )
    }
}

@InternalApi
private val defaultSessionListPreviewState = SessionListScreenState.Content(
    paging = PaginationState(items = listOf(createTestUserSessionSummary(), createTestUserSessionSummary()))
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(SessionListPreviewProvider::class) state: SessionListScreenState
) {
    ScreenPreviewContainer {
        SessionListScreenPreviewContent(state = state)
    }
}

@InternalApi
@PreviewScreenSizes
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        SessionListScreenPreviewContent(state = defaultSessionListPreviewState)
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        SessionListScreenPreviewContent(state = defaultSessionListPreviewState)
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        SessionListScreenPreviewContent(state = defaultSessionListPreviewState)
    }
}

/**
 * Test tags dictionary for [SessionListScreen].
 */
public object SessionListTestTags {
    /** Test tag for the screen title text node. */
    public const val TITLE: String = "SessionList_Title"

    /** Test tag for the top bar back button. */
    public const val BACK_BUTTON: String = "SessionList_BackButton"

    /** Test tag for the filter panel toggle button. */
    public const val FILTER_BUTTON: String = "SessionList_FilterButton"

    /** Test tag for the refresh button. */
    public const val REFRESH_BUTTON: String = "SessionList_RefreshButton"

    /** Test tag for the global error text node. */
    public const val GLOBAL_ERROR_TEXT: String = "SessionList_GlobalErrorText"

    /** Test tag for the lazy column displaying active user sessions. */
    public const val SESSION_LIST: String = "SessionList_List"

    /** Test tag for the button revoking all other sessions. */
    public const val REVOKE_ALL_OTHERS_BUTTON: String = "SessionList_RevokeAllOthersButton"

    /** Test tag for action error message text node. */
    public const val ACTION_ERROR_TEXT: String = "SessionList_ActionErrorText"
}
