package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.ListingConstants
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.appendResult
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toInitialLoading
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toNextPageLoading
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.DeleteUserIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.EmailChangePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddEmailIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddPhoneIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import kotlinx.coroutines.launch

/**
 * Default [IdentifierListComponent] implementation: manages account identifiers (email, phone).
 *
 * @param componentContext Decompose [ComponentContext].
 * @param getUserIdentifiersUseCase Lists linked identity records.
 * @param deleteUserIdentifierUseCase Removes a linked identity.
 * @param sendAddEmailIdentifierConfirmationUseCase Initiates email linking flow.
 * @param addUserIdentifierEmailUseCase Finalizes email linking with code.
 * @param sendAddPhoneIdentifierConfirmationUseCase Initiates phone linking flow.
 * @param addUserIdentifierPhoneUseCase Finalizes phone linking with code.
 * @param emailChangePasswordUseCase Updates account password.
 * @param onBack Pops this screen from the navigation stack.
 */
class IdentifierListComponentImpl(
    componentContext: ComponentContext,
    private val getUserIdentifiersUseCase: GetUserIdentifiersUseCase,
    private val deleteUserIdentifierUseCase: DeleteUserIdentifierUseCase,
    private val sendAddEmailIdentifierConfirmationUseCase: SendAddEmailIdentifierConfirmationUseCase,
    private val addUserIdentifierEmailUseCase: AddUserIdentifierEmailUseCase,
    private val sendAddPhoneIdentifierConfirmationUseCase: SendAddPhoneIdentifierConfirmationUseCase,
    private val addUserIdentifierPhoneUseCase: AddUserIdentifierPhoneUseCase,
    private val emailChangePasswordUseCase: EmailChangePasswordUseCase,
    private val onBack: () -> Unit
) : IdentifierListComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<IdentifierListScreenState>(IdentifierListScreenState.Loading)
    override val state: Value<IdentifierListScreenState> = _state

    init {
        loadIdentifiers()
    }

    override fun onRefresh() {
        loadIdentifiers()
    }

    override fun onDeleteIdentifierClick(identifierId: UserIdentifierId) {
        val current = _state.value as? IdentifierListScreenState.Content ?: return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            deleteUserIdentifierUseCase(identifierId)
                .onSuccess {
                    loadIdentifiers()
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onAddEmailClick(email: String) {
        val current = _state.value as? IdentifierListScreenState.Content ?: return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            sendAddEmailIdentifierConfirmationUseCase(email)
                .onSuccess {
                    _state.value = current.copy(
                        actionLoading = false,
                        addEmailState = IdentifierListScreenState.AddIdentifierState.EnteringCode(value = email)
                    )
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onEmailCodeChanged(code: String) {
        val current = _state.value as? IdentifierListScreenState.Content ?: return
        val addState = current.addEmailState as? IdentifierListScreenState.AddIdentifierState.EnteringCode ?: return
        _state.value = current.copy(addEmailState = addState.copy(code = code))
    }

    override fun onConfirmAddEmailClick(password: String) {
        val current = _state.value as? IdentifierListScreenState.Content ?: return
        val addState = current.addEmailState as? IdentifierListScreenState.AddIdentifierState.EnteringCode ?: return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            addUserIdentifierEmailUseCase(email = addState.value, password = password, confirmationCode = addState.code)
                .onSuccess {
                    loadIdentifiers()
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onAddPhoneClick(phoneNumber: String) {
        val current = _state.value as? IdentifierListScreenState.Content ?: return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            sendAddPhoneIdentifierConfirmationUseCase(phoneNumber)
                .onSuccess {
                    _state.value = current.copy(
                        actionLoading = false,
                        addPhoneState = IdentifierListScreenState.AddIdentifierState.EnteringCode(value = phoneNumber)
                    )
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onPhoneCodeChanged(code: String) {
        val current = _state.value as? IdentifierListScreenState.Content ?: return
        val addState = current.addPhoneState as? IdentifierListScreenState.AddIdentifierState.EnteringCode ?: return
        _state.value = current.copy(addPhoneState = addState.copy(code = code))
    }

    override fun onConfirmAddPhoneClick() {
        val current = _state.value as? IdentifierListScreenState.Content ?: return
        val addState = current.addPhoneState as? IdentifierListScreenState.AddIdentifierState.EnteringCode ?: return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            addUserIdentifierPhoneUseCase(phoneNumber = addState.value, confirmationCode = addState.code)
                .onSuccess {
                    loadIdentifiers()
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onCancelAddClick() {
        val current = _state.value as? IdentifierListScreenState.Content ?: return
        _state.value = current.copy(
            addEmailState = IdentifierListScreenState.AddIdentifierState.Idle,
            addPhoneState = IdentifierListScreenState.AddIdentifierState.Idle,
            changePasswordEmail = null,
            actionError = null
        )
    }

    override fun onChangePasswordClick(email: String) {
        val current = _state.value as? IdentifierListScreenState.Content ?: return
        _state.value = current.copy(changePasswordEmail = email, actionError = null)
    }

    override fun onConfirmChangePasswordClick(oldPassword: String, newPassword: String) {
        val current = _state.value as? IdentifierListScreenState.Content ?: return
        val email = current.changePasswordEmail ?: return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            emailChangePasswordUseCase(email = email, oldPassword = oldPassword, newPassword = newPassword)
                .onSuccess {
                    _state.value = current.copy(actionLoading = false, changePasswordEmail = null)
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onDismissChangePasswordDialog() {
        val current = _state.value as? IdentifierListScreenState.Content ?: return
        _state.value = current.copy(changePasswordEmail = null)
    }

    override fun onBackClick() {
        onBack()
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? IdentifierListScreenState.Content ?: return
        val paging = currentContent.paging
        if (!paging.canLoadMore) return

        _state.value = currentContent.copy(paging = paging.toNextPageLoading())
        fetchPage(pageNumber = paging.nextPageNumber)
    }

    private fun loadIdentifiers() {
        val currentContent = _state.value as? IdentifierListScreenState.Content

        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionError = null,
                addEmailState = IdentifierListScreenState.AddIdentifierState.Idle,
                addPhoneState = IdentifierListScreenState.AddIdentifierState.Idle
            )
        } else {
            _state.value = IdentifierListScreenState.Loading
        }

        fetchPage(pageNumber = ListingConstants.INITIAL_PAGE_NUMBER)
    }

    private fun fetchPage(pageNumber: Int) {
        scope.launch {
            getUserIdentifiersUseCase(pageNumber = pageNumber, pageSize = ListingConstants.DEFAULT_PAGE_SIZE)
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? IdentifierListScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserIdentifier>()).appendResult(pagedResult)
                    _state.value = IdentifierListScreenState.Content(paging = newPaging)
                }
                .onError { error ->
                    val currentContent = _state.value as? IdentifierListScreenState.Content
                    if (currentContent != null) {
                        _state.value = currentContent.copy(
                            paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                        )
                    } else {
                        _state.value = IdentifierListScreenState.Error(error)
                    }
                }
        }
    }
}
