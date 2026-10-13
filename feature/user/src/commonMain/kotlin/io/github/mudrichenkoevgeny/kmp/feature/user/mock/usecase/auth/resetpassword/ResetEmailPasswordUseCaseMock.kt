package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.ResetEmailPasswordUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate

@InternalApi
class ResetEmailPasswordUseCaseMock(
    resetPasswordRepository: ResetPasswordRepository = io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.resetpassword.ResetPasswordRepositoryMock()
) : ResetEmailPasswordUseCase(resetPasswordRepository) {

    var resultProvider: (String, String, String) -> AppResult<UserIdentifierPrivate> = { _, _, _ ->
        AppResult.Error(CommonError.Unknown())
    }

    override suspend fun execute(email: String, newPassword: String, confirmationCode: String): AppResult<UserIdentifierPrivate> {
        return resultProvider(email, newPassword, confirmationCode)
    }
}