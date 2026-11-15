package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.login.OpenLoginApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.otpconfirmation.toOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest

/**
 * Implements [LoginRepository] using [OpenLoginApi] and [ConfirmationRepository] for throttled phone
 * confirmation sends.
 *
 * @param openLoginApi HTTP endpoints for all login variants and phone confirmation send.
 * @param confirmationRepository Client-side cooldown for phone login confirmation sends.
 */
public class OpenLoginRepositoryImpl(
    private val openLoginApi: OpenLoginApi,
    private val confirmationRepository: ConfirmationRepository
) : LoginRepository {

    public override suspend fun loginByEmail(
        email: String,
        password: String
    ): AppResult<AuthData> = openLoginApi.loginByEmail(LoginByEmailRequest(email, password))
        .mapSuccess { authDataPayload ->
            authDataPayload.toAuthData()
        }

    public override suspend fun loginByPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<AuthData> = openLoginApi.loginByPhone(LoginByPhoneRequest(phoneNumber, confirmationCode))
        .mapSuccess { authDataPayload ->
            authDataPayload.toAuthData()
        }

    public override suspend fun loginByExternalAuthProvider(
        loginByExternalAuthProviderRequest: LoginByExternalAuthProviderRequest
    ): AppResult<AuthData> = openLoginApi.loginByExternalAuthProvider(loginByExternalAuthProviderRequest)
        .mapSuccess { authDataPayload ->
            authDataPayload.toAuthData()
        }

    public override suspend fun loginByTotp(
        mfaToken: String,
        code: String
    ): AppResult<AuthData> = openLoginApi.loginByTotp(
        VerifyTotpPayload(
            mfaToken = mfaToken,
            code = code
        )
    ).mapSuccess { authDataPayload ->
        authDataPayload.toAuthData()
    }

    public override suspend fun loginByTotpRecoveryCode(
        mfaToken: String,
        code: String
    ): AppResult<AuthData> = openLoginApi.loginByTotp(
        VerifyTotpPayload(
            mfaToken = mfaToken,
            code = code
        )
    ).mapSuccess { authDataPayload ->
        authDataPayload.toAuthData()
    }

    public override suspend fun sendLoginConfirmationToPhone(
        phoneNumber: String
    ): AppResult<OtpConfirmation> = confirmationRepository.executeWithTimer(
        type = ConfirmationType.LOGIN_PHONE,
        identifier = phoneNumber
    ) {
        openLoginApi.sendLoginConfirmationToPhone(
            SendConfirmationToPhoneRequest(phoneNumber)
        ).mapSuccess { response ->
            response.toOtpConfirmation()
        }
    }

    public override fun getRemainingLoginConfirmationDelayInSeconds(phoneNumber: String): Int =
        confirmationRepository.getRemainingDelay(
            type = ConfirmationType.LOGIN_PHONE,
            identifier = phoneNumber
        )
}
