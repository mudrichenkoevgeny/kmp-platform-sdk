package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.management.ManagementStorageSettingsRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.storagesettings.createTestManagementStorageSettings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
class ObserveManagementStorageSettingsUseCaseTest {

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val expected = createTestManagementStorageSettings()
        val repository = ManagementStorageSettingsRepositoryMock(initialSettings = expected)

        val useCase = ObserveManagementStorageSettingsUseCase(repository)
        val result = useCase().first()

        assertEquals(expected, result)
    }
}
