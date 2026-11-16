package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.ic_filter
import io.github.mudrichenkoevgeny.kmp.core.common.ic_refresh
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreCodeTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreEmailTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreOutlinedTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.ListingEmptyState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.ListingHeaderBar
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.OnBottomReach
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.PagingFooter
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.option.ListingOptionsPanel
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenOverlayLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.scrollbar.CoreLazyColumnScrollbar
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.dialog.AddIdentifierDialogChild
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreSmallText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewScreenSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.identifier.IdentifierListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth.AuthProviderButton
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth.AuthProviderButtonMode
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth.AuthProviderGrid
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.identifier.item.IdentifierItem
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierSummary
import org.jetbrains.compose.resources.painterResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

/**
 * Universal Screen displaying a paginated list of user identifiers (usable in both client and management contexts).
 *
 * @param component Decompose controller driving state and callbacks.
 * @param modifier Optional [Modifier] for screen positioning.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun IdentifierListScreen(
    component: IdentifierListComponent,
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
                        text = userUi.identifiers,
                        modifier = Modifier.testTag(IdentifierListTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(IdentifierListTestTags.BACK_BUTTON)
                    )
                },
                actions = {
                    if (component.includeFilterPanel) {
                        IconButton(
                            onClick = component::onToggleFilterPanel,
                            modifier = Modifier.testTag(IdentifierListTestTags.FILTER_BUTTON)
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
                        modifier = Modifier.testTag(IdentifierListTestTags.REFRESH_BUTTON)
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
        bottomBar = {
            val currentState = state as? IdentifierListScreenState.Content
            if (currentState?.isAddIdentifierSupported == true) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(CoreTheme.dimens.paddingMedium)
                ) {
                    CoreButton(
                        text = userUi.addIdentifier,
                        onClick = component::onAddIdentifierClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(IdentifierListTestTags.ADD_IDENTIFIER_BUTTON)
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val currentState = state
            when (currentState) {
                is IdentifierListScreenState.Loading -> FullscreenLoading()
                is IdentifierListScreenState.Content -> {
                    if (currentState.paging.isInitialLoading && currentState.paging.items.isEmpty()) {
                        FullscreenLoading()
                    } else {
                        Content(
                            state = currentState,
                            component = component,
                            onIdentifierClick = component::onIdentifierClick,
                            onLoadNextPage = component::onLoadNextPage
                        )
                    }

                    if (currentState.isAddIdentifierSupported) {
                        val dialogSlot by component.addIdentifierDialogSlot.subscribeAsState()
                        val dialogChild = dialogSlot.child?.instance
                        if (dialogChild != null) {
                            AddIdentifierDialog(
                                contentState = currentState,
                                dialogChild = dialogChild,
                                component = component
                            )
                        }
                    }
                }

                is IdentifierListScreenState.Error -> {
                    CoreErrorText(
                        text = currentState.error.toLocalizedMessage(),
                        modifier = Modifier.testTag(IdentifierListTestTags.GLOBAL_ERROR_TEXT)
                    )
                }
            }
        }
    }
}

@Composable
private fun AddIdentifierDialog(
    contentState: IdentifierListScreenState.Content,
    dialogChild: AddIdentifierDialogChild,
    component: IdentifierListComponent
) {
    val userUi = LocalUserStrings.current.ui
    AlertDialog(
        onDismissRequest = component::onAddIdentifierDialogDismiss,
        title = {
            CoreTitleText(
                text = userUi.addIdentifier,
                modifier = Modifier.testTag(IdentifierListTestTags.ADD_IDENTIFIER_DIALOG_TITLE)
            )
        },
        text = {
            Box {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (dialogChild) {
                        is AddIdentifierDialogChild.ProviderSelection -> {
                            AddIdentifierProviderSelectionContent(contentState, component)
                        }

                        is AddIdentifierDialogChild.EmailInput -> {
                            AddIdentifierEmailInputContent(contentState, component)
                        }

                        is AddIdentifierDialogChild.EmailConfirm -> {
                            AddIdentifierEmailConfirmContent(dialogChild.email, contentState, component)
                        }

                        is AddIdentifierDialogChild.PhoneInput -> {
                            AddIdentifierPhoneInputContent(contentState, component)
                        }

                        is AddIdentifierDialogChild.PhoneConfirm -> {
                            AddIdentifierPhoneConfirmContent(dialogChild.phone, contentState, component)
                        }
                    }

                    contentState.actionError?.let { error ->
                        CoreErrorText(
                            text = error.toLocalizedMessage(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                if (contentState.actionLoading) {
                    FullscreenOverlayLoading()
                }
            }
        },
        confirmButton = {
            CoreTextButton(
                text = userUi.cancel,
                onClick = component::onAddIdentifierDialogDismiss
            )
        }
    )
}

@Composable
private fun AddIdentifierProviderSelectionContent(
    contentState: IdentifierListScreenState.Content,
    component: IdentifierListComponent
) {
    val availableProviders = contentState.availableAuthProviders
    if (availableProviders != null) {
        availableProviders.primary.forEach { provider ->
            AuthProviderButton(
                authProvider = provider,
                onClick = { component.onAddIdentifierSelectProvider(provider) },
                mode = AuthProviderButtonMode.ADD,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (availableProviders.secondary.isNotEmpty()) {
            AuthProviderGrid(
                authProviders = availableProviders.secondary,
                onProviderClick = component::onAddIdentifierSelectProvider,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AddIdentifierEmailInputContent(
    contentState: IdentifierListScreenState.Content,
    component: IdentifierListComponent
) {
    val userUi = LocalUserStrings.current.ui
    Column {
        CoreEmailTextField(
            value = contentState.addEmail,
            onValueChange = component::onAddIdentifierEmailChanged,
            modifier = Modifier.fillMaxWidth()
        )

        CoreButton(
            text = userUi.sendCode,
            onClick = component::onAddIdentifierSendCode,
            enabled = contentState.isAddEmailValid && !contentState.actionLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AddIdentifierEmailConfirmContent(
    email: String,
    contentState: IdentifierListScreenState.Content,
    component: IdentifierListComponent
) {
    val userUi = LocalUserStrings.current.ui
    Column {
        CoreBodyText(userUi.codeSentTo(email))

        CoreCodeTextField(
            value = contentState.addCode,
            onValueChange = component::onAddIdentifierCodeChanged,
            label = { CoreBodyText(userUi.confirmationCode) },
            placeholder = { CoreBodyText(userUi.enterConfirmationCode) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

        CoreOutlinedTextField(
            value = contentState.addPassword,
            onValueChange = component::onAddIdentifierPasswordChanged,
            label = { CoreBodyText(userUi.password) },
            placeholder = { CoreBodyText(userUi.password) },
            modifier = Modifier.fillMaxWidth()
        )

        if (contentState.addResendTimerSeconds > 0) {
            CoreSmallText(userUi.resendCodeTimer(contentState.addResendTimerSeconds))
        } else {
            CoreTextButton(
                text = userUi.resendCode,
                onClick = component::onAddIdentifierSendCode,
                enabled = contentState.canResendCode
            )
        }

        CoreButton(
            text = userUi.addIdentifier,
            onClick = component::onAddIdentifierSubmit,
            enabled = contentState.canSubmitAddEmail,
            modifier = Modifier.fillMaxWidth()
        )

        CoreTextButton(
            text = userUi.changeEmail,
            onClick = component::onAddIdentifierDialogBack
        )
    }
}

@Composable
private fun AddIdentifierPhoneInputContent(
    contentState: IdentifierListScreenState.Content,
    component: IdentifierListComponent
) {
    val userUi = LocalUserStrings.current.ui
    Column {
        CoreOutlinedTextField(
            value = contentState.addPhone,
            onValueChange = component::onAddIdentifierPhoneChanged,
            label = { CoreBodyText(userUi.phoneNumber) },
            placeholder = { CoreBodyText(userUi.phoneNumber) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        CoreButton(
            text = userUi.sendCode,
            onClick = component::onAddIdentifierSendCode,
            enabled = contentState.isAddPhoneValid && !contentState.actionLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AddIdentifierPhoneConfirmContent(
    phone: String,
    contentState: IdentifierListScreenState.Content,
    component: IdentifierListComponent
) {
    val userUi = LocalUserStrings.current.ui
    Column {
        CoreBodyText(userUi.codeSentTo(phone))

        CoreCodeTextField(
            value = contentState.addCode,
            onValueChange = component::onAddIdentifierCodeChanged,
            label = { CoreBodyText(userUi.confirmationCode) },
            placeholder = { CoreBodyText(userUi.enterConfirmationCode) },
            modifier = Modifier.fillMaxWidth()
        )

        if (contentState.addResendTimerSeconds > 0) {
            CoreSmallText(userUi.resendCodeTimer(contentState.addResendTimerSeconds))
        } else {
            CoreTextButton(
                text = userUi.resendCode,
                onClick = component::onAddIdentifierSendCode,
                enabled = contentState.canResendCode
            )
        }

        CoreButton(
            text = userUi.addIdentifier,
            onClick = component::onAddIdentifierSubmit,
            enabled = contentState.canSubmitAddPhone,
            modifier = Modifier.fillMaxWidth()
        )

        CoreTextButton(
            text = userUi.changePhoneNumber,
            onClick = component::onAddIdentifierDialogBack
        )
    }
}

@Composable
private fun Content(
    state: IdentifierListScreenState.Content,
    component: IdentifierListComponent,
    onIdentifierClick: (UserIdentifierId) -> Unit,
    onLoadNextPage: () -> Unit
) {
    val listState = rememberLazyListState()

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
                        config = getIdentifierListingOptionsConfig(includeUserFilter = true),
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
                        .testTag(IdentifierListTestTags.IDENTIFIER_LIST),
                    contentPadding = PaddingValues(CoreTheme.dimens.paddingMedium),
                    verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
                ) {
                    items(state.paging.items, key = { it.id.value }) { identifier ->
                        val isSelfIdentifier = state.currentUserId != null && identifier.userId == state.currentUserId
                        val isCurrentIdentifier = identifier.id == state.currentIdentifierId
                        IdentifierItem(
                            identifier = identifier,
                            onClick = { onIdentifierClick(identifier.id) },
                            isCurrentIdentifier = isCurrentIdentifier,
                            isSelfIdentifier = isSelfIdentifier
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

            state.actionError?.let {
                CoreErrorText(
                    text = it.toLocalizedMessage(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(CoreTheme.dimens.paddingMedium)
                        .testTag(IdentifierListTestTags.ACTION_ERROR_TEXT)
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

@InternalApi
@Composable
private fun IdentifierListScreenPreviewContent(state: IdentifierListScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        IdentifierListScreen(
            component = IdentifierListComponentMock(initialState = state)
        )
    }
}

@InternalApi
private val defaultIdentifierListPreviewState = IdentifierListScreenState.Content(
    paging = PaginationState(items = listOf(createTestUserIdentifierSummary())),
    isAddIdentifierSupported = true
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(IdentifierListPreviewProvider::class) state: IdentifierListScreenState
) {
    ScreenPreviewContainer {
        IdentifierListScreenPreviewContent(state = state)
    }
}

@InternalApi
@PreviewScreenSizes
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        IdentifierListScreenPreviewContent(state = defaultIdentifierListPreviewState)
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        IdentifierListScreenPreviewContent(state = defaultIdentifierListPreviewState)
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        IdentifierListScreenPreviewContent(state = defaultIdentifierListPreviewState)
    }
}

/**
 * Test tags dictionary for [IdentifierListScreen].
 */
