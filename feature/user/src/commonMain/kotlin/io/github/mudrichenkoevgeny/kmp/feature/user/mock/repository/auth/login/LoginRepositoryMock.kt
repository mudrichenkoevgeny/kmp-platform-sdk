package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

@InternalApi
class LoginRepositoryMock : LoginRepository {

    var lastEmail: String? = null
    var lastPassword: String? = null
    var lastPhoneNumber: String? = null
    var lastConfirmationCode: String? = null
    var lastAuthProvider: UserAuthProvider? = null
    var lastToken: String? = null
    var lastMfaToken: String? = null
    var lastCode: String? = null

    var authDataResultProvider: () -> AppResult<AuthData> = {
        AppResult.Error(
            CommonError.ContractViolation(
                throwable = IllegalStateException("No mock AuthData provided.")
            )
        )
    }

    var otpConfirmationResultProvider: () -> AppResult<OtpConfirmation> = {
        AppResult.Error(
            CommonError.ContractViolation(
                throwable = IllegalStateException("No mock OtpConfirmation provided.")
            )
        )
    }

    var remainingDelayProvider: (phoneNumber: String) -> Int = { 0 }

    override suspend fun loginByEmail(email: String, password: String): AppResult<AuthData> {
        lastEmail = email
        lastPassword = password
        return authDataResultProvider()
    }

    override suspend fun loginByPhone(phoneNumber: String, confirmationCode: String): AppResult<AuthData> {
        lastPhoneNumber = phoneNumber
        lastConfirmationCode = confirmationCode
        return authDataResultProvider()
    }

    override suspend fun loginByExternalAuthProvider(
        authProvider: UserAuthProvider,
        token: String
    ): AppResult<AuthData> {
        lastAuthProvider = authProvider
        lastToken = token
        return authDataResultProvider()
    }

    override suspend fun loginByTotp(mfaToken: String, code: String): AppResult<AuthData> {
        lastMfaToken = mfaToken
        lastCode = code
        return authDataResultProvider()
    }

    override suspend fun loginByTotpRecoveryCode(mfaToken: String, code: String): AppResult<AuthData> {
        lastMfaToken = mfaToken
        lastCode = code
        return authDataResultProvider()
    }

    override suspend fun sendLoginConfirmationToPhone(phoneNumber: String): AppResult<OtpConfirmation> {
        lastPhoneNumber = phoneNumber
        return otpConfirmationResultProvider()
    }

    override fun getRemainingLoginConfirmationDelayInSeconds(phoneNumber: String): Int =
        remainingDelayProvider(phoneNumber)
}