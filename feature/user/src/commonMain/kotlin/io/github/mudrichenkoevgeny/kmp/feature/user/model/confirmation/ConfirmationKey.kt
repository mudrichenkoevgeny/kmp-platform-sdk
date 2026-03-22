package io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation

/** In-memory key for tracking an outstanding confirmation (flow type + target identifier such as email/phone). */
data class ConfirmationKey(
    val type: ConfirmationType,
    val identifier: String
)