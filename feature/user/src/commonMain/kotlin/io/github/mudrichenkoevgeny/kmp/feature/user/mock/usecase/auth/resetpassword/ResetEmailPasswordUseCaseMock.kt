package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.resetpassword.ResetPasswordRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.ResetEmailPasswordUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

@InternalApi
class ResetEmailPasswordUseCaseMock : ResetEmailPasswordUseCase(
    resetPasswordRepository = ResetPasswordRepositoryMock()
) {
    var resultProvider: (String, String, String) -> AppResult<UserIdentifier> = { _, _, _ ->
        AppResult.Error(CommonError.Unknown())
    }

    override suspend fun execute(email: String, newPassword: String, confirmationCode: String): AppResult<UserIdentifier> =
        resultProvider(email, newPassword, confirmationCode)
}
