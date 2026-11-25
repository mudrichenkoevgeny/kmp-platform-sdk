package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.open

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.open.OpenStorageSettingsRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.storagesettings.createTestOpenStorageSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GetOpenStorageSettingsUseCaseTest {

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val expected = createTestOpenStorageSettings()
        val repository = OpenStorageSettingsRepositoryMock(initialSettings = expected)
        val useCase = GetOpenStorageSettingsUseCase(repository)

        val result = useCase()

        val success = assertIs<AppResult.Success<OpenStorageSettings>>(result)
        assertEquals(expected, success.data)
    }
}
