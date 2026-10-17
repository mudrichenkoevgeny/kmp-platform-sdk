package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.DeleteUserIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.EmailChangePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifierUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.toUserIdentifierIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.detail.IdentifierDetailParams
import kotlinx.coroutines.launch

/**
 * Default implementation of [IdentifierDetailComponent].
 *
 * Supports displaying identifier details and deleting/modifying credentials across both end-user and administrative contexts.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Target identifier parameters.
 * @param userRepository Source of the current user profile state.
 * @param fetchIdentifier Suspend callback to fetch identifier details remotely.
 * @param deleteIdentifier Suspend callback to delete the target identifier.
 * @param changePassword Suspend callback to change password for email identifier.
 * @param deletePassword Suspend callback to delete password credential.
 * @param isCurrentIdentifier Indicates if this is the active session's identifier.
 * @param authStorage Storage used to resolve active identifier ID if [isCurrentIdentifier] is omitted.
 * @param onIdentifierDeleted Callback invoked when the identifier is deleted.
 * @param onNavigateToUserDetail Callback to navigate to user detail screen.
 * @param onNavigateToProfile Callback to navigate to main profile screen.
 * @param onBack Callback to pop this screen from navigation stack.
 */
class IdentifierDetailComponentImpl(
    componentContext: ComponentContext,
    private val params: IdentifierDetailParams,
    private val userRepository: UserRepository? = null,
    private val fetchIdentifier: (suspend (UserIdentifierId) -> AppResult<UserIdentifierPrivate>)? = null,
    private val deleteIdentifier: (suspend (UserIdentifierId) -> AppResult<Unit>)? = null,
    private val changePassword: (suspend (userIdentifierId: UserIdentifierId, oldPass: String, newPass: String) -> AppResult<Unit>)? = null,
    private val deletePassword: (suspend (UserIdentifierId) -> AppResult<Unit>)? = null,
    private val isCurrentIdentifier: Boolean? = null,
    private val authStorage: AuthStorage? = null,
    private val onIdentifierDeleted: ((UserIdentifierId) -> Unit)? = null,
    private val onNavigateToUserDetail: ((UserId) -> Unit)? = null,
    private val onNavigateToProfile: () -> Unit,
    private val onBack: () -> Unit
) : IdentifierDetailComponent, ComponentContext by componentContext {

    /**
     * Secondary constructor for standard self-identifier management workflows using UseCases.
     */
    constructor(
        componentContext: ComponentContext,
        params: IdentifierDetailParams,
        userRepository: UserRepository? = null,
        getUserIdentifierUseCase: GetUserIdentifierUseCase? = null,
        deleteUserIdentifierUseCase: DeleteUserIdentifierUseCase? = null,
        emailChangePasswordUseCase: EmailChangePasswordUseCase? = null,
        isCurrentIdentifier: Boolean? = null,
        authStorage: AuthStorage? = null,
        onIdentifierDeleted: ((UserIdentifierId) -> Unit)? = null,
        onNavigateToUserDetail: ((UserId) -> Unit)? = null,
        onNavigateToProfile: () -> Unit,
        onBack: () -> Unit
    ) : this(
        componentContext = componentContext,
        params = params,
        userRepository = userRepository,
        fetchIdentifier = getUserIdentifierUseCase?.let { useCase -> { id -> useCase(id) } },
        deleteIdentifier = deleteUserIdentifierUseCase?.let { useCase -> { id -> useCase(id) } },
        changePassword = emailChangePasswordUseCase?.let { useCase -> { email, oldPass, newPass -> useCase(email, oldPass, newPass) } },
        deletePassword = null,
        isCurrentIdentifier = isCurrentIdentifier,
        authStorage = authStorage,
        onIdentifierDeleted = onIdentifierDeleted,
        onNavigateToUserDetail = onNavigateToUserDetail,
        onNavigateToProfile = onNavigateToProfile,
        onBack = onBack
    )

    private val identifierId: UserIdentifierId? get() = params.typedIdentifierId

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<IdentifierDetailScreenState>(IdentifierDetailScreenState.Loading)
    override val state: Value<IdentifierDetailScreenState> = _state

    private var currentUserId: UserId? = null

    init {
        scope.launch {
            userRepository?.currentUser?.collect { user ->
                currentUserId = user?.id
            }
        }
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
        val targetId = identifierId ?: current.identifier.id
        if (deleteIdentifier == null) {
            return
        }

        _state.value = current.copy(actionLoading = true, actionError = null)
        scope.launch {
            deleteIdentifier.invoke(targetId)
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
        val targetId = identifierId ?: current.identifier.id
        if (changePassword == null) {
            return
        }

        _state.value = current.copy(actionLoading = true, actionError = null)
        scope.launch {
            changePassword.invoke(targetId, oldPassword, newPassword)
                .onSuccess {
                    _state.value = current.copy(
                        actionLoading = false,
                        isChangePasswordDialogVisible = false
                    )
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onDismissChangePasswordDialog() {
        val current = _state.value as? IdentifierDetailScreenState.Content ?: return
        _state.value = current.copy(isChangePasswordDialogVisible = false)
    }

    override fun onDeletePasswordClick() {
        val current = _state.value as? IdentifierDetailScreenState.Content ?: return
        val targetId = identifierId ?: current.identifier.id
        if (deletePassword == null) {
            return
        }

        _state.value = current.copy(actionLoading = true, actionError = null)
        scope.launch {
            deletePassword.invoke(targetId)
                .onSuccess {
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
        if ((backHandler as? BackDispatcher)?.back() != true) {
            onBack()
        }
    }

    override fun onUserClick() {
        val current = _state.value as? IdentifierDetailScreenState.Content ?: return
        if (onNavigateToUserDetail != null) {
            onNavigateToUserDetail.invoke(current.identifier.userId)
        } else {
            onNavigateToProfile()
        }
    }

    private fun initializeIdentifier() {
        scope.launch {
            val targetId = identifierId
            val activeIdentifierId = authStorage?.getIdentifierId()?.toUserIdentifierIdOrNull()
            val resolvedIsCurrent = isCurrentIdentifier ?: (targetId != null && targetId == activeIdentifierId)

            if (targetId != null && fetchIdentifier != null) {
                _state.value = IdentifierDetailScreenState.Loading
                fetchIdentifier(targetId)
                    .onSuccess { loaded ->
                        val isSelf = currentUserId != null && loaded.userId == currentUserId
                        _state.value = IdentifierDetailScreenState.Content(
                            identifier = loaded,
                            isCurrentIdentifier = resolvedIsCurrent,
                            isSelfIdentifier = isSelf,
                            canChangePassword = changePassword != null && loaded.userAuthProvider == UserAuthProvider.EMAIL && !isSelf,
                            canDeletePassword = deletePassword != null && loaded.userAuthProvider == UserAuthProvider.EMAIL && !isSelf
                        )
                    }
                    .onError { error ->
                        _state.value = IdentifierDetailScreenState.Error(error)
                    }
            } else {
                _state.value = IdentifierDetailScreenState.Error(
                    CommonError.ContractViolation(throwable = IllegalStateException("Invalid identifierId in params"))
                )
            }
        }
    }
}
