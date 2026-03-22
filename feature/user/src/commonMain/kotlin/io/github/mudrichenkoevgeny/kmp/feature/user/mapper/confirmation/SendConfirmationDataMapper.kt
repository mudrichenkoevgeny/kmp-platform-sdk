package io.github.mudrichenkoevgeny.kmp.feature.user.mapper.confirmation

import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.confirmation.SendConfirmationResponse

/**
 * Maps confirmation-send API metadata into [SendConfirmationData] (e.g. rate-limit hint).
 */
fun SendConfirmationResponse.toSendConfirmationData(): SendConfirmationData = SendConfirmationData(
    retryAfterSeconds = retryAfterSeconds
)