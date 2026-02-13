package io.github.mudrichenkoevgeny.kmp.core.common.error.model

import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.model.ApiErrorResponse

class ApiException(val apiErrorResponse: ApiErrorResponse) : Exception()