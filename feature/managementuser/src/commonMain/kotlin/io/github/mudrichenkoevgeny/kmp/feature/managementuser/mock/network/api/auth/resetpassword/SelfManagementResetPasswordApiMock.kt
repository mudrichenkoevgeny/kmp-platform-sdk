package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.resetpassword.ResetPasswordApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.ResetPasswordRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.SendResetPasswordConfirmationRequest

@InternalApi
class SelfManagementResetPasswordApiMock : ResetPasswordApi {

    var resetPasswordResult: AppResult<UserIdentifierPayload> = AppResult.Error(CommonError.Unknown())
    var sendResetPasswordConfirmationResult: AppResult<OtpConfirmationPayload> = AppResult.Error(CommonError.Unknown())

    var lastResetPasswordRequest: ResetPasswordRequest? = null
    var lastSendResetPasswordConfirmationRequest: SendResetPasswordConfirmationRequest? = null

    override suspend fun resetPassword(request: ResetPasswordRequest): AppResult<UserIdentifierPayload> {
        lastResetPasswordRequest = request
        return resetPasswordResult
    }

    override suspend fun sendResetPasswordConfirmationToEmail(
        request: SendResetPasswordConfirmationRequest
    ): AppResult<OtpConfirmationPayload> {
        lastSendResetPasswordConfirmationRequest = request
        return sendResetPasswordConfirmationResult
    }
}