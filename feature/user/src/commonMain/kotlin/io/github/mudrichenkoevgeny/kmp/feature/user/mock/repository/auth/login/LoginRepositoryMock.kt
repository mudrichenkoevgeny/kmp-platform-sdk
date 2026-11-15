package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest

/**
 * Mock implementation of [LoginRepository] for testing.
 */
@InternalApi
public class LoginRepositoryMock : LoginRepository {

    public var lastEmail: String? = null
    public var lastPassword: String? = null
    public var lastPhoneNumber: String? = null
    public var lastConfirmationCode: String? = null
    public var lastAuthProvider: UserAuthProvider? = null
    public var lastToken: String? = null
    public var lastMfaToken: String? = null
    public var lastCode: String? = null

    public var authDataResultProvider: () -> AppResult<AuthData> = {
        AppResult.Error(
            CommonError.ContractViolation(
                throwable = IllegalStateException("No mock AuthData provided.")
            )
        )
    }

    public var otpConfirmationResultProvider: () -> AppResult<OtpConfirmation> = {
        AppResult.Error(
            CommonError.ContractViolation(
                throwable = IllegalStateException("No mock OtpConfirmation provided.")
            )
        )
    }

    public var remainingDelayProvider: (phoneNumber: String) -> Int = { 0 }

    public override suspend fun loginByEmail(
        email: String,
        password: String
    ): AppResult<AuthData> {
        lastEmail = email
        lastPassword = password
        return authDataResultProvider()
    }

    public override suspend fun loginByPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<AuthData> {
        lastPhoneNumber = phoneNumber
        lastConfirmationCode = confirmationCode
        return authDataResultProvider()
    }

    public override suspend fun loginByExternalAuthProvider(
        loginByExternalAuthProviderRequest: LoginByExternalAuthProviderRequest
    ): AppResult<AuthData> {
        lastAuthProvider = UserAuthProvider.fromValueOrNull(loginByExternalAuthProviderRequest.authProvider)
        lastToken = loginByExternalAuthProviderRequest.externalProviderToken
        return authDataResultProvider()
    }

    public override suspend fun loginByTotp(
        mfaToken: String,
        code: String
    ): AppResult<AuthData> {
        lastMfaToken = mfaToken
        lastCode = code
        return authDataResultProvider()
    }

    public override suspend fun loginByTotpRecoveryCode(
        mfaToken: String,
        code: String
    ): AppResult<AuthData> {
        lastMfaToken = mfaToken
        lastCode = code
        return authDataResultProvider()
    }

    public override suspend fun sendLoginConfirmationToPhone(phoneNumber: String): AppResult<OtpConfirmation> {
        lastPhoneNumber = phoneNumber
        return otpConfirmationResultProvider()
    }

    public override fun getRemainingLoginConfirmationDelayInSeconds(phoneNumber: String): Int =
        remainingDelayProvider(phoneNumber)
}
