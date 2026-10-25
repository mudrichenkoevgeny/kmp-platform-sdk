package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.unlock.SelfManagementUnlockApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByEmailConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest

@InternalApi
open class SelfManagementUnlockApiMock : SelfManagementUnlockApi {

    var sendUnlockEmailConfirmationResult: AppResult<OtpConfirmationPayload> = AppResult.Error(CommonError.Unknown())
    var unlockByEmailResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())

    var lastSendUnlockEmailConfirmationRequest: SendConfirmationToEmailRequest? = null
    var lastUnlockByEmailRequest: UnlockByEmailConfirmationRequest? = null

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
}
