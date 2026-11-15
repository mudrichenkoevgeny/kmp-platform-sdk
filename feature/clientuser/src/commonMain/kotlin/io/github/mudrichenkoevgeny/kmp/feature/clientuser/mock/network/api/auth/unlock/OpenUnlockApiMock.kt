package io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.network.api.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.unlock.OpenUnlockApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByEmailConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByPhoneConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest

/**
 * Mock implementation of [OpenUnlockApi] for unit and UI testing.
 */
@InternalApi
public open class OpenUnlockApiMock : OpenUnlockApi {

    public var sendUnlockEmailConfirmationResult: AppResult<OtpConfirmationPayload> = AppResult.Error(
        CommonError.Unknown()
    )
    public var unlockByEmailResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())
    public var sendUnlockPhoneConfirmationResult: AppResult<OtpConfirmationPayload> = AppResult.Error(
        CommonError.Unknown()
    )
    public var unlockByPhoneResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())
    public var unlockByExternalAuthProviderResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())

    public var lastSendUnlockEmailConfirmationRequest: SendConfirmationToEmailRequest? = null
    public var lastUnlockByEmailRequest: UnlockByEmailConfirmationRequest? = null
    public var lastSendUnlockPhoneConfirmationRequest: SendConfirmationToPhoneRequest? = null
    public var lastUnlockByPhoneRequest: UnlockByPhoneConfirmationRequest? = null
    public var lastUnlockByExternalAuthProviderRequest: UnlockByExternalAuthProviderRequest? = null

    override suspend fun sendUnlockEmailConfirmation(
        request: SendConfirmationToEmailRequest
    ): AppResult<OtpConfirmationPayload> {
        lastSendUnlockEmailConfirmationRequest = request
        return sendUnlockEmailConfirmationResult
    }

    override suspend fun unlockByEmail(
        request: UnlockByEmailConfirmationRequest
    ): AppResult<Unit> {
        lastUnlockByEmailRequest = request
        return unlockByEmailResult
    }

    override suspend fun sendUnlockPhoneConfirmation(
        request: SendConfirmationToPhoneRequest
    ): AppResult<OtpConfirmationPayload> {
        lastSendUnlockPhoneConfirmationRequest = request
        return sendUnlockPhoneConfirmationResult
    }

    override suspend fun unlockByPhone(
        request: UnlockByPhoneConfirmationRequest
    ): AppResult<Unit> {
        lastUnlockByPhoneRequest = request
        return unlockByPhoneResult
    }

    override suspend fun unlockByExternalAuthProvider(
        request: UnlockByExternalAuthProviderRequest
    ): AppResult<Unit> {
        lastUnlockByExternalAuthProviderRequest = request
        return unlockByExternalAuthProviderResult
    }
}
