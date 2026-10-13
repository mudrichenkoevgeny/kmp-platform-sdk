package io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@InternalApi
class UserRepositoryMock : UserRepository {

    private val _currentUser = MutableStateFlow<UserPrivate?>(null)

    var currentUserProvider: () -> Flow<UserPrivate?> = { _currentUser.asStateFlow() }

    override val currentUser: Flow<UserPrivate?> get() = currentUserProvider()

    var resultProvider: () -> AppResult<UserPrivate> = {
        AppResult.Error(
            CommonError.ContractViolation(
                throwable = IllegalStateException("No mock result provided for UserRepositoryMock.")
            )
        )
    }

    override suspend fun refreshCurrentUser(): AppResult<UserPrivate> = handleUpdate()

    override suspend fun scheduleUserDeletion(): AppResult<UserPrivate> = handleUpdate()

    override suspend fun restoreUser(): AppResult<UserPrivate> = handleUpdate()

    override suspend fun clearSession() {
        _currentUser.value = null
    }

    private fun handleUpdate(): AppResult<UserPrivate> {
        val result = resultProvider()
        if (result is AppResult.Success) {
            _currentUser.value = result.data
        }
        return result
    }

    fun emit(user: UserPrivate?) {
        _currentUser.value = user
    }
}