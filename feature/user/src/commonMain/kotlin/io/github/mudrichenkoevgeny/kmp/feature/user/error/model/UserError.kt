package io.github.mudrichenkoevgeny.kmp.feature.user.error.model

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.kmp.feature.user.error.naming.ClientUserErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorCodes

/**
 * User-feature [AppError] variants for client-detected failures and stable server-aligned codes.
 *
 * [code] values follow shared `UserErrorCodes` or [ClientUserErrorCodes]. [args] is unused today except
 * where individual subclasses expose structured fields (e.g. retry delay). [isRetryable] is set per case.
 */
sealed class UserError(
    override val id: ErrorId,
    override val code: String,
    override val args: Map<String, String>? = null,
    override val isRetryable: Boolean
) : AppError {

    /** Emitted when the stored refresh token is rejected or missing while refreshing a session. */
    class InvalidRefreshToken : UserError(
        id = ErrorId.generate(),
        code = UserErrorCodes.INVALID_REFRESH_TOKEN,
        isRetryable = false
    )

    /**
     * User aborted an external OAuth/sign-in flow (e.g. dismissed system UI).
     *
     * @param throwable Optional underlying cancellation cause for logging; not shown directly to users.
     */
    class ExternalAuthCancelled(
        val throwable: Throwable?
    ) : UserError(
        id = ErrorId.generate(),
        code = ClientUserErrorCodes.EXTERNAL_AUTH_CANCELLED,
        isRetryable = false
    )

    /**
     * External OAuth/sign-in failed after user interaction (provider error, misconfiguration, etc.).
     *
     * @param throwable Optional failure cause for logging; not shown directly to users.
     */
    class ExternalAuthFailed(
        val throwable: Throwable?
    ) : UserError(
        id = ErrorId.generate(),
        code = ClientUserErrorCodes.EXTERNAL_AUTH_FAILED,
        isRetryable = false
    )

    /**
     * Rate limiting on confirmation-code requests (email/SMS); UI may surface [retryAfterSeconds].
     *
     * @param retryAfterSeconds Suggested wait time before retrying a send; safe to display to users.
     */
    class TooManyConfirmationRequests(
        val retryAfterSeconds: Int
    ) : UserError(
        id = ErrorId.generate(),
        code = ClientUserErrorCodes.TOO_MANY_CONFIRMATION_REQUESTS,
        isRetryable = false
    )
}