package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails

open class ScheduleUserDeletionUseCase(
    private val userRepository: UserRepository
) {

    open suspend operator fun invoke(): AppResult<UserDetails> {
        return userRepository.scheduleUserDeletion()
    }
}