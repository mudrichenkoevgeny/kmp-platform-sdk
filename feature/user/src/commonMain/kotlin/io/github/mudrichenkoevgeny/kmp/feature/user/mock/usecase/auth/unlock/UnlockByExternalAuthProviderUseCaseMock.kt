package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.unlock.UnlockRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByExternalAuthProviderUseCase

@InternalApi
class UnlockByExternalAuthProviderUseCaseMock : UnlockByExternalAuthProviderUseCase(
    unlockRepository = UnlockRepositoryMock()
)
