package io.github.mudrichenkoevgeny.kmp.core.common.error.mapper

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ServerError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.toErrorIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.model.ApiErrorResponse

fun ApiErrorResponse.toServerError(isRetryable: Boolean = false): ServerError = ServerError(
    id = this.id.toErrorIdOrNull() ?: ErrorId.generate(),
    code = this.code,
    message = this.message,
    args = this.args,
    isRetryable = isRetryable
)