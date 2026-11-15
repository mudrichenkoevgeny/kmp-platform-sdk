package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.unlock.UnlockRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.otpconfirmation.createTestOtpConfirmation

/**
 * Mock implementation of [UnlockRepository] for testing.
 */
@InternalApi
public class UnlockRepositoryMock : UnlockRepository {

    public var sendUnlockEmailConfirmationResult: AppResult<OtpConfirmation> = AppResult.Success(
        createTestOtpConfirmation()
    )
    public var remainingEmailDelaySeconds: Int = 0
    public var unlockByEmailResult: AppResult<Unit> = AppResult.Success(Unit)

    public var sendUnlockPhoneConfirmationResult: AppResult<OtpConfirmation> = AppResult.Success(
        createTestOtpConfirmation()
    )
    public var remainingPhoneDelaySeconds: Int = 0
    public var unlockByPhoneResult: AppResult<Unit> = AppResult.Success(Unit)

    public var unlockByExternalAuthProviderResult: AppResult<Unit> = AppResult.Success(Unit)

    public var lastEmail: String? = null
    public var lastPhoneNumber: String? = null
    public var lastConfirmationCode: String? = null
    public var lastAuthProvider: String? = null
    public var lastExternalProviderToken: String? = null

    public override suspend fun sendUnlockEmailConfirmation(email: String): AppResult<OtpConfirmation> {
        lastEmail = email
        return sendUnlockEmailConfirmationResult
    }

    public override fun getRemainingUnlockEmailConfirmationDelayInSeconds(email: String): Int =
        remainingEmailDelaySeconds

    public override suspend fun unlockByEmail(
        email: String,
        confirmationCode: String
    ): AppResult<Unit> {
        lastEmail = email
        lastConfirmationCode = confirmationCode
        return unlockByEmailResult
    }

    public override suspend fun sendUnlockPhoneConfirmation(phoneNumber: String): AppResult<OtpConfirmation> {
        lastPhoneNumber = phoneNumber
        return sendUnlockPhoneConfirmationResult
    }

    public override fun getRemainingUnlockPhoneConfirmationDelayInSeconds(phoneNumber: String): Int =
        remainingPhoneDelaySeconds

    public override suspend fun unlockByPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<Unit> {
        lastPhoneNumber = phoneNumber
        lastConfirmationCode = confirmationCode
        return unlockByPhoneResult
    }

    public override suspend fun unlockByExternalAuthProvider(
        authProvider: String,
        externalProviderToken: String
    ): AppResult<Unit> {
        lastAuthProvider = authProvider
        lastExternalProviderToken = externalProviderToken
        return unlockByExternalAuthProviderResult
    }
}
