package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session

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
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.OnBottomReached
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.PagingFooter
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.option.ListingOptionsPanel
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.scrollbar.CoreLazyColumnScrollbar
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.session.SessionListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.session_revoke_all_others
import io.github.mudrichenkoevgeny.kmp.feature.user.sessions
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.session.item.SessionItem
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionListScreen(component: SessionListComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.sessions),
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
            when (val currentState = state) {
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

    listState.OnBottomReached {
        onLoadNextPage()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = state.isFilterPanelExpanded) {
            ListingOptionsPanel(
                config = getSessionListListingConfig(),
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

        if (state.paging.items.size > 1) {
            CoreButton(
                text = stringResource(Res.string.session_revoke_all_others),
                onClick = onRevokeAllOthers,
                modifier = Modifier
                    .padding(horizontal = CoreTheme.dimens.paddingMedium)
                    .testTag(SessionListTestTags.REVOKE_ALL_OTHERS_BUTTON),
                enabled = !state.actionLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(SessionListTestTags.SESSION_LIST),
                contentPadding = PaddingValues(CoreTheme.dimens.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
            ) {
                items(state.paging.items, key = { it.id.value }) { session ->
                    SessionItem(
                        session = session,
                        onRevokeClick = { onRevokeSession(session.id) },
                        enabled = !state.actionLoading
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

        ErrorText(
            error = state.actionError,
            testTag = SessionListTestTags.ACTION_ERROR_TEXT
        )
    }
}

@Composable
private fun ErrorText(error: AppError?, testTag: String) {
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
internal class SessionListPreviewProvider : PreviewParameterProvider<SessionListScreenState> {
    private val items: List<Pair<String, SessionListScreenState>> = listOf(
        "Multiple Sessions" to SessionListScreenState.Content(
            paging = PaginationState(
                items = listOf(userSessionMock(), userSessionMock())
            )
        ),
        "Single Session" to SessionListScreenState.Content(
            paging = PaginationState(
                items = listOf(userSessionMock())
            )
        ),
        "Action Loading" to SessionListScreenState.Content(
            paging = PaginationState(
                items = listOf(userSessionMock())
            ),
            actionLoading = true
        ),
        "Global Error" to SessionListScreenState.Error(error = CommonError.Unknown()),
        "Fullscreen Loading" to SessionListScreenState.Loading
    )

    override val values: Sequence<SessionListScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
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
    paging = PaginationState(items = listOf(userSessionMock(), userSessionMock()))
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
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        SessionListScreenPreviewContent(state = defaultSessionListPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        SessionListScreenPreviewContent(state = defaultSessionListPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        SessionListScreenPreviewContent(state = defaultSessionListPreviewState)
    }
}

internal object SessionListTestTags {
    const val TITLE = "SessionList_Title"
    const val BACK_BUTTON = "SessionList_BackButton"
    const val FILTER_BUTTON = "SessionList_FilterButton"
    const val REFRESH_BUTTON = "SessionList_RefreshButton"
    const val GLOBAL_ERROR_TEXT = "SessionList_GlobalErrorText"
    const val SESSION_LIST = "SessionList_List"
    const val SESSION_ITEM_PREFIX = "SessionList_Item_"
    const val REVOKE_ALL_OTHERS_BUTTON = "SessionList_RevokeAllOthersButton"
    const val ACTION_ERROR_TEXT = "SessionList_ActionErrorText"
}
