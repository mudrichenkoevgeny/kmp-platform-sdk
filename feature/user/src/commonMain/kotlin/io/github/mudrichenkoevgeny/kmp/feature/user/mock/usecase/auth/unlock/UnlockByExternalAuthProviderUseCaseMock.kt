package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.unlock.UnlockRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByExternalAuthProviderUseCase

/**
 * Mock implementation of [UnlockByExternalAuthProviderUseCase] for testing.
 */
@InternalApi
public class UnlockByExternalAuthProviderUseCaseMock : UnlockByExternalAuthProviderUseCase(
    unlockRepository = UnlockRepositoryMock()
)
