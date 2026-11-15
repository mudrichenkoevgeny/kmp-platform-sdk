package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.domain.model.user.UpdateUserParams
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Mock implementation of [UserRepository] for testing.
 */
@InternalApi
public open class SelfManagementUserRepositoryMock : UserRepository {

    private val _currentUser = MutableStateFlow<UserPrivate?>(null)

    public var currentUserProvider: () -> Flow<UserPrivate?> = { _currentUser.asStateFlow() }

    public override val currentUser: Flow<UserPrivate?> get() = currentUserProvider()

    public var resultProvider: () -> AppResult<UserPrivate> = {
        _currentUser.value?.let { AppResult.Success(it) }
            ?: AppResult.Error(
                CommonError.ContractViolation(
                    throwable = IllegalStateException("No mock user provided.")
                )
            )
    }

    public override suspend fun refreshCurrentUser(): AppResult<UserPrivate> = handleUpdate()

    public override suspend fun scheduleUserDeletion(): AppResult<UserPrivate> = AppResult.Error(
        CommonError.ContractViolation(
            throwable = IllegalStateException("Self-schedule user deletion for Management User are not supported.")
        )
    )

    public override suspend fun restoreUser(): AppResult<UserPrivate> = AppResult.Error(
        CommonError.ContractViolation(
            throwable = IllegalStateException("Self-restore user deletion for Management User are not supported.")
        )
    )

    public override suspend fun selfUpdateUser(
        params: UpdateUserParams
    ): AppResult<UserPrivate> = handleUpdate()

    public override suspend fun clearSession() {
        _currentUser.value = null
    }

    private fun handleUpdate(): AppResult<UserPrivate> {
        val result = resultProvider()
        if (result is AppResult.Success) {
            _currentUser.value = result.data
        }
        return result
    }

    /** Emits a new [UserPrivate] value for subscribers. */
    public fun emit(user: UserPrivate?) {
        _currentUser.value = user
    }
}
