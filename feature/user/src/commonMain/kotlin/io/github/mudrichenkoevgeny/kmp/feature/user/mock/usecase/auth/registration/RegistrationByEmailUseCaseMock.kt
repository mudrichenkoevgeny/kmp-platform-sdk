package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.registration.RegistrationRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.RegistrationByEmailUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData

@InternalApi
class RegistrationByEmailUseCaseMock : RegistrationByEmailUseCase(
    registrationRepository = RegistrationRepositoryMock(),
    authStorage = AuthStorageMock(),
    userStorage = UserStorageMock()
) {
    var resultProvider: (String, String, String) -> AppResult<AuthData> = { _, _, _ ->
        AppResult.Error(CommonError.Unknown())
    }

    override suspend fun execute(email: String, password: String, confirmationCode: String): AppResult<AuthData> =
        resultProvider(email, password, confirmationCode)
}
