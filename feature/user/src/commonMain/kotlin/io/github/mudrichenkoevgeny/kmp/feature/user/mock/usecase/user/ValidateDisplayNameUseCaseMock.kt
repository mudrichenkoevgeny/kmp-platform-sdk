package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.OpenAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.validation.ValidateDisplayNameUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.validator.displayname.DisplayNameValidator
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.validator.displayname.DisplayNameValidatorImpl

@InternalApi
class ValidateDisplayNameUseCaseMock(
    displayNameValidator: DisplayNameValidator = DisplayNameValidatorImpl(),
    getAuthSettingsUseCase: GetAuthSettingsUseCase = GetAuthSettingsUseCase(OpenAuthSettingsRepositoryMock())
) : ValidateDisplayNameUseCase(displayNameValidator, getAuthSettingsUseCase) {

    var resultProvider: (String) -> AppResult<Unit> = { _ -> AppResult.Success(Unit) }

    override suspend fun invoke(newDisplayName: String): AppResult<Unit> {
        return resultProvider(newDisplayName)
    }
}
