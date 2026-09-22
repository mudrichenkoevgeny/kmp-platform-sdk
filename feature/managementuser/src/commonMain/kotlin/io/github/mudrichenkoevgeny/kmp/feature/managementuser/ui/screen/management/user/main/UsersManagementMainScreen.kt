package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main

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
import androidx.compose.foundation.layout.size
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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.create_user
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.main.UsersManagementMainComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.component.user.item.UserItem
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.users_management_title
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersManagementMainScreen(component: UsersManagementMainComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.users_management_title),
                        modifier = Modifier.testTag(UsersManagementMainTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(UsersManagementMainTestTags.BACK_BUTTON)
                    )
                },
                actions = {
                    IconButton(
                        onClick = component::onToggleFilterPanel,
                        modifier = Modifier.testTag(UsersManagementMainTestTags.FILTER_BUTTON)
                    ) {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_filter),
                            contentDescription = null,
                            modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                        )
                    }
                    IconButton(
                        onClick = component::onRefresh,
                        modifier = Modifier.testTag(UsersManagementMainTestTags.REFRESH_BUTTON)
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
                modifier = Modifier.testTag(UsersManagementMainTestTags.CREATE_USER_FAB)
            ) {
                Icon(
                    painter = painterResource(CommonRes.drawable.ic_profile),
                    contentDescription = stringResource(Res.string.create_user),
                    modifier = Modifier.size(CoreTheme.dimens.progressIndicatorSizeSmall)
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
                is UsersManagementMainScreenState.Loading -> FullscreenLoading()
                is UsersManagementMainScreenState.Content -> {
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
                is UsersManagementMainScreenState.Error -> {
                    CoreErrorText(
                        text = currentState.error.toLocalizedMessage(),
                        modifier = Modifier.testTag(UsersManagementMainTestTags.GLOBAL_ERROR_TEXT)
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: UsersManagementMainScreenState.Content,
    component: UsersManagementMainComponent,
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
                    config = getUsersManagementMainListingConfig(),
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
                    .testTag(UsersManagementMainTestTags.USER_LIST),
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

            ErrorText(
                error = state.actionError,
                testTag = UsersManagementMainTestTags.ACTION_ERROR_TEXT
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
internal class UsersManagementMainPreviewProvider : PreviewParameterProvider<UsersManagementMainScreenState> {
    private val items: List<Pair<String, UsersManagementMainScreenState>> = listOf(
        "Content" to UsersManagementMainScreenState.Content(
            paging = PaginationState(
                items = listOf(userDetailsMock(), userDetailsMock())
            )
        ),
        "Error" to UsersManagementMainScreenState.Error(error = CommonError.Unknown()),
        "Loading" to UsersManagementMainScreenState.Loading
    )

    override val values: Sequence<UsersManagementMainScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun UsersManagementMainScreenPreviewContent(state: UsersManagementMainScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        UsersManagementMainScreen(
            component = UsersManagementMainComponentMock(initialState = state)
        )
    }
}

@InternalApi
private val defaultUsersManagementMainPreviewState = UsersManagementMainScreenState.Content(
    paging = PaginationState(items = listOf(userDetailsMock(), userDetailsMock()))
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(UsersManagementMainPreviewProvider::class) state: UsersManagementMainScreenState
) {
    ScreenPreviewContainer {
        UsersManagementMainScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        UsersManagementMainScreenPreviewContent(state = defaultUsersManagementMainPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        UsersManagementMainScreenPreviewContent(state = defaultUsersManagementMainPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        UsersManagementMainScreenPreviewContent(state = defaultUsersManagementMainPreviewState)
    }
}

object UsersManagementMainTestTags {
    const val TITLE = "UsersManagementMain_Title"
    const val BACK_BUTTON = "UsersManagementMain_BackButton"
    const val FILTER_BUTTON = "UsersManagementMain_FilterButton"
    const val REFRESH_BUTTON = "UsersManagementMain_RefreshButton"
    const val CREATE_USER_FAB = "UsersManagementMain_CreateUserFab"
    const val GLOBAL_ERROR_TEXT = "UsersManagementMain_GlobalErrorText"
    const val USER_LIST = "UsersManagementMain_List"
    const val USER_ITEM_PREFIX = "UsersManagementMain_Item_"
    const val ACTION_ERROR_TEXT = "UsersManagementMain_ACTION_ERROR_TEXT"
}
