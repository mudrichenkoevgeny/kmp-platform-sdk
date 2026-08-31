package io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.network.api.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.registration.RegistrationApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.data.AuthDataPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.register.RegisterByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest

@InternalApi
open class OpenRegistrationApiMock : RegistrationApi {

    var registerByEmailResult: AppResult<AuthDataPayload> = AppResult.Error(CommonError.Unknown())
    var sendRegistrationConfirmationToEmailResult: AppResult<OtpConfirmationPayload> = AppResult.Error(CommonError.Unknown())

    var lastRegisterByEmailRequest: RegisterByEmailRequest? = null
    var lastSendRegistrationConfirmationToEmailRequest: SendConfirmationToEmailRequest? = null

    override suspend fun registerByEmail(request: RegisterByEmailRequest): AppResult<AuthDataPayload> {
        lastRegisterByEmailRequest = request
        return registerByEmailResult
    }

    override suspend fun sendRegistrationConfirmationToEmail(request: SendConfirmationToEmailRequest): AppResult<OtpConfirmationPayload> {
        lastSendRegistrationConfirmationToEmailRequest = request
        return sendRegistrationConfirmationToEmailResult
    }
}
