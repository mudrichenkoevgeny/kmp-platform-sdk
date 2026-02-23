package io.github.mudrichenkoevgeny.kmp.feature.user.error.model

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.kmp.feature.user.error.naming.ClientUserErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorCodes

sealed class UserError(
    override val id: ErrorId,
    override val code: String,
    override val args: Map<String, String>? = null,
    override val isRetryable: Boolean
) : AppError {

    class InvalidRefreshToken : UserError(
        id = ErrorId.generate(),
        code = UserErrorCodes.INVALID_REFRESH_TOKEN,
        isRetryable = false
    )

    class ExternalAuthCancelled(
        val throwable: Throwable?
    ) : UserError(
        id = ErrorId.generate(),
        code = ClientUserErrorCodes.EXTERNAL_AUTH_CANCELLED,
        isRetryable = false
    )

    class ExternalAuthFailed(
        val throwable: Throwable?
    ) : UserError(
        id = ErrorId.generate(),
        code = ClientUserErrorCodes.EXTERNAL_AUTH_FAILED,
        isRetryable = false
    )

    class TooManyConfirmationRequests(
        val retryAfterSeconds: Int
    ) : UserError(
        id = ErrorId.generate(),
        code = ClientUserErrorCodes.TOO_MANY_CONFIRMATION_REQUESTS,
        isRetryable = false
    )
}