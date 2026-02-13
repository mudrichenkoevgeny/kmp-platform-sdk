package io.github.mudrichenkoevgeny.kmp.core.common.error.model

import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorCodes

/**
 * Represents an application error that can be propagated through the layers of the system.
 *
 * @property id The unique identifier of the error [ErrorId].
 * @property code Machine-readable code of the error (e.g. [CommonErrorCodes]). Can be used for mapping to localized error messages.
 * @property args Dynamic metadata providing additional context for the failure (e.g. [CommonErrorArgs]).
 * @property isRetryable Indicates whether the operation can be retried (e.g. to show a "Try Again" action).
 */
interface AppError {
    val id: ErrorId
    val code: String
    val args: Map<String, String>?
    val isRetryable: Boolean
}