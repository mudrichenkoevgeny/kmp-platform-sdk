package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.login.SelfManagementLoginApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.data.AuthDataPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest

@InternalApi
class SelfManagementLoginApiMock : SelfManagementLoginApi {

    var loginByEmailResult: AppResult<AuthDataPayload> = AppResult.Error(CommonError.Unknown())
    var loginByTotpResult: AppResult<AuthDataPayload> = AppResult.Error(CommonError.Unknown())
    var loginByTotpRecoveryCodeResult: AppResult<AuthDataPayload> = AppResult.Error(CommonError.Unknown())

    var lastLoginByEmailRequest: LoginByEmailRequest? = null
    var lastLoginByTotpRequest: VerifyTotpPayload? = null
    var lastLoginByTotpRecoveryCodeRequest: VerifyTotpPayload? = null

    override suspend fun loginByEmail(request: LoginByEmailRequest): AppResult<AuthDataPayload> {
        lastLoginByEmailRequest = request
        return loginByEmailResult
    }

    override suspend fun loginByTotp(request: VerifyTotpPayload): AppResult<AuthDataPayload> {
        lastLoginByTotpRequest = request
        return loginByTotpResult
    }

    override suspend fun loginByTotpRecoveryCode(request: VerifyTotpPayload): AppResult<AuthDataPayload> {
        lastLoginByTotpRecoveryCodeRequest = request
        return loginByTotpRecoveryCodeResult
    }
}