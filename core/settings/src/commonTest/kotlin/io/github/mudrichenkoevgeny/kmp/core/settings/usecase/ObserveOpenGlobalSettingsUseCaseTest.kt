package io.github.mudrichenkoevgeny.kmp.core.settings.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.repository.OpenGlobalSettingsRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.settings.domain.model.globalsettings.createTestOpenGlobalSettings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
class ObserveOpenGlobalSettingsUseCaseTest {

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val expected = createTestOpenGlobalSettings()
        val repository = OpenGlobalSettingsRepositoryMock(initialSettings = expected)

        val useCase = ObserveOpenGlobalSettingsUseCase(repository)
        val result = useCase().first()

        assertEquals(expected, result)
    }
}
