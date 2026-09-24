package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.DeleteUserIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.EmailChangePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifierUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.toUserIdentifierIdOrNull
import kotlinx.coroutines.launch

/**
 * Default implementation of [IdentifierDetailComponent].
 *
 * Supports displaying identifier details and deleting/modifying credentials across both end-user and administrative contexts.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param identifier Initial [UserIdentifier] model if available.
 * @param identifierId Target [UserIdentifierId] identifier.
 * @param fetchIdentifier Suspend callback to fetch identifier details remotely when [identifier] is omitted.
 * @param deleteIdentifier Suspend callback to delete the target identifier.
 * @param changePassword Suspend callback to change password for email identifier.
 * @param deletePassword Suspend callback to delete password credential.
 * @param isCurrentIdentifier Indicates if this is the active session's identifier.
 * @param authStorage Storage used to resolve active identifier ID if [isCurrentIdentifier] is omitted.
 * @param onIdentifierDeleted Callback invoked when the identifier is deleted.
 * @param onBack Callback to pop this screen from navigation stack.
 */
class IdentifierDetailComponentImpl(
    componentContext: ComponentContext,
    private val identifier: UserIdentifier? = null,
    private val identifierId: UserIdentifierId? = identifier?.id,
    private val fetchIdentifier: (suspend (UserIdentifierId) -> AppResult<UserIdentifier>)? = null,
    private val deleteIdentifier: (suspend (UserIdentifierId) -> AppResult<Unit>)? = null,
    private val changePassword: (suspend (email: String, oldPass: String, newPass: String) -> AppResult<Unit>)? = null,
    private val deletePassword: (suspend (UserIdentifierId) -> AppResult<Unit>)? = null,
    private val isCurrentIdentifier: Boolean? = null,
    private val authStorage: AuthStorage? = null,
    private val onIdentifierDeleted: ((UserIdentifierId) -> Unit)? = null,
    private val onBack: () -> Unit
) : IdentifierDetailComponent, ComponentContext by componentContext {

    /**
     * Secondary constructor for standard self-identifier management workflows using UseCases.
     */
    constructor(
        componentContext: ComponentContext,
        identifier: UserIdentifier? = null,
        identifierId: UserIdentifierId? = identifier?.id,
        getUserIdentifierUseCase: GetUserIdentifierUseCase? = null,
        deleteUserIdentifierUseCase: DeleteUserIdentifierUseCase? = null,
        emailChangePasswordUseCase: EmailChangePasswordUseCase? = null,
        isCurrentIdentifier: Boolean? = null,
        authStorage: AuthStorage? = null,
        onIdentifierDeleted: ((UserIdentifierId) -> Unit)? = null,
        onBack: () -> Unit
    ) : this(
        componentContext = componentContext,
        identifier = identifier,
        identifierId = identifierId,
        fetchIdentifier = getUserIdentifierUseCase?.let { useCase -> { id -> useCase(id) } },
        deleteIdentifier = deleteUserIdentifierUseCase?.let { useCase -> { id -> useCase(id) } },
        changePassword = emailChangePasswordUseCase?.let { useCase -> { email, oldPass, newPass -> useCase(email, oldPass, newPass) } },
        deletePassword = null,
        isCurrentIdentifier = isCurrentIdentifier,
        authStorage = authStorage,
        onIdentifierDeleted = onIdentifierDeleted,
        onBack = onBack
    )

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<IdentifierDetailScreenState>(IdentifierDetailScreenState.Loading)
    override val state: Value<IdentifierDetailScreenState> = _state

    init {
        initializeIdentifier()
    }

    override fun onDeleteIdentifierRequested() {
        val current = _state.value as? IdentifierDetailScreenState.Content ?: return
        _state.value = current.copy(isDeleteConfirmationVisible = true, actionError = null)
    }

    override fun onDismissDeleteIdentifierDialog() {
        val current = _state.value as? IdentifierDetailScreenState.Content ?: return
        _state.value = current.copy(isDeleteConfirmationVisible = false)
    }

    override fun onDeleteIdentifierClick() {
        val current = _state.value as? IdentifierDetailScreenState.Content ?: return
        val targetId = current.identifier.id
        _state.value = current.copy(isDeleteConfirmationVisible = false, actionLoading = true, actionError = null)

        scope.launch {
            val result = deleteIdentifier?.invoke(targetId) ?: AppResult.Success(Unit)
            result
                .onSuccess {
                    onIdentifierDeleted?.invoke(targetId)
                    onBack()
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onChangePasswordClick() {
        val current = _state.value as? IdentifierDetailScreenState.Content ?: return
        _state.value = current.copy(isChangePasswordDialogVisible = true, actionError = null)
    }

    override fun onConfirmChangePasswordClick(oldPassword: String, newPassword: String) {
        val current = _state.value as? IdentifierDetailScreenState.Content ?: return
        val email = current.identifier.identifier
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            val result = changePassword?.invoke(email, oldPassword, newPassword) ?: AppResult.Success(Unit)
            result
                .onSuccess {
                    val latest = _state.value as? IdentifierDetailScreenState.Content ?: return@onSuccess
                    _state.value = latest.copy(actionLoading = false, isChangePasswordDialogVisible = false)
                }
                .onError { error ->
                    val latest = _state.value as? IdentifierDetailScreenState.Content ?: return@onError
                    _state.value = latest.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onDismissChangePasswordDialog() {
        val current = _state.value as? IdentifierDetailScreenState.Content ?: return
        _state.value = current.copy(isChangePasswordDialogVisible = false)
    }

    override fun onDeletePasswordClick() {
        val current = _state.value as? IdentifierDetailScreenState.Content ?: return
        val targetId = current.identifier.id
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            val result = deletePassword?.invoke(targetId) ?: AppResult.Success(Unit)
            result
                .onSuccess {
                    _state.value = current.copy(actionLoading = false)
                    initializeIdentifier()
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onRetry() {
        initializeIdentifier()
    }

    override fun onBackClick() {
        onBack()
    }

    private fun initializeIdentifier() {
        scope.launch {
            val targetId = identifierId ?: identifier?.id
            val activeIdentifierId = authStorage?.getIdentifierId()?.toUserIdentifierIdOrNull()
            val resolvedIsCurrent = isCurrentIdentifier ?: (targetId != null && targetId == activeIdentifierId)

            if (identifier != null) {
                _state.value = IdentifierDetailScreenState.Content(
                    identifier = identifier,
                    isCurrentIdentifier = resolvedIsCurrent,
                    canChangePassword = changePassword != null && identifier.userAuthProvider == UserAuthProvider.EMAIL,
                    canDeletePassword = deletePassword != null
                )
            } else if (targetId != null && fetchIdentifier != null) {
                _state.value = IdentifierDetailScreenState.Loading
                fetchIdentifier(targetId)
                    .onSuccess { loaded ->
                        _state.value = IdentifierDetailScreenState.Content(
                            identifier = loaded,
                            isCurrentIdentifier = resolvedIsCurrent,
                            canChangePassword = changePassword != null && loaded.userAuthProvider == UserAuthProvider.EMAIL,
                            canDeletePassword = deletePassword != null
                        )
                    }
                    .onError { error ->
                        _state.value = IdentifierDetailScreenState.Error(error)
                    }
            } else {
                _state.value = IdentifierDetailScreenState.Error(CommonError.Unknown())
            }
        }
    }
}
