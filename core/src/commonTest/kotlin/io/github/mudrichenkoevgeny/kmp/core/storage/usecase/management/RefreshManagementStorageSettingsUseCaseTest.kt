package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.management.ManagementStorageSettingsRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.storagesettings.createTestManagementStorageSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class RefreshManagementStorageSettingsUseCaseTest {

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val expected = createTestManagementStorageSettings()
        val repository = ManagementStorageSettingsRepositoryMock(initialSettings = expected)
        val useCase = RefreshManagementStorageSettingsUseCase(repository)

        val result = useCase()

        val success = assertIs<AppResult.Success<ManagementStorageSettings>>(result)
        assertEquals(expected, success.data)
    }
}
