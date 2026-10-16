package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.unlock.UnlockMethod
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockEmailConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockPhoneConfirmationUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.unlock.targetinput.UnlockTargetInputParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Default implementation of [UnlockTargetInputComponent].
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Unlock target input parameters.
 * @param prefilledInput Optional prefilled email or phone input string.
 * @param sendUnlockEmailConfirmationUseCase Sends unlock confirmation code to email.
 * @param sendUnlockPhoneConfirmationUseCase Sends unlock confirmation code to phone.
 * @param onNavigateToOtp Navigates to OTP verification screen.
 * @param onBack Pops this screen from the navigation stack.
 * @param coroutineScope Scope for async operations.
 */
class UnlockTargetInputComponentImpl(
    componentContext: ComponentContext,
    params: UnlockTargetInputParams = UnlockTargetInputParams(),
    prefilledInput: String = "",
    private val sendUnlockEmailConfirmationUseCase: SendUnlockEmailConfirmationUseCase,
    private val sendUnlockPhoneConfirmationUseCase: SendUnlockPhoneConfirmationUseCase,
    private val onNavigateToOtp: (target: String, initialDelaySeconds: Int) -> Unit,
    private val onBack: () -> Unit,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) : UnlockTargetInputComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        method: UnlockMethod,
        prefilledInput: String = "",
        sendUnlockEmailConfirmationUseCase: SendUnlockEmailConfirmationUseCase,
        sendUnlockPhoneConfirmationUseCase: SendUnlockPhoneConfirmationUseCase,
        onNavigateToOtp: (target: String, initialDelaySeconds: Int) -> Unit,
        onBack: () -> Unit,
        coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    ) : this(
        componentContext = componentContext,
        params = UnlockTargetInputParams(unlockMethod = method),
        prefilledInput = prefilledInput,
        sendUnlockEmailConfirmationUseCase = sendUnlockEmailConfirmationUseCase,
        sendUnlockPhoneConfirmationUseCase = sendUnlockPhoneConfirmationUseCase,
        onNavigateToOtp = onNavigateToOtp,
        onBack = onBack,
        coroutineScope = coroutineScope
    )

    private val _state = MutableValue(
        UnlockTargetInputScreenState(
            method = params.unlockMethod ?: UnlockMethod.EMAIL,
            input = prefilledInput
        )
    )
    override val state: Value<UnlockTargetInputScreenState> = _state

    override fun onInputChanged(value: String) {
        _state.update { it.copy(input = value, actionError = null) }
    }

    override fun onSendCodeClick() {
        val current = _state.value
        if (!current.canSendCode) {
            return
        }

        val target = current.input.trim()
        _state.update { it.copy(actionLoading = true, actionError = null) }

        coroutineScope.launch {
            val result = when (current.method) {
                UnlockMethod.EMAIL -> sendUnlockEmailConfirmationUseCase.execute(target)
                UnlockMethod.PHONE -> sendUnlockPhoneConfirmationUseCase.execute(target)
            }

            when (result) {
                is AppResult.Success -> {
                    _state.update { it.copy(actionLoading = false) }
                    onNavigateToOtp(target, result.data.retryAfterSeconds)
                }
                is AppResult.Error -> {
                    _state.update { it.copy(actionLoading = false, actionError = result.error) }
                }
            }
        }
    }

    override fun onBackClick() {
        onBack()
    }
}
