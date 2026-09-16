package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByEmailConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByPhoneConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest

/** Account unlock flows for the client user feature. */
interface OpenUnlockApi {

    /**
     * Sends an account unlock confirmation code (OTP) to the specified email address.
     *
     * @param request Target email address request payload.
     * @return Confirmation dispatch result, or a mapped failure.
     */
    suspend fun sendUnlockEmailConfirmation(
        request: SendConfirmationToEmailRequest
    ): AppResult<OtpConfirmationPayload>

    /**
     * Unlocks a temporarily locked account using an email confirmation code.
     *
     * @param request Unlock payload containing email and confirmation code.
     * @return Unit result on success, or a mapped failure.
     */
    suspend fun unlockByEmail(
        request: UnlockByEmailConfirmationRequest
    ): AppResult<Unit>

    /**
     * Sends an account unlock confirmation code (OTP) to the specified phone number.
     *
     * @param request Target phone number request payload.
     * @return Confirmation dispatch result, or a mapped failure.
     */
    suspend fun sendUnlockPhoneConfirmation(
        request: SendConfirmationToPhoneRequest
    ): AppResult<OtpConfirmationPayload>

    /**
     * Unlocks a temporarily locked account using a phone confirmation code.
     *
     * @param request Unlock payload containing phone number and confirmation code.
     * @return Unit result on success, or a mapped failure.
     */
    suspend fun unlockByPhone(
        request: UnlockByPhoneConfirmationRequest
    ): AppResult<Unit>

    /**
     * Unlocks a temporarily locked account via an external authentication provider token.
     *
     * @param request Unlock payload containing external auth provider and token.
     * @return Unit result on success, or a mapped failure.
     */
    suspend fun unlockByExternalAuthProvider(
        request: UnlockByExternalAuthProviderRequest
    ): AppResult<Unit>
}
