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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.OnBottomReached
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.PagingFooter
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.session.SessionListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.session_revoke
import io.github.mudrichenkoevgeny.kmp.feature.user.session_revoke_all_others
import io.github.mudrichenkoevgeny.kmp.feature.user.sessions
import io.github.mudrichenkoevgeny.kmp.feature.user.session_expires_at
import io.github.mudrichenkoevgeny.kmp.feature.user.session_last_accessed
import io.github.mudrichenkoevgeny.kmp.feature.user.session_ip_address
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.session.item.SessionItem
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionListScreen(component: SessionListComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.sessions),
                        modifier = Modifier.testTag(SessionListTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(SessionListTestTags.BACK_BUTTON)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = component::onRefresh,
                        modifier = Modifier.testTag(SessionListTestTags.REFRESH_BUTTON)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
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
                    if (currentState.paging.isInitialLoading) {
                        FullscreenLoading()
                    } else {
                        Content(
                            state = currentState,
                            onRevokeSession = component::onRevokeSessionClick,
                            onRevokeAllOthers = component::onRevokeAllOtherSessionsClick,
                            onLoadNextPage = component::onLoadNextPage
                        )
                    }
                }
                is SessionListScreenState.Error -> {
                    Text(
                        text = currentState.error.toLocalizedMessage(),
                        color = MaterialTheme.colorScheme.error,
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
    onRevokeSession: (UserSessionId) -> Unit,
    onRevokeAllOthers: () -> Unit,
    onLoadNextPage: () -> Unit
) {
    val listState = rememberLazyListState()

    listState.OnBottomReached {
        onLoadNextPage()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (state.paging.items.size > 1) {
            Button(
                onClick = onRevokeAllOthers,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.paddingMedium)
                    .testTag(SessionListTestTags.REVOKE_ALL_OTHERS_BUTTON),
                enabled = !state.actionLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(Res.string.session_revoke_all_others))
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .testTag(SessionListTestTags.SESSION_LIST),
            contentPadding = PaddingValues(Dimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
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
            Text(
                text = it.toLocalizedMessage(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(Dimens.paddingMedium)
                    .fillMaxWidth()
                    .testTag(testTag)
            )
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun SessionListContentPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                Content(
                    state = SessionListScreenState.Content(
                        paging = PaginationState(
                            items = listOf(
                                userSessionMock(),
                                userSessionMock()
                            )
                        )
                    ),
                    onRevokeSession = {},
                    onRevokeAllOthers = {},
                    onLoadNextPage = {}
                )
            }
        }
    }
}

internal object SessionListTestTags {
    const val TITLE = "SessionList_Title"
    const val BACK_BUTTON = "SessionList_BackButton"
    const val REFRESH_BUTTON = "SessionList_RefreshButton"
    const val GLOBAL_ERROR_TEXT = "SessionList_GlobalErrorText"
    const val SESSION_LIST = "SessionList_List"
    const val SESSION_ITEM_PREFIX = "SessionList_Item_"
    const val REVOKE_ALL_OTHERS_BUTTON = "SessionList_RevokeAllOthersButton"
    const val ACTION_ERROR_TEXT = "SessionList_ActionErrorText"
}
