package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.security.ManagementDisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.UpdateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.flow.firstOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.detail.UserDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.UpdateUserRequest
import kotlinx.coroutines.launch

/**
 * Default [UserDetailComponent] implementation: manages single user account detail inspection and administrative updates.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Screen destination parameters containing the target user ID.
 * @param getUserUseCase Use case to fetch target user profile data.
 * @param updateUserUseCase Use case to update target user account status and settings.
 * @param deleteUserUseCase Use case to administratively delete target user account.
 * @param managementDisableTotpUseCase Use case to administratively disable TOTP 2FA for target user.
 * @param onNavigateToSessions Callback to navigate to the user's sessions list.
 * @param onNavigateToIdentifiers Callback to navigate to the user's identifiers list.
 * @param onNavigateToAuditEvents Callback to navigate to audit events filtered by the user's ID.
 * @param onUserDeleted Callback invoked when the user account is deleted.
 * @param onBack Pops this screen from the navigation stack.
 */
class UserDetailComponentImpl(
    componentContext: ComponentContext,
    private val params: UserDetailParams,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val managementDisableTotpUseCase: ManagementDisableTotpUseCase,
    private val userRepository: UserRepository? = null,
    private val onNavigateToSessions: (UserId) -> Unit,
    private val onNavigateToIdentifiers: (UserId) -> Unit,
    private val onNavigateToAuditEvents: (UserId) -> Unit,
    private val onNavigateToProfile: (() -> Unit)? = null,
    private val onUserDeleted: ((UserId) -> Unit)? = null,
    private val onBack: () -> Unit
) : UserDetailComponent, ComponentContext by componentContext {

    private val userId: UserId? get() = params.typedUserId

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<UserDetailScreenState>(UserDetailScreenState.Loading)
    override val state: Value<UserDetailScreenState> = _state

    init {
        loadUser()
    }

    override fun onAuthorityLevelChanged(value: String) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(authorityLevelInput = value, saveError = null)
    }

    override fun onAccountStatusChanged(value: String) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(accountStatusInput = value, saveError = null)
    }

    override fun onLockoutTypeChanged(value: String) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(lockoutTypeInput = value, saveError = null)
    }

    override fun onTemporaryLockoutUntilChanged(value: String) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(temporaryLockoutUntilInput = value, saveError = null)
    }

    override fun onUpdateClick() {
        val targetId = userId ?: return
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        if (currentContent.isSaving) {
            return
        }

        _state.value = currentContent.copy(isSaving = true, saveError = null)

        val authorityLevel = currentContent.authorityLevelInput.toIntOrNull()
        val accountStatus = currentContent.accountStatusInput.takeIf { it.isNotBlank() }
        val lockoutType = currentContent.lockoutTypeInput.takeIf { it.isNotBlank() }
        val lockoutUntil = currentContent.temporaryLockoutUntilInput.toLongOrNull()

        val request = UpdateUserRequest(
            accountStatus = accountStatus,
            authorityLevel = authorityLevel,
            permissionCodes = null,
            lockoutType = lockoutType,
            temporaryLockoutUntil = lockoutUntil
        )

        scope.launch {
            updateUserUseCase(targetId, request)
                .onSuccess {
                    loadUser()
                }
                .onError { error ->
                    val content = _state.value as? UserDetailScreenState.Content
                    if (content != null) {
                        _state.value = content.copy(isSaving = false, saveError = error)
                    }
                }
        }
    }

    override fun onDeleteClick() {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(isDeleteConfirmationVisible = true)
    }

    override fun onConfirmDeleteClick() {
        val targetId = userId ?: return
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(isDeleting = true, deleteError = null, isDeleteConfirmationVisible = false)

        scope.launch {
            deleteUserUseCase(targetId)
                .onSuccess {
                    onUserDeleted?.invoke(targetId)
                    onBack()
                }
                .onError { error ->
                    val content = _state.value as? UserDetailScreenState.Content
                    if (content != null) {
                        _state.value = content.copy(isDeleting = false, deleteError = error)
                    }
                }
        }
    }

    override fun onDismissDeleteDialog() {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(isDeleteConfirmationVisible = false)
    }

    override fun onDisableTotpClick() {
        val targetId = userId ?: return
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(isDisablingTotp = true, disableTotpError = null)

        scope.launch {
            managementDisableTotpUseCase(targetId)
                .onSuccess {
                    loadUser()
                }
                .onError { error ->
                    val content = _state.value as? UserDetailScreenState.Content
                    if (content != null) {
                        _state.value = content.copy(isDisablingTotp = false, disableTotpError = error)
                    }
                }
        }
    }

    override fun onSessionsClick() {
        val targetId = userId ?: return
        onNavigateToSessions(targetId)
    }

    override fun onIdentifiersClick() {
        val targetId = userId ?: return
        onNavigateToIdentifiers(targetId)
    }

    override fun onAuditEventsClick() {
        val targetId = userId ?: return
        onNavigateToAuditEvents(targetId)
    }

    override fun onRetry() {
        loadUser()
    }

    override fun onBackClick() {
        onBack()
    }

    private fun loadUser() {
        val targetId = userId
        if (targetId == null) {
            _state.value = UserDetailScreenState.Error(
                CommonError.ContractViolation(throwable = IllegalStateException("Invalid userId in params"))
            )
            return
        }

        _state.value = UserDetailScreenState.Loading

        scope.launch {
            val selfUserId = userRepository?.currentUser?.firstOrNull()?.id

            getUserUseCase(targetId)
                .onSuccess { user ->
                    val isSelfUser = selfUserId != null && user.id == selfUserId
                    _state.value = UserDetailScreenState.Content(
                        user = user,
                        authorityLevelInput = user.authorityLevel.toString(),
                        accountStatusInput = user.accountStatus.name,
                        lockoutTypeInput = user.lockoutType.name,
                        temporaryLockoutUntilInput = user.temporaryLockoutUntil?.toEpochMilliseconds()?.toString() ?: "",
                        isSelf = isSelfUser
                    )
                }
                .onError { error ->
                    _state.value = UserDetailScreenState.Error(error)
                }
        }
    }
}
