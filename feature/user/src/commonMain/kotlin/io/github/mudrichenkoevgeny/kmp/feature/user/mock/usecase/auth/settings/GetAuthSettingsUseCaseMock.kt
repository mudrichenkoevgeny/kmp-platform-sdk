package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.OpenAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAuthSettingsUseCase

@InternalApi
fun createGetAuthSettingsUseCaseMock(
    openAuthSettingsRepository: OpenAuthSettingsRepository = OpenAuthSettingsRepositoryMock()
): GetAuthSettingsUseCase = GetAuthSettingsUseCase(openAuthSettingsRepository)
