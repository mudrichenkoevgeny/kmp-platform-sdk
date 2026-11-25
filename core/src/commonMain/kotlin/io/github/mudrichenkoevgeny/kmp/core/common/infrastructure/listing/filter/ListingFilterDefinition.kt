package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

/**
 * Defines a generic filter option for listing infrastructure.
 */
public sealed interface ListingFilterDefinition {
    /** Unique identifier of the filter. */
    public val id: String

    /** Display title for the filter. */
    public val title: String
}
