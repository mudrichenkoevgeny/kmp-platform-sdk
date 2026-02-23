package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository

class SendLoginConfirmationToPhoneUseCase(
    private val loginRepository: LoginRepository
) {
    suspend fun execute(phoneNumber: String): AppResult<SendConfirmationData> {
        return loginRepository.sendLoginConfirmationToPhone(phoneNumber)
    }
}