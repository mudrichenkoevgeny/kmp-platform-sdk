package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist

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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.OnBottomReached
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.PagingFooter
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.scrollbar.CoreLazyColumnScrollbar
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.sessions.UserSessionListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.revoke_all_sessions
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.user_sessions
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.session.item.SessionItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSessionListScreen(component: UserSessionListComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.user_sessions),
                        modifier = Modifier.testTag(UserSessionListTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(UserSessionListTestTags.BACK_BUTTON)
                    )
                },
                actions = {
                    IconButton(
                        onClick = component::onRefresh,
                        modifier = Modifier.testTag(UserSessionListTestTags.REFRESH_BUTTON)
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
                is UserSessionListScreenState.Loading -> FullscreenLoading()
                is UserSessionListScreenState.Content -> {
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
                is UserSessionListScreenState.Error -> {
                    CoreErrorText(
                        text = currentState.error.toLocalizedMessage(),
                        modifier = Modifier.testTag(UserSessionListTestTags.GLOBAL_ERROR_TEXT)
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: UserSessionListScreenState.Content,
    component: UserSessionListComponent,
    onLoadNextPage: () -> Unit
) {
    val listState = rememberLazyListState()

    listState.OnBottomReached {
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
            state.actionError?.let {
                CoreErrorText(
                    text = it.toLocalizedMessage(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(CoreTheme.dimens.paddingMedium)
                        .testTag(UserSessionListTestTags.ACTION_ERROR_TEXT)
                )
            }

            if (state.paging.items.size > 1) {
                CoreButton(
                    text = stringResource(Res.string.revoke_all_sessions),
                    onClick = component::onDeleteAllSessionsClick,
                    modifier = Modifier
                        .padding(horizontal = CoreTheme.dimens.paddingMedium)
                        .testTag(UserSessionListTestTags.REVOKE_ALL_BUTTON),
                    enabled = !state.actionLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .testTag(UserSessionListTestTags.SESSION_LIST),
                contentPadding = PaddingValues(CoreTheme.dimens.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
            ) {
                items(state.paging.items, key = { it.id.value }) { session ->
                    SessionItem(
                        session = session,
                        onRevokeClick = { component.onDeleteSessionClick(session.id.asHexDashString()) },
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
        }

        CoreLazyColumnScrollbar(
            lazyListState = listState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        )
    }
}

@InternalApi
internal class UserSessionsPreviewProvider : PreviewParameterProvider<UserSessionListScreenState> {
    private val items: List<Pair<String, UserSessionListScreenState>> = listOf(
        "Content" to UserSessionListScreenState.Content(
            paging = PaginationState(
                items = listOf(userSessionMock(), userSessionMock())
            )
        ),
        "Error" to UserSessionListScreenState.Error(error = CommonError.Unknown()),
        "Loading" to UserSessionListScreenState.Loading
    )

    override val values: Sequence<UserSessionListScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun UserSessionsScreenPreviewContent(state: UserSessionListScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        UserSessionListScreen(
            component = UserSessionListComponentMock(initialState = state)
        )
    }
}

@InternalApi
private val defaultUserSessionsPreviewState = UserSessionListScreenState.Content(
    paging = PaginationState(items = listOf(userSessionMock(), userSessionMock()))
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(UserSessionsPreviewProvider::class) state: UserSessionListScreenState
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

object UserSessionListTestTags {
    const val TITLE = "UserSessionList_Title"
    const val BACK_BUTTON = "UserSessionList_BackButton"
    const val REVOKE_ALL_BUTTON = "UserSessionList_RevokeAllButton"
    const val FILTER_BUTTON = "UserSessionList_FilterButton"
    const val REFRESH_BUTTON = "UserSessionList_RefreshButton"
    const val GLOBAL_ERROR_TEXT = "UserSessionList_GlobalErrorText"
    const val ACTION_ERROR_TEXT = "UserSessionList_ActionErrorText"
    const val SESSION_LIST = "UserSessionList_List"
}
