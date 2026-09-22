package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreCodeTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreEmailTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreOutlinedTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CorePasswordTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.scrollbar.CoreLazyColumnScrollbar
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.OnBottomReached
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.PagingFooter
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.option.ListingOptionsPanel
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.identifier.IdentifierListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.identifier.item.IdentifierItem
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdentifierListScreen(component: IdentifierListComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.identifiers),
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
                        onClick = component::onToggleFilterPanel,
                        modifier = Modifier.testTag(IdentifierListTestTags.FILTER_BUTTON)
                    ) {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_filter),
                            contentDescription = null,
                            modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                        )
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
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = state) {
                is IdentifierListScreenState.Loading -> FullscreenLoading()
                is IdentifierListScreenState.Content -> {
                    if (currentState.paging.isInitialLoading && currentState.paging.items.isEmpty()) {
                        FullscreenLoading()
                    } else {
                        Content(
                            state = currentState,
                            component = component,
                            onDeleteIdentifier = component::onDeleteIdentifierClick,
                            onChangePasswordClick = component::onChangePasswordClick,
                            onConfirmChangePassword = component::onConfirmChangePasswordClick,
                            onDismissChangePassword = component::onDismissChangePasswordDialog,
                            onAddEmail = component::onAddEmailClick,
                            onEmailCodeChanged = component::onEmailCodeChanged,
                            onConfirmEmail = component::onConfirmAddEmailClick,
                            onAddPhone = component::onAddPhoneClick,
                            onPhoneCodeChanged = component::onPhoneCodeChanged,
                            onConfirmPhone = component::onConfirmAddPhoneClick,
                            onCancelAdd = component::onCancelAddClick,
                            onLoadNextPage = component::onLoadNextPage
                        )
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
private fun Content(
    state: IdentifierListScreenState.Content,
    component: IdentifierListComponent,
    onDeleteIdentifier: (UserIdentifierId) -> Unit,
    onChangePasswordClick: (String) -> Unit,
    onConfirmChangePassword: (oldPassword: String, newPassword: String) -> Unit,
    onDismissChangePassword: () -> Unit,
    onAddEmail: (String) -> Unit,
    onEmailCodeChanged: (String) -> Unit,
    onConfirmEmail: (String) -> Unit,
    onAddPhone: (String) -> Unit,
    onPhoneCodeChanged: (String) -> Unit,
    onConfirmPhone: () -> Unit,
    onCancelAdd: () -> Unit,
    onLoadNextPage: () -> Unit
) {
    val listState = rememberLazyListState()

    listState.OnBottomReached {
        onLoadNextPage()
    }

    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = state.isFilterPanelExpanded) {
            ListingOptionsPanel(
                config = getIdentifierListListingConfig(),
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
                        onDeleteClick = { onDeleteIdentifier(identifier.id) },
                        onChangePasswordClick = if (identifier.userAuthProvider == UserAuthProvider.EMAIL) {
                            { onChangePasswordClick(identifier.identifier) }
                        } else null,
                        enabled = !state.actionLoading &&
                            state.addEmailState is IdentifierListScreenState.AddIdentifierState.Idle &&
                            state.addPhoneState is IdentifierListScreenState.AddIdentifierState.Idle
                    )
                }

                item {
                    PagingFooter(
                        state = state.paging,
                        onRetry = onLoadNextPage
                    )
                }

                item {
                    Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))
                    HorizontalDivider()
                    Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))
                }

                item {
                    AddEmailSection(
                        state = state.addEmailState,
                        emailInput = emailInput,
                        onEmailInputChange = { emailInput = it },
                        passwordInput = passwordInput,
                        onPasswordInputChange = { passwordInput = it },
                        onAddClick = { onAddEmail(emailInput) },
                        onCodeChanged = onEmailCodeChanged,
                        onConfirmClick = { onConfirmEmail(passwordInput) },
                        onCancelClick = onCancelAdd,
                        enabled = !state.actionLoading && state.addPhoneState is IdentifierListScreenState.AddIdentifierState.Idle
                    )
                }

                item {
                    Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))
                }

                item {
                    AddPhoneSection(
                        state = state.addPhoneState,
                        phoneInput = phoneInput,
                        onPhoneInputChange = { phoneInput = it },
                        onAddClick = { onAddPhone(phoneInput) },
                        onCodeChanged = onPhoneCodeChanged,
                        onConfirmClick = onConfirmPhone,
                        onCancelClick = onCancelAdd,
                        enabled = !state.actionLoading && state.addEmailState is IdentifierListScreenState.AddIdentifierState.Idle
                    )
                }

                item {
                    ErrorText(
                        error = state.actionError,
                        testTag = IdentifierListTestTags.ACTION_ERROR_TEXT
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

        if (state.changePasswordEmail != null) {
            ChangePasswordDialog(
                email = state.changePasswordEmail,
                onConfirm = onConfirmChangePassword,
                onDismiss = onDismissChangePassword,
                enabled = !state.actionLoading
            )
        }
    }
}

