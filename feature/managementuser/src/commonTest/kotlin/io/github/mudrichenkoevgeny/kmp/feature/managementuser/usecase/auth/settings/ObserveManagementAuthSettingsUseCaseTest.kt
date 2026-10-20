package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.auth.settings.ManagementAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.auth.settings.createTestManagementAuthSettings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
class ObserveManagementAuthSettingsUseCaseTest {

    @Test
    fun `should observe settings from repository`() = runTest {
        val authSettings = createTestManagementAuthSettings()
        val repository = ManagementAuthSettingsRepositoryMock()
        val useCase = ObserveManagementAuthSettingsUseCase(repository)

        repository.emit(authSettings)
        val result = useCase().first()

        assertEquals(authSettings, result)
    }
}
