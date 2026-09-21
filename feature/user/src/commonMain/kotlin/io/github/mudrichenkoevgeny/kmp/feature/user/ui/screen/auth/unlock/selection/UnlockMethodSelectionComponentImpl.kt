package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByGoogleUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Default implementation of [UnlockMethodSelectionComponent]. */
class UnlockMethodSelectionComponentImpl(
    componentContext: ComponentContext,
    lockoutType: AccountLockoutType? = null,
    lockoutUntil: Long? = null,
    private val getUserIdentifiersUseCase: GetUserIdentifiersUseCase? = null,
    private val unlockByGoogleUseCase: UnlockByGoogleUseCase? = null,
    private val onNavigateToEmailInput: () -> Unit,
    private val onNavigateToPhoneInput: () -> Unit,
    private val onUnlockSuccess: () -> Unit,
    private val onBack: () -> Unit,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) : UnlockMethodSelectionComponent, ComponentContext by componentContext {

    private val _state = MutableValue(
        UnlockMethodSelectionScreenState(
            lockoutType = lockoutType,
            lockoutUntil = lockoutUntil
        )
    )
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
                            currentState.copy(
                                knownIdentifiers = pagedResult.items
                            )
                        }
                    }
                    is AppResult.Error -> {
                    }
                }
            }
        }
    }

    override fun onSelectEmailUnlock() {
        onNavigateToEmailInput()
    }

    override fun onSelectPhoneUnlock() {
        onNavigateToPhoneInput()
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
