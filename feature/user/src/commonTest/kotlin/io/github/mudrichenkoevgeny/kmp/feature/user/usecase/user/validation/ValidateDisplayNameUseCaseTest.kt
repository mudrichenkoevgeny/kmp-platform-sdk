package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.validation

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.OpenAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAuthSettingsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.validator.displayname.DisplayNameValidatorImpl
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.auth.settings.createTestOpenAuthSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ValidateDisplayNameUseCaseTest {

    private val defaultSettings = createTestOpenAuthSettings(
        userDisplayNameMinLength = 2,
        userDisplayNameMaxLength = 20
    )
    private val authSettingsRepository = OpenAuthSettingsRepositoryMock().apply {
        emit(defaultSettings)
    }
    private val getAuthSettingsUseCase = GetAuthSettingsUseCase(authSettingsRepository)
    private val validator = DisplayNameValidatorImpl()
    private val useCase = ValidateDisplayNameUseCase(validator, getAuthSettingsUseCase)

    @Test
    fun invoke_returnsSuccess_whenDisplayNameIsValid() = runTest {
        val result = useCase(newDisplayName = "Valid Name")

        assertIs<AppResult.Success<Unit>>(result)
    }

    @Test
    fun invoke_returnsError_whenDisplayNameTooShort() = runTest {
        val result = useCase(newDisplayName = "A")

        val errorResult = assertIs<AppResult.Error>(result)
        val userError = assertIs<UserError.DisplayNameTooShort>(errorResult.error)
        assertEquals(2, userError.minLength)
    }

    @Test
    fun invoke_returnsError_whenDisplayNameTooLong() = runTest {
        val result = useCase(newDisplayName = "A".repeat(25))

        val errorResult = assertIs<AppResult.Error>(result)
        val userError = assertIs<UserError.DisplayNameTooLong>(errorResult.error)
        assertEquals(20, userError.maxLength)
    }
}
