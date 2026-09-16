package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockEmailConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockPhoneConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByGoogleUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Default implementation of [UnlockMethodSelectionComponent]. */
class UnlockMethodSelectionComponentImpl(
    componentContext: ComponentContext,
    private val getUserIdentifiersUseCase: GetUserIdentifiersUseCase? = null,
    private val sendUnlockEmailConfirmationUseCase: SendUnlockEmailConfirmationUseCase,
    private val sendUnlockPhoneConfirmationUseCase: SendUnlockPhoneConfirmationUseCase,
    private val unlockByGoogleUseCase: UnlockByGoogleUseCase? = null,
    private val onNavigateToEmailOtp: (email: String) -> Unit,
    private val onNavigateToPhoneOtp: (phone: String) -> Unit,
    private val onUnlockSuccess: () -> Unit,
    private val onBack: () -> Unit,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) : UnlockMethodSelectionComponent, ComponentContext by componentContext {

    private val _state = MutableValue(UnlockMethodSelectionScreenState())
    override val state: Value<UnlockMethodSelectionScreenState> = _state

    init {
        loadIdentifiers()
    }

    private fun loadIdentifiers() {
        getUserIdentifiersUseCase?.let { useCase ->
            coroutineScope.launch {
                when (val result = useCase()) {
                    is AppResult.Success -> {
                        val pagedResult = result.data
                        _state.update { currentState ->
                            val defaultEmail = pagedResult.items
                                .firstOrNull { it.userAuthProvider == UserAuthProvider.EMAIL }?.identifier.orEmpty()
                            val defaultPhone = pagedResult.items
                                .firstOrNull { it.userAuthProvider == UserAuthProvider.PHONE }?.identifier.orEmpty()
                            currentState.copy(
                                knownIdentifiers = pagedResult.items,
                                emailInput = currentState.emailInput.ifBlank { defaultEmail },
                                phoneInput = currentState.phoneInput.ifBlank { defaultPhone }
                            )
                        }
                    }
                    is AppResult.Error -> {
                        // Keep blank defaults if identifiers could not be loaded
                    }
                }
            }
        }
    }

    override fun onEmailInputChanged(email: String) {
        _state.update { it.copy(emailInput = email, actionError = null) }
    }

    override fun onPhoneInputChanged(phone: String) {
        _state.update { it.copy(phoneInput = phone, actionError = null) }
    }

    override fun onSelectEmailUnlock() {
        val email = _state.value.emailInput.trim()
        if (email.isBlank()) return
        _state.update { it.copy(actionLoading = true, actionError = null) }
        coroutineScope.launch {
            when (val result = sendUnlockEmailConfirmationUseCase.execute(email)) {
                is AppResult.Success -> {
                    _state.update { currentState -> currentState.copy(actionLoading = false) }
                    onNavigateToEmailOtp(email)
                }
                is AppResult.Error -> {
                    _state.update { currentState -> currentState.copy(actionLoading = false, actionError = result.error) }
                }
            }
        }
    }

    override fun onSelectPhoneUnlock() {
        val phone = _state.value.phoneInput.trim()
        if (phone.isBlank()) return
        _state.update { it.copy(actionLoading = true, actionError = null) }
        coroutineScope.launch {
            when (val result = sendUnlockPhoneConfirmationUseCase.execute(phone)) {
                is AppResult.Success -> {
                    _state.update { currentState -> currentState.copy(actionLoading = false) }
                    onNavigateToPhoneOtp(phone)
                }
                is AppResult.Error -> {
                    _state.update { currentState -> currentState.copy(actionLoading = false, actionError = result.error) }
                }
            }
        }
    }

    override fun onSelectGoogleUnlock() {
        val unlockByGoogle = unlockByGoogleUseCase
        if (unlockByGoogle == null) {
            _state.update {
                it.copy(
                    actionError = CommonError.ContractViolation(
                        IllegalStateException("Unlock by google is not supported.")
                    )
                )
            }
            return
        }

        _state.update { it.copy(actionLoading = true, actionError = null) }
        coroutineScope.launch {
            when (val result = unlockByGoogle.execute()) {
                is AppResult.Success -> {
                    _state.update { currentState -> currentState.copy(actionLoading = false) }
                    onUnlockSuccess()
                }
                is AppResult.Error -> {
                    _state.update { currentState -> currentState.copy(actionLoading = false, actionError = result.error) }
                }
            }
        }
    }

    override fun onSelectAppleUnlock() {
        _state.update {
            it.copy(
                actionError = UserError.ExternalAuthFailed(
                    Exception("Apple auth not supported")
                )
            )
        }
    }

    override fun onBackClick() {
        onBack()
    }
}
