package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider

interface LoginRepository {
    suspend fun loginByEmail(email: String, password: String): AppResult<AuthData>
    suspend fun loginByPhone(phoneNumber: String, confirmationCode: String): AppResult<AuthData>
    suspend fun loginByExternalAuthProvider(authProvider: UserAuthProvider, token: String): AppResult<AuthData>
    suspend fun sendLoginConfirmationToPhone(phoneNumber: String): AppResult<SendConfirmationData>
    fun getRemainingLoginConfirmationDelayInSeconds(phoneNumber: String): Int
}