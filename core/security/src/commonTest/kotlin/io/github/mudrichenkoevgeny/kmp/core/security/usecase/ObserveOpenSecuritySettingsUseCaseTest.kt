package io.github.mudrichenkoevgeny.kmp.core.security.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.security.mock.repository.OpenSecuritySettingsRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.securitysettings.createTestOpenSecuritySettings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
class ObserveOpenSecuritySettingsUseCaseTest {

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val expected = createTestOpenSecuritySettings()
        val repository = OpenSecuritySettingsRepositoryMock(initialSettings = expected)

        val useCase = ObserveOpenSecuritySettingsUseCase(repository)
        val result = useCase().first()

        assertEquals(expected, result)
    }
}