@Composable
private fun ChangePasswordDialog(
    email: String,
    onConfirm: (oldPassword: String, newPassword: String) -> Unit,
    onDismiss: () -> Unit,
    enabled: Boolean
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var isOldPasswordVisible by remember { mutableStateOf(false) }
    var isNewPasswordVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = { CoreTitleText(text = stringResource(Res.string.change_password)) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                CoreBodyText(
                    text = stringResource(Res.string.email_prefix, email)
                )
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                CorePasswordTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    isPasswordVisible = isOldPasswordVisible,
                    onTogglePasswordVisibility = { isOldPasswordVisible = !isOldPasswordVisible },
                    label = { CoreBodyText(stringResource(Res.string.old_password)) },
                    placeholder = { CoreBodyText(stringResource(Res.string.old_password)) },
                    enabled = enabled,
                    modifier = Modifier.testTag(IdentifierListTestTags.CHANGE_PASSWORD_OLD_INPUT)
                )
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                CorePasswordTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    isPasswordVisible = isNewPasswordVisible,
                    onTogglePasswordVisibility = { isNewPasswordVisible = !isNewPasswordVisible },
                    label = { CoreBodyText(stringResource(Res.string.new_password)) },
                    placeholder = { CoreBodyText(stringResource(Res.string.new_password)) },
                    enabled = enabled,
                    modifier = Modifier.testTag(IdentifierListTestTags.CHANGE_PASSWORD_NEW_INPUT)
                )
            }
        },
        confirmButton = {
            CoreButton(
                text = stringResource(Res.string.confirm),
                onClick = { onConfirm(oldPassword, newPassword) },
                enabled = enabled && oldPassword.isNotBlank() && newPassword.isNotBlank(),
                modifier = Modifier.testTag(IdentifierListTestTags.CONFIRM_CHANGE_PASSWORD_BUTTON)
            )
        },
        dismissButton = {
            CoreTextButton(
                text = stringResource(Res.string.dialog_cancel),
                onClick = onDismiss,
                enabled = enabled
            )
        }
    )
}

