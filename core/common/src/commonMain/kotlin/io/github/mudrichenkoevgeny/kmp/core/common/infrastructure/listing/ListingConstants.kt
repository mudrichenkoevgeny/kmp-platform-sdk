package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing

/**
 * Common configuration constants for paged listings.
 */
object ListingConstants {

    /** Default page size used when no specific requirement is defined. */
    const val DEFAULT_PAGE_SIZE = 20

    /** Larger page size for lists that are expected to be relatively small. */
    const val LARGE_PAGE_SIZE = 100

    /** The one-based index of the first page. */
    const val INITIAL_PAGE_NUMBER = 1
}
