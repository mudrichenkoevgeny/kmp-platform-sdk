package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.ListingConstants
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.appendResult
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toInitialLoading
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toNextPageLoading
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.core.common.time.resendCountdown
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierGoogleUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddEmailIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddPhoneIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.toUserIdentifierIdOrNull
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Default [SelfIdentifierListComponent] implementation: manages account identifiers list and inline dialog add flow.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param appType Operational context (Client or Management).
 * @param getUserIdentifiersUseCase Lists linked identity records.
 * @param getAvailableUserAuthProvidersUseCase Fetches allowed auth providers for adding identifiers.
 * @param sendAddEmailIdentifierConfirmationUseCase Triggers email confirmation OTP.
 * @param addUserIdentifierEmailUseCase Links email identifier.
 * @param sendAddPhoneIdentifierConfirmationUseCase Triggers phone confirmation OTP.
 * @param addUserIdentifierPhoneUseCase Links phone identifier.
 * @param addUserIdentifierGoogleUseCase Links Google identifier.
 * @param identifierRepository Checks remaining confirmation delays.
 * @param authStorage Storage used to resolve active identifier ID.
 * @param onIdentifierSelect Callback invoked when an identifier is tapped.
 * @param onBack Pops this screen from the navigation stack.
 */
class SelfIdentifierListComponentImpl(
    componentContext: ComponentContext,
    private val appType: AppType = AppType.CLIENT,
    private val getUserIdentifiersUseCase: GetUserIdentifiersUseCase,
    private val getAvailableUserAuthProvidersUseCase: GetAvailableUserAuthProvidersUseCase? = null,
    private val sendAddEmailIdentifierConfirmationUseCase: SendAddEmailIdentifierConfirmationUseCase? = null,
    private val addUserIdentifierEmailUseCase: AddUserIdentifierEmailUseCase? = null,
    private val sendAddPhoneIdentifierConfirmationUseCase: SendAddPhoneIdentifierConfirmationUseCase? = null,
    private val addUserIdentifierPhoneUseCase: AddUserIdentifierPhoneUseCase? = null,
    private val addUserIdentifierGoogleUseCase: AddUserIdentifierGoogleUseCase? = null,
    private val identifierRepository: IdentifierRepository? = null,
    private val authStorage: AuthStorage? = null,
    private val onIdentifierSelect: ((UserIdentifierId) -> Unit)? = null,
    private val onBack: () -> Unit
) : SelfIdentifierListComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private var timerJob: Job? = null

    private val _state = MutableValue<SelfIdentifierListScreenState>(SelfIdentifierListScreenState.Loading)
    override val state: Value<SelfIdentifierListScreenState> = _state

    init {
        loadIdentifiers()
    }

    override fun onRefresh() {
        loadIdentifiers()
    }

    override fun onIdentifierClick(identifierId: UserIdentifierId) {
        onIdentifierSelect?.invoke(identifierId)
    }

    override fun onAddIdentifierClick() {
        if (appType != AppType.CLIENT) return

        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        if (currentContent.availableAuthProviders != null) {
            _state.value = currentContent.copy(addIdentifierDialogState = AddIdentifierDialogState.ProviderSelection)
        } else {
            val useCase = getAvailableUserAuthProvidersUseCase
            if (useCase == null) {
                _state.value = currentContent.copy(
                    actionError = CommonError.ContractViolation(
                        throwable = IllegalStateException("GetAvailableUserAuthProvidersUseCase is null")
                    )
                )
                return
            }

            _state.value = currentContent.copy(actionLoading = true, actionError = null)
            scope.launch {
                useCase()
                    .onSuccess { providers ->
                        val content = _state.value as? SelfIdentifierListScreenState.Content
                        if (content != null) {
                            _state.value = content.copy(
                                availableAuthProviders = providers,
                                addIdentifierDialogState = AddIdentifierDialogState.ProviderSelection,
                                actionLoading = false
                            )
                        }
                    }
                    .onError { error ->
                        val content = _state.value as? SelfIdentifierListScreenState.Content
                        if (content != null) {
                            _state.value = content.copy(
                                actionLoading = false,
                                actionError = error
                            )
                        }
                    }
            }
        }
    }

    override fun onAddIdentifierSelectProvider(authProvider: UserAuthProvider) {
        when (authProvider) {
            UserAuthProvider.EMAIL -> {
                updateDialogState(AddIdentifierDialogState.EmailFlow())
            }
            UserAuthProvider.PHONE -> {
                updateDialogState(AddIdentifierDialogState.PhoneFlow())
            }
            UserAuthProvider.GOOGLE -> {
                addGoogleIdentifier()
            }
            UserAuthProvider.APPLE -> {
                updateDialogError(
                    UserError.ExternalAuthFailed(
                        Exception("Apple auth is not supported yet")
                    )
                )
            }
        }
    }

    override fun onAddIdentifierEmailChanged(email: String) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val flow = currentContent.addIdentifierDialogState as? AddIdentifierDialogState.EmailFlow ?: return
        val isValid = FieldValidator.isEmailValid(email)

        updateDialogState(flow.copy(email = email, isEmailValid = isValid, actionError = null))

        if (isValid) {
            val remaining = identifierRepository?.getRemainingEmailConfirmationDelayInSeconds(email) ?: 0
            if (remaining > 0) {
                moveToEmailConfirmation(email, remaining)
            }
        }
    }

    override fun onAddIdentifierPasswordChanged(password: String) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val flow = currentContent.addIdentifierDialogState as? AddIdentifierDialogState.EmailFlow ?: return

        updateDialogState(flow.copy(password = password, isPasswordValid = password.isNotBlank(), actionError = null))
    }

    override fun onAddIdentifierTogglePasswordVisibility() {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val flow = currentContent.addIdentifierDialogState as? AddIdentifierDialogState.EmailFlow ?: return

        updateDialogState(flow.copy(isPasswordVisible = !flow.isPasswordVisible))
    }

    override fun onAddIdentifierPhoneChanged(phone: String) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val flow = currentContent.addIdentifierDialogState as? AddIdentifierDialogState.PhoneFlow ?: return
        val isValid = FieldValidator.isPhoneNumberValid(phone)

        updateDialogState(flow.copy(phoneNumber = phone, isPhoneNumberValid = isValid, actionError = null))

        if (isValid) {
            val remaining = identifierRepository?.getRemainingPhoneNumberConfirmationDelayInSeconds(phone) ?: 0
            if (remaining > 0) {
                moveToPhoneConfirmation(phone, remaining)
            }
        }
    }

    override fun onAddIdentifierCodeChanged(code: String) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val flow = currentContent.addIdentifierDialogState ?: return

        when (flow) {
            is AddIdentifierDialogState.EmailFlow -> {
                if (code.length <= FieldValidator.DEFAULT_OTP_LENGTH) {
                    updateDialogState(flow.copy(code = code, actionError = null))
                }
            }
            is AddIdentifierDialogState.PhoneFlow -> {
                if (code.length <= FieldValidator.DEFAULT_OTP_LENGTH) {
                    val updated = flow.copy(code = code, actionError = null)
                    updateDialogState(updated)
                    if (updated.canSubmit) {
                        onAddIdentifierSubmit()
                    }
                }
            }
            else -> {}
        }
    }

    override fun onAddIdentifierSendCode() {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val flow = currentContent.addIdentifierDialogState ?: return

        when (flow) {
            is AddIdentifierDialogState.EmailFlow -> {
                val useCase = sendAddEmailIdentifierConfirmationUseCase ?: return
                updateDialogState(flow.copy(actionLoading = true, actionError = null))

                scope.launch {
                    useCase(flow.email)
                        .onSuccess { otpConfirmation ->
                            val seconds = otpConfirmation.retryAfterSeconds.coerceAtLeast(0)
                            moveToEmailConfirmation(flow.email, seconds)
                        }
                        .onError { error ->
                            if (error is UserError.TooManyConfirmationRequests) {
                                moveToEmailConfirmation(flow.email, error.retryAfterSeconds)
                            } else {
                                updateDialogError(error)
                            }
                        }
                }
            }
            is AddIdentifierDialogState.PhoneFlow -> {
                val useCase = sendAddPhoneIdentifierConfirmationUseCase ?: return
                updateDialogState(flow.copy(actionLoading = true, actionError = null))

                scope.launch {
                    useCase(flow.phoneNumber)
                        .onSuccess { otpConfirmation ->
                            val seconds = otpConfirmation.retryAfterSeconds.coerceAtLeast(0)
                            moveToPhoneConfirmation(flow.phoneNumber, seconds)
                        }
                        .onError { error ->
                            if (error is UserError.TooManyConfirmationRequests) {
                                moveToPhoneConfirmation(flow.phoneNumber, error.retryAfterSeconds)
                            } else {
                                updateDialogError(error)
                            }
                        }
                }
            }
            else -> {}
        }
    }

    override fun onAddIdentifierSubmit() {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val flow = currentContent.addIdentifierDialogState ?: return

        when (flow) {
            is AddIdentifierDialogState.EmailFlow -> {
                val useCase = addUserIdentifierEmailUseCase ?: return
                if (!flow.canSubmit) return
                updateDialogState(flow.copy(actionLoading = true, actionError = null))

                scope.launch {
                    useCase(flow.email, flow.password, flow.code)
                        .onSuccess {
                            onAddIdentifierDialogDismiss()
                            loadIdentifiers()
                        }
                        .onError { error ->
                            updateDialogError(error)
                        }
                }
            }
            is AddIdentifierDialogState.PhoneFlow -> {
                val useCase = addUserIdentifierPhoneUseCase ?: return
                if (!flow.canSubmit) return
                updateDialogState(flow.copy(actionLoading = true, actionError = null))

                scope.launch {
                    useCase(flow.phoneNumber, flow.code)
                        .onSuccess {
                            onAddIdentifierDialogDismiss()
                            loadIdentifiers()
                        }
                        .onError { error ->
                            updateDialogError(error)
                        }
                }
            }
            else -> {}
        }
    }

    override fun onAddIdentifierDialogBack() {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        when (currentContent.addIdentifierDialogState) {
            is AddIdentifierDialogState.EmailFlow, is AddIdentifierDialogState.PhoneFlow -> {
                timerJob?.cancel()
                updateDialogState(AddIdentifierDialogState.ProviderSelection)
            }
            else -> onAddIdentifierDialogDismiss()
        }
    }

    override fun onAddIdentifierDialogDismiss() {
        timerJob?.cancel()
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        _state.value = currentContent.copy(addIdentifierDialogState = null)
    }

    override fun onIdentifierDeleted(identifierId: UserIdentifierId) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val updatedItems = currentContent.paging.items.filterNot { it.id == identifierId }
        _state.value = currentContent.copy(
            paging = currentContent.paging.copy(items = updatedItems)
        )
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val paging = currentContent.paging
        if (!paging.canLoadMore) return

        _state.value = currentContent.copy(paging = paging.toNextPageLoading())
        fetchPage(pageNumber = paging.nextPageNumber)
    }

    override fun onBackClick() {
        onBack()
    }

    private fun addGoogleIdentifier() {
        val googleUseCase = addUserIdentifierGoogleUseCase ?: return
        updateDialogLoading(true)

        scope.launch {
            googleUseCase.execute()
                .onSuccess {
                    onAddIdentifierDialogDismiss()
                    loadIdentifiers()
                }
                .onError { error ->
                    updateDialogError(error)
                }
        }
    }

    private fun moveToEmailConfirmation(email: String, seconds: Int) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val currentFlow = currentContent.addIdentifierDialogState as? AddIdentifierDialogState.EmailFlow
        updateDialogState(
            AddIdentifierDialogState.EmailFlow(
                email = email,
                isEmailValid = true,
                code = currentFlow?.code ?: "",
                password = currentFlow?.password ?: "",
                isPasswordValid = currentFlow?.isPasswordValid ?: false,
                isConfirmationSent = true,
                resendTimerSeconds = seconds
            )
        )
        startTimer(seconds)
    }

    private fun moveToPhoneConfirmation(phone: String, seconds: Int) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val currentFlow = currentContent.addIdentifierDialogState as? AddIdentifierDialogState.PhoneFlow
        updateDialogState(
            AddIdentifierDialogState.PhoneFlow(
                phoneNumber = phone,
                isPhoneNumberValid = true,
                code = currentFlow?.code ?: "",
                isConfirmationSent = true,
                resendTimerSeconds = seconds
            )
        )
        startTimer(seconds)
    }

    private fun startTimer(seconds: Int) {
        timerJob?.cancel()
        if (seconds <= 0) return

        timerJob = scope.launch {
            resendCountdown(
                totalSeconds = seconds,
                onTick = ::updateTimerState
            )
        }
    }

    private fun updateTimerState(seconds: Int) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        when (val flow = currentContent.addIdentifierDialogState) {
            is AddIdentifierDialogState.EmailFlow -> updateDialogState(flow.copy(resendTimerSeconds = seconds))
            is AddIdentifierDialogState.PhoneFlow -> updateDialogState(flow.copy(resendTimerSeconds = seconds))
            else -> {}
        }
    }

    private fun updateDialogState(dialogState: AddIdentifierDialogState) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        _state.value = currentContent.copy(addIdentifierDialogState = dialogState)
    }

    private fun updateDialogError(error: AppError) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val updatedDialog = when (val flow = currentContent.addIdentifierDialogState) {
            is AddIdentifierDialogState.EmailFlow -> flow.copy(actionLoading = false, actionError = error)
            is AddIdentifierDialogState.PhoneFlow -> flow.copy(actionLoading = false, actionError = error)
            else -> flow
        }
        _state.value = currentContent.copy(addIdentifierDialogState = updatedDialog, actionLoading = false, actionError = error)
    }

    private fun updateDialogLoading(loading: Boolean) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val updatedDialog = when (val flow = currentContent.addIdentifierDialogState) {
            is AddIdentifierDialogState.EmailFlow -> flow.copy(actionLoading = loading, actionError = null)
            is AddIdentifierDialogState.PhoneFlow -> flow.copy(actionLoading = loading, actionError = null)
            else -> flow
        }
        _state.value = currentContent.copy(addIdentifierDialogState = updatedDialog, actionLoading = loading, actionError = null)
    }

    private fun loadIdentifiers() {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content

        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionError = null
            )
        } else {
            _state.value = SelfIdentifierListScreenState.Loading
        }

        fetchPage(pageNumber = ListingConstants.INITIAL_PAGE_NUMBER)
    }

    private fun fetchPage(pageNumber: Int) {
        scope.launch {
            val activeIdentifierId = authStorage?.getIdentifierId()?.toUserIdentifierIdOrNull()
            getUserIdentifiersUseCase(
                pageNumber = pageNumber,
                pageSize = ListingConstants.DEFAULT_PAGE_SIZE
            )
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? SelfIdentifierListScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserIdentifier>()).appendResult(pagedResult)
                    _state.value = SelfIdentifierListScreenState.Content(
                        paging = newPaging,
                        currentIdentifierId = activeIdentifierId,
                        isAddIdentifierSupported = (appType == AppType.CLIENT)
                    )
                }
                .onError { error ->
                    val currentContent = _state.value as? SelfIdentifierListScreenState.Content
                    if (currentContent != null) {
                        _state.value = currentContent.copy(
                            paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                        )
                    } else {
                        _state.value = SelfIdentifierListScreenState.Error(error)
                    }
                }
        }
    }
}
