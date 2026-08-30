package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@InternalApi
open class SelfManagementUserRepositoryMock : UserRepository {

    private val _currentUser = MutableStateFlow<UserDetails?>(null)

    var currentUserProvider: () -> Flow<UserDetails?> = { _currentUser.asStateFlow() }

    override val currentUser: Flow<UserDetails?> get() = currentUserProvider()

    var resultProvider: () -> AppResult<UserDetails> = {
        _currentUser.value?.let { AppResult.Success(it) }
            ?: AppResult.Error(
                CommonError.ContractViolation(
                    throwable = IllegalStateException("No mock user provided.")
                )
            )
    }

    override suspend fun refreshCurrentUser(): AppResult<UserDetails> = handleUpdate()

    override suspend fun scheduleUserDeletion(): AppResult<UserDetails> = AppResult.Error(
        CommonError.ContractViolation(
            throwable = IllegalStateException("Self-schedule user deletion for Management User are not supported.")
        )
    )

    override suspend fun restoreUser(): AppResult<UserDetails> = AppResult.Error(
        CommonError.ContractViolation(
            throwable = IllegalStateException("Self-restore user deletion for Management User are not supported.")
        )
    )

    private fun handleUpdate(): AppResult<UserDetails> {
        val result = resultProvider()
        if (result is AppResult.Success) {
            _currentUser.value = result.data
        }
        return result
    }

    fun emit(user: UserDetails?) {
        _currentUser.value = user
    }
}