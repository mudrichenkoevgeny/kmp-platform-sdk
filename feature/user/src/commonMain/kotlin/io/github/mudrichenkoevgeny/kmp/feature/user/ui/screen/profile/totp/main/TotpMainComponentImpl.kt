package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.DisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.EnableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.SetupTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.totp.TotpStep
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.totp.main.TotpMainParams
import kotlinx.coroutines.launch

/**
 * Default implementation of [TotpMainComponent].
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params TOTP main screen parameters.
 * @param userRepository Source of current user profile state.
 * @param setupTotpUseCase Generates TOTP secret and setup URI.
 * @param enableTotpUseCase Verifies initial code and enables TOTP.
 * @param disableTotpUseCase Turns off TOTP for the account.
 * @param onNavigateToRecoveryCodes Opens the recovery codes screen.
 * @param onBack Pops this screen from the navigation stack.
 */
class TotpMainComponentImpl(
    componentContext: ComponentContext,
    params: TotpMainParams = TotpMainParams(),
    private val userRepository: UserRepository,
    private val setupTotpUseCase: SetupTotpUseCase,
    private val enableTotpUseCase: EnableTotpUseCase,
    private val disableTotpUseCase: DisableTotpUseCase,
    private val onNavigateToRecoveryCodes: () -> Unit,
    val onBack: () -> Unit
) : TotpMainComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()

    private val startStepConfig: TotpStepConfig = if (params.totpStep == TotpStep.SETUP) {
        TotpStepConfig.Setup(mfaToken = "", otpAuthUrl = "", secretKey = "")
    } else {
        TotpStepConfig.Status
    }

    override val stackNavigation = StackNavigation<TotpStepConfig>()
    override val dialogSlotNavigation = SlotNavigation<TotpDialogConfig>()

    override val stack: Value<ChildStack<TotpStepConfig, TotpMainComponent.Child>> =
        childStack(
            source = stackNavigation,
            serializer = TotpStepConfig.serializer(),
            initialConfiguration = startStepConfig,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override val dialogSlot: Value<ChildSlot<TotpDialogConfig, TotpMainComponent.DialogChild>> =
        childSlot(
            source = dialogSlotNavigation,
            serializer = TotpDialogConfig.serializer(),
            handleBackButton = true,
            childFactory = ::createDialogChild
        )

    private val _state = MutableValue(TotpMainScreenState())
    override val state: Value<TotpMainScreenState> = _state

    init {
        loadUserStatus()
    }

    @OptIn(DelicateDecomposeApi::class)
    override fun onSetupClick() {
        _state.value = _state.value.copy(actionLoading = true, actionError = null)
        scope.launch {
            setupTotpUseCase()
                .onSuccess { setupData ->
                    val setupConfig = TotpStepConfig.Setup(
                        mfaToken = setupData.mfaToken,
                        otpAuthUrl = setupData.otpAuthUrl,
                        secretKey = setupData.secretKey
                    )
                    stackNavigation.push(setupConfig)
                    _state.value = _state.value.copy(actionLoading = false)
                }
                .onError { error ->
                    _state.value = _state.value.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onCodeChanged(code: String) {
        if (code.length <= FieldValidator.TOTP_CODE_LENGTH) {
            _state.value = _state.value.copy(code = code, actionError = null)
        }
    }

    override fun onConfirmSetupClick() {
        val currentCode = _state.value.code
        if (currentCode.length != FieldValidator.TOTP_CODE_LENGTH) {
            return
        }
        val activeSetupConfig = stack.value.active.configuration as? TotpStepConfig.Setup ?: return
        _state.value = _state.value.copy(actionLoading = true, actionError = null)

        scope.launch {
            enableTotpUseCase(activeSetupConfig.mfaToken, currentCode)
                .onSuccess {
                    _state.value = _state.value.copy(
                        isEnabled = true,
                        code = "",
                        actionLoading = false
                    )
                    stackNavigation.pop()
                    onNavigateToRecoveryCodes()
                }
                .onError { error ->
                    _state.value = _state.value.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onRecoveryCodesClick() {
        onNavigateToRecoveryCodes()
    }

    override fun onDisableClick() {
        dialogSlotNavigation.activate(TotpDialogConfig.ConfirmDisable)
    }

    override fun onConfirmDisable() {
        _state.value = _state.value.copy(actionLoading = true, actionError = null)
        scope.launch {
            disableTotpUseCase()
                .onSuccess {
                    dialogSlotNavigation.dismiss()
                    _state.value = _state.value.copy(
                        isEnabled = false,
                        code = "",
                        actionLoading = false
                    )
                }
                .onError { error ->
                    dialogSlotNavigation.dismiss()
                    _state.value = _state.value.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onDismissDialogs() {
        dialogSlotNavigation.dismiss()
    }

    override fun onBackClick() {
        if ((backHandler as? BackDispatcher)?.back() != true) {
            onBack()
        }
    }

    private fun loadUserStatus() {
        scope.launch {
            try {
                userRepository.currentUser.collect { user ->
                    if (user != null) {
                        _state.value = _state.value.copy(
                            isInitialized = true,
                            isEnabled = user.isTotpEnabled
                        )
                    }
                }
            } catch (_: Exception) {
                _state.value = _state.value.copy(
                    isInitialized = true,
                    initError = CommonError.Unknown()
                )
            }
        }
    }

    private fun createChild(
        config: TotpStepConfig,
        context: ComponentContext
    ): TotpMainComponent.Child = when (config) {
        is TotpStepConfig.Status -> TotpMainComponent.Child.Status
        is TotpStepConfig.Setup -> TotpMainComponent.Child.Setup(
            mfaToken = config.mfaToken,
            otpAuthUrl = config.otpAuthUrl,
            secretKey = config.secretKey
        )
    }

    private fun createDialogChild(
        config: TotpDialogConfig,
        context: ComponentContext
    ): TotpMainComponent.DialogChild = when (config) {
        is TotpDialogConfig.ConfirmDisable -> TotpMainComponent.DialogChild.ConfirmDisable
    }
}
