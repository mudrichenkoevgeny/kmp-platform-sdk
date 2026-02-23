package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.register.RegisterByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.AuthDataResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.confirmation.SendConfirmationResponse

interface RegistrationApi {
    suspend fun registerByEmail(request: RegisterByEmailRequest): AppResult<AuthDataResponse>
    suspend fun sendRegistrationConfirmationToEmail(
        request: SendConfirmationToEmailRequest
    ): AppResult<SendConfirmationResponse>
}