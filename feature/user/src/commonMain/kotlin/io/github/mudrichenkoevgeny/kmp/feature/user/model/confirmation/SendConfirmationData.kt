package io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation

data class SendConfirmationData(
    override val retryAfterSeconds: Int
) : HasRetryDelay