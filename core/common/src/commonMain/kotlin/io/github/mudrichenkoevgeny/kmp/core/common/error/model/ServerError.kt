package io.github.mudrichenkoevgeny.kmp.core.common.error.model

import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorCodes

/**
 * Represents an application error that can be propagated through the layers of the system.
 *
 * @property id The unique identifier of the error [ErrorId].
 * @property code Machine-readable code of the error (e.g. [CommonErrorCodes]). Can be used for mapping to localized error messages.
 * @property message Human-readable description from the server, used as a fallback if [code] cannot be localized.
 * @property args Dynamic metadata providing additional context for the failure (e.g. [CommonErrorArgs]).
 * @property isRetryable Indicates whether the failure is transient and the operation can be safely retried.
 */
data class ServerError(
    override val id: ErrorId,
    override val code: String,
    val message: String,
    override val args: Map<String, String> = emptyMap(),
    override val isRetryable: Boolean
) : AppError