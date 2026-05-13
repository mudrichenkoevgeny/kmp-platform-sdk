package io.github.mudrichenkoevgeny.kmp.core.common.error.naming

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.CommonErrorParser

/**
 * Stable keys for dynamic [AppError.args] maps produced by the client/domain layer.
 *
 * These keys are used by [CommonError] variants to attach structured metadata for diagnostics
 * and logging. Localized user-facing text is still expected to be
 * resolved from the error code via [CommonErrorParser] (or delegates via feature parsers)
 */
object ClientCommonErrorArgs {
    /** [VIOLATIONS] aggregates invalid field reports as `fieldName=rawValue` entries joined with `"; "`. */
    const val VIOLATIONS = "violations"
}
