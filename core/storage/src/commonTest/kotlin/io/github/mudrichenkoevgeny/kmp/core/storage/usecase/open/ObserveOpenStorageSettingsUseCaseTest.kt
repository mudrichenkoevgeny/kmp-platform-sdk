package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.open

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.open.OpenStorageSettingsRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.storagesettings.createTestOpenStorageSettings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
class ObserveOpenStorageSettingsUseCaseTest {

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val expected = createTestOpenStorageSettings()
        val repository = OpenStorageSettingsRepositoryMock(initialSettings = expected)

        val useCase = ObserveOpenStorageSettingsUseCase(repository)
        val result = useCase().first()

        assertEquals(expected, result)
    }
}
