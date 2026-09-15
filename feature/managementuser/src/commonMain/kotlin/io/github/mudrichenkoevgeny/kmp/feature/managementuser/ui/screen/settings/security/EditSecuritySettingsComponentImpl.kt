package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.security

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.GetManagementSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.SaveRemoteSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.iprestriction.IpRestrictionPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.ManagementPasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings
import kotlinx.coroutines.launch

/**
 * Default implementation of [EditSecuritySettingsComponent].
 */
class EditSecuritySettingsComponentImpl(
    componentContext: ComponentContext,
    private val getManagementSecuritySettingsUseCase: GetManagementSecuritySettingsUseCase,
    private val saveRemoteSecuritySettingsUseCase: SaveRemoteSecuritySettingsUseCase,
    private val onBack: () -> Unit
) : EditSecuritySettingsComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<EditSecuritySettingsScreenState>(EditSecuritySettingsScreenState.Loading)
    override val state: Value<EditSecuritySettingsScreenState> = _state

    init {
        loadSettings()
    }

    override fun onRetry() {
        loadSettings()
    }

    private fun loadSettings() {
        scope.launch {
            _state.value = EditSecuritySettingsScreenState.Loading
            val result = getManagementSecuritySettingsUseCase()
            val nextState = when (result) {
                is AppResult.Success -> {
                    val settings = result.data
                    EditSecuritySettingsScreenState.Content(
                        recentAuthenticationValiditySecondsForOpenUser = settings.recentAuthenticationValiditySecondsForOpenUser.toString(),
                        recentAuthenticationValiditySecondsForManagementUser = settings.recentAuthenticationValiditySecondsForManagementUser.toString(),
                        mfaTokenExpirationSeconds = settings.mfaTokenExpirationSeconds.toString(),
                        passwordMinLength = settings.passwordPolicy.minLength.toString(),
                        passwordRequireLetter = settings.passwordPolicy.requireLetter,
                        passwordRequireUpperCase = settings.passwordPolicy.requireUpperCase,
                        passwordRequireLowerCase = settings.passwordPolicy.requireLowerCase,
                        passwordRequireDigit = settings.passwordPolicy.requireDigit,
                        passwordRequireSpecialChar = settings.passwordPolicy.requireSpecialChar,
                        commonPasswords = settings.passwordPolicy.commonPasswords.joinToString(","),
                        accountLockoutMaxFailedPasswordAttempts = settings.accountLockoutPolicy.maxFailedPasswordAttempts.toString(),
                        accountLockoutMaxFailedOtpAttempts = settings.accountLockoutPolicy.maxFailedOtpAttempts.toString(),
                        accountLockoutMaxFailedTotpAttempts = settings.accountLockoutPolicy.maxFailedTotpAttempts.toString(),
                        accountLockoutFailedAttemptsWindowSeconds = settings.accountLockoutPolicy.failedAttemptsWindowSeconds.toString(),
                        accountLockoutDurationSeconds = settings.accountLockoutPolicy.lockoutDurationSeconds.toString(),
                        accountLockoutIndefiniteLockoutThreshold = settings.accountLockoutPolicy.indefiniteLockoutThreshold.toString(),
                        accountLockoutIsSelfServiceUnlockEnabled = settings.accountLockoutPolicy.isSelfServiceUnlockEnabled,
                        accountLockoutCheckIntervalSeconds = settings.accountLockoutCheckIntervalSeconds.toString(),
                        refreshTokenRotationGracePeriodSeconds = settings.refreshTokenRotationGracePeriodSeconds.toString(),
                        openIpBlacklistEnabled = settings.openIpRestrictionPolicy.isBlacklistEnabled,
                        openIpBlacklist = settings.openIpRestrictionPolicy.blacklist.joinToString(","),
                        openIpWhitelistEnabled = settings.openIpRestrictionPolicy.isWhitelistEnabled,
                        openIpWhitelist = settings.openIpRestrictionPolicy.whitelist.joinToString(","),
                        managementIpBlacklistEnabled = settings.managementIpRestrictionPolicy.isBlacklistEnabled,
                        managementIpBlacklist = settings.managementIpRestrictionPolicy.blacklist.joinToString(","),
                        managementIpWhitelistEnabled = settings.managementIpRestrictionPolicy.isWhitelistEnabled,
                        managementIpWhitelist = settings.managementIpRestrictionPolicy.whitelist.joinToString(","),
                        otpRetryAfterSeconds = settings.otpConfirmation.retryAfterSeconds.toString(),
                        otpNumberOfSymbols = settings.otpConfirmation.numberOfSymbols.toString(),
                        otpExpirationSeconds = settings.otpConfirmation.expirationSeconds.toString(),
                        maxRequestsPerPeriod = settings.maxRequestsPerPeriod.toString(),
                        rateLimitPeriodSeconds = settings.rateLimitPeriodSeconds.toString()
                    )
                }
                is AppResult.Error -> EditSecuritySettingsScreenState.Error(result.error)
            }
            _state.value = nextState
        }
    }

    override fun onRecentAuthenticationValidityForOpenUserChanged(value: String) {
        updateContent { copy(recentAuthenticationValiditySecondsForOpenUser = value, saveError = null) }
    }

    override fun onRecentAuthenticationValidityForManagementUserChanged(value: String) {
        updateContent { copy(recentAuthenticationValiditySecondsForManagementUser = value, saveError = null) }
    }

    override fun onMfaTokenExpirationSecondsChanged(value: String) {
        updateContent { copy(mfaTokenExpirationSeconds = value, saveError = null) }
    }

    override fun onPasswordMinLengthChanged(value: String) {
        updateContent { copy(passwordMinLength = value, saveError = null) }
    }

    override fun onPasswordRequireLetterToggled(enabled: Boolean) {
        updateContent { copy(passwordRequireLetter = enabled, saveError = null) }
    }

    override fun onPasswordRequireUpperCaseToggled(enabled: Boolean) {
        updateContent { copy(passwordRequireUpperCase = enabled, saveError = null) }
    }

    override fun onPasswordRequireLowerCaseToggled(enabled: Boolean) {
        updateContent { copy(passwordRequireLowerCase = enabled, saveError = null) }
    }

    override fun onPasswordRequireDigitToggled(enabled: Boolean) {
        updateContent { copy(passwordRequireDigit = enabled, saveError = null) }
    }

    override fun onPasswordRequireSpecialCharToggled(enabled: Boolean) {
        updateContent { copy(passwordRequireSpecialChar = enabled, saveError = null) }
    }

    override fun onCommonPasswordsChanged(value: String) {
        updateContent { copy(commonPasswords = value, saveError = null) }
    }

    override fun onAccountLockoutMaxFailedPasswordAttemptsChanged(value: String) {
        updateContent { copy(accountLockoutMaxFailedPasswordAttempts = value, saveError = null) }
    }

    override fun onAccountLockoutMaxFailedOtpAttemptsChanged(value: String) {
        updateContent { copy(accountLockoutMaxFailedOtpAttempts = value, saveError = null) }
    }

    override fun onAccountLockoutMaxFailedTotpAttemptsChanged(value: String) {
        updateContent { copy(accountLockoutMaxFailedTotpAttempts = value, saveError = null) }
    }

    override fun onAccountLockoutFailedAttemptsWindowSecondsChanged(value: String) {
        updateContent { copy(accountLockoutFailedAttemptsWindowSeconds = value, saveError = null) }
    }

    override fun onAccountLockoutDurationSecondsChanged(value: String) {
        updateContent { copy(accountLockoutDurationSeconds = value, saveError = null) }
    }

    override fun onAccountLockoutIndefiniteLockoutThresholdChanged(value: String) {
        updateContent { copy(accountLockoutIndefiniteLockoutThreshold = value, saveError = null) }
    }

    override fun onAccountLockoutIsSelfServiceUnlockEnabledToggled(enabled: Boolean) {
        updateContent { copy(accountLockoutIsSelfServiceUnlockEnabled = enabled, saveError = null) }
    }

    override fun onAccountLockoutCheckIntervalSecondsChanged(value: String) {
        updateContent { copy(accountLockoutCheckIntervalSeconds = value, saveError = null) }
    }

    override fun onRefreshTokenRotationGracePeriodSecondsChanged(value: String) {
        updateContent { copy(refreshTokenRotationGracePeriodSeconds = value, saveError = null) }
    }

    override fun onOpenIpBlacklistEnabledToggled(enabled: Boolean) {
        updateContent { copy(openIpBlacklistEnabled = enabled, saveError = null) }
    }

    override fun onOpenIpBlacklistChanged(value: String) {
        updateContent { copy(openIpBlacklist = value, saveError = null) }
    }

    override fun onOpenIpWhitelistEnabledToggled(enabled: Boolean) {
        updateContent { copy(openIpWhitelistEnabled = enabled, saveError = null) }
    }

    override fun onOpenIpWhitelistChanged(value: String) {
        updateContent { copy(openIpWhitelist = value, saveError = null) }
    }

    override fun onManagementIpBlacklistEnabledToggled(enabled: Boolean) {
        updateContent { copy(managementIpBlacklistEnabled = enabled, saveError = null) }
    }

    override fun onManagementIpBlacklistChanged(value: String) {
        updateContent { copy(managementIpBlacklist = value, saveError = null) }
    }

    override fun onManagementIpWhitelistEnabledToggled(enabled: Boolean) {
        updateContent { copy(managementIpWhitelistEnabled = enabled, saveError = null) }
    }

    override fun onManagementIpWhitelistChanged(value: String) {
        updateContent { copy(managementIpWhitelist = value, saveError = null) }
    }

    override fun onOtpRetryAfterSecondsChanged(value: String) {
        updateContent { copy(otpRetryAfterSeconds = value, saveError = null) }
    }

    override fun onOtpNumberOfSymbolsChanged(value: String) {
        updateContent { copy(otpNumberOfSymbols = value, saveError = null) }
    }

    override fun onOtpExpirationSecondsChanged(value: String) {
        updateContent { copy(otpExpirationSeconds = value, saveError = null) }
    }

    override fun onMaxRequestsPerPeriodChanged(value: String) {
        updateContent { copy(maxRequestsPerPeriod = value, saveError = null) }
    }

    override fun onRateLimitPeriodSecondsChanged(value: String) {
        updateContent { copy(rateLimitPeriodSeconds = value, saveError = null) }
    }

    private inline fun updateContent(transform: EditSecuritySettingsScreenState.Content.() -> EditSecuritySettingsScreenState.Content) {
        val current = _state.value as? EditSecuritySettingsScreenState.Content ?: return
        _state.value = current.transform()
    }

    override fun onSaveClick() {
        val current = _state.value as? EditSecuritySettingsScreenState.Content ?: return
        scope.launch {
            _state.value = current.copy(isSaving = true, saveError = null)

            val settings = ManagementSecuritySettings(
                recentAuthenticationValiditySecondsForOpenUser = current.recentAuthenticationValiditySecondsForOpenUser.toIntOrNull() ?: 300,
                recentAuthenticationValiditySecondsForManagementUser = current.recentAuthenticationValiditySecondsForManagementUser.toIntOrNull() ?: 300,
                passwordPolicy = ManagementPasswordPolicy(
                    minLength = current.passwordMinLength.toIntOrNull() ?: 8,
                    requireLetter = current.passwordRequireLetter,
                    requireUpperCase = current.passwordRequireUpperCase,
                    requireLowerCase = current.passwordRequireLowerCase,
                    requireDigit = current.passwordRequireDigit,
                    requireSpecialChar = current.passwordRequireSpecialChar,
                    commonPasswords = current.commonPasswords.split(",").map { it.trim() }.filter { it.isNotBlank() }.toSet()
                ),
                otpConfirmation = OtpConfirmation(
                    retryAfterSeconds = current.otpRetryAfterSeconds.toIntOrNull() ?: 60,
                    numberOfSymbols = current.otpNumberOfSymbols.toIntOrNull() ?: 6,
                    expirationSeconds = current.otpExpirationSeconds.toIntOrNull() ?: 300
                ),
                accountLockoutPolicy = AccountLockoutPolicy(
                    maxFailedPasswordAttempts = current.accountLockoutMaxFailedPasswordAttempts.toIntOrNull() ?: 5,
                    maxFailedOtpAttempts = current.accountLockoutMaxFailedOtpAttempts.toIntOrNull() ?: 5,
                    maxFailedTotpAttempts = current.accountLockoutMaxFailedTotpAttempts.toIntOrNull() ?: 5,
                    failedAttemptsWindowSeconds = current.accountLockoutFailedAttemptsWindowSeconds.toIntOrNull() ?: 300,
                    lockoutDurationSeconds = current.accountLockoutDurationSeconds.toIntOrNull() ?: 300,
                    indefiniteLockoutThreshold = current.accountLockoutIndefiniteLockoutThreshold.toIntOrNull() ?: 3,
                    isSelfServiceUnlockEnabled = current.accountLockoutIsSelfServiceUnlockEnabled
                ),
                accountLockoutCheckIntervalSeconds = current.accountLockoutCheckIntervalSeconds.toIntOrNull() ?: 60,
                openIpRestrictionPolicy = IpRestrictionPolicy(
                    isBlacklistEnabled = current.openIpBlacklistEnabled,
                    blacklist = current.openIpBlacklist.split(",").map { it.trim() }.filter { it.isNotBlank() },
                    isWhitelistEnabled = current.openIpWhitelistEnabled,
                    whitelist = current.openIpWhitelist.split(",").map { it.trim() }.filter { it.isNotBlank() }
                ),
                managementIpRestrictionPolicy = IpRestrictionPolicy(
                    isBlacklistEnabled = current.managementIpBlacklistEnabled,
                    blacklist = current.managementIpBlacklist.split(",").map { it.trim() }.filter { it.isNotBlank() },
                    isWhitelistEnabled = current.managementIpWhitelistEnabled,
                    whitelist = current.managementIpWhitelist.split(",").map { it.trim() }.filter { it.isNotBlank() }
                ),
                mfaTokenExpirationSeconds = current.mfaTokenExpirationSeconds.toIntOrNull() ?: 180,
                maxRequestsPerPeriod = current.maxRequestsPerPeriod.toIntOrNull() ?: 100,
                rateLimitPeriodSeconds = current.rateLimitPeriodSeconds.toIntOrNull() ?: 60,
                refreshTokenRotationGracePeriodSeconds = current.refreshTokenRotationGracePeriodSeconds.toIntOrNull() ?: 30
            )

            val saveResult = saveRemoteSecuritySettingsUseCase(settings)
            val nextState = when (saveResult) {
                is AppResult.Success -> {
                    onBack()
                    current.copy(isSaving = false)
                }
                is AppResult.Error -> current.copy(isSaving = false, saveError = saveResult.error)
            }
            _state.value = nextState
        }
    }

    override fun onBackClick() {
        onBack()
    }
}