@Composable
private fun AddEmailSection(
    state: IdentifierListScreenState.AddIdentifierState,
    emailInput: String,
    onEmailInputChange: (String) -> Unit,
    passwordInput: String,
    onPasswordInputChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onCodeChanged: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    enabled: Boolean
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        CoreTitleText(
            text = stringResource(Res.string.identifier_add_email),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

        when (state) {
            is IdentifierListScreenState.AddIdentifierState.Idle -> {
                CoreEmailTextField(
                    value = emailInput,
                    onValueChange = onEmailInputChange,
                    label = { CoreBodyText(stringResource(Res.string.email)) },
                    placeholder = { CoreBodyText(stringResource(Res.string.email)) },
                    enabled = enabled,
                    modifier = Modifier.testTag(IdentifierListTestTags.ADD_EMAIL_INPUT)
                )
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                CoreButton(
                    text = stringResource(Res.string.identifier_add_email),
                    onClick = onAddClick,
                    enabled = enabled && emailInput.isNotBlank(),
                    modifier = Modifier.testTag(IdentifierListTestTags.ADD_EMAIL_BUTTON)
                )
            }
            is IdentifierListScreenState.AddIdentifierState.EnteringCode -> {
                CoreBodyText(text = stringResource(Res.string.email_prefix, state.value))
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                CorePasswordTextField(
                    value = passwordInput,
                    onValueChange = onPasswordInputChange,
                    isPasswordVisible = isPasswordVisible,
                    onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
                    label = { CoreBodyText(stringResource(Res.string.password)) },
                    placeholder = { CoreBodyText(stringResource(Res.string.password)) },
                    enabled = enabled,
                    modifier = Modifier.testTag(IdentifierListTestTags.ADD_EMAIL_PASSWORD_INPUT)
                )
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                CoreCodeTextField(
                    value = state.code,
                    onValueChange = onCodeChanged,
                    label = { CoreBodyText(stringResource(Res.string.confirmation_code)) },
                    placeholder = { CoreBodyText(stringResource(Res.string.confirmation_code)) },
                    enabled = enabled,
                    modifier = Modifier.testTag(IdentifierListTestTags.ADD_EMAIL_CODE_INPUT)
                )
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
                ) {
                    CoreTextButton(
                        text = stringResource(Res.string.cancel),
                        onClick = onCancelClick,
                        modifier = Modifier.weight(1f)
                    )
                    CoreButton(
                        text = stringResource(Res.string.confirm),
                        onClick = onConfirmClick,
                        enabled = enabled && state.code.length == 6 && passwordInput.isNotBlank(),
                        modifier = Modifier
                            .weight(1f)
                            .testTag(IdentifierListTestTags.CONFIRM_ADD_EMAIL_BUTTON)
                    )
                }
            }
        }
    }
}

