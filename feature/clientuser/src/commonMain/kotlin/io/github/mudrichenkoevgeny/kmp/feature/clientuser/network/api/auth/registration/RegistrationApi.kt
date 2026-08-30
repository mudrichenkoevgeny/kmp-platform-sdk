package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.data.AuthDataPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.register.RegisterByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest

/** Email registration and confirmation flows for the user feature. */
interface RegistrationApi {
    /**
     * Creates a new account using email registration data.
     *
     * @param request Registration payload from the shared contract.
     * @return Session tokens and related auth payload, or a mapped failure.
     */
    suspend fun registerByEmail(request: RegisterByEmailRequest): AppResult<AuthDataPayload>

    /**
     * Sends a registration confirmation message to the user email.
     *
     * @param request Target email and template parameters from the shared contract.
     * @return Confirmation dispatch result, or a mapped failure.
     */
    suspend fun sendRegistrationConfirmationToEmail(
        request: SendConfirmationToEmailRequest
    ): AppResult<OtpConfirmationPayload>
}