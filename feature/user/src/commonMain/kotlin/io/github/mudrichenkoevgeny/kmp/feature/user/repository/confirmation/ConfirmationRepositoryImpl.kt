package io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationKey
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.HasRetryDelay
import kotlin.time.Clock

class ConfirmationRepositoryImpl(
    private val clock: Clock
) : ConfirmationRepository {

    private val blockedUntilMap = mutableMapOf<ConfirmationKey, Long>()

    override suspend fun <T> executeWithTimer(
        type: ConfirmationType,
        identifier: String,
        action: suspend () -> AppResult<T>
    ): AppResult<T> {
        val key = ConfirmationKey(type, identifier)
        val remaining = getRemainingDelay(type, identifier)

        if (remaining > 0) {
            return AppResult.Error(
                UserError.TooManyConfirmationRequests(retryAfterSeconds = remaining)
            )
        }

        return action().onSuccess { result ->
            if (result is HasRetryDelay) {
                val now = clock.now().toEpochMilliseconds()
                blockedUntilMap[key] = now + (result.retryAfterSeconds * 1000L)
            }
        }
    }

    override fun getRemainingDelay(type: ConfirmationType, identifier: String): Int {
        val key = ConfirmationKey(type, identifier)
        val blockedUntil = blockedUntilMap[key] ?: return 0
        val now = clock.now().toEpochMilliseconds()
        val diff = blockedUntil - now

        return if (diff > 0) (diff / 1000).toInt() else 0
    }
}