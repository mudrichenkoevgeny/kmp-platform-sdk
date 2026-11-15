package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error.FullscreenError
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreOutlinedTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewScreenSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.settings.auth.EditAuthSettingsComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

/**
 * Screen for editing authentication settings.
 *
 * @param component Decompose controller for auth settings editing.
 * @param modifier Optional modifier.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun EditAuthSettingsScreen(
    component: EditAuthSettingsComponent,
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
                        text = userUi.editAuthSettingsTitle,
                        modifier = Modifier.testTag(EditAuthSettingsTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(EditAuthSettingsTestTags.BACK_BUTTON)
                    )
                }
            )
        }
    ) { padding ->
        val currentState = state
        when (currentState) {
            is EditAuthSettingsScreenState.Loading -> {
                FullscreenLoading(modifier = Modifier.padding(padding))
            }

            is EditAuthSettingsScreenState.Error -> {
                FullscreenError(
                    error = currentState.error,
                    onRetry = component::onRetry,
                    modifier = Modifier
                        .padding(padding)
                        .testTag(EditAuthSettingsTestTags.GLOBAL_ERROR)
                )
            }

            is EditAuthSettingsScreenState.Content -> {
                EditAuthSettingsForm(
                    state = currentState,
                    onProviderToggle = component::onProviderToggled,
                    onMaxTotalIdentifiersChange = component::onMaxTotalIdentifiersChanged,
                    onMaxEmailIdentifiersChange = component::onMaxEmailIdentifiersChanged,
                    onMaxPhoneIdentifiersChange = component::onMaxPhoneIdentifiersChanged,
                    onMaxIdentifiersPerExternalProviderChange = component::onMaxIdentifiersPerExternalProviderChanged,
                    onUserNicknameMinLengthChange = component::onUserNicknameMinLengthChanged,
                    onUserNicknameMaxLengthChange = component::onUserNicknameMaxLengthChanged,
                    onUserDisplayNameMinLengthChange = component::onUserDisplayNameMinLengthChanged,
                    onUserDisplayNameMaxLengthChange = component::onUserDisplayNameMaxLengthChanged,
                    onIsUserNicknameChangeAllowedToggle = component::onIsUserNicknameChangeAllowedToggled,
                    onMaxActiveSessionsForOpenUserChange = component::onMaxActiveSessionsForOpenUserChanged,
                    onMaxActiveSessionsForManagementUserChange =
                        component::onMaxActiveSessionsForManagementUserChanged,
                    onAccessTokenExpirationSecondsChange = component::onAccessTokenExpirationSecondsChanged,
                    onRefreshTokenExpirationSecondsChange = component::onRefreshTokenExpirationSecondsChanged,
                    onAccountDeletionGracePeriodSecondsChange =
                        component::onAccountDeletionGracePeriodSecondsChanged,
                    onAccountDeletionCheckIntervalSecondsChange =
                        component::onAccountDeletionCheckIntervalSecondsChanged,
                    onRegistrationEnabledToggle = component::onRegistrationEnabledToggled,
                    onOpenEmailBlacklistEnabledToggle = component::onOpenEmailBlacklistEnabledToggled,
                    onOpenEmailBlacklistChange = component::onOpenEmailBlacklistChanged,
                    onOpenEmailWhitelistEnabledToggle = component::onOpenEmailWhitelistEnabledToggled,
                    onOpenEmailWhitelistChange = component::onOpenEmailWhitelistChanged,
                    onManagementEmailBlacklistEnabledToggle = component::onManagementEmailBlacklistEnabledToggled,
                    onManagementEmailBlacklistChange = component::onManagementEmailBlacklistChanged,
                    onManagementEmailWhitelistEnabledToggle = component::onManagementEmailWhitelistEnabledToggled,
                    onManagementEmailWhitelistChange = component::onManagementEmailWhitelistChanged,
                    onSaveClick = component::onSaveClick,
                    onResetClick = component::onResetClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(CoreTheme.dimens.paddingMedium)
                        .verticalScroll(rememberScrollState())
                )

                val dialogSlot by component.dialogSlot.subscribeAsState()
                val dialogChild = dialogSlot.child?.instance
                if (dialogChild != null) {
                    when (dialogChild) {
                        is SettingsDialogChild.ConfirmReset -> {
                            AlertDialog(
                                onDismissRequest = component::onDismissDialog,
                                title = { CoreTitleText(text = userUi.dialogConfirmTitle) },
                                text = { CoreBodyText(text = userUi.resetToDefaults) },
                                confirmButton = {
                                    CoreTextButton(
                                        text = userUi.dialogConfirm,
                                        onClick = component::onConfirmReset
                                    )
                                },
                                dismissButton = {
                                    CoreTextButton(
                                        text = userUi.dialogCancel,
                                        onClick = component::onDismissDialog
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditAuthSettingsForm(
    state: EditAuthSettingsScreenState.Content,
    onProviderToggle: (UserAuthProvider, Boolean) -> Unit,
    onMaxTotalIdentifiersChange: (String) -> Unit,
    onMaxEmailIdentifiersChange: (String) -> Unit,
    onMaxPhoneIdentifiersChange: (String) -> Unit,
    onMaxIdentifiersPerExternalProviderChange: (String) -> Unit,
    onUserNicknameMinLengthChange: (String) -> Unit,
    onUserNicknameMaxLengthChange: (String) -> Unit,
    onUserDisplayNameMinLengthChange: (String) -> Unit,
    onUserDisplayNameMaxLengthChange: (String) -> Unit,
    onIsUserNicknameChangeAllowedToggle: (Boolean) -> Unit,
    onMaxActiveSessionsForOpenUserChange: (String) -> Unit,
    onMaxActiveSessionsForManagementUserChange: (String) -> Unit,
    onAccessTokenExpirationSecondsChange: (String) -> Unit,
    onRefreshTokenExpirationSecondsChange: (String) -> Unit,
    onAccountDeletionGracePeriodSecondsChange: (String) -> Unit,
    onAccountDeletionCheckIntervalSecondsChange: (String) -> Unit,
    onRegistrationEnabledToggle: (Boolean) -> Unit,
    onOpenEmailBlacklistEnabledToggle: (Boolean) -> Unit,
    onOpenEmailBlacklistChange: (String) -> Unit,
    onOpenEmailWhitelistEnabledToggle: (Boolean) -> Unit,
    onOpenEmailWhitelistChange: (String) -> Unit,
    onManagementEmailBlacklistEnabledToggle: (Boolean) -> Unit,
    onManagementEmailBlacklistChange: (String) -> Unit,
    onManagementEmailWhitelistEnabledToggle: (Boolean) -> Unit,
    onManagementEmailWhitelistChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userUi = LocalUserStrings.current.ui

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        EnabledProvidersSection(
            state = state,
            onProviderToggle = onProviderToggle,
            onRegistrationEnabledToggle = onRegistrationEnabledToggle
        )

        LimitsAndExpirationsSection(
            state = state,
            onMaxTotalIdentifiersChange = onMaxTotalIdentifiersChange,
            onMaxEmailIdentifiersChange = onMaxEmailIdentifiersChange,
            onMaxPhoneIdentifiersChange = onMaxPhoneIdentifiersChange,
            onMaxIdentifiersPerExternalProviderChange = onMaxIdentifiersPerExternalProviderChange,
            onUserNicknameMinLengthChange = onUserNicknameMinLengthChange,
            onUserNicknameMaxLengthChange = onUserNicknameMaxLengthChange,
            onUserDisplayNameMinLengthChange = onUserDisplayNameMinLengthChange,
            onUserDisplayNameMaxLengthChange = onUserDisplayNameMaxLengthChange,
            onIsUserNicknameChangeAllowedToggle = onIsUserNicknameChangeAllowedToggle,
            onMaxActiveSessionsForOpenUserChange = onMaxActiveSessionsForOpenUserChange,
            onMaxActiveSessionsForManagementUserChange = onMaxActiveSessionsForManagementUserChange,
            onAccessTokenExpirationSecondsChange = onAccessTokenExpirationSecondsChange,
            onRefreshTokenExpirationSecondsChange = onRefreshTokenExpirationSecondsChange,
            onAccountDeletionGracePeriodSecondsChange = onAccountDeletionGracePeriodSecondsChange,
            onAccountDeletionCheckIntervalSecondsChange = onAccountDeletionCheckIntervalSecondsChange
        )

        EmailRestrictionSettingsSection(
            state = state,
            onOpenEmailBlacklistEnabledToggle = onOpenEmailBlacklistEnabledToggle,
            onOpenEmailBlacklistChange = onOpenEmailBlacklistChange,
            onOpenEmailWhitelistEnabledToggle = onOpenEmailWhitelistEnabledToggle,
            onOpenEmailWhitelistChange = onOpenEmailWhitelistChange,
            onManagementEmailBlacklistEnabledToggle = onManagementEmailBlacklistEnabledToggle,
            onManagementEmailBlacklistChange = onManagementEmailBlacklistChange,
            onManagementEmailWhitelistEnabledToggle = onManagementEmailWhitelistEnabledToggle,
            onManagementEmailWhitelistChange = onManagementEmailWhitelistChange
        )

        ErrorText(
            error = state.saveError,
            testTag = EditAuthSettingsTestTags.SAVE_ERROR_TEXT
        )

        CoreButton(
            text = if (state.isSaving) userUi.saving else userUi.save,
            onClick = onSaveClick,
            enabled = !state.isSaving,
            modifier = Modifier.testTag(EditAuthSettingsTestTags.SAVE_BUTTON)
        )

        CoreTextButton(
            text = userUi.resetToDefaults,
            onClick = onResetClick,
            enabled = !state.isSaving,
            modifier = Modifier.testTag(EditAuthSettingsTestTags.RESET_BUTTON)
        )
    }
}

@Composable
private fun EnabledProvidersSection(
    state: EditAuthSettingsScreenState.Content,
    onProviderToggle: (UserAuthProvider, Boolean) -> Unit,
    onRegistrationEnabledToggle: (Boolean) -> Unit
) {
    val userUi = LocalUserStrings.current.ui

    Column {
        CoreTitleText(
            text = userUi.enabledAuthProviders,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditAuthSettingsTestTags.SECTION_PROVIDERS_TITLE)
        )

        UserAuthProvider.entries.forEach { provider ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = provider in state.enabledProviders,
                    onCheckedChange = { enabled ->
                        onProviderToggle(provider, enabled)
                    },
                    modifier = Modifier.testTag(EditAuthSettingsTestTags.getProviderCheckboxTag(provider))
                )
                CoreBodyText(
                    text = provider.name,
                    modifier = Modifier.padding(start = CoreTheme.dimens.paddingSmall)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = state.isRegistrationEnabled,
                onCheckedChange = onRegistrationEnabledToggle,
                modifier = Modifier.testTag(EditAuthSettingsTestTags.IS_REGISTRATION_ENABLED_CHECKBOX)
            )
            CoreBodyText(
                text = userUi.isRegistrationEnabled,
                modifier = Modifier.padding(start = CoreTheme.dimens.paddingSmall)
            )
        }
    }
}

@Composable
private fun LimitsAndExpirationsSection(
    state: EditAuthSettingsScreenState.Content,
    onMaxTotalIdentifiersChange: (String) -> Unit,
    onMaxEmailIdentifiersChange: (String) -> Unit,
    onMaxPhoneIdentifiersChange: (String) -> Unit,
    onMaxIdentifiersPerExternalProviderChange: (String) -> Unit,
    onUserNicknameMinLengthChange: (String) -> Unit,
    onUserNicknameMaxLengthChange: (String) -> Unit,
    onUserDisplayNameMinLengthChange: (String) -> Unit,
    onUserDisplayNameMaxLengthChange: (String) -> Unit,
    onIsUserNicknameChangeAllowedToggle: (Boolean) -> Unit,
    onMaxActiveSessionsForOpenUserChange: (String) -> Unit,
    onMaxActiveSessionsForManagementUserChange: (String) -> Unit,
    onAccessTokenExpirationSecondsChange: (String) -> Unit,
    onRefreshTokenExpirationSecondsChange: (String) -> Unit,
    onAccountDeletionGracePeriodSecondsChange: (String) -> Unit,
    onAccountDeletionCheckIntervalSecondsChange: (String) -> Unit
) {
    val userUi = LocalUserStrings.current.ui

    Column {
        CoreTitleText(
            text = userUi.limitsAndExpirations,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditAuthSettingsTestTags.SECTION_LIMITS_TITLE)
        )

        CoreOutlinedTextField(
            value = state.maxTotalIdentifiers,
            onValueChange = onMaxTotalIdentifiersChange,
            label = { CoreBodyText(userUi.maxTotalIdentifiers) },
            placeholder = { CoreBodyText(userUi.maxTotalIdentifiers) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.MAX_TOTAL_IDENTIFIERS_INPUT)
        )

        CoreOutlinedTextField(
            value = state.maxEmailIdentifiers,
            onValueChange = onMaxEmailIdentifiersChange,
            label = { CoreBodyText(userUi.maxEmailIdentifiers) },
            placeholder = { CoreBodyText(userUi.maxEmailIdentifiers) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.MAX_EMAIL_IDENTIFIERS_INPUT)
        )

        CoreOutlinedTextField(
            value = state.maxPhoneIdentifiers,
            onValueChange = onMaxPhoneIdentifiersChange,
            label = { CoreBodyText(userUi.maxPhoneIdentifiers) },
            placeholder = { CoreBodyText(userUi.maxPhoneIdentifiers) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.MAX_PHONE_IDENTIFIERS_INPUT)
        )

        CoreOutlinedTextField(
            value = state.maxIdentifiersPerExternalProvider,
            onValueChange = onMaxIdentifiersPerExternalProviderChange,
            label = { CoreBodyText(userUi.maxIdentifiersPerExternalProvider) },
            placeholder = { CoreBodyText(userUi.maxIdentifiersPerExternalProvider) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.MAX_IDENTIFIERS_PER_EXTERNAL_PROVIDER_INPUT)
        )

        CoreOutlinedTextField(
            value = state.userNicknameMinLength,
            onValueChange = onUserNicknameMinLengthChange,
            label = { CoreBodyText(userUi.userNicknameMinLength) },
            placeholder = { CoreBodyText(userUi.userNicknameMinLength) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.USER_NICKNAME_MIN_LENGTH_INPUT)
        )

        CoreOutlinedTextField(
            value = state.userNicknameMaxLength,
            onValueChange = onUserNicknameMaxLengthChange,
            label = { CoreBodyText(userUi.userNicknameMaxLength) },
            placeholder = { CoreBodyText(userUi.userNicknameMaxLength) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.USER_NICKNAME_MAX_LENGTH_INPUT)
        )

        CoreOutlinedTextField(
            value = state.userDisplayNameMinLength,
            onValueChange = onUserDisplayNameMinLengthChange,
            label = { CoreBodyText(userUi.userDisplayNameMinLength) },
            placeholder = { CoreBodyText(userUi.userDisplayNameMinLength) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.USER_DISPLAY_NAME_MIN_LENGTH_INPUT)
        )

        CoreOutlinedTextField(
            value = state.userDisplayNameMaxLength,
            onValueChange = onUserDisplayNameMaxLengthChange,
            label = { CoreBodyText(userUi.userDisplayNameMaxLength) },
            placeholder = { CoreBodyText(userUi.userDisplayNameMaxLength) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.USER_DISPLAY_NAME_MAX_LENGTH_INPUT)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = state.isUserNicknameChangeAllowed,
                onCheckedChange = onIsUserNicknameChangeAllowedToggle,
                modifier = Modifier.testTag(EditAuthSettingsTestTags.IS_USER_NICKNAME_CHANGE_ALLOWED_CHECKBOX)
            )
            CoreBodyText(
                text = userUi.isUserNicknameChangeAllowed,
                modifier = Modifier.padding(start = CoreTheme.dimens.paddingSmall)
            )
        }

        CoreOutlinedTextField(
            value = state.maxActiveSessionsForOpenUser,
            onValueChange = onMaxActiveSessionsForOpenUserChange,
            label = { CoreBodyText(userUi.maxActiveSessionsOpen) },
            placeholder = { CoreBodyText(userUi.maxActiveSessionsOpen) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.MAX_ACTIVE_SESSIONS_OPEN_INPUT)
        )

        CoreOutlinedTextField(
            value = state.maxActiveSessionsForManagementUser,
            onValueChange = onMaxActiveSessionsForManagementUserChange,
            label = { CoreBodyText(userUi.maxActiveSessionsManagement) },
            placeholder = { CoreBodyText(userUi.maxActiveSessionsManagement) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.MAX_ACTIVE_SESSIONS_MANAGEMENT_INPUT)
        )

        CoreOutlinedTextField(
            value = state.accessTokenExpirationSeconds,
            onValueChange = onAccessTokenExpirationSecondsChange,
            label = { CoreBodyText(userUi.accessTokenExpirationSeconds) },
            placeholder = { CoreBodyText(userUi.accessTokenExpirationSeconds) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.ACCESS_TOKEN_EXPIRATION_INPUT)
        )

        CoreOutlinedTextField(
            value = state.refreshTokenExpirationSeconds,
            onValueChange = onRefreshTokenExpirationSecondsChange,
            label = { CoreBodyText(userUi.refreshTokenExpirationSeconds) },
            placeholder = { CoreBodyText(userUi.refreshTokenExpirationSeconds) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.REFRESH_TOKEN_EXPIRATION_INPUT)
        )

        CoreOutlinedTextField(
            value = state.accountDeletionGracePeriodSeconds,
            onValueChange = onAccountDeletionGracePeriodSecondsChange,
            label = { CoreBodyText(userUi.accountDeletionDelaySeconds) },
            placeholder = { CoreBodyText(userUi.accountDeletionDelaySeconds) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.ACCOUNT_DELETION_DELAY_INPUT)
        )

        CoreOutlinedTextField(
            value = state.accountDeletionCheckIntervalSeconds,
            onValueChange = onAccountDeletionCheckIntervalSecondsChange,
            label = { CoreBodyText(userUi.accountDeletionCheckIntervalSeconds) },
            placeholder = { CoreBodyText(userUi.accountDeletionCheckIntervalSeconds) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.ACCOUNT_DELETION_CHECK_INTERVAL_INPUT)
        )
    }
}

@Composable
private fun EmailRestrictionSettingsSection(
    state: EditAuthSettingsScreenState.Content,
    onOpenEmailBlacklistEnabledToggle: (Boolean) -> Unit,
    onOpenEmailBlacklistChange: (String) -> Unit,
    onOpenEmailWhitelistEnabledToggle: (Boolean) -> Unit,
    onOpenEmailWhitelistChange: (String) -> Unit,
    onManagementEmailBlacklistEnabledToggle: (Boolean) -> Unit,
    onManagementEmailBlacklistChange: (String) -> Unit,
    onManagementEmailWhitelistEnabledToggle: (Boolean) -> Unit,
    onManagementEmailWhitelistChange: (String) -> Unit
) {
    val userUi = LocalUserStrings.current.ui

    Column {
        CoreTitleText(
            text = userUi.openEmailRestrictionPolicy,
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = state.openEmailBlacklistEnabled,
                onCheckedChange = onOpenEmailBlacklistEnabledToggle
            )
            CoreBodyText(
                text = userUi.blacklistEnabled,
                modifier = Modifier.padding(start = CoreTheme.dimens.paddingSmall)
            )
        }

        CoreOutlinedTextField(
            value = state.openEmailBlacklist,
            onValueChange = onOpenEmailBlacklistChange,
            label = { CoreBodyText(userUi.emailBlacklistPlaceholder) },
            placeholder = { CoreBodyText(userUi.emailBlacklistPlaceholder) }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = state.openEmailWhitelistEnabled,
                onCheckedChange = onOpenEmailWhitelistEnabledToggle
            )
            CoreBodyText(
                text = userUi.whitelistEnabled,
                modifier = Modifier.padding(start = CoreTheme.dimens.paddingSmall)
            )
        }

        CoreOutlinedTextField(
            value = state.openEmailWhitelist,
            onValueChange = onOpenEmailWhitelistChange,
            label = { CoreBodyText(userUi.emailWhitelistPlaceholder) },
            placeholder = { CoreBodyText(userUi.emailWhitelistPlaceholder) }
        )

        CoreTitleText(
            text = userUi.managementEmailRestrictionPolicy,
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = state.managementEmailBlacklistEnabled,
                onCheckedChange = onManagementEmailBlacklistEnabledToggle
            )
            CoreBodyText(
                text = userUi.blacklistEnabled,
                modifier = Modifier.padding(start = CoreTheme.dimens.paddingSmall)
            )
        }

        CoreOutlinedTextField(
            value = state.managementEmailBlacklist,
            onValueChange = onManagementEmailBlacklistChange,
            label = { CoreBodyText(userUi.emailBlacklistPlaceholder) },
            placeholder = { CoreBodyText(userUi.emailBlacklistPlaceholder) }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = state.managementEmailWhitelistEnabled,
                onCheckedChange = onManagementEmailWhitelistEnabledToggle
            )
            CoreBodyText(
                text = userUi.blacklistEnabled,
                modifier = Modifier.padding(start = CoreTheme.dimens.paddingSmall)
            )
        }

        CoreOutlinedTextField(
            value = state.managementEmailWhitelist,
            onValueChange = onManagementEmailWhitelistChange,
            label = { CoreBodyText(userUi.emailWhitelistPlaceholder) },
            placeholder = { CoreBodyText(userUi.emailWhitelistPlaceholder) }
        )
    }
}

@Composable
private fun ErrorText(
    error: AppError?,
    testTag: String
) {
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
                    .padding(top = CoreTheme.dimens.paddingSmall)
                    .testTag(testTag)
            )
        }
    }
}

@InternalApi
internal class EditAuthSettingsPreviewProvider : PreviewParameterProvider<EditAuthSettingsScreenState> {
    private val sampleContent = EditAuthSettingsScreenState.Content(
        enabledProviders = setOf(UserAuthProvider.EMAIL, UserAuthProvider.GOOGLE),
        maxTotalIdentifiers = "10",
        maxEmailIdentifiers = "5",
        maxPhoneIdentifiers = "5",
        maxIdentifiersPerExternalProvider = "2",
        userNicknameMinLength = "3",
        userNicknameMaxLength = "30",
        userDisplayNameMinLength = "1",
        userDisplayNameMaxLength = "50",
        isUserNicknameChangeAllowed = true,
        maxActiveSessionsForOpenUser = "3",
        maxActiveSessionsForManagementUser = "5",
        accessTokenExpirationSeconds = "3600",
        refreshTokenExpirationSeconds = "86400",
        accountDeletionGracePeriodSeconds = "604800",
        accountDeletionCheckIntervalSeconds = "86400",
        isRegistrationEnabled = true
    )

    private val items: List<Pair<String, EditAuthSettingsScreenState>> = listOf(
        "Content" to sampleContent,
        "Saving" to sampleContent.copy(isSaving = true),
        "Save Error" to sampleContent.copy(saveError = CommonError.Unknown()),
        "Error" to EditAuthSettingsScreenState.Error(error = CommonError.Unknown()),
        "Loading" to EditAuthSettingsScreenState.Loading
    )

    override val values: Sequence<EditAuthSettingsScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun EditAuthSettingsScreenPreviewContent(state: EditAuthSettingsScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        EditAuthSettingsScreen(
            component = EditAuthSettingsComponentMock(initialState = state)
        )
    }
}

@InternalApi
@PreviewScreenSizes
@Composable
private fun ScreenSizePreview(
    @PreviewParameter(EditAuthSettingsPreviewProvider::class) state: EditAuthSettingsScreenState
) {
    ScreenPreviewContainer {
        EditAuthSettingsScreenPreviewContent(state = state)
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview(
    @PreviewParameter(EditAuthSettingsPreviewProvider::class) state: EditAuthSettingsScreenState
) {
    ScreenPreviewContainer {
        EditAuthSettingsScreenPreviewContent(state = state)
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview(
    @PreviewParameter(EditAuthSettingsPreviewProvider::class) state: EditAuthSettingsScreenState
) {
    ScreenPreviewContainer {
        EditAuthSettingsScreenPreviewContent(state = state)
    }
}

public object EditAuthSettingsTestTags {
    public const val TITLE: String = "EditAuthSettings_Title"
    public const val BACK_BUTTON: String = "EditAuthSettings_BackButton"
    public const val GLOBAL_ERROR: String = "EditAuthSettings_GlobalError"

    public const val SECTION_PROVIDERS_TITLE: String = "EditAuthSettings_SectionProvidersTitle"
    public const val IS_REGISTRATION_ENABLED_CHECKBOX: String = "EditAuthSettings_IsRegistrationEnabledCheckbox"

    public const val SECTION_LIMITS_TITLE: String = "EditAuthSettings_SectionLimitsTitle"
    public const val MAX_TOTAL_IDENTIFIERS_INPUT: String = "EditAuthSettings_MaxTotalIdentifiersInput"
    public const val MAX_EMAIL_IDENTIFIERS_INPUT: String = "EditAuthSettings_MaxEmailIdentifiersInput"
    public const val MAX_PHONE_IDENTIFIERS_INPUT: String = "EditAuthSettings_MaxPhoneIdentifiersInput"
    public const val MAX_IDENTIFIERS_PER_EXTERNAL_PROVIDER_INPUT: String =
        "EditAuthSettings_MaxIdentifiersPerExternalProviderInput"
    public const val USER_NICKNAME_MIN_LENGTH_INPUT: String = "EditAuthSettings_UserNicknameMinLengthInput"
    public const val USER_NICKNAME_MAX_LENGTH_INPUT: String = "EditAuthSettings_UserNicknameMaxLengthInput"
    public const val USER_DISPLAY_NAME_MIN_LENGTH_INPUT: String = "EditAuthSettings_UserDisplayNameMinLengthInput"
    public const val USER_DISPLAY_NAME_MAX_LENGTH_INPUT: String = "EditAuthSettings_UserDisplayNameMaxLengthInput"
    public const val IS_USER_NICKNAME_CHANGE_ALLOWED_CHECKBOX: String =
        "EditAuthSettings_IsUserNicknameChangeAllowedCheckbox"
    public const val MAX_ACTIVE_SESSIONS_OPEN_INPUT: String = "EditAuthSettings_MaxActiveSessionsOpenInput"
    public const val MAX_ACTIVE_SESSIONS_MANAGEMENT_INPUT: String = "EditAuthSettings_MaxActiveSessionsManagementInput"
    public const val ACCESS_TOKEN_EXPIRATION_INPUT: String = "EditAuthSettings_AccessTokenExpirationInput"
    public const val REFRESH_TOKEN_EXPIRATION_INPUT: String = "EditAuthSettings_RefreshTokenExpirationInput"
    public const val ACCOUNT_DELETION_DELAY_INPUT: String = "EditAuthSettings_AccountDeletionDelayInput"
    public const val ACCOUNT_DELETION_CHECK_INTERVAL_INPUT: String =
        "EditAuthSettings_AccountDeletionCheckIntervalInput"

    public const val SAVE_ERROR_TEXT: String = "EditAuthSettings_SaveErrorText"
    public const val SAVE_BUTTON: String = "EditAuthSettings_SaveButton"
    public const val RESET_BUTTON: String = "EditAuthSettings_ResetButton"

    /**
     * Generates a test tag for an authentication provider checkbox.
     *
     * @param provider The target authentication provider.
     * @return Test tag string for the provider checkbox.
     */
    public fun getProviderCheckboxTag(provider: UserAuthProvider): String =
        "EditAuthSettings_ProviderCheckbox_${provider.name}"
}
