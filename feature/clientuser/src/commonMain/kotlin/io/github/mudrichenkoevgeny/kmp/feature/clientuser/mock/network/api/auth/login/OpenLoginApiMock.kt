package io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.network.api.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.login.OpenLoginApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.data.AuthDataPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest

@InternalApi
open class OpenLoginApiMock : OpenLoginApi {

    var loginByEmailResult: AppResult<AuthDataPayload> = AppResult.Error(CommonError.Unknown())
    var loginByPhoneResult: AppResult<AuthDataPayload> = AppResult.Error(CommonError.Unknown())
    var loginByExternalAuthProviderResult: AppResult<AuthDataPayload> = AppResult.Error(CommonError.Unknown())
    var loginByTotpResult: AppResult<AuthDataPayload> = AppResult.Error(CommonError.Unknown())
    var loginByTotpRecoveryCodeResult: AppResult<AuthDataPayload> = AppResult.Error(CommonError.Unknown())
    var sendLoginConfirmationToPhoneResult: AppResult<OtpConfirmationPayload> = AppResult.Error(CommonError.Unknown())

    var lastLoginByEmailRequest: LoginByEmailRequest? = null
    var lastLoginByPhoneRequest: LoginByPhoneRequest? = null
    var lastLoginByExternalAuthProviderRequest: LoginByExternalAuthProviderRequest? = null
    var lastLoginByTotpRequest: VerifyTotpPayload? = null
    var lastLoginByTotpRecoveryCodeRequest: VerifyTotpPayload? = null
    var lastSendLoginConfirmationToPhoneRequest: SendConfirmationToPhoneRequest? = null

    override suspend fun loginByEmail(request: LoginByEmailRequest): AppResult<AuthDataPayload> {
        lastLoginByEmailRequest = request
        return loginByEmailResult
    }

    override suspend fun loginByPhone(request: LoginByPhoneRequest): AppResult<AuthDataPayload> {
        lastLoginByPhoneRequest = request
        return loginByPhoneResult
    }

    override suspend fun loginByExternalAuthProvider(request: LoginByExternalAuthProviderRequest): AppResult<AuthDataPayload> {
        lastLoginByExternalAuthProviderRequest = request
        return loginByExternalAuthProviderResult
    }

    override suspend fun loginByTotp(request: VerifyTotpPayload): AppResult<AuthDataPayload> {
        lastLoginByTotpRequest = request
        return loginByTotpResult
    }

    override suspend fun loginByTotpRecoveryCode(request: VerifyTotpPayload): AppResult<AuthDataPayload> {
        lastLoginByTotpRecoveryCodeRequest = request
        return loginByTotpRecoveryCodeResult
    }

    override suspend fun sendLoginConfirmationToPhone(request: SendConfirmationToPhoneRequest): AppResult<OtpConfirmationPayload> {
        lastSendLoginConfirmationToPhoneRequest = request
        return sendLoginConfirmationToPhoneResult
    }
}
