package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionPrivate

@InternalApi
class GetSessionUseCaseMock(
    sessionRepository: SessionRepository = SessionRepositoryMock()
) : GetSessionUseCase(sessionRepository) {

    var executeCalls = 0
    var resultProvider: (UserSessionId) -> AppResult<UserSessionPrivate> = { AppResult.Success(createTestUserSessionPrivate()) }

    override suspend fun invoke(userSessionId: UserSessionId): AppResult<UserSessionPrivate> {
        executeCalls++
        return resultProvider(userSessionId)
    }
}