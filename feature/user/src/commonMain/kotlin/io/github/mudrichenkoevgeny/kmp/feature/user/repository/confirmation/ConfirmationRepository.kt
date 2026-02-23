package io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType

interface ConfirmationRepository {
    suspend fun <T> executeWithTimer(
        type: ConfirmationType,
        identifier: String,
        action: suspend () -> AppResult<T>
    ): AppResult<T>

    fun getRemainingDelay(
        type: ConfirmationType,
        identifier: String
    ): Int
}