public object IdentifierListTestTags {
    /** Test tag for the screen title text node. */
    public const val TITLE: String = "IdentifierList_Title"

    /** Test tag for the top bar back button. */
    public const val BACK_BUTTON: String = "IdentifierList_BackButton"

    /** Test tag for the filter panel toggle button. */
    public const val FILTER_BUTTON: String = "IdentifierList_FilterButton"

    /** Test tag for the refresh button. */
    public const val REFRESH_BUTTON: String = "IdentifierList_RefreshButton"

    /** Test tag for the global error text node. */
    public const val GLOBAL_ERROR_TEXT: String = "IdentifierList_GlobalErrorText"

    /** Test tag for the lazy column displaying user identifiers. */
    public const val IDENTIFIER_LIST: String = "IdentifierList_List"

    /** Test tag prefix for individual user identifier list item cards. */
    public const val IDENTIFIER_ITEM_PREFIX: String = "IdentifierList_Item_"

    /** Test tag for the add identifier trigger button. */
    public const val ADD_IDENTIFIER_BUTTON: String = "IdentifierList_AddIdentifierButton"

    /** Test tag for the add identifier modal dialog title. */
    public const val ADD_IDENTIFIER_DIALOG_TITLE: String = "IdentifierList_AddIdentifierDialogTitle"

    /** Test tag for action error message text node. */
    public const val ACTION_ERROR_TEXT: String = "IdentifierList_ActionErrorText"
}
