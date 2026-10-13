package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * Default [TotpMainComponent]: implementation of TOTP lifecycle management (setup, verification, recovery).
 *
 * @param componentContext Decompose [ComponentContext].
 * @param initialConfig Optional initial step configuration.
 * @param userRepository Source of the current user profile state.
 * @param setupTotpUseCase Generates TOTP secret and setup URI.
 * @param enableTotpUseCase Verifies initial code and enables TOTP.
 * @param disableTotpUseCase Turns off TOTP for the account.
 * @param onNavigateToRecoveryCodes Opens the recovery codes screen.
 * @param onBack Pops this screen from the navigation stack.
 */
class TotpMainComponentImpl(
    componentContext: ComponentContext,
    initialConfig: TotpStepConfig = TotpStepConfig.Status,
    private val userRepository: UserRepository,
    private val setupTotpUseCase: SetupTotpUseCase,
    private val enableTotpUseCase: EnableTotpUseCase,
    private val disableTotpUseCase: DisableTotpUseCase,
    private val onNavigateToRecoveryCodes: () -> Unit,
    val onBack: () -> Unit
) : TotpMainComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()

    override val stackNavigation = StackNavigation<TotpStepConfig>()
    override val dialogSlotNavigation = SlotNavigation<TotpDialogConfig>()

    override val stack: Value<ChildStack<TotpStepConfig, TotpMainComponent.Child>> =
        childStack(
            source = stackNavigation,
            serializer = TotpStepConfig.serializer(),
            initialConfiguration = initialConfig,
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
        scope.launch {
            userRepository.currentUser
                .catch { _state.value = _state.value.copy(initError = CommonError.Unknown()) }
                .collect { user ->
                    if (user == null) {
                        _state.value = _state.value.copy(initError = CommonError.Unknown())
                    } else {
                        _state.value = _state.value.copy(
                            isInitialized = true,
                            isEnabled = user.isTotpEnabled,
                            initError = null
                        )
                        if (!user.isTotpEnabled && stack.value.active.configuration !is TotpStepConfig.Setup) {
                            stackNavigation.bringToFront(TotpStepConfig.Status)
                        } else if (user.isTotpEnabled && stack.value.active.configuration !is TotpStepConfig.Status) {
                            stackNavigation.bringToFront(TotpStepConfig.Status)
                        }
                    }
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

    override fun onSetupClick() {
        if (_state.value.actionLoading) return
        _state.value = _state.value.copy(actionLoading = true, actionError = null)

        scope.launch {
            setupTotpUseCase()
                .onSuccess { setupData ->
                    _state.value = _state.value.copy(actionLoading = false, actionError = null, code = "")
                    stackNavigation.bringToFront(
                        TotpStepConfig.Setup(
                            mfaToken = setupData.mfaToken,
                            otpAuthUrl = setupData.otpAuthUrl,
                            secretKey = setupData.secretKey
                        )
                    )
                }
                .onError { error ->
                    _state.value = _state.value.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onCodeChanged(code: String) {
        _state.value = _state.value.copy(code = code, actionError = null)
    }

    override fun onConfirmSetupClick() {
        val currentChild = stack.value.active.configuration as? TotpStepConfig.Setup ?: return
        if (_state.value.actionLoading || !_state.value.canConfirm) return

        _state.value = _state.value.copy(actionLoading = true, actionError = null)

        scope.launch {
            enableTotpUseCase(
                mfaToken = currentChild.mfaToken,
                code = _state.value.code
            )
                .onSuccess {
                    userRepository.refreshCurrentUser()
                    stackNavigation.bringToFront(TotpStepConfig.Status)
                    _state.value = _state.value.copy(actionLoading = false, code = "", actionError = null)
                    onNavigateToRecoveryCodes()
                }
                .onError { error ->
                    _state.value = _state.value.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onDisableClick() {
        if (_state.value.actionLoading) return
        dialogSlotNavigation.activate(TotpDialogConfig.ConfirmDisable)
    }

    override fun onConfirmDisable() {
        _state.value = _state.value.copy(actionLoading = true, actionError = null)

        scope.launch {
            disableTotpUseCase()
                .onSuccess {
                    userRepository.refreshCurrentUser()
                    _state.value = _state.value.copy(actionLoading = false, actionError = null)
                    dialogSlotNavigation.dismiss()
                }
                .onError { error ->
                    _state.value = _state.value.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onDismissDialogs() {
        dialogSlotNavigation.dismiss()
    }

    override fun onRecoveryCodesClick() {
        onNavigateToRecoveryCodes()
    }

    override fun onBackClick() {
        if ((backHandler as? BackDispatcher)?.back() != true) {
            onBack()
        }
    }
}
