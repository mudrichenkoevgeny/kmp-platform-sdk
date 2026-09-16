package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.unlock.SelfManagementUnlockApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByEmailConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByPhoneConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest

@InternalApi
open class SelfManagementUnlockApiMock : SelfManagementUnlockApi {

    var sendUnlockEmailConfirmationResult: AppResult<OtpConfirmationPayload> = AppResult.Error(CommonError.Unknown())
    var unlockByEmailResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())
    var sendUnlockPhoneConfirmationResult: AppResult<OtpConfirmationPayload> = AppResult.Error(CommonError.Unknown())
    var unlockByPhoneResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())
    var unlockByExternalAuthProviderResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())

    var lastSendUnlockEmailConfirmationRequest: SendConfirmationToEmailRequest? = null
    var lastUnlockByEmailRequest: UnlockByEmailConfirmationRequest? = null
    var lastSendUnlockPhoneConfirmationRequest: SendConfirmationToPhoneRequest? = null
    var lastUnlockByPhoneRequest: UnlockByPhoneConfirmationRequest? = null
    var lastUnlockByExternalAuthProviderRequest: UnlockByExternalAuthProviderRequest? = null

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
