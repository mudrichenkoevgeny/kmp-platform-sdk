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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.option.ListingOptionsPanel
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.identifier.IdentifierListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.identifier.item.IdentifierItem
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdentifierListScreen(component: IdentifierListComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.identifiers),
                        modifier = Modifier.testTag(IdentifierListTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(IdentifierListTestTags.BACK_BUTTON)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = component::onToggleFilterPanel,
                        modifier = Modifier.testTag(IdentifierListTestTags.FILTER_BUTTON)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                    }
                    IconButton(
                        onClick = component::onRefresh,
                        modifier = Modifier.testTag(IdentifierListTestTags.REFRESH_BUTTON)
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
                    Text(
                        text = currentState.error.toLocalizedMessage(),
                        color = MaterialTheme.colorScheme.error,
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
                    .padding(Dimens.paddingMedium)
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .testTag(IdentifierListTestTags.IDENTIFIER_LIST),
            contentPadding = PaddingValues(Dimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
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
                    Spacer(Modifier.height(Dimens.paddingMedium))
                    HorizontalDivider()
                    Spacer(Modifier.height(Dimens.paddingMedium))
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
                    Spacer(Modifier.height(Dimens.paddingMedium))
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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(Res.string.change_password)) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Email: $email",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(Dimens.paddingSmall))
                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    modifier = Modifier.fillMaxWidth().testTag(IdentifierListTestTags.CHANGE_PASSWORD_OLD_INPUT),
                    label = { Text(stringResource(Res.string.old_password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = enabled,
                    singleLine = true
                )
                Spacer(Modifier.height(Dimens.paddingSmall))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    modifier = Modifier.fillMaxWidth().testTag(IdentifierListTestTags.CHANGE_PASSWORD_NEW_INPUT),
                    label = { Text(stringResource(Res.string.new_password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = enabled,
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(oldPassword, newPassword) },
                enabled = enabled && oldPassword.isNotBlank() && newPassword.isNotBlank(),
                modifier = Modifier.testTag(IdentifierListTestTags.CONFIRM_CHANGE_PASSWORD_BUTTON)
            ) {
                Text(text = stringResource(Res.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = enabled
            ) {
                Text(text = stringResource(Res.string.dialog_cancel))
            }
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
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.identifier_add_email),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(Dimens.paddingSmall))

        when (state) {
            is IdentifierListScreenState.AddIdentifierState.Idle -> {
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = onEmailInputChange,
                    modifier = Modifier.fillMaxWidth().testTag(IdentifierListTestTags.ADD_EMAIL_INPUT),
                    label = { Text(stringResource(Res.string.email)) },
                    enabled = enabled,
                    singleLine = true
                )
                Spacer(Modifier.height(Dimens.paddingSmall))
                Button(
                    onClick = onAddClick,
                    modifier = Modifier.fillMaxWidth().testTag(IdentifierListTestTags.ADD_EMAIL_BUTTON),
                    enabled = enabled && emailInput.isNotBlank()
                ) {
                    Text(stringResource(Res.string.identifier_add_email))
                }
            }
            is IdentifierListScreenState.AddIdentifierState.EnteringCode -> {
                Text(text = "Email: ${state.value}")
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = onPasswordInputChange,
                    modifier = Modifier.fillMaxWidth().testTag(IdentifierListTestTags.ADD_EMAIL_PASSWORD_INPUT),
                    label = { Text(stringResource(Res.string.password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = enabled,
                    singleLine = true
                )
                OutlinedTextField(
                    value = state.code,
                    onValueChange = onCodeChanged,
                    modifier = Modifier.fillMaxWidth().testTag(IdentifierListTestTags.ADD_EMAIL_CODE_INPUT),
                    label = { Text(stringResource(Res.string.confirmation_code)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = enabled,
                    singleLine = true
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)) {
                    TextButton(onClick = onCancelClick, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = onConfirmClick,
                        modifier = Modifier.weight(1f).testTag(IdentifierListTestTags.CONFIRM_ADD_EMAIL_BUTTON),
                        enabled = enabled && state.code.length == 6 && passwordInput.isNotBlank()
                    ) {
                        Text(stringResource(Res.string.confirm))
                    }
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
        Text(
            text = stringResource(Res.string.identifier_add_phone),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(Dimens.paddingSmall))

        when (state) {
            is IdentifierListScreenState.AddIdentifierState.Idle -> {
                OutlinedTextField(
                    value = phoneInput,
                    onValueChange = onPhoneInputChange,
                    modifier = Modifier.fillMaxWidth().testTag(IdentifierListTestTags.ADD_PHONE_INPUT),
                    label = { Text(stringResource(Res.string.phone_number)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    enabled = enabled,
                    singleLine = true
                )
                Spacer(Modifier.height(Dimens.paddingSmall))
                Button(
                    onClick = onAddClick,
                    modifier = Modifier.fillMaxWidth().testTag(IdentifierListTestTags.ADD_PHONE_BUTTON),
                    enabled = enabled && phoneInput.isNotBlank()
                ) {
                    Text(stringResource(Res.string.identifier_add_phone))
                }
            }
            is IdentifierListScreenState.AddIdentifierState.EnteringCode -> {
                Text(text = "Phone: ${state.value}")
                OutlinedTextField(
                    value = state.code,
                    onValueChange = onCodeChanged,
                    modifier = Modifier.fillMaxWidth().testTag(IdentifierListTestTags.ADD_PHONE_CODE_INPUT),
                    label = { Text(stringResource(Res.string.confirmation_code)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = enabled,
                    singleLine = true
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)) {
                    TextButton(onClick = onCancelClick, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = onConfirmClick,
                        modifier = Modifier.weight(1f).testTag(IdentifierListTestTags.CONFIRM_ADD_PHONE_BUTTON),
                        enabled = enabled && state.code.length == 6
                    ) {
                        Text(stringResource(Res.string.confirm))
                    }
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
private fun IdentifierListContentPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                Content(
                    state = IdentifierListScreenState.Content(
                        paging = PaginationState(
                            items = listOf(
                                userIdentifierMock()
                            )
                        )
                    ),
                    component = IdentifierListComponentMock(),
                    onDeleteIdentifier = {},
                    onChangePasswordClick = {},
                    onConfirmChangePassword = { _, _ -> },
                    onDismissChangePassword = {},
                    onAddEmail = {},
                    onEmailCodeChanged = {},
                    onConfirmEmail = {},
                    onAddPhone = {},
                    onPhoneCodeChanged = {},
                    onConfirmPhone = {},
                    onCancelAdd = {},
                    onLoadNextPage = {}
                )
            }
        }
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
