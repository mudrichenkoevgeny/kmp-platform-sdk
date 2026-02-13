package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure

/**
 * Marks declarations that are internal to the SDK and used only for mocks/tests.
 */
@Suppress("ExperimentalAnnotationRetention")
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "Internal SDK API"
)
@Retention(AnnotationRetention.BINARY)
annotation class InternalApi