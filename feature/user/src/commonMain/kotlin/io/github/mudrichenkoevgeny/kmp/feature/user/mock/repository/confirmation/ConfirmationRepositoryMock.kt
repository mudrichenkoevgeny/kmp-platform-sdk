package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.confirmation

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository

@InternalApi
class ConfirmationRepositoryMock : ConfirmationRepository {

    var executeWithTimerResult: AppResult<Any>? = null
    var delayReturn: Int = 0
    var lastType: ConfirmationType? = null
    var lastIdentifier: String? = null

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> executeWithTimer(
        type: ConfirmationType,
        identifier: String,
        action: suspend () -> AppResult<T>
    ): AppResult<T> {
        lastType = type
        lastIdentifier = identifier
        return (executeWithTimerResult as? AppResult<T>) ?: action()
    }

    override fun getRemainingDelay(type: ConfirmationType, identifier: String): Int {
        lastType = type
        lastIdentifier = identifier
        return delayReturn
    }
}