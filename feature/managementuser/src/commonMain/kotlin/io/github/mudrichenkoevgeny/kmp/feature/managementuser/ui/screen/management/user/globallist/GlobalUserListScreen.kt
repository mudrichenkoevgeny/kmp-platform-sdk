package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.ic_add
import io.github.mudrichenkoevgeny.kmp.core.common.ic_filter
import io.github.mudrichenkoevgeny.kmp.core.common.ic_refresh
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.ListingEmptyState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.ListingHeaderBar
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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.main.GlobalUserListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.component.user.item.UserItem
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.users_management_title
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

/**
 * Main management screen for displaying paginated list of all users, filtering, and creating new users.
 *
 * @param component Controller driving state and callbacks.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalUserListScreen(component: GlobalUserListComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.users_management_title),
                        modifier = Modifier.testTag(GlobalUserListTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(GlobalUserListTestTags.BACK_BUTTON)
                    )
                },
                actions = {
                    IconButton(
                        onClick = component::onToggleFilterPanel,
                        modifier = Modifier.testTag(GlobalUserListTestTags.FILTER_BUTTON)
                    ) {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_filter),
                            contentDescription = null,
                            modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                        )
                    }
                    IconButton(
                        onClick = component::onRefresh,
                        modifier = Modifier.testTag(GlobalUserListTestTags.REFRESH_BUTTON)
                    ) {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_refresh),
                            contentDescription = null,
                            modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = component::onCreateUserClick,
                modifier = Modifier.testTag(GlobalUserListTestTags.CREATE_USER_FAB)
            ) {
                Icon(
                    painter = painterResource(CommonRes.drawable.ic_add),
                    contentDescription = null
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = state) {
                is GlobalUserListScreenState.Loading -> FullscreenLoading()
                is GlobalUserListScreenState.Content -> {
                    if (currentState.paging.isInitialLoading && currentState.paging.items.isEmpty()) {
                        FullscreenLoading()
                    } else {
                        Content(
                            state = currentState,
                            component = component,
                            onUserClick = component::onUserClick,
                            onLoadNextPage = component::onLoadNextPage
                        )
                    }
                }
                is GlobalUserListScreenState.Error -> {
                    CoreErrorText(
                        text = currentState.error.toLocalizedMessage(),
                        modifier = Modifier.testTag(GlobalUserListTestTags.GLOBAL_ERROR_TEXT)
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: GlobalUserListScreenState.Content,
    component: GlobalUserListComponent,
    onUserClick: (UserId) -> Unit,
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
            AnimatedVisibility(visible = state.isFilterPanelExpanded) {
                ListingOptionsPanel(
                    config = getGlobalUserListingOptionsConfig(),
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

            if (state.paging.isEmpty && !state.paging.isInitialLoading && state.paging.error == null) {
                ListingEmptyState(
                    modifier = Modifier.weight(1f)
                )
            } else {
                ListingHeaderBar(
                    state = state.paging,
                    lazyListState = listState
                )

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .testTag(GlobalUserListTestTags.USER_LIST),
                    contentPadding = PaddingValues(CoreTheme.dimens.paddingMedium),
                    verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
                ) {
                    items(state.paging.items, key = { it.id.value }) { user ->
                        UserItem(
                            user = user,
                            onClick = { onUserClick(user.id) }
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

            ErrorText(
                error = state.actionError,
                testTag = GlobalUserListTestTags.ACTION_ERROR_TEXT
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
private fun ErrorText(error: AppError?, testTag: String) {
    if (error != null) {
        CoreErrorText(
            text = error.toLocalizedMessage(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(CoreTheme.dimens.paddingMedium)
                .testTag(testTag)
        )
    }
}

@InternalApi
internal class GlobalUserListPreviewProvider : PreviewParameterProvider<GlobalUserListScreenState> {
    private val items: List<Pair<String, GlobalUserListScreenState>> = listOf(
        "Content" to GlobalUserListScreenState.Content(
            paging = PaginationState(
                items = listOf(userDetailsMock(), userDetailsMock())
            )
        ),
        "Error" to GlobalUserListScreenState.Error(error = CommonError.Unknown()),
        "Loading" to GlobalUserListScreenState.Loading
    )

    override val values: Sequence<GlobalUserListScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun GlobalUserListScreenPreviewContent(state: GlobalUserListScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        GlobalUserListScreen(
            component = GlobalUserListComponentMock(initialState = state)
        )
    }
}

@InternalApi
private val defaultGlobalUserListPreviewState = GlobalUserListScreenState.Content(
    paging = PaginationState(items = listOf(userDetailsMock(), userDetailsMock()))
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(GlobalUserListPreviewProvider::class) state: GlobalUserListScreenState
) {
    ScreenPreviewContainer {
        GlobalUserListScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        GlobalUserListScreenPreviewContent(state = defaultGlobalUserListPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        GlobalUserListScreenPreviewContent(state = defaultGlobalUserListPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        GlobalUserListScreenPreviewContent(state = defaultGlobalUserListPreviewState)
    }
}

object GlobalUserListTestTags {
    const val TITLE = "GlobalUserList_Title"
    const val BACK_BUTTON = "GlobalUserList_BackButton"
    const val FILTER_BUTTON = "GlobalUserList_FilterButton"
    const val REFRESH_BUTTON = "GlobalUserList_RefreshButton"
    const val CREATE_USER_FAB = "GlobalUserList_CreateUserFab"
    const val GLOBAL_ERROR_TEXT = "GlobalUserList_GlobalErrorText"
    const val USER_LIST = "GlobalUserList_List"
    const val USER_ITEM_PREFIX = "GlobalUserList_Item_"
    const val ACTION_ERROR_TEXT = "GlobalUserList_ACTION_ERROR_TEXT"
}