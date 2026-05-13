package io.github.mudrichenkoevgeny.kmp.core.common.error.logger

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError

/**
 * Logs this error at error severity via [Logger], using [getLogMessage] as the message body.
 *
 * Use this for developer diagnostics.
 */
fun AppError.log() {
    Logger.e(null) { this.getLogMessage() }
}

private fun AppError.getLogMessage(): String = buildString {
    append("id=").append(id.asHexDashString())
    append(", code=").append(code)
    val argsSnapshot = args
    if (!argsSnapshot.isNullOrEmpty()) {
        append(", args=").append(argsSnapshot)
    }
    appendThrowableSuffix(this@getLogMessage)
}

private fun StringBuilder.appendThrowableSuffix(appError: AppError) {
    when (appError) {
        is CommonError.Internal ->
            append(", cause=").append(appError.throwable.stackTraceToString())
        is CommonError.NoInternetConnection ->
            append(", cause=").append(appError.throwable.stackTraceToString())
        is CommonError.Network ->
            append(", cause=").append(appError.throwable.stackTraceToString())
        is CommonError.ContractViolation -> {
            val cause = appError.throwable
            if (cause != null) {
                append(", cause=").append(cause.stackTraceToString())
            }
        }
        else -> Unit
    }
}
