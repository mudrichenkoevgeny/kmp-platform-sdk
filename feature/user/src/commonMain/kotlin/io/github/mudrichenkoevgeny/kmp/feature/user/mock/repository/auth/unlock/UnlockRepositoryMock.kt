package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.otpConfirmationMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.unlock.UnlockRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation

@InternalApi
class UnlockRepositoryMock : UnlockRepository {

    var sendUnlockEmailConfirmationResult: AppResult<OtpConfirmation> = AppResult.Success(otpConfirmationMock())
    var remainingEmailDelaySeconds: Int = 0
    var unlockByEmailResult: AppResult<Unit> = AppResult.Success(Unit)

    var sendUnlockPhoneConfirmationResult: AppResult<OtpConfirmation> = AppResult.Success(otpConfirmationMock())
    var remainingPhoneDelaySeconds: Int = 0
    var unlockByPhoneResult: AppResult<Unit> = AppResult.Success(Unit)

    var unlockByExternalAuthProviderResult: AppResult<Unit> = AppResult.Success(Unit)

    var lastEmail: String? = null
    var lastPhoneNumber: String? = null
    var lastConfirmationCode: String? = null
    var lastAuthProvider: String? = null
    var lastExternalProviderToken: String? = null

    override suspend fun sendUnlockEmailConfirmation(email: String): AppResult<OtpConfirmation> {
        lastEmail = email
        return sendUnlockEmailConfirmationResult
    }

    override fun getRemainingUnlockEmailConfirmationDelayInSeconds(email: String): Int {
        return remainingEmailDelaySeconds
    }

    override suspend fun unlockByEmail(email: String, confirmationCode: String): AppResult<Unit> {
        lastEmail = email
        lastConfirmationCode = confirmationCode
        return unlockByEmailResult
    }

    override suspend fun sendUnlockPhoneConfirmation(phoneNumber: String): AppResult<OtpConfirmation> {
        lastPhoneNumber = phoneNumber
        return sendUnlockPhoneConfirmationResult
    }

    override fun getRemainingUnlockPhoneConfirmationDelayInSeconds(phoneNumber: String): Int {
        return remainingPhoneDelaySeconds
    }

    override suspend fun unlockByPhone(phoneNumber: String, confirmationCode: String): AppResult<Unit> {
        lastPhoneNumber = phoneNumber
        lastConfirmationCode = confirmationCode
        return unlockByPhoneResult
    }

    override suspend fun unlockByExternalAuthProvider(
        authProvider: String,
        externalProviderToken: String
    ): AppResult<Unit> {
        lastAuthProvider = authProvider
        lastExternalProviderToken = externalProviderToken
        return unlockByExternalAuthProviderResult
    }
}
