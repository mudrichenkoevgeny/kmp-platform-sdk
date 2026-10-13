package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate

/**
 * Restores a pending deletion account.
 */
open class RestoreUserUseCase(
    private val userRepository: UserRepository
) {

    open suspend operator fun invoke(): AppResult<UserPrivate> {
        return userRepository.restoreUser()
    }
}