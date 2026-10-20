package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list

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
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.OnBottomReached
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.PagingFooter
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenOverlayLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.scrollbar.CoreLazyColumnScrollbar
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreSmallText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.identifier.SelfIdentifierListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth.AuthProviderButton
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth.AuthProviderButtonMode
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth.AuthProviderGrid
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.identifier.item.IdentifierItem
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierSummary
import org.jetbrains.compose.resources.painterResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelfIdentifierListScreen(component: SelfIdentifierListComponent) {
    val state by component.state.subscribeAsState()
    val userUi = LocalUserStrings.current.ui

    Scaffold(
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
            val currentState = state as? SelfIdentifierListScreenState.Content
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
            when (val currentState = state) {
                is SelfIdentifierListScreenState.Loading -> FullscreenLoading()
                is SelfIdentifierListScreenState.Content -> {
                    if (currentState.paging.isInitialLoading && currentState.paging.items.isEmpty()) {
                        FullscreenLoading()
                    } else {
                        Content(
                            state = currentState,
                            onIdentifierClick = component::onIdentifierClick,
                            onLoadNextPage = component::onLoadNextPage
                        )
                    }

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
                is SelfIdentifierListScreenState.Error -> {
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
    contentState: SelfIdentifierListScreenState.Content,
    dialogChild: SelfIdentifierListComponent.AddIdentifierDialogChild,
    component: SelfIdentifierListComponent
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
                        is SelfIdentifierListComponent.AddIdentifierDialogChild.ProviderSelection -> {
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
                        is SelfIdentifierListComponent.AddIdentifierDialogChild.EmailInput -> {
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
                        is SelfIdentifierListComponent.AddIdentifierDialogChild.EmailConfirm -> {
                            CoreBodyText(userUi.codeSentTo(dialogChild.email))

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
                        is SelfIdentifierListComponent.AddIdentifierDialogChild.PhoneInput -> {
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
                        is SelfIdentifierListComponent.AddIdentifierDialogChild.PhoneConfirm -> {
                            CoreBodyText(userUi.codeSentTo(dialogChild.phone))

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
private fun Content(
    state: SelfIdentifierListScreenState.Content,
    onIdentifierClick: (UserIdentifierId) -> Unit,
    onLoadNextPage: () -> Unit
) {
    val listState = rememberLazyListState()

    listState.OnBottomReached {
        onLoadNextPage()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(IdentifierListTestTags.IDENTIFIER_LIST),
                contentPadding = PaddingValues(CoreTheme.dimens.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
            ) {
                items(state.paging.items, key = { it.id.value }) { identifier ->
                    IdentifierItem(
                        identifier = identifier,
                        onClick = { onIdentifierClick(identifier.id) },
                        isCurrentIdentifier = (identifier.id == state.currentIdentifierId)
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

        if (state.actionError != null) {
            CoreErrorText(
                text = state.actionError.toLocalizedMessage(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CoreTheme.dimens.paddingMedium)
                    .testTag(IdentifierListTestTags.ACTION_ERROR_TEXT)
            )
        }
    }
}

@InternalApi
internal class SelfIdentifierListPreviewProvider : PreviewParameterProvider<SelfIdentifierListScreenState> {
    private val items: List<Pair<String, SelfIdentifierListScreenState>> = listOf(
        "Content List" to SelfIdentifierListScreenState.Content(
            paging = PaginationState(
                items = listOf(createTestUserIdentifierSummary())
            ),
            isAddIdentifierSupported = true
        ),
        "Action Loading" to SelfIdentifierListScreenState.Content(
            paging = PaginationState(items = listOf(createTestUserIdentifierSummary())),
            isAddIdentifierSupported = true,
            actionLoading = true
        ),
        "Global Error" to SelfIdentifierListScreenState.Error(error = CommonError.Unknown()),
        "Fullscreen Loading" to SelfIdentifierListScreenState.Loading
    )

    override val values: Sequence<SelfIdentifierListScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun SelfIdentifierListScreenPreviewContent(state: SelfIdentifierListScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        SelfIdentifierListScreen(
            component = SelfIdentifierListComponentMock(initialState = state)
        )
    }
}

@InternalApi
private val defaultSelfIdentifierListPreviewState = SelfIdentifierListScreenState.Content(
    paging = PaginationState(items = listOf(createTestUserIdentifierSummary())),
    isAddIdentifierSupported = true
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreviewSelf(
    @PreviewParameter(SelfIdentifierListPreviewProvider::class) state: SelfIdentifierListScreenState
) {
    ScreenPreviewContainer {
        SelfIdentifierListScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreviewSelf() {
    ScreenPreviewContainer {
        SelfIdentifierListScreenPreviewContent(state = defaultSelfIdentifierListPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreviewSelf() {
    ScreenPreviewContainer {
        SelfIdentifierListScreenPreviewContent(state = defaultSelfIdentifierListPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreviewSelf() {
    ScreenPreviewContainer {
        SelfIdentifierListScreenPreviewContent(state = defaultSelfIdentifierListPreviewState)
    }
}

object IdentifierListTestTags {
    const val TITLE = "IdentifierList_Title"
    const val BACK_BUTTON = "IdentifierList_BackButton"
    const val REFRESH_BUTTON = "IdentifierList_RefreshButton"
    const val GLOBAL_ERROR_TEXT = "IdentifierList_GlobalErrorText"
    const val IDENTIFIER_LIST = "IdentifierList_List"
    const val IDENTIFIER_ITEM_PREFIX = "IdentifierList_Item_"
    const val ADD_IDENTIFIER_BUTTON = "IdentifierList_AddIdentifierButton"
    const val ADD_IDENTIFIER_DIALOG_TITLE = "IdentifierList_AddIdentifierDialogTitle"
    const val ACTION_ERROR_TEXT = "IdentifierList_ActionErrorText"
}
