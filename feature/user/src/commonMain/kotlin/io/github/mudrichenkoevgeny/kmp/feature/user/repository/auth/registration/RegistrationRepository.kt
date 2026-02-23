package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData

interface RegistrationRepository {
    suspend fun registerByEmail(email: String, password: String, confirmationCode: String): AppResult<AuthData>
    suspend fun sendRegistrationConfirmationToEmail(email: String): AppResult<SendConfirmationData>
    fun getRemainingRegistrationConfirmationDelayInSeconds(email: String): Int
}