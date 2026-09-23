package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId

@InternalApi
class GetSessionUseCaseMock : GetSessionUseCase(
    sessionRepository = SessionRepositoryMock()
) {
    var resultProvider: (UserSessionId) -> AppResult<UserSession> = { AppResult.Success(userSessionMock()) }
    var executeCalls = 0
    var lastSessionId: UserSessionId? = null

    override suspend fun invoke(userSessionId: UserSessionId): AppResult<UserSession> {
        executeCalls++
        lastSessionId = userSessionId
        return resultProvider(userSessionId)
    }
}
