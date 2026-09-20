package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.auth

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.*
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.settings.auth.EditAuthSettingsComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import org.jetbrains.compose.resources.stringResource

/**
 * Screen for editing authentication settings.
 *
 * @param component Decompose controller for auth settings editing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAuthSettingsScreen(component: EditAuthSettingsComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.edit_auth_settings_title),
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
                    onProviderToggled = component::onProviderToggled,
                    onMaxTotalIdentifiersChanged = component::onMaxTotalIdentifiersChanged,
                    onMaxEmailIdentifiersChanged = component::onMaxEmailIdentifiersChanged,
                    onMaxPhoneIdentifiersChanged = component::onMaxPhoneIdentifiersChanged,
                    onMaxIdentifiersPerExternalProviderChanged = component::onMaxIdentifiersPerExternalProviderChanged,
                    onMaxActiveSessionsForOpenUserChanged = component::onMaxActiveSessionsForOpenUserChanged,
                    onMaxActiveSessionsForManagementUserChanged = component::onMaxActiveSessionsForManagementUserChanged,
                    onAccessTokenExpirationSecondsChanged = component::onAccessTokenExpirationSecondsChanged,
                    onRefreshTokenExpirationSecondsChanged = component::onRefreshTokenExpirationSecondsChanged,
                    onAccountDeletionGracePeriodSecondsChanged = component::onAccountDeletionGracePeriodSecondsChanged,
                    onAccountDeletionCheckIntervalSecondsChanged = component::onAccountDeletionCheckIntervalSecondsChanged,
                    onRegistrationEnabledToggled = component::onRegistrationEnabledToggled,
                    onOpenEmailBlacklistEnabledToggled = component::onOpenEmailBlacklistEnabledToggled,
                    onOpenEmailBlacklistChanged = component::onOpenEmailBlacklistChanged,
                    onOpenEmailWhitelistEnabledToggled = component::onOpenEmailWhitelistEnabledToggled,
                    onOpenEmailWhitelistChanged = component::onOpenEmailWhitelistChanged,
                    onManagementEmailBlacklistEnabledToggled = component::onManagementEmailBlacklistEnabledToggled,
                    onManagementEmailBlacklistChanged = component::onManagementEmailBlacklistChanged,
                    onManagementEmailWhitelistEnabledToggled = component::onManagementEmailWhitelistEnabledToggled,
                    onManagementEmailWhitelistChanged = component::onManagementEmailWhitelistChanged,
                    onSaveClick = component::onSaveClick,
                    onResetClick = component::onResetClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(CoreTheme.dimens.paddingMedium)
                        .verticalScroll(rememberScrollState())
                )
            }
        }
    }
}

@Composable
private fun EditAuthSettingsForm(
    state: EditAuthSettingsScreenState.Content,
    onProviderToggled: (UserAuthProvider, Boolean) -> Unit,
    onMaxTotalIdentifiersChanged: (String) -> Unit,
    onMaxEmailIdentifiersChanged: (String) -> Unit,
    onMaxPhoneIdentifiersChanged: (String) -> Unit,
    onMaxIdentifiersPerExternalProviderChanged: (String) -> Unit,
    onMaxActiveSessionsForOpenUserChanged: (String) -> Unit,
    onMaxActiveSessionsForManagementUserChanged: (String) -> Unit,
    onAccessTokenExpirationSecondsChanged: (String) -> Unit,
    onRefreshTokenExpirationSecondsChanged: (String) -> Unit,
    onAccountDeletionGracePeriodSecondsChanged: (String) -> Unit,
    onAccountDeletionCheckIntervalSecondsChanged: (String) -> Unit,
    onRegistrationEnabledToggled: (Boolean) -> Unit,
    onOpenEmailBlacklistEnabledToggled: (Boolean) -> Unit,
    onOpenEmailBlacklistChanged: (String) -> Unit,
    onOpenEmailWhitelistEnabledToggled: (Boolean) -> Unit,
    onOpenEmailWhitelistChanged: (String) -> Unit,
    onManagementEmailBlacklistEnabledToggled: (Boolean) -> Unit,
    onManagementEmailBlacklistChanged: (String) -> Unit,
    onManagementEmailWhitelistEnabledToggled: (Boolean) -> Unit,
    onManagementEmailWhitelistChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreTitleText(
            text = stringResource(Res.string.enabled_auth_providers),
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
                        onProviderToggled(provider, enabled)
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
                onCheckedChange = onRegistrationEnabledToggled,
                modifier = Modifier.testTag(EditAuthSettingsTestTags.IS_REGISTRATION_ENABLED_CHECKBOX)
            )
            CoreBodyText(
                text = stringResource(Res.string.is_registration_enabled),
                modifier = Modifier.padding(start = CoreTheme.dimens.paddingSmall)
            )
        }

        CoreTitleText(
            text = stringResource(Res.string.limits_and_expirations),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditAuthSettingsTestTags.SECTION_LIMITS_TITLE)
        )

        CoreOutlinedTextField(
            value = state.maxTotalIdentifiers,
            onValueChange = onMaxTotalIdentifiersChanged,
            label = { CoreBodyText(stringResource(Res.string.max_total_identifiers)) },
            placeholder = { CoreBodyText(stringResource(Res.string.max_total_identifiers)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.MAX_TOTAL_IDENTIFIERS_INPUT)
        )

        CoreOutlinedTextField(
            value = state.maxEmailIdentifiers,
            onValueChange = onMaxEmailIdentifiersChanged,
            label = { CoreBodyText(stringResource(Res.string.max_email_identifiers)) },
            placeholder = { CoreBodyText(stringResource(Res.string.max_email_identifiers)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.MAX_EMAIL_IDENTIFIERS_INPUT)
        )

        CoreOutlinedTextField(
            value = state.maxPhoneIdentifiers,
            onValueChange = onMaxPhoneIdentifiersChanged,
            label = { CoreBodyText(stringResource(Res.string.max_phone_identifiers)) },
            placeholder = { CoreBodyText(stringResource(Res.string.max_phone_identifiers)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.MAX_PHONE_IDENTIFIERS_INPUT)
        )

        CoreOutlinedTextField(
            value = state.maxIdentifiersPerExternalProvider,
            onValueChange = onMaxIdentifiersPerExternalProviderChanged,
            label = { CoreBodyText(stringResource(Res.string.max_identifiers_per_external_provider)) },
            placeholder = { CoreBodyText(stringResource(Res.string.max_identifiers_per_external_provider)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.MAX_IDENTIFIERS_PER_EXTERNAL_PROVIDER_INPUT)
        )

        CoreOutlinedTextField(
            value = state.maxActiveSessionsForOpenUser,
            onValueChange = onMaxActiveSessionsForOpenUserChanged,
            label = { CoreBodyText(stringResource(Res.string.max_active_sessions_open)) },
            placeholder = { CoreBodyText(stringResource(Res.string.max_active_sessions_open)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.MAX_ACTIVE_SESSIONS_OPEN_INPUT)
        )

        CoreOutlinedTextField(
            value = state.maxActiveSessionsForManagementUser,
            onValueChange = onMaxActiveSessionsForManagementUserChanged,
            label = { CoreBodyText(stringResource(Res.string.max_active_sessions_management)) },
            placeholder = { CoreBodyText(stringResource(Res.string.max_active_sessions_management)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.MAX_ACTIVE_SESSIONS_MANAGEMENT_INPUT)
        )

        CoreOutlinedTextField(
            value = state.accessTokenExpirationSeconds,
            onValueChange = onAccessTokenExpirationSecondsChanged,
            label = { CoreBodyText(stringResource(Res.string.access_token_expiration_seconds)) },
            placeholder = { CoreBodyText(stringResource(Res.string.access_token_expiration_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.ACCESS_TOKEN_EXPIRATION_INPUT)
        )

        CoreOutlinedTextField(
            value = state.refreshTokenExpirationSeconds,
            onValueChange = onRefreshTokenExpirationSecondsChanged,
            label = { CoreBodyText(stringResource(Res.string.refresh_token_expiration_seconds)) },
            placeholder = { CoreBodyText(stringResource(Res.string.refresh_token_expiration_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.REFRESH_TOKEN_EXPIRATION_INPUT)
        )

        CoreOutlinedTextField(
            value = state.accountDeletionGracePeriodSeconds,
            onValueChange = onAccountDeletionGracePeriodSecondsChanged,
            label = { CoreBodyText(stringResource(Res.string.account_deletion_delay_seconds)) },
            placeholder = { CoreBodyText(stringResource(Res.string.account_deletion_delay_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.ACCOUNT_DELETION_DELAY_INPUT)
        )

        CoreOutlinedTextField(
            value = state.accountDeletionCheckIntervalSeconds,
            onValueChange = onAccountDeletionCheckIntervalSecondsChanged,
            label = { CoreBodyText(stringResource(Res.string.account_deletion_check_interval_seconds)) },
            placeholder = { CoreBodyText(stringResource(Res.string.account_deletion_check_interval_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditAuthSettingsTestTags.ACCOUNT_DELETION_CHECK_INTERVAL_INPUT)
        )

        CoreTitleText(
            text = stringResource(Res.string.open_email_restriction_policy),
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = state.openEmailBlacklistEnabled,
                onCheckedChange = onOpenEmailBlacklistEnabledToggled
            )
            CoreBodyText(text = stringResource(Res.string.blacklist_enabled), modifier = Modifier.padding(start = CoreTheme.dimens.paddingSmall))
        }

        CoreOutlinedTextField(
            value = state.openEmailBlacklist,
            onValueChange = onOpenEmailBlacklistChanged,
            label = { CoreBodyText(stringResource(Res.string.email_blacklist_placeholder)) },
            placeholder = { CoreBodyText(stringResource(Res.string.email_blacklist_placeholder)) }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = state.openEmailWhitelistEnabled,
                onCheckedChange = onOpenEmailWhitelistEnabledToggled
            )
            CoreBodyText(text = stringResource(Res.string.whitelist_enabled), modifier = Modifier.padding(start = CoreTheme.dimens.paddingSmall))
        }

        CoreOutlinedTextField(
            value = state.openEmailWhitelist,
            onValueChange = onOpenEmailWhitelistChanged,
            label = { CoreBodyText(stringResource(Res.string.email_whitelist_placeholder)) },
            placeholder = { CoreBodyText(stringResource(Res.string.email_whitelist_placeholder)) }
        )

        CoreTitleText(
            text = stringResource(Res.string.management_email_restriction_policy),
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = state.managementEmailBlacklistEnabled,
                onCheckedChange = onManagementEmailBlacklistEnabledToggled
            )
            CoreBodyText(text = stringResource(Res.string.blacklist_enabled), modifier = Modifier.padding(start = CoreTheme.dimens.paddingSmall))
        }

        CoreOutlinedTextField(
            value = state.managementEmailBlacklist,
            onValueChange = onManagementEmailBlacklistChanged,
            label = { CoreBodyText(stringResource(Res.string.email_blacklist_placeholder)) },
            placeholder = { CoreBodyText(stringResource(Res.string.email_blacklist_placeholder)) }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = state.managementEmailWhitelistEnabled,
                onCheckedChange = onManagementEmailWhitelistEnabledToggled
            )
            CoreBodyText(text = stringResource(Res.string.whitelist_enabled), modifier = Modifier.padding(start = CoreTheme.dimens.paddingSmall))
        }

        CoreOutlinedTextField(
            value = state.managementEmailWhitelist,
            onValueChange = onManagementEmailWhitelistChanged,
            label = { CoreBodyText(stringResource(Res.string.email_whitelist_placeholder)) },
            placeholder = { CoreBodyText(stringResource(Res.string.email_whitelist_placeholder)) }
        )

        ErrorText(
            error = state.saveError,
            testTag = EditAuthSettingsTestTags.SAVE_ERROR_TEXT
        )

        CoreButton(
            text = stringResource(if (state.isSaving) Res.string.saving else Res.string.save),
            onClick = onSaveClick,
            enabled = !state.isSaving,
            modifier = Modifier.testTag(EditAuthSettingsTestTags.SAVE_BUTTON)
        )

        CoreTextButton(
            text = stringResource(Res.string.reset_to_defaults),
            onClick = onResetClick,
            enabled = !state.isSaving,
            modifier = Modifier.testTag(EditAuthSettingsTestTags.RESET_BUTTON)
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

private val defaultEditAuthSettingsPreviewState = EditAuthSettingsScreenState.Content(
    enabledProviders = setOf(UserAuthProvider.EMAIL, UserAuthProvider.GOOGLE),
    maxTotalIdentifiers = "10",
    maxEmailIdentifiers = "5",
    maxPhoneIdentifiers = "5",
    maxIdentifiersPerExternalProvider = "2",
    maxActiveSessionsForOpenUser = "3",
    maxActiveSessionsForManagementUser = "5",
    accessTokenExpirationSeconds = "3600",
    refreshTokenExpirationSeconds = "86400",
    accountDeletionGracePeriodSeconds = "604800",
    accountDeletionCheckIntervalSeconds = "86400"
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(EditAuthSettingsPreviewProvider::class) state: EditAuthSettingsScreenState
) {
    ScreenPreviewContainer {
        EditAuthSettingsScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        EditAuthSettingsScreenPreviewContent(state = defaultEditAuthSettingsPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        EditAuthSettingsScreenPreviewContent(state = defaultEditAuthSettingsPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        EditAuthSettingsScreenPreviewContent(state = defaultEditAuthSettingsPreviewState)
    }
}

object EditAuthSettingsTestTags {
    const val TITLE = "EditAuthSettings_Title"
    const val BACK_BUTTON = "EditAuthSettings_BackButton"
    const val GLOBAL_ERROR = "EditAuthSettings_GlobalError"

    const val SECTION_PROVIDERS_TITLE = "EditAuthSettings_SectionProvidersTitle"
    fun getProviderCheckboxTag(provider: UserAuthProvider) = "EditAuthSettings_ProviderCheckbox_${provider.name}"
    const val IS_REGISTRATION_ENABLED_CHECKBOX = "EditAuthSettings_IsRegistrationEnabledCheckbox"

    const val SECTION_LIMITS_TITLE = "EditAuthSettings_SectionLimitsTitle"
    const val MAX_TOTAL_IDENTIFIERS_INPUT = "EditAuthSettings_MaxTotalIdentifiersInput"
    const val MAX_EMAIL_IDENTIFIERS_INPUT = "EditAuthSettings_MaxEmailIdentifiersInput"
    const val MAX_PHONE_IDENTIFIERS_INPUT = "EditAuthSettings_MaxPhoneIdentifiersInput"
    const val MAX_IDENTIFIERS_PER_EXTERNAL_PROVIDER_INPUT = "EditAuthSettings_MaxIdentifiersPerExternalProviderInput"
    const val MAX_ACTIVE_SESSIONS_OPEN_INPUT = "EditAuthSettings_MaxActiveSessionsOpenInput"
    const val MAX_ACTIVE_SESSIONS_MANAGEMENT_INPUT = "EditAuthSettings_MaxActiveSessionsManagementInput"
    const val ACCESS_TOKEN_EXPIRATION_INPUT = "EditAuthSettings_AccessTokenExpirationInput"
    const val REFRESH_TOKEN_EXPIRATION_INPUT = "EditAuthSettings_RefreshTokenExpirationInput"
    const val ACCOUNT_DELETION_DELAY_INPUT = "EditAuthSettings_AccountDeletionDelayInput"
    const val ACCOUNT_DELETION_CHECK_INTERVAL_INPUT = "EditAuthSettings_AccountDeletionCheckIntervalInput"

    const val SAVE_ERROR_TEXT = "EditAuthSettings_SaveErrorText"
    const val SAVE_BUTTON = "EditAuthSettings_SaveButton"
    const val RESET_BUTTON = "EditAuthSettings_ResetButton"
}
