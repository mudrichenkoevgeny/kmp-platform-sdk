package io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation

/** API metadata after requesting a confirmation code (e.g. SMS/email), including rate-limit hint. */
data class SendConfirmationData(
    override val retryAfterSeconds: Int
) : HasRetryDelay