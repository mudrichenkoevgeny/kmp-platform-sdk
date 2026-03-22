package io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation

/** Marks confirmation / rate-limit payloads that expose a suggested retry delay in seconds. */
interface HasRetryDelay {
    /** Suggested wait time before the client retries a send or submit action. */
    val retryAfterSeconds: Int
}