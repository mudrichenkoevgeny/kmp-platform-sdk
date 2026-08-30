package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.login.SelfManagementLoginApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest

/**
 * Implements [LoginRepository] using [SelfManagementLoginApi].
 *
 * @param selfManagementLoginApi HTTP endpoints for all login variants.
 */
class SelfManagementLoginRepositoryImpl(
    private val selfManagementLoginApi: SelfManagementLoginApi
) : LoginRepository {

    override suspend fun loginByEmail(
        email: String,
        password: String
    ): AppResult<AuthData> {
        return selfManagementLoginApi.loginByEmail(LoginByEmailRequest(email, password))
            .mapSuccess { authDataPayload ->
                authDataPayload.toAuthData()
            }
    }

    override suspend fun loginByPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<AuthData> {
        return methodNotSupported()
    }

    override suspend fun loginByExternalAuthProvider(
        authProvider: UserAuthProvider,
        token: String
    ): AppResult<AuthData> {
        return methodNotSupported()
    }

    override suspend fun loginByTotp(mfaToken: String, code: String): AppResult<AuthData> {
        return selfManagementLoginApi.loginByTotp(
            VerifyTotpPayload(
                mfaToken = mfaToken,
                code = code
            )
        ).mapSuccess { authDataPayload ->
            authDataPayload.toAuthData()
        }
    }

    override suspend fun loginByTotpRecoveryCode(
        mfaToken: String,
        code: String
    ): AppResult<AuthData> {
        return selfManagementLoginApi.loginByTotp(
            VerifyTotpPayload(
                mfaToken = mfaToken,
                code = code
            )
        ).mapSuccess { authDataPayload ->
            authDataPayload.toAuthData()
        }
    }

    override suspend fun sendLoginConfirmationToPhone(
        phoneNumber: String
    ): AppResult<OtpConfirmation> {
        return methodNotSupported()
    }

    override fun getRemainingLoginConfirmationDelayInSeconds(phoneNumber: String): Int = 0

    private fun <T> methodNotSupported(): AppResult<T> = AppResult.Error(
        CommonError.ContractViolation(
            throwable = IllegalStateException(
                "This authentication method is not supported in management context."
            )
        )
    )
}