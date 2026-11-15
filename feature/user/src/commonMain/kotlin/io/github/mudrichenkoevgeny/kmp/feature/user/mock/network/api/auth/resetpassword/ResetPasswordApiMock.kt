package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.api.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.resetpassword.ResetPasswordApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.ResetPasswordRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.SendResetPasswordConfirmationRequest

/**
 * Mock implementation of [ResetPasswordApi] for testing.
 */
@InternalApi
public open class ResetPasswordApiMock : ResetPasswordApi {

    public var resetPasswordResult: AppResult<UserIdentifierPrivatePayload> = AppResult.Error(CommonError.Unknown())
    public var sendResetPasswordConfirmationToEmailResult: AppResult<OtpConfirmationPayload> =
        AppResult.Error(CommonError.Unknown())

    public var lastResetPasswordRequest: ResetPasswordRequest? = null
    public var lastSendResetPasswordConfirmationRequest: SendResetPasswordConfirmationRequest? = null

    public override suspend fun resetPassword(request: ResetPasswordRequest): AppResult<UserIdentifierPrivatePayload> {
        lastResetPasswordRequest = request
        return resetPasswordResult
    }

    public override suspend fun sendResetPasswordConfirmationToEmail(
        request: SendResetPasswordConfirmationRequest
    ): AppResult<OtpConfirmationPayload> {
        lastSendResetPasswordConfirmationRequest = request
        return sendResetPasswordConfirmationToEmailResult
    }
}
