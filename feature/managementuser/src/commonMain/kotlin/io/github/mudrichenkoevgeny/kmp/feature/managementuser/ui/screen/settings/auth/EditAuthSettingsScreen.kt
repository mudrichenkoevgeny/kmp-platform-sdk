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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error.FullscreenError
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.*
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
                    Text(
                        text = stringResource(Res.string.edit_auth_settings_title),
                        modifier = Modifier.testTag(EditAuthSettingsTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(EditAuthSettingsTestTags.BACK_BUTTON)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
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
                    onMaxActiveSessionsChanged = component::onMaxActiveSessionsChanged,
                    onAccessTokenExpirationSecondsChanged = component::onAccessTokenExpirationSecondsChanged,
                    onRefreshTokenExpirationSecondsChanged = component::onRefreshTokenExpirationSecondsChanged,
                    onAccountDeletionDelaySecondsChanged = component::onAccountDeletionDelaySecondsChanged,
                    onRegistrationEnabledToggled = component::onRegistrationEnabledToggled,
                    onSaveClick = component::onSaveClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(Dimens.paddingMedium)
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
    onMaxActiveSessionsChanged: (String) -> Unit,
    onAccessTokenExpirationSecondsChanged: (String) -> Unit,
    onRefreshTokenExpirationSecondsChanged: (String) -> Unit,
    onAccountDeletionDelaySecondsChanged: (String) -> Unit,
    onRegistrationEnabledToggled: (Boolean) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
    ) {
        Text(
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
                Text(
                    text = provider.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = Dimens.paddingSmall)
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
            Text(
                text = stringResource(Res.string.is_registration_enabled),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = Dimens.paddingSmall)
            )
        }

        Text(
            text = stringResource(Res.string.limits_and_expirations),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditAuthSettingsTestTags.SECTION_LIMITS_TITLE)
        )

        OutlinedTextField(
            value = state.maxTotalIdentifiers,
            onValueChange = onMaxTotalIdentifiersChanged,
            label = { Text(text = stringResource(Res.string.max_total_identifiers)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditAuthSettingsTestTags.MAX_TOTAL_IDENTIFIERS_INPUT)
        )

        OutlinedTextField(
            value = state.maxEmailIdentifiers,
            onValueChange = onMaxEmailIdentifiersChanged,
            label = { Text(text = stringResource(Res.string.max_email_identifiers)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditAuthSettingsTestTags.MAX_EMAIL_IDENTIFIERS_INPUT)
        )

        OutlinedTextField(
            value = state.maxPhoneIdentifiers,
            onValueChange = onMaxPhoneIdentifiersChanged,
            label = { Text(text = stringResource(Res.string.max_phone_identifiers)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditAuthSettingsTestTags.MAX_PHONE_IDENTIFIERS_INPUT)
        )

        OutlinedTextField(
            value = state.maxIdentifiersPerExternalProvider,
            onValueChange = onMaxIdentifiersPerExternalProviderChanged,
            label = { Text(text = stringResource(Res.string.max_identifiers_per_external_provider)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditAuthSettingsTestTags.MAX_IDENTIFIERS_PER_EXTERNAL_PROVIDER_INPUT)
        )

        OutlinedTextField(
            value = state.maxActiveSessions,
            onValueChange = onMaxActiveSessionsChanged,
            label = { Text(text = stringResource(Res.string.max_active_sessions)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditAuthSettingsTestTags.MAX_ACTIVE_SESSIONS_INPUT)
        )

        OutlinedTextField(
            value = state.accessTokenExpirationSeconds,
            onValueChange = onAccessTokenExpirationSecondsChanged,
            label = { Text(text = stringResource(Res.string.access_token_expiration_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditAuthSettingsTestTags.ACCESS_TOKEN_EXPIRATION_INPUT)
        )

        OutlinedTextField(
            value = state.refreshTokenExpirationSeconds,
            onValueChange = onRefreshTokenExpirationSecondsChanged,
            label = { Text(text = stringResource(Res.string.refresh_token_expiration_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditAuthSettingsTestTags.REFRESH_TOKEN_EXPIRATION_INPUT)
        )

        OutlinedTextField(
            value = state.accountDeletionDelaySeconds,
            onValueChange = onAccountDeletionDelaySecondsChanged,
            label = { Text(text = stringResource(Res.string.account_deletion_delay_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditAuthSettingsTestTags.ACCOUNT_DELETION_DELAY_INPUT)
        )

        ErrorText(
            error = state.saveError,
            testTag = EditAuthSettingsTestTags.SAVE_ERROR_TEXT
        )

        Button(
            onClick = onSaveClick,
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditAuthSettingsTestTags.SAVE_BUTTON)
        ) {
            Text(text = stringResource(if (state.isSaving) Res.string.saving else Res.string.save))
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
                    .padding(top = Dimens.paddingSmall)
                    .testTag(testTag)
            )
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun EditAuthSettingsContentPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                EditAuthSettingsForm(
                    state = EditAuthSettingsScreenState.Content(
                        enabledProviders = setOf(UserAuthProvider.EMAIL, UserAuthProvider.GOOGLE),
                        maxTotalIdentifiers = "10",
                        maxEmailIdentifiers = "5",
                        maxPhoneIdentifiers = "5",
                        maxIdentifiersPerExternalProvider = "2",
                        maxActiveSessions = "3",
                        accessTokenExpirationSeconds = "3600",
                        refreshTokenExpirationSeconds = "86400",
                        accountDeletionDelaySeconds = "604800",
                        isRegistrationEnabled = true
                    ),
                    onProviderToggled = { _, _ -> },
                    onMaxTotalIdentifiersChanged = {},
                    onMaxEmailIdentifiersChanged = {},
                    onMaxPhoneIdentifiersChanged = {},
                    onMaxIdentifiersPerExternalProviderChanged = {},
                    onMaxActiveSessionsChanged = {},
                    onAccessTokenExpirationSecondsChanged = {},
                    onRefreshTokenExpirationSecondsChanged = {},
                    onAccountDeletionDelaySecondsChanged = {},
                    onRegistrationEnabledToggled = {},
                    onSaveClick = {}
                )
            }
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun EditAuthSettingsErrorPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                EditAuthSettingsForm(
                    state = EditAuthSettingsScreenState.Content(
                        enabledProviders = setOf(UserAuthProvider.EMAIL),
                        maxTotalIdentifiers = "10",
                        maxEmailIdentifiers = "5",
                        maxPhoneIdentifiers = "5",
                        maxIdentifiersPerExternalProvider = "2",
                        maxActiveSessions = "3",
                        accessTokenExpirationSeconds = "3600",
                        refreshTokenExpirationSeconds = "86400",
                        accountDeletionDelaySeconds = "604800",
                        isRegistrationEnabled = true,
                        saveError = CommonError.Unknown()
                    ),
                    onProviderToggled = { _, _ -> },
                    onMaxTotalIdentifiersChanged = {},
                    onMaxEmailIdentifiersChanged = {},
                    onMaxPhoneIdentifiersChanged = {},
                    onMaxIdentifiersPerExternalProviderChanged = {},
                    onMaxActiveSessionsChanged = {},
                    onAccessTokenExpirationSecondsChanged = {},
                    onRefreshTokenExpirationSecondsChanged = {},
                    onAccountDeletionDelaySecondsChanged = {},
                    onRegistrationEnabledToggled = {},
                    onSaveClick = {}
                )
            }
        }
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
    const val MAX_ACTIVE_SESSIONS_INPUT = "EditAuthSettings_MaxActiveSessionsInput"
    const val ACCESS_TOKEN_EXPIRATION_INPUT = "EditAuthSettings_AccessTokenExpirationInput"
    const val REFRESH_TOKEN_EXPIRATION_INPUT = "EditAuthSettings_RefreshTokenExpirationInput"
    const val ACCOUNT_DELETION_DELAY_INPUT = "EditAuthSettings_AccountDeletionDelayInput"

    const val SAVE_ERROR_TEXT = "EditAuthSettings_SaveErrorText"
    const val SAVE_BUTTON = "EditAuthSettings_SaveButton"
}
