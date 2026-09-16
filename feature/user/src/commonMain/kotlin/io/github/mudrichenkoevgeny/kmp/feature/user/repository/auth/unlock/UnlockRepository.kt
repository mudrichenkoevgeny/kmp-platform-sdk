package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation

/** Operations for self-service account unlocking. */
interface UnlockRepository {

    /**
     * Sends an account unlock confirmation code (OTP) to the specified email address.
     *
     * @param email Target email address.
     * @return Confirmation metadata on success, or an error [AppResult] on failure.
     */
    suspend fun sendUnlockEmailConfirmation(email: String): AppResult<OtpConfirmation>

    /**
     * Gets the remaining client-side cooldown in seconds before another email unlock OTP can be sent.
     *
     * @param email Target email address.
     * @return Remaining delay in seconds.
     */
    fun getRemainingUnlockEmailConfirmationDelayInSeconds(email: String): Int

    /**
     * Unlocks an account using an email confirmation code.
     *
     * @param email Account email address.
     * @param confirmationCode Verification code sent to the email.
     * @return Unit result on success, or an error [AppResult] on failure.
     */
    suspend fun unlockByEmail(email: String, confirmationCode: String): AppResult<Unit>

    /**
     * Sends an account unlock confirmation code (OTP) to the specified phone number.
     *
     * @param phoneNumber Target phone number.
     * @return Confirmation metadata on success, or an error [AppResult] on failure.
     */
    suspend fun sendUnlockPhoneConfirmation(phoneNumber: String): AppResult<OtpConfirmation>

    /**
     * Gets the remaining client-side cooldown in seconds before another phone unlock OTP can be sent.
     *
     * @param phoneNumber Target phone number.
     * @return Remaining delay in seconds.
     */
    fun getRemainingUnlockPhoneConfirmationDelayInSeconds(phoneNumber: String): Int

    /**
     * Unlocks an account using a phone confirmation code.
     *
     * @param phoneNumber Account phone number.
     * @param confirmationCode Verification code sent to the phone.
     * @return Unit result on success, or an error [AppResult] on failure.
     */
    suspend fun unlockByPhone(phoneNumber: String, confirmationCode: String): AppResult<Unit>

    /**
     * Unlocks an account using an external authentication provider token.
     *
     * @param authProvider External provider identifier (e.g., `google`, `apple`).
     * @param externalProviderToken Identity provider token.
     * @return Unit result on success, or an error [AppResult] on failure.
     */
    suspend fun unlockByExternalAuthProvider(authProvider: String, externalProviderToken: String): AppResult<Unit>
}
