package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByEmailConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest

/** Self-service account unlock flows for management users. */
interface SelfManagementUnlockApi {

    /**
     * Sends an account unlock confirmation code (OTP) to the specified management email address.
     *
     * @param request Target email address request payload.
     * @return Confirmation dispatch result, or a mapped failure.
     */
    suspend fun sendUnlockEmailConfirmation(
        request: SendConfirmationToEmailRequest
    ): AppResult<OtpConfirmationPayload>

    /**
     * Unlocks a temporarily locked management account using an email confirmation code.
     *
     * @param request Unlock payload containing email and confirmation code.
     * @return Unit result on success, or a mapped failure.
     */
    suspend fun unlockByEmail(
        request: UnlockByEmailConfirmationRequest
    ): AppResult<Unit>
}
