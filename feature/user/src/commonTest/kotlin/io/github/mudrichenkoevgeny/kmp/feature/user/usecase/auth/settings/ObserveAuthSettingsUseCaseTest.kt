package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.auth.settings.publicAuthSettingsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.OpenAuthSettingsRepositoryMock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@InternalApi
class ObserveAuthSettingsUseCaseTest {

    @Test
    fun `should observe settings from repository`() = runTest {
        val repository = OpenAuthSettingsRepositoryMock()
        val useCase = ObserveAuthSettingsUseCase(repository)
        val authSettings = publicAuthSettingsMock()

        assertNull(useCase().first())

        repository.emit(authSettings)

        assertEquals(authSettings, useCase().first { it != null })
    }
}
