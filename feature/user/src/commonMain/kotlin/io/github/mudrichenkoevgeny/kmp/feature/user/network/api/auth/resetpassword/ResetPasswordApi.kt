package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.ResetPasswordRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.SendResetPasswordConfirmationRequest

/** Password reset and related confirmation calls for unauthenticated recovery flows. */
interface ResetPasswordApi {
    /**
     * Completes password reset using the server-issued reset flow payload.
     *
     * @param request Reset token and new secret from the shared contract.
     * @return Updated user identifier context, or a mapped failure.
     */
    suspend fun resetPassword(request: ResetPasswordRequest): AppResult<UserIdentifierPayload>

    /**
     * Sends a password-reset confirmation link or code to the user email.
     *
     * @param request Target account hint from the shared contract.
     * @return Confirmation dispatch result, or a mapped failure.
     */
    suspend fun sendResetPasswordConfirmationToEmail(
        request: SendResetPasswordConfirmationRequest
    ): AppResult<OtpConfirmationPayload>
}