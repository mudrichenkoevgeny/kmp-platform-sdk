package io.github.mudrichenkoevgeny.kmp.feature.user.error.model

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.client.naming.ClientUserErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorCodes

/**
 * User-feature [AppError] variants for client-detected failures and stable server-aligned codes.
 *
 * [code] values follow shared [UserErrorCodes] or [ClientUserErrorCodes].
 */
public sealed class UserError(
    public override val id: ErrorId,
    public override val code: String,
    public override val args: Map<String, String>? = null,
    public override val isRetryable: Boolean
) : AppError {

    /** Emitted when the stored refresh token is rejected or missing while refreshing a session. */
    public class InvalidRefreshToken : UserError(
        id = ErrorId.generate(),
        code = UserErrorCodes.INVALID_REFRESH_TOKEN,
        isRetryable = false
    )

    /**
     * User aborted an external OAuth/sign-in flow (e.g. dismissed system UI).
     *
     * @param throwable Optional underlying cancellation cause for logging; not shown directly to users.
     */
    public class ExternalAuthCancelled(
        public val throwable: Throwable?
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
    public class ExternalAuthFailed(
        public val throwable: Throwable?
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
    public class TooManyConfirmationRequests(
        public val retryAfterSeconds: Int
    ) : UserError(
        id = ErrorId.generate(),
        code = ClientUserErrorCodes.TOO_MANY_CONFIRMATION_REQUESTS,
        isRetryable = false
    )

    /** Registration is disabled by administrator in auth settings. */
    public class RegistrationDisabled : UserError(
        id = ErrorId.generate(),
        code = UserErrorCodes.REGISTRATION_DISABLED,
        isRetryable = false
    )

    /** Emitted when attempting to change a nickname when changes are disabled by policy. */
    public class NicknameChangeDisabled : UserError(
        id = ErrorId.generate(),
        code = UserErrorCodes.NICKNAME_CHANGE_DISABLED,
        isRetryable = false
    )

    /** Emitted when nickname length is below the configured minimum. */
    public class NicknameTooShort(
        public val minLength: Int
    ) : UserError(
        id = ErrorId.generate(),
        code = UserErrorCodes.NICKNAME_TOO_SHORT,
        args = mapOf(UserErrorArgs.NICKNAME_MIN_LENGTH to minLength.toString()),
        isRetryable = false
    )

    /** Emitted when nickname length is above the configured maximum. */
    public class NicknameTooLong(
        public val maxLength: Int
    ) : UserError(
        id = ErrorId.generate(),
        code = UserErrorCodes.NICKNAME_TOO_LONG,
        args = mapOf(UserErrorArgs.NICKNAME_MAX_LENGTH to maxLength.toString()),
        isRetryable = false
    )

    /** Emitted when nickname contains forbidden characters. */
    public class InvalidNicknameCharacters : UserError(
        id = ErrorId.generate(),
        code = UserErrorCodes.INVALID_NICKNAME_CHARACTERS,
        isRetryable = false
    )

    /** Emitted when display name length is below the configured minimum. */
    public class DisplayNameTooShort(
        public val minLength: Int
    ) : UserError(
        id = ErrorId.generate(),
        code = UserErrorCodes.DISPLAY_NAME_TOO_SHORT,
        args = mapOf(UserErrorArgs.DISPLAY_NAME_MIN_LENGTH to minLength.toString()),
        isRetryable = false
    )

    /** Emitted when display name length is above the configured maximum. */
    public class DisplayNameTooLong(
        public val maxLength: Int
    ) : UserError(
        id = ErrorId.generate(),
        code = UserErrorCodes.DISPLAY_NAME_TOO_LONG,
        args = mapOf(UserErrorArgs.DISPLAY_NAME_MAX_LENGTH to maxLength.toString()),
        isRetryable = false
    )

    /** Emitted when display name contains forbidden characters. */
    public class InvalidDisplayNameCharacters : UserError(
        id = ErrorId.generate(),
        code = UserErrorCodes.INVALID_DISPLAY_NAME_CHARACTERS,
        isRetryable = false
    )
}
