package io.github.mudrichenkoevgeny.kmp.feature.user.error.naming

/** Stable string codes for errors raised on the client before or outside backend `UserErrorCodes`. */
object ClientUserErrorCodes {
    const val EXTERNAL_AUTH_CANCELLED = "EXTERNAL_AUTH_CANCELLED"
    const val EXTERNAL_AUTH_FAILED = "EXTERNAL_AUTH_FAILED"
    const val TOO_MANY_CONFIRMATION_REQUESTS = "TOO_MANY_CONFIRMATION_REQUESTS"
}