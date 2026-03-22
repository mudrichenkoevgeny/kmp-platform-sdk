package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.toAuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.confirmation.toSendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.registration.RegistrationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.register.RegisterByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest

/**
 * Implements [RegistrationRepository] using [RegistrationApi] and [ConfirmationRepository] for
 * throttled confirmation sends.
 *
 * @param registrationApi Registration and send-confirmation HTTP endpoints.
 * @param confirmationRepository Client-side cooldown for registration email codes.
 */
class RegistrationRepositoryImpl(
    private val registrationApi: RegistrationApi,
    private val confirmationRepository: ConfirmationRepository
) : RegistrationRepository {

    override suspend fun registerByEmail(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<AuthData> {
        return registrationApi.registerByEmail(
            RegisterByEmailRequest(email, password, confirmationCode)
        ).mapSuccess { authDataResponse ->
            authDataResponse.toAuthData()
        }
    }

    override suspend fun sendRegistrationConfirmationToEmail(
        email: String
    ): AppResult<SendConfirmationData> {
        return confirmationRepository.executeWithTimer(
            type = ConfirmationType.REGISTRATION_EMAIL,
            identifier = email
        ) {
            registrationApi.sendRegistrationConfirmationToEmail(
                SendConfirmationToEmailRequest(email)
            ).mapSuccess { response ->
                response.toSendConfirmationData()
            }
        }
    }

    override fun getRemainingRegistrationConfirmationDelayInSeconds(email: String): Int {
        return confirmationRepository.getRemainingDelay(
            type = ConfirmationType.REGISTRATION_EMAIL,
            identifier = email
        )
    }
}