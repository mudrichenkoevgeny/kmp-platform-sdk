package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.api.auth.login.LoginApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.toAuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.confirmation.toSendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest

class LoginRepositoryImpl(
    private val loginApi: LoginApi
) : LoginRepository {

    override suspend fun loginByEmail(
        email: String,
        password: String
    ): AppResult<AuthData> {
        return loginApi.loginByEmail(LoginByEmailRequest(email, password))
            .mapSuccess { authDataResponse ->
                authDataResponse.toAuthData()
            }
    }

    override suspend fun loginByPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<AuthData> {
        return loginApi.loginByPhone(LoginByPhoneRequest(phoneNumber, confirmationCode))
            .mapSuccess { authDataResponse ->
                authDataResponse.toAuthData()
            }
    }

    override suspend fun loginByExternalAuthProvider(
        authProvider: UserAuthProvider,
        token: String
    ): AppResult<AuthData> {
        return loginApi.loginByExternalAuthProvider(
            LoginByExternalAuthProviderRequest(authProvider.serialName, token)
        ).mapSuccess { authDataResponse ->
            authDataResponse.toAuthData()
        }
    }

    override suspend fun sendLoginConfirmationToPhone(
        phoneNumber: String
    ): AppResult<SendConfirmationData> {
        return loginApi.sendLoginConfirmationToPhone(
            SendConfirmationToPhoneRequest(phoneNumber)
        ).mapSuccess { sendConfirmationResponse ->
            sendConfirmationResponse.toSendConfirmationData()
        }
    }
}