package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.confirmation

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.confirmation.ConfirmationType

/**
 * Mock implementation of [ConfirmationRepository] for testing.
 */
@InternalApi
public class ConfirmationRepositoryMock : ConfirmationRepository {

    public var executeWithTimerResult: AppResult<Any>? = null
    public var delayReturn: Int = 0
    public var lastType: ConfirmationType? = null
    public var lastIdentifier: String? = null

    @Suppress("UNCHECKED_CAST")
    public override suspend fun <T> executeWithTimer(
        type: ConfirmationType,
        identifier: String,
        action: suspend () -> AppResult<T>
    ): AppResult<T> {
        lastType = type
        lastIdentifier = identifier
        return (executeWithTimerResult as? AppResult<T>) ?: action()
    }

    public override fun getRemainingDelay(
        type: ConfirmationType,
        identifier: String
    ): Int {
        lastType = type
        lastIdentifier = identifier
        return delayReturn
    }
}
