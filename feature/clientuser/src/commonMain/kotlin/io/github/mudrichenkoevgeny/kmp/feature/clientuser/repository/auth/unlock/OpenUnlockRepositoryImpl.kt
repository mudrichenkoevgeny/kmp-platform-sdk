package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.unlock.OpenUnlockApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.unlock.UnlockRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.otpconfirmation.toOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByEmailConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByPhoneConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest

/**
 * Implements [UnlockRepository] using [OpenUnlockApi] and [ConfirmationRepository] for
 * client-side rate limiting on email and phone unlock OTP requests.
 *
 * @param openUnlockApi Account unlock HTTP endpoints.
 * @param confirmationRepository Cooldown manager for OTP confirmation requests.
 */
public class OpenUnlockRepositoryImpl(
    private val openUnlockApi: OpenUnlockApi,
    private val confirmationRepository: ConfirmationRepository
) : UnlockRepository {

    public override suspend fun sendUnlockEmailConfirmation(email: String): AppResult<OtpConfirmation> =
        confirmationRepository.executeWithTimer(
            type = ConfirmationType.UNLOCK_EMAIL,
            identifier = email
        ) {
            openUnlockApi.sendUnlockEmailConfirmation(
                SendConfirmationToEmailRequest(email)
            ).mapSuccess { payload ->
                payload.toOtpConfirmation()
            }
        }

    public override fun getRemainingUnlockEmailConfirmationDelayInSeconds(email: String): Int =
        confirmationRepository.getRemainingDelay(
            type = ConfirmationType.UNLOCK_EMAIL,
            identifier = email
        )

    public override suspend fun unlockByEmail(
        email: String,
        confirmationCode: String
    ): AppResult<Unit> = openUnlockApi.unlockByEmail(
        UnlockByEmailConfirmationRequest(email = email, confirmationCode = confirmationCode)
    )

    public override suspend fun sendUnlockPhoneConfirmation(phoneNumber: String): AppResult<OtpConfirmation> =
        confirmationRepository.executeWithTimer(
            type = ConfirmationType.UNLOCK_PHONE,
            identifier = phoneNumber
        ) {
            openUnlockApi.sendUnlockPhoneConfirmation(
                SendConfirmationToPhoneRequest(phoneNumber)
            ).mapSuccess { payload ->
                payload.toOtpConfirmation()
            }
        }

    public override fun getRemainingUnlockPhoneConfirmationDelayInSeconds(phoneNumber: String): Int =
        confirmationRepository.getRemainingDelay(
            type = ConfirmationType.UNLOCK_PHONE,
            identifier = phoneNumber
        )

    public override suspend fun unlockByPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<Unit> = openUnlockApi.unlockByPhone(
        UnlockByPhoneConfirmationRequest(phoneNumber = phoneNumber, confirmationCode = confirmationCode)
    )

    public override suspend fun unlockByExternalAuthProvider(
        authProvider: String,
        externalProviderToken: String
    ): AppResult<Unit> = openUnlockApi.unlockByExternalAuthProvider(
        UnlockByExternalAuthProviderRequest(
            authProvider = authProvider,
            externalProviderToken = externalProviderToken
        )
    )
}
