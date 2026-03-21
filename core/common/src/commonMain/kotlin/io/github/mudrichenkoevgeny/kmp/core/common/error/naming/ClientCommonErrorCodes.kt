package io.github.mudrichenkoevgeny.kmp.core.common.error.naming

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.CommonErrorParser

/**
 * Stable machine-readable error codes produced by the client/domain layer.
 *
 * These codes are used by [CommonError] variants and are expected to be mapped
 * to localized strings by [CommonErrorParser] (or delegates via feature parsers).
 */
object ClientCommonErrorCodes {
    const val NO_INTERNET_CONNECTION = "NO_INTERNET_CONNECTION"
    const val NETWORK = "NETWORK"
    const val CONTRACT_VIOLATION = "CONTRACT_VIOLATION"
    const val LIFECYCLE_ERROR = "LIFECYCLE_ERROR"
}