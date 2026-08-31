package io.github.mudrichenkoevgeny.kmp.core.common.time

import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Simple countdown utility for resending operations (e.g. resend OTP).
 *
 * @param totalSeconds Total duration of the countdown in seconds.
 * @param interval Tick interval (defaults to 1 second).
 * @param onTick Callback invoked on each tick with the remaining seconds.
 */
suspend fun resendCountdown(
    totalSeconds: Int,
    interval: Duration = 1.seconds,
    onTick: (remainingSeconds: Int) -> Unit
) {
    var left = totalSeconds
    while (left > 0) {
        delay(interval)
        left--
        onTick(left)
    }
}