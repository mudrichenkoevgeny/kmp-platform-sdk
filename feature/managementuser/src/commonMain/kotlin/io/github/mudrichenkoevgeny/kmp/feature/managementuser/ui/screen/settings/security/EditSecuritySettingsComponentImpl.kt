package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.security

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.GetManagementSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.SaveRemoteSecuritySettingsUseCase
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

    private var initialCommonPasswords: Set<String> = emptySet()

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
                    initialCommonPasswords = settings.passwordPolicy.commonPasswords
                    EditSecuritySettingsScreenState.Content(
                        recentAuthenticationValiditySeconds = settings.recentAuthenticationValiditySeconds.toString(),
                        recentAuthenticationValiditySecondsForManagement = settings.recentAuthenticationValiditySecondsForManagement.toString(),
                        mfaTokenExpirationSeconds = settings.mfaTokenExpirationSeconds.toString(),
                        passwordMinLength = settings.passwordPolicy.minLength.toString(),
                        passwordRequireLetter = settings.passwordPolicy.requireLetter,
                        passwordRequireUpperCase = settings.passwordPolicy.requireUpperCase,
                        passwordRequireLowerCase = settings.passwordPolicy.requireLowerCase,
                        passwordRequireDigit = settings.passwordPolicy.requireDigit,
                        passwordRequireSpecialChar = settings.passwordPolicy.requireSpecialChar,
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

    override fun onRecentAuthenticationValiditySecondsChanged(value: String) {
        updateContent { copy(recentAuthenticationValiditySeconds = value, saveError = null) }
    }

    override fun onRecentAuthenticationValidityForManagementChanged(value: String) {
        updateContent { copy(recentAuthenticationValiditySecondsForManagement = value, saveError = null) }
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
                recentAuthenticationValiditySeconds = current.recentAuthenticationValiditySeconds.toIntOrNull() ?: 300,
                recentAuthenticationValiditySecondsForManagement = current.recentAuthenticationValiditySecondsForManagement.toIntOrNull() ?: 300,
                mfaTokenExpirationSeconds = current.mfaTokenExpirationSeconds.toIntOrNull() ?: 300,
                passwordPolicy = ManagementPasswordPolicy(
                    minLength = current.passwordMinLength.toIntOrNull() ?: 8,
                    requireLetter = current.passwordRequireLetter,
                    requireUpperCase = current.passwordRequireUpperCase,
                    requireLowerCase = current.passwordRequireLowerCase,
                    requireDigit = current.passwordRequireDigit,
                    requireSpecialChar = current.passwordRequireSpecialChar,
                    commonPasswords = initialCommonPasswords
                ),
                otpConfirmation = OtpConfirmation(
                    retryAfterSeconds = current.otpRetryAfterSeconds.toIntOrNull() ?: 60,
                    numberOfSymbols = current.otpNumberOfSymbols.toIntOrNull() ?: 6,
                    expirationSeconds = current.otpExpirationSeconds.toIntOrNull() ?: 300
                ),
                maxRequestsPerPeriod = current.maxRequestsPerPeriod.toIntOrNull() ?: 100,
                rateLimitPeriodSeconds = current.rateLimitPeriodSeconds.toIntOrNull() ?: 60
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
