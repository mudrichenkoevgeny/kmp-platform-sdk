package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
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
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.toUserIdentifierIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.list.SelfIdentifierListParams
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Default [SelfIdentifierListComponent] implementation: manages account identifiers list and inline dialog add flow.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Self-service identifiers list parameters.
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
    private val params: SelfIdentifierListParams = SelfIdentifierListParams(),
    initialDialogConfig: AddIdentifierDialogConfig? = null,
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

    override val addIdentifierDialogSlotNavigation = SlotNavigation<AddIdentifierDialogConfig>()

    override val addIdentifierDialogSlot: Value<ChildSlot<AddIdentifierDialogConfig, SelfIdentifierListComponent.AddIdentifierDialogChild>> =
        childSlot(
            source = addIdentifierDialogSlotNavigation,
            serializer = AddIdentifierDialogConfig.serializer(),
            handleBackButton = true,
            childFactory = ::createAddIdentifierDialogChild
        )

    private fun createAddIdentifierDialogChild(
        config: AddIdentifierDialogConfig,
        context: ComponentContext
    ): SelfIdentifierListComponent.AddIdentifierDialogChild = when (config) {
        is AddIdentifierDialogConfig.ProviderSelection -> SelfIdentifierListComponent.AddIdentifierDialogChild.ProviderSelection
        is AddIdentifierDialogConfig.EmailInput -> SelfIdentifierListComponent.AddIdentifierDialogChild.EmailInput(config.initialEmail)
        is AddIdentifierDialogConfig.EmailConfirm -> SelfIdentifierListComponent.AddIdentifierDialogChild.EmailConfirm(config.email)
        is AddIdentifierDialogConfig.PhoneInput -> SelfIdentifierListComponent.AddIdentifierDialogChild.PhoneInput(config.initialPhone)
        is AddIdentifierDialogConfig.PhoneConfirm -> SelfIdentifierListComponent.AddIdentifierDialogChild.PhoneConfirm(config.phone)
    }

    private val _state = MutableValue<SelfIdentifierListScreenState>(SelfIdentifierListScreenState.Loading)
    override val state: Value<SelfIdentifierListScreenState> = _state

    init {
        loadIdentifiers()
        initialDialogConfig?.let {
            addIdentifierDialogSlotNavigation.activate(it)
        }
    }

    override fun onRefresh() {
        loadIdentifiers()
    }

    override fun onIdentifierClick(identifierId: UserIdentifierId) {
        onIdentifierSelect?.invoke(identifierId)
    }

    override fun onAddIdentifierClick() {
        if (appType != AppType.CLIENT) {
            return
        }

        addIdentifierDialogSlotNavigation.activate(AddIdentifierDialogConfig.ProviderSelection)

        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        if (currentContent.availableAuthProviders == null) {
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
                addIdentifierDialogSlotNavigation.activate(AddIdentifierDialogConfig.EmailInput())
            }
            UserAuthProvider.PHONE -> {
                addIdentifierDialogSlotNavigation.activate(AddIdentifierDialogConfig.PhoneInput())
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
        val isValid = FieldValidator.isEmailValid(email)
        _state.value = currentContent.copy(addEmail = email, isAddEmailValid = isValid, actionError = null)

        if (isValid) {
            val remaining = identifierRepository?.getRemainingEmailConfirmationDelayInSeconds(email) ?: 0
            if (remaining > 0) {
                moveToEmailConfirmation(email, remaining)
            }
        }
    }

    override fun onAddIdentifierPasswordChanged(password: String) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        _state.value = currentContent.copy(addPassword = password, isAddPasswordValid = password.isNotBlank(), actionError = null)
    }

    override fun onAddIdentifierTogglePasswordVisibility() {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        _state.value = currentContent.copy(isAddPasswordVisible = !currentContent.isAddPasswordVisible)
    }

    override fun onAddIdentifierPhoneChanged(phone: String) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val isValid = FieldValidator.isPhoneNumberValid(phone)
        _state.value = currentContent.copy(addPhone = phone, isAddPhoneValid = isValid, actionError = null)

        if (isValid) {
            val remaining = identifierRepository?.getRemainingPhoneNumberConfirmationDelayInSeconds(phone) ?: 0
            if (remaining > 0) {
                moveToPhoneConfirmation(phone, remaining)
            }
        }
    }

    override fun onAddIdentifierCodeChanged(code: String) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        if (code.length <= FieldValidator.DEFAULT_OTP_LENGTH) {
            _state.value = currentContent.copy(addCode = code, actionError = null)
            if (currentContent.canSubmitAddPhone && addIdentifierDialogSlot.value.child?.configuration is AddIdentifierDialogConfig.PhoneConfirm) {
                onAddIdentifierSubmit()
            }
        }
    }

    override fun onAddIdentifierSendCode() {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return

        when (addIdentifierDialogSlot.value.child?.configuration) {
            is AddIdentifierDialogConfig.EmailInput, is AddIdentifierDialogConfig.EmailConfirm -> {
                val useCase = sendAddEmailIdentifierConfirmationUseCase ?: return
                updateDialogLoading(true)

                scope.launch {
                    useCase(currentContent.addEmail)
                        .onSuccess { otpConfirmation ->
                            val seconds = otpConfirmation.retryAfterSeconds.coerceAtLeast(0)
                            moveToEmailConfirmation(currentContent.addEmail, seconds)
                        }
                        .onError { error ->
                            if (error is UserError.TooManyConfirmationRequests) {
                                moveToEmailConfirmation(currentContent.addEmail, error.retryAfterSeconds)
                            } else {
                                updateDialogError(error)
                            }
                        }
                }
            }
            is AddIdentifierDialogConfig.PhoneInput, is AddIdentifierDialogConfig.PhoneConfirm -> {
                val useCase = sendAddPhoneIdentifierConfirmationUseCase ?: return
                updateDialogLoading(true)

                scope.launch {
                    useCase(currentContent.addPhone)
                        .onSuccess { otpConfirmation ->
                            val seconds = otpConfirmation.retryAfterSeconds.coerceAtLeast(0)
                            moveToPhoneConfirmation(currentContent.addPhone, seconds)
                        }
                        .onError { error ->
                            if (error is UserError.TooManyConfirmationRequests) {
                                moveToPhoneConfirmation(currentContent.addPhone, error.retryAfterSeconds)
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

        when (addIdentifierDialogSlot.value.child?.configuration) {
            is AddIdentifierDialogConfig.EmailConfirm -> {
                val useCase = addUserIdentifierEmailUseCase ?: return
                if (!currentContent.canSubmitAddEmail) {
                    return
                }
                updateDialogLoading(true)

                scope.launch {
                    useCase(currentContent.addEmail, currentContent.addPassword, currentContent.addCode)
                        .onSuccess {
                            onAddIdentifierDialogDismiss()
                            loadIdentifiers()
                        }
                        .onError { error ->
                            updateDialogError(error)
                        }
                }
            }
            is AddIdentifierDialogConfig.PhoneConfirm -> {
                val useCase = addUserIdentifierPhoneUseCase ?: return
                if (!currentContent.canSubmitAddPhone) {
                    return
                }
                updateDialogLoading(true)

                scope.launch {
                    useCase(currentContent.addPhone, currentContent.addCode)
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
        when (addIdentifierDialogSlot.value.child?.configuration) {
            is AddIdentifierDialogConfig.EmailConfirm -> {
                timerJob?.cancel()
                val email = (addIdentifierDialogSlot.value.child?.configuration as? AddIdentifierDialogConfig.EmailConfirm)?.email ?: ""
                addIdentifierDialogSlotNavigation.activate(AddIdentifierDialogConfig.EmailInput(email))
            }
            is AddIdentifierDialogConfig.PhoneConfirm -> {
                timerJob?.cancel()
                val phone = (addIdentifierDialogSlot.value.child?.configuration as? AddIdentifierDialogConfig.PhoneConfirm)?.phone ?: ""
                addIdentifierDialogSlotNavigation.activate(AddIdentifierDialogConfig.PhoneInput(phone))
            }
            is AddIdentifierDialogConfig.EmailInput, is AddIdentifierDialogConfig.PhoneInput -> {
                timerJob?.cancel()
                addIdentifierDialogSlotNavigation.activate(AddIdentifierDialogConfig.ProviderSelection)
            }
            else -> onAddIdentifierDialogDismiss()
        }
    }

    override fun onAddIdentifierDialogDismiss() {
        timerJob?.cancel()
        addIdentifierDialogSlotNavigation.dismiss()
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        _state.value = currentContent.copy(
            addEmail = "",
            isAddEmailValid = false,
            addPhone = "",
            isAddPhoneValid = false,
            addCode = "",
            addPassword = "",
            isAddPasswordValid = false,
            addResendTimerSeconds = 0,
            actionLoading = false,
            actionError = null
        )
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
        if (!paging.canLoadMore) {
            return
        }

        _state.value = currentContent.copy(paging = paging.toNextPageLoading())
        fetchPage(pageNumber = paging.nextPageNumber)
    }

    override fun onBackClick() {
        if ((backHandler as? BackDispatcher)?.back() != true) {
            onBack()
        }
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
        addIdentifierDialogSlotNavigation.activate(AddIdentifierDialogConfig.EmailConfirm(email, seconds))
        startTimer(seconds)
    }

    private fun moveToPhoneConfirmation(phone: String, seconds: Int) {
        addIdentifierDialogSlotNavigation.activate(AddIdentifierDialogConfig.PhoneConfirm(phone, seconds))
        startTimer(seconds)
    }

    private fun startTimer(seconds: Int) {
        timerJob?.cancel()
        if (seconds <= 0) {
            return
        }

        timerJob = scope.launch {
            resendCountdown(
                totalSeconds = seconds,
                onTick = ::updateTimerState
            )
        }
    }

    private fun updateTimerState(seconds: Int) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        _state.value = currentContent.copy(addResendTimerSeconds = seconds)
    }

    private fun updateDialogError(error: AppError) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        _state.value = currentContent.copy(actionLoading = false, actionError = error)
    }

    private fun updateDialogLoading(loading: Boolean) {
        val currentContent = _state.value as? SelfIdentifierListScreenState.Content ?: return
        _state.value = currentContent.copy(actionLoading = loading, actionError = null)
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

        fetchPage(pageNumber = params.pageNumber)
    }

    private fun fetchPage(pageNumber: Int) {
        scope.launch {
            val activeIdentifierId = authStorage?.getIdentifierId()?.toUserIdentifierIdOrNull()
            getUserIdentifiersUseCase(
                pageNumber = pageNumber,
                pageSize = params.pageSize
            )
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? SelfIdentifierListScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserIdentifierSummary>()).appendResult(pagedResult)
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
