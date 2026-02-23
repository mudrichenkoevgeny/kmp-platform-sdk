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
     * @property throwable The underlying exception that caused the failure.
     */
    class Internal(
        val throwable: Throwable,
        isRetryable: Boolean = false
    ) : CommonError(
        id = ErrorId.generate(),
        code = CommonErrorCodes.INTERNAL,
        isRetryable = isRetryable
    )

    class NoInternetConnection(
        val throwable: Throwable,
        isRetryable: Boolean = true
    ) : CommonError(
        id = ErrorId.generate(),
        code = ClientCommonErrorCodes.NO_INTERNET_CONNECTION,
        isRetryable = isRetryable
    )

    class Network(
        val throwable: Throwable,
        isRetryable: Boolean = true
    ) : CommonError(
        id = ErrorId.generate(),
        code = ClientCommonErrorCodes.NETWORK,
        isRetryable = isRetryable
    )

    class ContractViolation(
        val throwable: Throwable,
        isRetryable: Boolean = false
    ) : CommonError(
        id = ErrorId.generate(),
        code = ClientCommonErrorCodes.CONTRACT_VIOLATION,
        isRetryable = isRetryable
    )

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