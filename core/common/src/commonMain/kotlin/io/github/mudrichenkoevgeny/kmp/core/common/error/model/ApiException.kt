package io.github.mudrichenkoevgeny.kmp.core.common.error.model

import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.model.ApiErrorResponse
import io.github.mudrichenkoevgeny.kmp.core.common.error.mapper.toServerError

/**
 * Exception thrown by networking layer when the server returns a structured [ApiErrorResponse].
 *
 * The exception is later converted into a domain-level [ServerError] (via [toServerError]).
 *
 * @param apiErrorResponse Raw server error payload.
 */
class ApiException(val apiErrorResponse: ApiErrorResponse) : Exception()