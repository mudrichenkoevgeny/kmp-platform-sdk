package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.unlock.SelfManagementUnlockApi
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.unlock.UnlockRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.otpconfirmation.toOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByEmailConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByPhoneConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest

/**
 * Implements [UnlockRepository] using [SelfManagementUnlockApi] and [ConfirmationRepository] for
 * self-management account unlock rate limiting.
 *
 * @param selfManagementUnlockApi Account unlock HTTP endpoints for management users.
 * @param confirmationRepository Cooldown manager for OTP confirmation requests.
 */
class SelfManagementUnlockRepositoryImpl(
    private val selfManagementUnlockApi: SelfManagementUnlockApi,
    private val confirmationRepository: ConfirmationRepository
) : UnlockRepository {

    override suspend fun sendUnlockEmailConfirmation(email: String): AppResult<OtpConfirmation> {
        return confirmationRepository.executeWithTimer(
            type = ConfirmationType.UNLOCK_EMAIL,
            identifier = email
        ) {
            selfManagementUnlockApi.sendUnlockEmailConfirmation(
                SendConfirmationToEmailRequest(email)
            ).mapSuccess { payload ->
                payload.toOtpConfirmation()
            }
        }
    }

    override fun getRemainingUnlockEmailConfirmationDelayInSeconds(email: String): Int {
        return confirmationRepository.getRemainingDelay(
            type = ConfirmationType.UNLOCK_EMAIL,
            identifier = email
        )
    }

    override suspend fun unlockByEmail(email: String, confirmationCode: String): AppResult<Unit> {
        return selfManagementUnlockApi.unlockByEmail(
            UnlockByEmailConfirmationRequest(email = email, confirmationCode = confirmationCode)
        )
    }

    override suspend fun sendUnlockPhoneConfirmation(phoneNumber: String): AppResult<OtpConfirmation> {
        return confirmationRepository.executeWithTimer(
            type = ConfirmationType.UNLOCK_PHONE,
            identifier = phoneNumber
        ) {
            selfManagementUnlockApi.sendUnlockPhoneConfirmation(
                SendConfirmationToPhoneRequest(phoneNumber)
            ).mapSuccess { payload ->
                payload.toOtpConfirmation()
            }
        }
    }

    override fun getRemainingUnlockPhoneConfirmationDelayInSeconds(phoneNumber: String): Int {
        return confirmationRepository.getRemainingDelay(
            type = ConfirmationType.UNLOCK_PHONE,
            identifier = phoneNumber
        )
    }

    override suspend fun unlockByPhone(phoneNumber: String, confirmationCode: String): AppResult<Unit> {
        return selfManagementUnlockApi.unlockByPhone(
            UnlockByPhoneConfirmationRequest(phoneNumber = phoneNumber, confirmationCode = confirmationCode)
        )
    }

    override suspend fun unlockByExternalAuthProvider(
        authProvider: String,
        externalProviderToken: String
    ): AppResult<Unit> {
        return selfManagementUnlockApi.unlockByExternalAuthProvider(
            UnlockByExternalAuthProviderRequest(
                authProvider = authProvider,
                externalProviderToken = externalProviderToken
            )
        )
    }
}
