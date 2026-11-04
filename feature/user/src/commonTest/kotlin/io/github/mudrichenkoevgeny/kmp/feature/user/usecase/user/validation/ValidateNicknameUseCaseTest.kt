package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.validation

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.OpenAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAuthSettingsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.validator.nickname.NicknameValidatorImpl
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.auth.settings.createTestOpenAuthSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ValidateNicknameUseCaseTest {

    private val defaultSettings = createTestOpenAuthSettings(
        userNicknameMinLength = 3,
        userNicknameMaxLength = 15,
        isUserNicknameChangeAllowed = true
    )
    private val authSettingsRepository = OpenAuthSettingsRepositoryMock().apply {
        emit(defaultSettings)
    }
    private val getAuthSettingsUseCase = GetAuthSettingsUseCase(authSettingsRepository)
    private val validator = NicknameValidatorImpl()
    private val useCase = ValidateNicknameUseCase(validator, getAuthSettingsUseCase)

    @Test
    fun invoke_returnsSuccess_whenNicknameIsValid() = runTest {
        val result = useCase(newNickname = "validnick", currentNickname = null)

        assertIs<AppResult.Success<Unit>>(result)
    }

    @Test
    fun invoke_returnsError_whenNicknameTooShort() = runTest {
        val result = useCase(newNickname = "ab", currentNickname = null)

        val errorResult = assertIs<AppResult.Error>(result)
        val userError = assertIs<UserError.NicknameTooShort>(errorResult.error)
        assertEquals(3, userError.minLength)
    }

    @Test
    fun invoke_returnsError_whenNicknameTooLong() = runTest {
        val result = useCase(newNickname = "a".repeat(20), currentNickname = null)

        val errorResult = assertIs<AppResult.Error>(result)
        val userError = assertIs<UserError.NicknameTooLong>(errorResult.error)
        assertEquals(15, userError.maxLength)
    }

    @Test
    fun invoke_returnsError_whenNicknameChangeDisabled() = runTest {
        authSettingsRepository.emit(
            createTestOpenAuthSettings(
                isUserNicknameChangeAllowed = false
            )
        )

        val result = useCase(newNickname = "newnick", currentNickname = "oldnick")

        val errorResult = assertIs<AppResult.Error>(result)
        assertIs<UserError.NicknameChangeDisabled>(errorResult.error)
    }
}
