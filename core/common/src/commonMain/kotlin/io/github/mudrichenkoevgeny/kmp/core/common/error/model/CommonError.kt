package io.github.mudrichenkoevgeny.kmp.core.common.error.model

import io.github.mudrichenkoevgeny.kmp.core.common.error.naming.ClientCommonErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorCodes

/**
 * Represents a common application error that can be propagated through the layers of the system.
 *
 * @property id The unique identifier of the error [ErrorId].
 * @property code Machine-readable code of the error (e.g. [CommonErrorCodes]). Can be used for mapping to localized error messages.
 * @property args Dynamic metadata providing additional context for the failure (e.g. [CommonErrorArgs]).
 * @property isRetryable Indicates whether the failure is transient and the operation can be safely retried.
 */
sealed class CommonError(
    override val id: ErrorId,
    override val code: String,
    override val args: Map<String, String>? = null,
    override val isRetryable: Boolean
) : AppError {

    /**
     * Represents an error with an undefined or unrecognized cause.
     *
     * This error does not set [args].
     *
     * @param isRetryable Whether the failure is considered transient and can be retried.
     */
    class Unknown(
        isRetryable: Boolean = false
    ) : CommonError(
        id = ErrorId.generate(),
        code = CommonErrorCodes.UNKNOWN,
        isRetryable = isRetryable
    )

    /**
     * Represents a technical or unexpected system failure.
     *
     * This error does not set [args].
     *
     * @param throwable The underlying exception that caused the failure.
     * @param isRetryable Whether the failure is considered transient and can be retried.
     */
    class Internal(
        val throwable: Throwable,
        isRetryable: Boolean = false
    ) : CommonError(
        id = ErrorId.generate(),
        code = CommonErrorCodes.INTERNAL,
        isRetryable = isRetryable
    )

    /**
     * Represents a failure caused by the absence of internet connectivity.
     *
     * This error does not set [args] (it is meant to be mapped to a localized message by [code]).
     *
     * @param throwable Underlying exception/cause.
     * @param isRetryable Whether the failure is transient and can be retried.
     */
    class NoInternetConnection(
        val throwable: Throwable,
        isRetryable: Boolean = true
    ) : CommonError(
        id = ErrorId.generate(),
        code = ClientCommonErrorCodes.NO_INTERNET_CONNECTION,
        isRetryable = isRetryable
    )

    /**
     * Represents a network transport failure (e.g. timeouts, DNS issues) that is typically transient.
     *
     * @param throwable Underlying exception/cause.
     * @param isRetryable Whether the failure is transient and can be retried.
     */
    class Network(
        val throwable: Throwable,
        isRetryable: Boolean = true
    ) : CommonError(
        id = ErrorId.generate(),
        code = ClientCommonErrorCodes.NETWORK,
        isRetryable = isRetryable
    )

    /**
     * Represents a broken contract between layers (e.g. invalid input/state that should not happen).
     *
     * This error does not set [args].
     *
     * @param throwable Underlying exception that indicates the contract violation.
     * @param isRetryable Contract violations are usually non-retryable.
     */
    class ContractViolation(
        val throwable: Throwable,
        isRetryable: Boolean = false
    ) : CommonError(
        id = ErrorId.generate(),
        code = ClientCommonErrorCodes.CONTRACT_VIOLATION,
        isRetryable = isRetryable
    )

    /**
     * Represents a lifecycle-related error caused by invalid runtime state transitions.
     *
     * The [message] is propagated into [AppError.args] under [CommonErrorArgs.MESSAGE] so that
     * UI/localization can use it in a user-friendly way.
     *
     * @param message Lifecycle error message to include in [args].
     * @param isRetryable Whether the failure is transient and can be retried.
     */
    class Lifecycle(
        message: String,
        isRetryable: Boolean = false
    ) : CommonError(
        id = ErrorId.generate(),
        code = ClientCommonErrorCodes.LIFECYCLE_ERROR,
        args = mapOf(CommonErrorArgs.MESSAGE to message),
        isRetryable = isRetryable
    )
}