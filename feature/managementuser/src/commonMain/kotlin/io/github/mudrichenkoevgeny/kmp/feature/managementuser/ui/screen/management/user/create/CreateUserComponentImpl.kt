package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.CreateUserUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserStorageConfigurationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest
import kotlinx.coroutines.launch

/**
 * Default [CreateUserComponent] implementation: manages user account creation form state and execution.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param createUserUseCase Use case to administratively create a new user account.
 * @param onUserCreated Optional callback invoked when user creation succeeds to refresh parent list state.
 * @param onSuccess Callback invoked when user creation succeeds to dismiss/pop creation screen.
 * @param onBack Pops this screen from the navigation stack.
 */
class CreateUserComponentImpl(
    componentContext: ComponentContext,
    private val createUserUseCase: CreateUserUseCase,
    private val onUserCreated: (() -> Unit)? = null,
    private val onSuccess: () -> Unit,
    private val onBack: () -> Unit
) : CreateUserComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue(CreateUserScreenState())
    override val state: Value<CreateUserScreenState> = _state

    override fun onEmailChanged(value: String) {
        _state.value = _state.value.copy(email = value, error = null)
    }

    override fun onPasswordChanged(value: String) {
        _state.value = _state.value.copy(password = value, error = null)
    }

    override fun onRoleChanged(value: String) {
        _state.value = _state.value.copy(role = value, error = null)
    }

    override fun onStatusChanged(value: String) {
        _state.value = _state.value.copy(status = value, error = null)
    }

    override fun onAuthorityLevelChanged(value: String) {
        _state.value = _state.value.copy(authorityLevel = value, error = null)
    }

    override fun onQuotaBytesChanged(value: String) {
        _state.value = _state.value.copy(quotaBytes = value, error = null)
    }

    override fun onMaxUploadSizeBytesChanged(value: String) {
        _state.value = _state.value.copy(maxUploadSizeBytes = value, error = null)
    }

    override fun onMaxPendingUploadsChanged(value: String) {
        _state.value = _state.value.copy(maxPendingUploads = value, error = null)
    }

    override fun onCreateClick() {
        val current = _state.value
        val authLevel = current.authorityLevel.toIntOrNull() ?: 0
        val quotaBytes = current.quotaBytes.toLongOrNull()
        val maxUploadSizeBytes = current.maxUploadSizeBytes.toLongOrNull()
        val maxPendingUploads = current.maxPendingUploads.toIntOrNull()
        val storageConfiguration = if (quotaBytes != null || maxUploadSizeBytes != null || maxPendingUploads != null) {
            UserStorageConfigurationPayload(
                quotaBytes = quotaBytes,
                maxUploadSizeBytes = maxUploadSizeBytes,
                maxPendingUploads = maxPendingUploads
            )
        } else {
            null
        }

        _state.value = current.copy(isLoading = true, error = null)
        scope.launch {
            createUserUseCase(
                CreateByEmailRequest(
                    email = current.email,
                    password = current.password.ifBlank { null },
                    role = current.role,
                    status = current.status,
                    authorityLevel = authLevel,
                    permissionCodes = emptySet(),
                    storageConfiguration = storageConfiguration
                )
            ).onSuccess {
                onUserCreated?.invoke()
                onSuccess()
            }.onError { error ->
                _state.value = current.copy(isLoading = false, error = error)
            }
        }
    }

    override fun onBackClick() {
        if ((backHandler as? BackDispatcher)?.back() != true) {
            onBack()
        }
    }
}
