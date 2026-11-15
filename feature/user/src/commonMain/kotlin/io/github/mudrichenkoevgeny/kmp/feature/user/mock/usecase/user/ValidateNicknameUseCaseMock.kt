package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.OpenAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.validation.ValidateNicknameUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.validator.nickname.NicknameValidator
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.validator.nickname.NicknameValidatorImpl

/**
 * Mock implementation of [ValidateNicknameUseCase] for testing.
 */
@InternalApi
public class ValidateNicknameUseCaseMock(
    nicknameValidator: NicknameValidator = NicknameValidatorImpl(),
    getAuthSettingsUseCase: GetAuthSettingsUseCase = GetAuthSettingsUseCase(OpenAuthSettingsRepositoryMock())
) : ValidateNicknameUseCase(nicknameValidator, getAuthSettingsUseCase) {

    public var resultProvider: (String, String?) -> AppResult<Unit> = { _, _ -> AppResult.Success(Unit) }

    public override suspend fun invoke(
        newNickname: String,
        currentNickname: String?
    ): AppResult<Unit> = resultProvider(newNickname, currentNickname)
}
