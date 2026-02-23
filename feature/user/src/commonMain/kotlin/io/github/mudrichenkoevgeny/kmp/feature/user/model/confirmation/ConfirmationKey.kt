package io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation

data class ConfirmationKey(
    val type: ConfirmationType,
    val identifier: String
)