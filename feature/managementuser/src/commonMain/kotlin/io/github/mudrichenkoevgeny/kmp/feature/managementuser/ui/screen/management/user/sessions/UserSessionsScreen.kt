package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.sessions

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.sessions.UserSessionsComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.user_sessions
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.session.item.SessionItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSessionsScreen(component: UserSessionsComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.user_sessions),
                        modifier = Modifier.testTag(UserSessionsTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(UserSessionsTestTags.BACK_BUTTON)
                    )
                },
                actions = {
                    IconButton(
                        onClick = component::onToggleFilterPanel,
                        modifier = Modifier.testTag(UserSessionsTestTags.FILTER_BUTTON)
                    ) {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_filter),
                            contentDescription = null,
                            modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                        )
                    }
                    IconButton(
                        onClick = component::onRefresh,
                        modifier = Modifier.testTag(UserSessionsTestTags.REFRESH_BUTTON)
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
                is UserSessionsScreenState.Loading -> FullscreenLoading()
                is UserSessionsScreenState.Content -> {
                    if (currentState.paging.isInitialLoading && currentState.paging.items.isEmpty()) {
                        FullscreenLoading()
                    } else {
                        Content(
                            state = currentState,
                            component = component,
                            onLoadNextPage = component::onLoadNextPage
                        )
                    }
                }
                is UserSessionsScreenState.Error -> {
                    CoreErrorText(
                        text = currentState.error.toLocalizedMessage(),
                        modifier = Modifier.testTag(UserSessionsTestTags.GLOBAL_ERROR_TEXT)
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: UserSessionsScreenState.Content,
    component: UserSessionsComponent,
    onLoadNextPage: () -> Unit
) {
    val listState = rememberLazyListState()

    listState.OnBottomReached {
        onLoadNextPage()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = state.isFilterPanelExpanded) {
            ListingOptionsPanel(
                config = getUserSessionsListingConfig(),
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
                .testTag(UserSessionsTestTags.SESSION_LIST),
            contentPadding = PaddingValues(CoreTheme.dimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
        ) {
            items(state.paging.items, key = { it.id.value }) { session ->
                SessionItem(
                    session = session,
                    onRevokeClick = {},
                    enabled = false
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
internal class UserSessionsPreviewProvider : PreviewParameterProvider<UserSessionsScreenState> {
    private val items: List<Pair<String, UserSessionsScreenState>> = listOf(
        "Content" to UserSessionsScreenState.Content(
            paging = PaginationState(
                items = listOf(userSessionMock(), userSessionMock())
            )
        ),
        "Error" to UserSessionsScreenState.Error(error = CommonError.Unknown()),
        "Loading" to UserSessionsScreenState.Loading
    )

    override val values: Sequence<UserSessionsScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun UserSessionsScreenPreviewContent(state: UserSessionsScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        UserSessionsScreen(
            component = UserSessionsComponentMock(initialState = state)
        )
    }
}

@InternalApi
private val defaultUserSessionsPreviewState = UserSessionsScreenState.Content(
    paging = PaginationState(items = listOf(userSessionMock(), userSessionMock()))
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(UserSessionsPreviewProvider::class) state: UserSessionsScreenState
) {
    ScreenPreviewContainer {
        UserSessionsScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        UserSessionsScreenPreviewContent(state = defaultUserSessionsPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        UserSessionsScreenPreviewContent(state = defaultUserSessionsPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        UserSessionsScreenPreviewContent(state = defaultUserSessionsPreviewState)
    }
}

object UserSessionsTestTags {
    const val TITLE = "UserSessions_Title"
    const val BACK_BUTTON = "UserSessions_BackButton"
    const val FILTER_BUTTON = "UserSessions_FilterButton"
    const val REFRESH_BUTTON = "UserSessions_RefreshButton"
    const val GLOBAL_ERROR_TEXT = "UserSessions_GlobalErrorText"
    const val SESSION_LIST = "UserSessions_List"
}
