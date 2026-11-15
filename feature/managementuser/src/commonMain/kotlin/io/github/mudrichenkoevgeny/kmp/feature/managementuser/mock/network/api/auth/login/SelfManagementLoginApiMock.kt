package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.login.SelfManagementLoginApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.data.AuthDataPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest

/**
 * Mock implementation of [SelfManagementLoginApi] for testing.
 */
@InternalApi
public class SelfManagementLoginApiMock : SelfManagementLoginApi {

    public var loginByEmailResult: AppResult<AuthDataPayload> = AppResult.Error(CommonError.Unknown())
    public var loginByTotpResult: AppResult<AuthDataPayload> = AppResult.Error(CommonError.Unknown())
    public var loginByTotpRecoveryCodeResult: AppResult<AuthDataPayload> = AppResult.Error(CommonError.Unknown())

    public var lastLoginByEmailRequest: LoginByEmailRequest? = null
    public var lastLoginByTotpRequest: VerifyTotpPayload? = null
    public var lastLoginByTotpRecoveryCodeRequest: VerifyTotpPayload? = null

    public override suspend fun loginByEmail(request: LoginByEmailRequest): AppResult<AuthDataPayload> {
        lastLoginByEmailRequest = request
        return loginByEmailResult
    }

    public override suspend fun loginByTotp(request: VerifyTotpPayload): AppResult<AuthDataPayload> {
        lastLoginByTotpRequest = request
        return loginByTotpResult
    }

    public override suspend fun loginByTotpRecoveryCode(request: VerifyTotpPayload): AppResult<AuthDataPayload> {
        lastLoginByTotpRecoveryCodeRequest = request
        return loginByTotpRecoveryCodeResult
    }
}
