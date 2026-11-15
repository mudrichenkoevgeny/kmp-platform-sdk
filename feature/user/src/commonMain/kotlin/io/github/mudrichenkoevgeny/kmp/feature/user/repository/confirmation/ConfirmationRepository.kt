package io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.confirmation.ConfirmationType

/**
 * Client-side rate limiting for confirmation flows keyed by [ConfirmationType] and a logical
 * identifier (for example email or phone) so repeated send-code requests respect retry delays.
 */
public interface ConfirmationRepository {
    /**
     * Invokes [action] when the cooldown for the given key has elapsed; otherwise returns an error
     * [AppResult] without calling [action].
     *
     * On successful [action], if the result is [OtpConfirmation], a new cooldown is stored
     * using `retryAfterSeconds`.
     *
     * @param type Which confirmation flow is being throttled.
     * @param identifier Stable key for the target (for example normalized email or phone number).
     * @param action Deferred work to run when allowed (typically a network request).
     * @return The outcome of [action], or an error result when the caller must wait before retrying.
     */
    public suspend fun <T> executeWithTimer(
        type: ConfirmationType,
        identifier: String,
        action: suspend () -> AppResult<T>
    ): AppResult<T>

    /**
     * Returns how many whole seconds remain before [executeWithTimer] will allow another attempt
     * for the same [type] and [identifier], or `0` when there is no active cooldown.
     *
     * @param type Which confirmation flow is being queried.
     * @param identifier Same key as used for [executeWithTimer].
     * @return Remaining cooldown in seconds, or `0` if none.
     */
    public fun getRemainingDelay(
        type: ConfirmationType,
        identifier: String
    ): Int
}