@Composable
private fun AddPhoneSection(
    state: IdentifierListScreenState.AddIdentifierState,
    phoneInput: String,
    onPhoneInputChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onCodeChanged: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    enabled: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CoreTitleText(
            text = stringResource(Res.string.identifier_add_phone),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

        when (state) {
            is IdentifierListScreenState.AddIdentifierState.Idle -> {
                CoreOutlinedTextField(
                    value = phoneInput,
                    onValueChange = onPhoneInputChange,
                    label = { CoreBodyText(stringResource(Res.string.phone_number)) },
                    placeholder = { CoreBodyText(stringResource(Res.string.phone_number)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    enabled = enabled,
                    modifier = Modifier.testTag(IdentifierListTestTags.ADD_PHONE_INPUT)
                )
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                CoreButton(
                    text = stringResource(Res.string.identifier_add_phone),
                    onClick = onAddClick,
                    enabled = enabled && phoneInput.isNotBlank(),
                    modifier = Modifier.testTag(IdentifierListTestTags.ADD_PHONE_BUTTON)
                )
            }
            is IdentifierListScreenState.AddIdentifierState.EnteringCode -> {
                CoreBodyText(text = stringResource(Res.string.phone_prefix, state.value))
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                CoreCodeTextField(
                    value = state.code,
                    onValueChange = onCodeChanged,
                    label = { CoreBodyText(stringResource(Res.string.confirmation_code)) },
                    placeholder = { CoreBodyText(stringResource(Res.string.confirmation_code)) },
                    enabled = enabled,
                    modifier = Modifier.testTag(IdentifierListTestTags.ADD_PHONE_CODE_INPUT)
                )
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
                ) {
                    CoreTextButton(
                        text = stringResource(Res.string.cancel),
                        onClick = onCancelClick,
                        modifier = Modifier.weight(1f)
                    )
                    CoreButton(
                        text = stringResource(Res.string.confirm),
                        onClick = onConfirmClick,
                        enabled = enabled && state.code.length == 6,
                        modifier = Modifier
                            .weight(1f)
                            .testTag(IdentifierListTestTags.CONFIRM_ADD_PHONE_BUTTON)
                    )
                }
            }
        }
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
internal class IdentifierListPreviewProvider : PreviewParameterProvider<IdentifierListScreenState> {
    private val items: List<Pair<String, IdentifierListScreenState>> = listOf(
        "Content List" to IdentifierListScreenState.Content(
            paging = PaginationState(
                items = listOf(userIdentifierMock())
            )
        ),
        "Add Email Entering Code" to IdentifierListScreenState.Content(
            paging = PaginationState(items = listOf(userIdentifierMock())),
            addEmailState = IdentifierListScreenState.AddIdentifierState.EnteringCode("user@example.com", "123456")
        ),
        "Add Phone Entering Code" to IdentifierListScreenState.Content(
            paging = PaginationState(items = listOf(userIdentifierMock())),
            addPhoneState = IdentifierListScreenState.AddIdentifierState.EnteringCode("+1234567890", "123456")
        ),
        "Change Password Active" to IdentifierListScreenState.Content(
            paging = PaginationState(items = listOf(userIdentifierMock())),
            changePasswordEmail = "user@example.com"
        ),
        "Action Loading" to IdentifierListScreenState.Content(
            paging = PaginationState(items = listOf(userIdentifierMock())),
            actionLoading = true
        ),
        "Global Error" to IdentifierListScreenState.Error(error = CommonError.Unknown()),
        "Fullscreen Loading" to IdentifierListScreenState.Loading
    )

    override val values: Sequence<IdentifierListScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
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
    paging = PaginationState(items = listOf(userIdentifierMock()))
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
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        IdentifierListScreenPreviewContent(state = defaultIdentifierListPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        IdentifierListScreenPreviewContent(state = defaultIdentifierListPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        IdentifierListScreenPreviewContent(state = defaultIdentifierListPreviewState)
    }
}

object IdentifierListTestTags {
    const val TITLE = "IdentifierList_Title"
    const val BACK_BUTTON = "IdentifierList_BackButton"
    const val FILTER_BUTTON = "IdentifierList_FilterButton"
    const val REFRESH_BUTTON = "IdentifierList_RefreshButton"
    const val GLOBAL_ERROR_TEXT = "IdentifierList_GlobalErrorText"
    const val IDENTIFIER_LIST = "IdentifierList_List"
    const val IDENTIFIER_ITEM_PREFIX = "IdentifierList_Item_"
    const val DELETE_BUTTON_PREFIX = "IdentifierList_DeleteButton_"
    const val CHANGE_PASSWORD_BUTTON_PREFIX = "IdentifierList_ChangePasswordButton_"
    const val CHANGE_PASSWORD_OLD_INPUT = "IdentifierList_ChangePasswordOldInput"
    const val CHANGE_PASSWORD_NEW_INPUT = "IdentifierList_ChangePasswordNewInput"
    const val CONFIRM_CHANGE_PASSWORD_BUTTON = "IdentifierList_ConfirmChangePasswordButton"
    const val ADD_EMAIL_INPUT = "IdentifierList_AddEmailInput"
    const val ADD_EMAIL_BUTTON = "IdentifierList_AddEmailButton"
    const val ADD_EMAIL_PASSWORD_INPUT = "IdentifierList_AddEmailPasswordInput"
    const val ADD_EMAIL_CODE_INPUT = "IdentifierList_AddEmailCodeInput"
    const val CONFIRM_ADD_EMAIL_BUTTON = "IdentifierList_ConfirmAddEmailButton"
    const val ADD_PHONE_INPUT = "IdentifierList_AddPhoneInput"
    const val ADD_PHONE_BUTTON = "IdentifierList_AddPhoneButton"
    const val ADD_PHONE_CODE_INPUT = "IdentifierList_AddPhoneCodeInput"
    const val CONFIRM_ADD_PHONE_BUTTON = "IdentifierList_ConfirmAddPhoneButton"
    const val ACTION_ERROR_TEXT = "IdentifierList_ActionErrorText"
}
